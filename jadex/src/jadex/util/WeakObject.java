package jadex.util;

import java.io.Serializable;
import java.lang.ref.*;

/**
 *  Weakreference has the disadvantage that it does not support
 *  transparent equals() and hashcode() methods.
 *  WeakObject provides this support.
 */
public final class WeakObject extends WeakReference implements Serializable
{
	//-------- attributes --------

	/** The hash code. */
	protected final int hashcode;

	//-------- constructors --------

	/**
	 *  Construct a new reference.
	 *  @param object Object to reference.
	 */
	public WeakObject(Object object)
	{
		super(object);
		hashcode = object.hashCode();
	}


	/**
	 * Construct a <tt>WeakObject</tt>.
	 * @param obj Object to reference.
	 * @param queue Reference queue.
	 */
	public WeakObject(Object obj, ReferenceQueue queue)
	{
		super(obj, queue);
		hashcode = obj.hashCode();
	}

	//-------- methods --------

	/**
	 *  Check the equality of an object with this.
	 *  @param object Object to test equality with.
	 *  @return True if object is equal.
	 */
	public boolean equals(final Object object)
	{
		// Check identity for speed.
		boolean ret = object==this;

		if(!ret && object instanceof WeakObject)
		{
			WeakObject wr = (WeakObject)object;
			
			// Check hashcodes for speed.
			if(this.hashcode==wr.hashcode)
			{
				// Compare referenced objects.
				Object a = this.get();
				Object b = wr.get();
				ret = a!=null && b!=null && a.equals(b);
			}
		}
		return ret;
	}

	/**
	 *  Return the hash code of the nested object.
	 *  @return The hash code of the nested object.
	 */
	public int hashCode()
	{
		return hashcode;
	}
}

