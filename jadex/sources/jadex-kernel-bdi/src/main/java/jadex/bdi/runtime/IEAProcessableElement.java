package jadex.bdi.runtime;

import jadex.commons.IFuture;

/**
 *  Interface for all processable elements (goals, internal and message events).
 */
public interface IEAProcessableElement extends IEAParameterElement
{
	//-------- BDI event properties --------

	/**
	 *  Is it a post-to-all event.
	 *  @return True, if post-to-all is set.
	 */
	public IFuture isPostToAll();

	/**
	 *  Get the random selection flag.
	 *  @return True, when applicable
	 *  selection is random style.
	 */
	public IFuture isRandomSelection();
}

