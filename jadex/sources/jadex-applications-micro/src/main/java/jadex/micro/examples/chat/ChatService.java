package jadex.micro.examples.chat;

import jadex.bridge.IExternalAccess;
import jadex.commons.ChangeEvent;
import jadex.commons.IChangeListener;
import jadex.commons.concurrent.DefaultResultListener;
import jadex.commons.service.BasicService;
import jadex.commons.service.SServiceProvider;
import jadex.micro.IMicroExternalAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 */
public class ChatService extends BasicService implements IChatService
{
	//-------- attributes --------
	
	/** The agent. */
	protected IMicroExternalAccess agent;
	
	/** The listeners. */
	protected List listeners;
	
	//-------- constructors --------
	
	/**
	 *  Create a new helpline service.
	 */
	public ChatService(IExternalAccess agent)
	{
		super(agent.getServiceProvider().getId(), IChatService.class, null);
		this.agent = (IMicroExternalAccess)agent;
		this.listeners = Collections.synchronizedList(new ArrayList());
	}
	
	//-------- methods --------
	
	/**
	 *  Tell something.
	 *  @param name The name.
	 *  @param text The text.
	 */
	public void tell(final String name, final String text)
	{
		SServiceProvider.getServices(agent.getServiceProvider(), IChatService.class, true, true)
			.addResultListener(new DefaultResultListener()
		{
			public void resultAvailable(Object source, Object result)
			{
				if(result!=null)
				{
					for(Iterator it=((Collection)result).iterator(); it.hasNext(); )
					{
						IChatService cs = (IChatService)it.next();
						cs.hear(name, text);
					}
				}
			}
		});
	}
	
	/**
	 *  Hear something.
	 *  @param name The name.
	 *  @param text The text.
	 */
	public void hear(String name, String text)
	{
		IChangeListener[] lis = (IChangeListener[])listeners.toArray(new IChangeListener[0]);
		for(int i=0; i<lis.length; i++)
		{
			lis[i].changeOccurred(new ChangeEvent(this, null, new String[]{name, text}));
		}
	}
	
	/**
	 *  Add a change listener.
	 */
	public void addChangeListener(IChangeListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 *  Remove a change listener.
	 */
	public void removeChangeListener(IChangeListener listener)
	{
		listeners.remove(listener);
	}
	
	/**
	 *  Get the string representation.
	 *  @return The string representation.
	 */
	public String toString()
	{
		return "ChatService, "+agent.getComponentIdentifier();
	}
}
