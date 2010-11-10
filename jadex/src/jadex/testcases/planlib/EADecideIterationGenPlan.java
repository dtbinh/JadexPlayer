package jadex.testcases.planlib;

import jadex.planlib.IOfferGenerator;
import jadex.runtime.Plan;

/**
 *  Decide about the next round and the cfp for it.
 */
public class EADecideIterationGenPlan extends Plan
{	
	/**
	 *  The plan body.
	 */
	public void body()
	{
		IOfferGenerator gen = (IOfferGenerator)getParameter("cfp_info").getValue();
		gen.setNextRound();
		
		// Use offer only when limit was not reached yet.
		if(!gen.getLimitOffer().equals(gen.getLastOffer()))
			getParameter("cfp").setValue(gen.getCurrentOffer());
	}
}