package jadex.bdi.planlib.cms;

import jadex.bdi.runtime.IBDIInternalAccess;
import jadex.bdi.runtime.Plan;
import jadex.bridge.ComponentTerminatedException;
import jadex.bridge.ICMSComponentListener;
import jadex.bridge.IComponentDescription;
import jadex.bridge.IComponentListener;
import jadex.bridge.IComponentManagementService;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.ChangeEvent;
import jadex.commons.IFuture;
import jadex.commons.service.SServiceProvider;

/**
 *  Update the belief set containing the local components.
 */
public class CMSLocalUpdateComponentsPlan extends Plan
{ 
	//-------- attributes --------
	
	/** The listener. */
	protected ICMSComponentListener	listener;
	
	//-------- methods --------
	
	/**
	 *  The body method.
	 */
	public void body()
	{
		final IComponentManagementService	ces	= (IComponentManagementService)SServiceProvider.getServiceUpwards(
			getScope().getServiceProvider(), IComponentManagementService.class).get(this);
		this.listener	= new ICMSComponentListener()
		{
			public void componentAdded(final IComponentDescription desc)
			{
				try
				{
					getExternalAccess().scheduleStep(new IComponentStep()
					{
						public Object execute(IInternalAccess ia)
						{
							((IBDIInternalAccess)ia).getBeliefbase().getBeliefSet("components").addFact(desc);
							return null;
						}
					});
				}
				catch(ComponentTerminatedException ate)
				{
					ces.removeComponentListener(null, this);
				}
			}
					
			public void componentRemoved(final IComponentDescription desc, java.util.Map results)
			{
				try
				{
					getExternalAccess().scheduleStep(new IComponentStep()
					{
						public Object execute(IInternalAccess ia)
						{
							((IBDIInternalAccess)ia).getBeliefbase().getBeliefSet("components").removeFact(desc);
							return null;
						}
					});
				}
				catch(ComponentTerminatedException ate)
				{
					ces.removeComponentListener(null, this);
				}
			}

			public void componentChanged(IComponentDescription desc)
			{
			}
		};
		
		ces.addComponentListener(null, listener);
		
		IFuture fut = ces.getComponentDescriptions();
		IComponentDescription[] descs = (IComponentDescription[])fut.get(this);
		getBeliefbase().getBeliefSet("components").addFacts(descs);
		
		getScope().addComponentListener(new IComponentListener()
		{
			
			public void componentTerminating(ChangeEvent ae)
			{
				ces.removeComponentListener(null, listener);
			}
			
			public void componentTerminated(ChangeEvent ae)
			{
			}
		});
	}
}
