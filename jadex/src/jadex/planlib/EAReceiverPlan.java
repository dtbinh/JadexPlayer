package jadex.planlib;

import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.*;
import jadex.util.collection.SCollection;

import java.util.List;

/**
 *  This plan implements the receiver of the "FIPA Dutch Auction Interaction
 *  Protocol Specification" (XC00032 - Experimental).
 *  
 *  An English auction is one where bidders continously can increase the current
 *  offer until no one is willing to increase any more.
 */
public class EAReceiverPlan extends AbstractReceiverPlan
{
	/**
	 *  The plan body.
	 */
	public void body()
	{
		// Fetch the auction information.
		IMessageEvent me = (IMessageEvent)getParameter("message").getValue();
		AuctionDescription auctiondesc = (AuctionDescription)me.getParameter(SFipa.CONTENT).getValue();
		
		getLogger().info(getAgentName()+": Received inform_start_auction message with auction description " +
			"start time: "+auctiondesc.getStarttime()+" Round time "+auctiondesc.getRoundTimeout()
			+" topic: "+auctiondesc.getTopic());
		if(auctiondesc.getRoundTimeout()<=0)
		{
			getLogger().warning(getAgentName()+"No round timeout specified");
			fail();
		}
		
		// Offer the possibility to decide not to participate in the auction
		Object[] tmp = decideParticipation(auctiondesc, (AgentIdentifier)me.getParameter(SFipa.SENDER).getValue());
		boolean participate = ((Boolean)tmp[0]).booleanValue();
		Object auctioninfo = tmp[1];
		
		// Wait for messages with the conversation-id of the initial event (inform_start_auction).
		String convid = (String)me.getParameter(SFipa.CONVERSATION_ID).getValue();
		MessageEventFilter mef = new MessageEventFilter(null);
		mef.addValue(SFipa.CONVERSATION_ID, convid);
		getWaitqueue().addFilter(mef);
		
		long buftimeout = (long)auctiondesc.getRoundTimeout();
		long firsttimeout = auctiondesc.getStarttime()==0 || (auctiondesc.getStarttime()-System.currentTimeMillis()<=0)
			? -1 : auctiondesc.getStarttime()-System.currentTimeMillis()+buftimeout;
	
		List offers = SCollection.createArrayList();
		boolean running = true;
		Object winning_offer = null; // my winning offer
		Object auction_wo = null; // the winning offer of the auction
		int missing_cnt = 0;
		
		//System.out.println(getAgentName()+" timeout: "+timeout);
			
		while(participate && running)
		{
			try
			{
				//System.out.println(getAgentName()+" waiting for: "+(firsttimeout==-1? buftimeout: firsttimeout));
				getLogger().info(getAgentName()+" waiting for: "+(firsttimeout==-1? buftimeout: firsttimeout));
				IMessageEvent msg = (IMessageEvent)waitFor(mef, firsttimeout==-1? buftimeout: firsttimeout);
				getLogger().info(getAgentName()+" received msg: "+msg.getType());
				missing_cnt = 0; // Reset missing_cnt as auction continues
				firsttimeout=-1;
				//System.out.println(getAgentName()+" received msg: "+msg.getType());
				//lastcfptime = System.currentTimeMillis();
				
				if(msg.getType().equals("ea_cfp"))
				{
					// If has sitting out last round and a new round has been
					// issued reset winning_offer and sitting_out and continue
					// the auction
						
					if(winning_offer!=null)
					{
						//System.out.println(getAgentName()+" sitting out: "+msg.getContent());
						getLogger().info(getAgentName()+" sitting out: "+msg.getContent());
						winning_offer = null;
					}
					else
					{
						handleCFP(msg, auctiondesc, auctioninfo, winning_offer, offers);
					}
				}
				else if(msg.getType().equals("ea_accept_proposal"))
				{
					winning_offer = msg.getContent();
					getLogger().info(getAgentName()+" is currently winner for "+winning_offer);
					//System.out.println(getAgentName()+" is currently winner for "+winning_offer);
				}
				else if(msg.getType().equals("ea_reject_proposal"))
				{
					winning_offer = null;
				}
				else if(msg.getType().equals("ea_inform_end_auction"))
				{
					// res contains [boolean won, Object winning_offer] 
					Object[] res = (Object[])msg.getContent();
					
					if(!((Boolean)res[0]).booleanValue())
						winning_offer = null;
					
					auction_wo = res[1];
					running = false;
				}
				else
				{
					getLogger().warning((getAgentName()+" could not understand: "+msg));
				}
			}
			catch(TimeoutException e)
			{
				getLogger().info(getAgentName()+" "+e.getMessage());
				// Exit when no offers are received any more (for 3 times).
				//System.out.println(getAgentName()+" missed cfp: "+missing_cnt);
				if(++missing_cnt==3)
				{
					getLogger().info(getAgentName()+" leaving auction due to 3 missed cfps");
					//System.out.println(getAgentName()+" leaving auction due to 3 missed cfps");
					running = false;
				}
			}
		}
		
		//System.out.println(getAgentName()+" end");
		getLogger().info(getAgentName()+" auction end");
			
		if(!running)
			getParameter("result").setValue(new Object[]{winning_offer, auction_wo});
		
		getWaitqueue().removeFilter(mef);
	}
	
