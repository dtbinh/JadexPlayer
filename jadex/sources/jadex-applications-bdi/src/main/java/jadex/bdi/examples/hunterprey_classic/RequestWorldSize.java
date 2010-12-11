/*
 * RequestWorldSize.java Generated by Protege plugin Beanynizer. Changes will be lost!
 */
package jadex.bdi.examples.hunterprey_classic;

import jadex.base.fipa.IComponentAction;


/**
 *  Java class for concept RequestWorldSize of hunterprey_beans ontology.
 */
public class RequestWorldSize implements IComponentAction
{
	//-------- attributes ----------

	/** The height of the world. */
	protected int height;

	/** The width of the world. */
	protected int width;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>RequestWorldSize</code>.
	 */
	public RequestWorldSize()
	{
	}

	//-------- accessor methods --------

	/**
	 *  Get the height of this RequestWorldSize.
	 *  The height of the world.
	 * @return height
	 */
	public int getHeight()
	{
		return this.height;
	}

	/**
	 *  Set the height of this RequestWorldSize.
	 *  The height of the world.
	 * @param height the value to be set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 *  Get the width of this RequestWorldSize.
	 *  The width of the world.
	 * @return width
	 */
	public int getWidth()
	{
		return this.width;
	}

	/**
	 *  Set the width of this RequestWorldSize.
	 *  The width of the world.
	 * @param width the value to be set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	//-------- object methods --------

	/**
	 *  Get a string representation of this RequestWorldSize.
	 *  @return The string representation.
	 */
	public String toString()
	{
		return "RequestWorldSize(" + ")";
	}

}
