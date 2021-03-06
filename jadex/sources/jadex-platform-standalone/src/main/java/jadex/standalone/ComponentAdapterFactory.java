package jadex.standalone;

import jadex.bridge.IComponentAdapter;
import jadex.bridge.IComponentAdapterFactory;
import jadex.bridge.IComponentDescription;
import jadex.bridge.IComponentInstance;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IModelInfo;

/**
 *  Standalone version of the adapter factory.
 */
public class ComponentAdapterFactory implements IComponentAdapterFactory
{
	/**
	 *  Create a component adapter for a component instance.
	 *  @param desc The component description.
	 *  @param model The component model.
	 *  @param instance The component instance.
	 *  @param parent The external access of the component's parent.
	 *  @return The component adapter.
	 */
	public IComponentAdapter createComponentAdapter(IComponentDescription desc, IModelInfo model, IComponentInstance instance, IExternalAccess parent)
	{
		return new StandaloneComponentAdapter(desc, model, instance, parent);
	}
	
	/**
	 *  Execute a step of the component via triggering the adapter.
	 *  @param adapter The component adapter.
	 *  @return true, if component wants to be executed again. 
	 */
	public boolean executeStep(IComponentAdapter adapter)
	{
		return ((StandaloneComponentAdapter)adapter).execute();
	}
}
