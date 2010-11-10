package jadex.testcases.planlib;

import jadex.planlib.AuctionDescription;
import jadex.runtime.Plan;


/**
 *  Decide on participation.
 */
public class EADecideParticipationPlan extends Plan
{
	/**
	 *  The plan body.
	 */
	public void body()
	{
		boolean participate = ((Boolean)getBeliefbase().getBelief("participate").getFact()).booleanValue();
		getLogger().info(getAgentName()+" deciding on participation in auction "
			+((AuctionDescription)getParameter("auction_description").getValue()).getTopic()+" "+participate);
		getParameter("participate").setValue(new Boolean(participate));
	}
}
