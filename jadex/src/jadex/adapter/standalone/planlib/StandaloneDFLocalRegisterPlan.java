package jadex.adapter.standalone.planlib;

import jadex.adapter.fipa.AgentDescription;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.standalone.StandaloneAgentAdapter;
import jadex.runtime.BasicAgentIdentifier;
import jadex.runtime.Plan;

import java.util.Date;


/**
 *  Plan to register at the standalone df.
 */
public class StandaloneDFLocalRegisterPlan extends Plan
{
	/**
	 *  Plan body.
	 */
	public void body()
	{
		// Todo: support other parameters!?
		AgentDescription desc = (AgentDescription)getParameter("description").getValue();
		if(desc.getName()==null)
		{
			BasicAgentIdentifier	bid	= getScope().getAgentIdentifier();
			desc.setName(bid instanceof AgentIdentifier ? (AgentIdentifier)bid
				: new AgentIdentifier(bid.getName()));
		}

		// Set the lease time.
		Number lt = (Number)getParameter("leasetime").getValue();
		if(lt!=null)
			desc.setLeaseTime(new Date(System.currentTimeMillis()+lt.longValue()));

		getLogger().info("Trying to register: "+desc);

		StandaloneAgentAdapter	agent	= (StandaloneAgentAdapter)getCapability().getPlatformAgent();
		try
		{
			agent.getPlatform().getDF().register(desc); // todo: supply return value or throw exception?
		}
		catch(RuntimeException e)
		{
			fail();
		}
		getParameter("result").setValue(desc.clone());
	}
}
