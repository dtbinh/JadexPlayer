/*
 * AMSAgentDescriptionData.java
 *
 * Generated by Protege plugin Beanynizer. 
 * Changes will be lost! 
 */
package jadex.adapter.fipa;



/**
 *  Java class for concept AMSAgentDescription of beanynizer_beans_fipa_default ontology.
 */
public abstract class AMSAgentDescriptionData implements java.beans.BeanInfo , java.io.Serializable 
{
	//-------- constants ----------

	/** Predefined value "initiated" for slot state. */
	public static String  STATE_INITIATED  = "initiated";
	/** Predefined value "active" for slot state. */
	public static String  STATE_ACTIVE  = "active";
	/** Predefined value "suspended" for slot state. */
	public static String  STATE_SUSPENDED  = "suspended";
	/** Predefined value "waiting" for slot state. */
	public static String  STATE_WAITING  = "waiting";
	/** Predefined value "transit" for slot state. */
	public static String  STATE_TRANSIT  = "transit";
	/** Predefined value "terminated" for slot state. */
	public static String  STATE_TERMINATED  = "terminated";
	/** Predefined value "terminating" for slot state. */
	public static String  STATE_TERMINATING  = "terminating";

	//-------- attributes ----------

	/** Attribute for slot state. */
	protected  String  state;

	/** Attribute for slot agentidentifier. */
	protected  AgentIdentifier  name;

	/** Attribute for slot ownership. */
	protected  String  ownership;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>AMSAgentDescription</code>.
	 */
	public AMSAgentDescriptionData()  {
	}

	//-------- accessor methods --------

	/**
	 *  Get the state of this AMSAgentDescription.
	 * @return state
	 */
	public String  getState() {
		return this.state;
	}

	/**
	 *  Set the state of this AMSAgentDescription.
	 * @param state the value to be set
	 */
	public void  setState(String state) {
		this.state = state;
	}

	/**
	 *  Get the agentidentifier of this AMSAgentDescription.
	 * @return agentidentifier
	 */
	public AgentIdentifier  getName() {
		return this.name;
	}

	/**
	 *  Set the agentidentifier of this AMSAgentDescription.
	 * @param name the value to be set
	 */
	public void  setName(AgentIdentifier name) {
		this.name = name;
	}

	/**
	 *  Get the ownership of this AMSAgentDescription.
	 * @return ownership
	 */
	public String  getOwnership() {
		return this.ownership;
	}

	/**
	 *  Set the ownership of this AMSAgentDescription.
	 * @param ownership the value to be set
	 */
	public void  setOwnership(String ownership) {
		this.ownership = ownership;
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
					 new java.beans.PropertyDescriptor("state", this.getClass(), "getState", "setState")
					, new java.beans.PropertyDescriptor("name", this.getClass(), "getName", "setName")
					, new java.beans.PropertyDescriptor("ownership", this.getClass(), "getOwnership", "setOwnership")
				};
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
	 *  Get a string representation of this AMSAgentDescription.
	 *  @return The string representation.
	 */
	public String toString() {
		return "AMSAgentDescription("
           + ")";
	}

}
