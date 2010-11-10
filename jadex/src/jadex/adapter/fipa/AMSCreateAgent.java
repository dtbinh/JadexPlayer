/*
 * AMSCreateAgent.java
 *
 * Generated by Protege plugin Beanynizer. 
 * Changes will be lost! 
 */
package jadex.adapter.fipa;



/**
 *  Java class for concept AMSCreateAgent of beanynizer_beans_fipa_default ontology.
 */
public class AMSCreateAgent	extends AgentAction implements java.beans.BeanInfo 
{
	//-------- constants ----------

	//-------- attributes ----------

	/** Attribute for slot type. */
	protected  String  type;

	/** Attribute for slot configuration. */
	protected  String  configuration;

	/** Attribute for slot agentidentifier. */
	protected  AgentIdentifier  agentidentifier;

	/** Attribute for slot name. */
	protected  String  name;

	/** Attribute for slot arguments. */
	protected  java.util.Map  arguments;

	/** Attribute for slot start. */
	protected  boolean  start  = true;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>AMSCreateAgent</code>.
	 */
	public AMSCreateAgent()  {
	}

	//-------- accessor methods --------

	/**
	 *  Get the type of this AMSCreateAgent.
	 * @return type
	 */
	public String  getType() {
		return this.type;
	}

	/**
	 *  Set the type of this AMSCreateAgent.
	 * @param type the value to be set
	 */
	public void  setType(String type) {
		this.type = type;
	}

	/**
	 *  Get the configuration of this AMSCreateAgent.
	 * @return configuration
	 */
	public String  getConfiguration() {
		return this.configuration;
	}

	/**
	 *  Set the configuration of this AMSCreateAgent.
	 * @param configuration the value to be set
	 */
	public void  setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	/**
	 *  Get the agentidentifier of this AMSCreateAgent.
	 * @return agentidentifier
	 */
	public AgentIdentifier  getAgentIdentifier() {
		return this.agentidentifier;
	}

	/**
	 *  Set the agentidentifier of this AMSCreateAgent.
	 * @param agentidentifier the value to be set
	 */
	public void  setAgentIdentifier(AgentIdentifier agentidentifier) {
		this.agentidentifier = agentidentifier;
	}

	/**
	 *  Get the name of this AMSCreateAgent.
	 * @return name
	 */
	public String  getName() {
		return this.name;
	}

	/**
	 *  Set the name of this AMSCreateAgent.
	 * @param name the value to be set
	 */
	public void  setName(String name) {
		this.name = name;
	}

	/**
	 *  Get the arguments of this AMSCreateAgent.
	 * @return arguments
	 */
	public java.util.Map  getArguments() {
		return this.arguments;
	}

	/**
	 *  Set the arguments of this AMSCreateAgent.
	 * @param arguments the value to be set
	 */
	public void  setArguments(java.util.Map arguments) {
		this.arguments = arguments;
	}

	/**
	 *  Get the start of this AMSCreateAgent.
	 * @return start
	 */
	public boolean  isStart() {
		return this.start;
	}

	/**
	 *  Set the start of this AMSCreateAgent.
	 * @param start the value to be set
	 */
	public void  setStart(boolean start) {
		this.start = start;
	}

	//-------- bean related methods --------

	/** The property descriptors, constructed on first access. */
	private java.beans.PropertyDescriptor[] pds = null;

	/**
	 *  Get the bean descriptor.
	 *  @return The bean descriptor.
	 */
	public java.beans.BeanDescriptor getBeanDescriptor() {
		return null;
	}

	/**
	 *  Get the property descriptors.
	 *  @return The property descriptors.
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		if(pds==null) {
			try {
				pds = new java.beans.PropertyDescriptor[]{
					 new java.beans.PropertyDescriptor("type", this.getClass(), "getType", "setType")
					, new java.beans.PropertyDescriptor("configuration", this.getClass(), "getConfiguration", "setConfiguration")
					, new java.beans.PropertyDescriptor("agentidentifier", this.getClass(), "getAgentIdentifier", "setAgentIdentifier")
					, new java.beans.PropertyDescriptor("name", this.getClass(), "getName", "setName")
					, new java.beans.PropertyDescriptor("arguments", this.getClass(), "getArguments", "setArguments")
					, new java.beans.PropertyDescriptor("start", this.getClass(), "isStart", "setStart")
				};
				java.beans.PropertyDescriptor[] spds = super.getPropertyDescriptors();
				int	l1	= pds.length;
				int	l2	= spds.length;
				Object	res	= java.lang.reflect.Array.newInstance(java.beans.PropertyDescriptor.class, l1+l2);
				System.arraycopy(pds, 0, res, 0, l1);
				System.arraycopy(spds, 0, res, l1, l2);
				pds = (java.beans.PropertyDescriptor[])res;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return pds;
	}

	/**
	 *  Get the default property index.
	 *  @return The property index.
	 */
	public int getDefaultPropertyIndex() {
		return -1;
	}

	/**
	 *  Get the event set descriptors.
	 *  @return The event set descriptors.
	 */
	public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
		return null;
	}

	/**
	 *  Get the default event index.
	 *  @return The default event index.
	 */
	public int getDefaultEventIndex() {
		return -1;
	}

	/**
	 *  Get the method descriptors.
	 *  @return The method descriptors.
	 */
	public java.beans.MethodDescriptor[] getMethodDescriptors() {
		return null;
	}

	/**
	 *  Get additional bean info.
	 *  @return Get additional bean info.
	 */
	public java.beans.BeanInfo[] getAdditionalBeanInfo() {
		return null;
	}

	/**
	 *  Get the icon.
	 *  @return The icon.
	 */
	public java.awt.Image getIcon(int iconKind) {
		return null;
	}

	/**
	 *  Load the image.
	 *  @return The image.
	 */
	public java.awt.Image loadImage(final String resourceName) {
		try {
			final Class c = getClass();
			java.awt.image.ImageProducer ip = (java.awt.image.ImageProducer)
				java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {
					public Object run(){
						java.net.URL url;
						if((url = c.getResource(resourceName))==null) {
							return null;
						}
						else {
							try {
								return url.getContent();
							}
							catch(java.io.IOException ioe) {
								return null;
							}
						}
					}
				});
			if(ip==null)
				return null;
			java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
			return tk.createImage(ip);
		}
		catch(Exception ex) {
			return null;
		}
	}

	//-------- additional methods --------

	/**
	 *  Get a string representation of this AMSCreateAgent.
	 *  @return The string representation.
	 */
	public String toString() {
		return "AMSCreateAgent("
           + ")";
	}

}