	/**
	 *  Decide about participation.
	 *  If the goal is not handled participation is true.
	 *  @param auctiondesc The auction description.
	 *  @return The participation state (Boolean) and the local auction info (Object).
	 */
	protected Object[] decideParticipation(AuctionDescription auctiondesc, AgentIdentifier initiator)
	{
		Object[] ret = new Object[2];
		ret[0] = Boolean.TRUE; // participate
		ret[1] = null; // auction info
		
		try
		{
			IGoal dp = getScope().getGoalbase().createGoal("ea_decide_participation");
			dp.getParameter("auction_description").setValue(auctiondesc);
			dp.getParameter("initiator").setValue(initiator);
			dispatchSubgoalAndWait(dp);
			ret[1] = dp.getParameter("auction_info").getValue();
			Boolean part = (Boolean)dp.getParameter("participate").getValue();
			ret[0] = part==null? Boolean.TRUE: part;
		}
		catch(GoalFailureException e)
		{
			// Participate if no explicit decision was made.
			getLogger().info("Optional goal ea_decide_request has not been handled.");
		}
		
		return ret;
	}
	
	/**
	 *  Handle a cfp message.
	 *  @param auctiondesc The auction description.
	 *  @return The participation state (Boolean) and the local auction info (Object).
	 */
	protected Object[] handleCFP(IMessageEvent cfp, AuctionDescription auctiondesc, Object auctioninfo,
		Object winning_offer, List offers)
	{
		//System.out.println(getAgentName()+" handleCFP: "+cfp);
	
		Object[] ret = new Object[2];
		ret[0] = Boolean.TRUE; // participate
		ret[1] = auctioninfo;
		
		// Instantiate make_proposal-goal with the offer of the received CFP.
		IGoal mp = createGoal("ea_make_proposal");
		Object offer = cfp.getContent();
		offers.add(offer);
		mp.getParameter("cfp").setValue(offer);
		mp.getParameter("auction_description").setValue(auctiondesc);
		mp.getParameter("auction_info").setValue(auctioninfo);
		mp.getParameterSet("history").addValues(offers.toArray());
		try
		{
			dispatchSubgoalAndWait(mp, auctiondesc.getRoundTimeout());
		}
		catch(TimeoutException e)
		{
			getLogger().info(getAgentName() + e.getMessage());
		}
		ret[1] = mp.getParameter("auction_info").getValue();
		Boolean leave = (Boolean)mp.getParameter("leave").getValue();
		Boolean accept = (Boolean)mp.getParameter("accept").getValue();
		if(leave!=null && leave.booleanValue())
		{
			getLogger().info(getAgentName() + " informs the initiator of the auction "
				+auctiondesc.getTopic()+" that it doesn't want to participate.");
		
			sendMessage(cfp.createReply("ea_not_understood"));
			
			// Set participate to false.
			ret[0] = Boolean.FALSE;
		}
		else if(accept!=null && accept.booleanValue())
		{
			//System.out.println(getAgentName()+" sending proposal: "+offer);
			getLogger().info(getAgentName()+" sending proposal: "+offer);
			// Send propsal.
			sendMessage(cfp.createReply("ea_propose"));
		}
		
		return ret;
	}
	
}