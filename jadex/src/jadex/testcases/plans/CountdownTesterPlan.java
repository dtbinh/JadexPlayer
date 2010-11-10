package jadex.testcases.plans;

import jadex.runtime.Plan;
import jadex.planlib.TestReport;

/**
 *  Plan that tests if the countdown plan has been terminated in
 *  consequence of an invalid context condition.
 */
public class CountdownTesterPlan extends Plan
{
	/**
	 * The body method is called on the
	 * instatiated plan instance from the scheduler.
	 */
	public void body()
	{
		waitFor(300);
		TestReport tr = new TestReport("#1", "Test plan context condition.");
		if(getPlanbase().getPlans().length==1)
		{
			tr.setSucceeded(true);
		}
		else
		{
			tr.setReason("Plan was not terminated on invalid context.");
		}
		getBeliefbase().getBeliefSet("reports").addFact(tr);
	}
}
