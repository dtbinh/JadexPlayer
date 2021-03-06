package jadex.micro.benchmarks;

import jadex.bridge.ComponentResultListener;
import jadex.bridge.CreationInfo;
import jadex.bridge.IArgument;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentManagementService;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.concurrent.CounterResultListener;
import jadex.commons.concurrent.DefaultResultListener;
import jadex.commons.concurrent.IResultListener;
import jadex.commons.service.SServiceProvider;
import jadex.commons.service.clock.IClockService;
import jadex.micro.MicroAgent;
import jadex.micro.MicroAgentMetaInfo;

import java.util.HashMap;
import java.util.Map;

/**
 *  Agent creation benchmark. 
 */
public class ParallelAgentCreationAgent extends MicroAgent
{
	//-------- methods --------
	
	/**
	 *  Execute an agent step.
	 */
	public void executeBody()
	{
		Map arguments = getArguments();			
		final int num	= ((Integer)arguments.get("num")).intValue();
		if(num>0)
		{
			SServiceProvider.getServiceUpwards(getServiceProvider(), IComponentManagementService.class).addResultListener(new ComponentResultListener(new DefaultResultListener()
			{
				public void resultAvailable(Object source, Object result)
				{
					final IComponentManagementService	cms	= (IComponentManagementService)result;
					SServiceProvider.getService(getServiceProvider(), IClockService.class).addResultListener(createResultListener(new DefaultResultListener()
					{
						public void resultAvailable(Object source, Object result)
						{
							final IClockService	clock	= (IClockService)result;
							final long	startmem	= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
							final long	starttime	= clock.getTime();
							final long[]	omem	= new long[1];
							final double[]	dur	= new double[1];
							final long[]	killstarttime	= new long[1];
							
							IResultListener	creationlis	= new CounterResultListener(num, new IResultListener()
							{
								public void resultAvailable(Object source, Object result)
								{
									scheduleStep(new IComponentStep()
									{
										public Object execute(IInternalAccess ia)
										{
											long used = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
											omem[0] = (used-startmem)/1024;
											double upera = ((long)(1000*omem[0]/num))/1000.0;
											System.out.println("Overall memory usage: "+omem[0]+"kB. Per agent: "+upera+" kB.");
											long end = clock.getTime();
											System.out.println("Last peer created. "+num+" agents started.");
											dur[0] = ((double)end-starttime)/1000.0;
											double pera = dur[0]/num;
											System.out.println("Needed: "+dur[0]+" secs. Per agent: "+pera+" sec. Corresponds to "+(1/pera)+" agents per sec.");
											
											killstarttime[0]	= clock.getTime();
											for(int i=num; i>0; i--)
											{
												String name = createPeerName(i);
												IComponentIdentifier cid = cms.createComponentIdentifier(name, true, null);
												cms.destroyComponent(cid);	// Kill listener already added on create.
											}		
											return null;
										}
									});
								}
								
								public void exceptionOccurred(Object source, final Exception exception)
								{
									scheduleStep(new IComponentStep()
									{
										public Object execute(IInternalAccess ia)
										{
											if(exception instanceof RuntimeException)
												throw (RuntimeException)exception;
											else
												throw new RuntimeException(exception);
										}
									});
								}
							})
							{
								public void intermediateResultAvailable(Object source, Object result)
								{
									System.out.println("Created peer: "+getCnt());
								}
							};
							
							
							IResultListener	killlis	= new CounterResultListener(num, new IResultListener()
							{
								public void resultAvailable(Object source, Object result)
								{
									scheduleStep(new IComponentStep()
									{
										public Object execute(IInternalAccess ia)
										{
											long killend = clock.getTime();
											System.out.println("Last peer destroyed. "+num+" agents killed.");
											double killdur = ((double)killend-killstarttime[0])/1000.0;
											double killpera = killdur/num;
											
											Runtime.getRuntime().gc();
											long stillused = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024;
											double pera = dur[0]/num;
											double upera = ((long)(1000*omem[0]/num))/1000.0;

											System.out.println("\nCumulated results:");
											System.out.println("Creation needed: "+dur[0]+" secs. Per agent: "+pera+" sec. Corresponds to "+(1/pera)+" agents per sec.");
											System.out.println("Killing needed:  "+killdur+" secs. Per agent: "+killpera+" sec. Corresponds to "+(1/killpera)+" agents per sec.");
											System.out.println("Overall memory usage: "+omem[0]+"kB. Per agent: "+upera+" kB.");
											System.out.println("Still used memory: "+stillused+"kB.");
									
											killAgent();
											
											return null;
										}
									});
								}
								
								public void exceptionOccurred(Object source, final Exception exception)
								{
									scheduleStep(new IComponentStep()
									{
										public Object execute(IInternalAccess ia)
										{
											if(exception instanceof RuntimeException)
												throw (RuntimeException)exception;
											else
												throw new RuntimeException(exception);
										}
									});
								}
							})
							{
								public void intermediateResultAvailable(Object source, Object result)
								{
									System.out.println("Successfully destroyed peer: "+source);
								}
							};
							
							Map	args	= new HashMap();
							args.put("num", new Integer(0));
							CreationInfo	cinfo	= new CreationInfo(args);
							for(int i=1; i<=num; i++)
							{
								cms.createComponent(createPeerName(i), ParallelAgentCreationAgent.this.getClass().getName()+".class", cinfo, killlis)
									.addResultListener(creationlis);
							}
						}
					}));
				}
			}, getAgentAdapter()));

		}
	}

	/**
	 *  Create a name for a peer with a given number.
	 */
	protected String createPeerName(int num)
	{
		return getComponentIdentifier().getLocalName() + "Peer_#" + num;
	}
	
	/**
	 *  Get the meta information about the agent.
	 */
	public static Object getMetaInfo()
	{
		return new MicroAgentMetaInfo("This agents benchmarks parallel agent creation and termination.", 
			new String[0],
			new IArgument[]{new IArgument()
		{
			public Object getDefaultValue(String configname)
			{
				return new Integer(10000);
			}
			public String getDescription()
			{
				return "Number of agents to create.";
			}
			public String getName()
			{
				return "num";
			}
			public String getTypename()
			{
				return "Integer";
			}
			public boolean validate(String input)
			{
				boolean ret = true;
				try
				{
					Integer.parseInt(input);
				}
				catch(Exception e)
				{
					ret = false;
				}
				return ret;
			}
		}}, null, null, null);
	}
}
