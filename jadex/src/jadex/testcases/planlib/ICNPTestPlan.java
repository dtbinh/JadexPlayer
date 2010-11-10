package jadex.testcases.planlib;

import jadex.adapter.fipa.AgentIdentifier;
import jadex.planlib.*;
import jadex.runtime.GoalFailureException;
import jadex.runtime.IGoal;
import jadex.testcases.AbstractMultipleAgentsPlan;
import jadex.util.SUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Test the cnp protocol execution.
 */
public class ICNPTestPlan extends AbstractMultipleAgentsPlan
{
	//-------- methods --------
	
	/**
	 *  The plan body.
	 */
	public void body()
	{
		IProposalEvaluator	pe	= new ProposalEvaluator(new Integer(5), false);
		// Todo: provide default implementation.
		IQueryNextroundInfo	qnri	= new IQueryNextroundInfo()
		{
			public boolean queryNextroundInfo(NextroundInfo info, NegotiationRecord[] history, ParticipantProposal[] proposals)
			{
				return history.length<3;
			}
		};
		Object	cfp_info	= new ICNPHandler(pe, qnri);

		performTest("one", "Test executing one proposal using custom plans", false, null);
		performTest("one-default",  "Test executing one proposal using default plans", false, cfp_info);
		performTest("all",  "Test executing all proposals using custom plans", true, null);
		performTest("all-default",  "Test executing all proposals using default plans", true, cfp_info);
	}
	
	//-------- helper methods --------
	
	/**
	 *  Test initiating a contract net interaction with given settings.
	 *  @param executeall	True, if all acceptable proposals should be executed.
	 *  @param cfp_info	Use given cfp info (e.g. for default plans) 
	 */
	protected void	performTest(String name, String description, boolean executeall, Object cfp_info)
	{
		// Create 3 participants with different offers.
		Map[] args = new Map[]{new HashMap(), new HashMap(), new HashMap()};
		
		args[0].put("offer", new Integer(5));
		args[0].put("execute", Boolean.TRUE);
		
		args[1].put("offer", new Integer(1));
		args[1].put("execute", Boolean.TRUE);
		
		args[2].put("offer", new Integer(7));
		args[2].put("execute", new Boolean(executeall));	// Fails when only one is needed, to check if proposal 5 gets executed.

		List agents = createAgents("jadex.testcases.planlib.ICNPReceiver", args);	

		TestReport tr = new TestReport(name, description);
		if(assureTest(tr))
		{
			try
			{
				IGoal cnpini = createGoal("icnp_initiate");
				cnpini.getParameterSet("receivers").addValues(agents.toArray(new AgentIdentifier[agents.size()]));
				cnpini.getParameter("cfp").setValue("CFP for a task.");
				cnpini.getParameter("cfp_info").setValue(cfp_info);
				cnpini.getParameter("executeall").setValue(new Boolean(executeall));
				dispatchSubgoalAndWait(cnpini);
				getLogger().info("CNP result:"+ SUtil.arrayToString(cnpini.getParameterSet("result").getValues()));
				
				// Check for correct number of negotiation rounds.
				NegotiationRecord[]	history	= (NegotiationRecord[])cnpini.getParameterSet("history").getValues();
				if(history.length==4)	// 3 rounds + execution.
				{
					// Check final proposals executed in last round.
					ParticipantProposal[]	finalproposals	= history[history.length-1].getProposals();
					
					if(executeall)
					{
						// Check number of proposals.
						if(finalproposals.length==2)
						{
							// Check values of proposals.
							if(((Number)finalproposals[0].getProposal()).intValue()==7
								&& ((Number)finalproposals[1].getProposal()).intValue()==5)
							{
								// For executeall, both evaluation values should be success. 
								if("success".equals(finalproposals[0].getEvaluation())
									&& "success".equals(finalproposals[1].getEvaluation()))
								{
									tr.setSucceeded(true);
								}
								else
								{
									tr.setFailed("Wrong evaluations for accepted proposals: "+SUtil.arrayToString(finalproposals));
								}
							}
							else
							{
								tr.setFailed("Wrong values for accepted proposals: "+SUtil.arrayToString(finalproposals));
							}
						}
						else
						{
							tr.setFailed("Wrong number of accepted proposals: "+SUtil.arrayToString(finalproposals));
						}
					}
					else
					{
						// Check number of proposals.
						if(finalproposals.length==1)
						{
							// Check values of proposals.
							if(((Number)finalproposals[0].getProposal()).intValue()==5)
							{
								// For not executeall, evaluation value should be success. 
								if("success".equals(finalproposals[0].getEvaluation()))
								{
									tr.setSucceeded(true);
								}
								else
								{
									tr.setFailed("Wrong evaluations for accepted proposals: "+SUtil.arrayToString(finalproposals));
								}
							}
							else
							{
								tr.setFailed("Wrong values for accepted proposals: "+SUtil.arrayToString(finalproposals));
							}
						}
						else
						{
							tr.setFailed("Wrong number of accepted proposals: "+SUtil.arrayToString(finalproposals));
						}
					}
				}
				else
				{
					tr.setFailed("Wrong number of negotiation rounds: "+history.length);
				}
			}
			catch(GoalFailureException e)
			{
				tr.setFailed("Exception occurred: "+e);
			}
		}

		// Destroy agents first, as setting report might abort plan unexpectedly.
		destroyAgents();
		
		getBeliefbase().getBeliefSet("reports").addFact(tr);
	}
}
