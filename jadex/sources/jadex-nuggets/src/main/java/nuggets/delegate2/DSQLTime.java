/*
 * DString.java
 * Copyright (c) 2005 by University of Hamburg. All Rights Reserved.
 * Departament of Informatics. 
 * Distributed Systems and Information Systems.
 *
 * Created by walczak on Jan 17, 2006.  
 * Last revision $Revision: 4687 $ by:
 * $Author: walczak $ on $Date: 2006-12-21 11:38:59 +0100 (Do, 21 Dez 2006) $.
 */
package nuggets.delegate2;

import java.sql.Time;

import nuggets.IAssembler;
import nuggets.ICruncher;
import nuggets.delegate.ADelegate;


/** DString 
 * @author walczak
 * @since  Jan 17, 2006
 */
public class DSQLTime extends ADelegate
{
	/** 
	 * @param clazz
	 * @param asm
	 * @return a new instance of this array
	 * @throws Exception
	 */
	public Object getInstance(Class clazz, IAssembler asm) throws Exception
	{
		return new Time(Long.parseLong((String)asm.getAttributeValue("v")));
	}

	/** 
	 * @param o
	 * @param mill
	 * @see nuggets.delegate.ASimpleDelegate#persist(java.lang.Object, nuggets.ICruncher)
	 */
	public void persist(Object o, ICruncher mill, ClassLoader classloader)
	{
		mill.startConcept(o);
		mill.put("v", Long.toString(((Time)o).getTime()));
	}
//
//	/** 
//	 * @param exp
//	 * @return a call to the format method
//	 * @see nuggets.delegate.ASimpleDelegate#getMarshallString(java.lang.String)
//	 */
//	public String getMarshallString(String exp)
//	{
//		return "Long.toString((" + exp + ").getTime())";
//	}

	/** 
	 * @param clazz
	 * @param o
	 * @return the name of the class
	 * @see nuggets.delegate.ADelegate#marshall(java.lang.Class, java.lang.Object)
	 */
	public String marshall(Class clazz, Object o)
	{
		return Long.toString(((Time)o).getTime());
	}
//
//	/** 
//	 * @param exp
//	 * @return a call to the parse method
//	 * @see nuggets.delegate.ASimpleDelegate#getUnmarshallString(String, java.lang.String)
//	 */
//	public String getUnmarshallString(String className, String exp)
//	{
//		return "new java.sql.Time(Long.parseLong((String)" + exp + "))";
//	}

//
//	/** 
//	 * @param clazz
//	 * @param value
//	 * @return the boolean expression
//	 * @see nuggets.delegate.ASimpleDelegate#unmarshall(java.lang.Class, java.lang.Object)
//	 */
//	public Object unmarshall(Class clazz, Object value)
//	{
//		return new Time(Long.parseLong((String)value));
//	}
}
