/*
 * SearchConstraints.java
 *
 * Generated by Protege plugin Beanynizer. 
 * Changes will be lost! 
 */
package jadex.adapter.fipa;



/**
 *  Java class for concept SearchConstraints of beanynizer_beans_fipa_default ontology.
 */
public class SearchConstraints implements java.beans.BeanInfo , java.io.Serializable 
{
	//-------- constants ----------

	//-------- attributes ----------

	/** Attribute for slot search-id. */
	protected  String  searchid;

	/** Attribute for slot max-results. */
	protected  int  maxresults  = 1;

	/** Attribute for slot max-depth. */
	protected  int  maxdepth  = 0;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>SearchConstraints</code>.
	 */
	public SearchConstraints()  {
	}

	//-------- accessor methods --------

	/**
	 *  Get the search-id of this SearchConstraints.
	 * @return search-id
	 */
	public String  getSearchId() {
		return this.searchid;
	}

	/**
	 *  Set the search-id of this SearchConstraints.
	 * @param searchid the value to be set
	 */
	public void  setSearchId(String searchid) {
		this.searchid = searchid;
	}

	/**
	 *  Get the max-results of this SearchConstraints.
	 * @return max-results
	 */
	public int  getMaxResults() {
		return this.maxresults;
	}

	/**
	 *  Set the max-results of this SearchConstraints.
	 * @param maxresults the value to be set
	 */
	public void  setMaxResults(int maxresults) {
		this.maxresults = maxresults;
	}

	/**
	 *  Get the max-depth of this SearchConstraints.
	 * @return max-depth
	 */
	public int  getMaxDepth() {
		return this.maxdepth;
	}

	/**
	 *  Set the max-depth of this SearchConstraints.
	 * @param maxdepth the value to be set
	 */
	public void  setMaxDepth(int maxdepth) {
		this.maxdepth = maxdepth;
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
					 new java.beans.PropertyDescriptor("searchId", this.getClass(), "getSearchId", "setSearchId")
					, new java.beans.PropertyDescriptor("maxResults", this.getClass(), "getMaxResults", "setMaxResults")
					, new java.beans.PropertyDescriptor("maxDepth", this.getClass(), "getMaxDepth", "setMaxDepth")
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
	 *  Get a string representation of this SearchConstraints.
	 *  @return The string representation.
	 */
	public String toString() {
		return "SearchConstraints("
           + ")";
	}

}
