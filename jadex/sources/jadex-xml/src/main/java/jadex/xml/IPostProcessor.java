package jadex.xml;

/**
 *  Post-processes objects after an XML has been loaded.
 */
public interface IPostProcessor
{
	//-------- methods --------
	
	/**
	 *  Post-process an object after an XML has been loaded.
	 *  @param context The context.
	 *  @param object The object to post process.
	 *  @return A possibly other object for replacing the original. 
	 *  		Null for no change.
	 *  		Only possibly when processor is applied in first pass.
	 */
	public Object postProcess(IContext context, Object object);
	
	/**
	 *  Get the pass number.
	 *  @return The pass number (starting with 0 for first pass).
	 */
	public int getPass();
}
