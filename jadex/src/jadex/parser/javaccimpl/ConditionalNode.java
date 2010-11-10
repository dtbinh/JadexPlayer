package jadex.parser.javaccimpl;

import jadex.util.SReflect;

import java.util.Map;


/**
 *  Conditional node returns the value of it's second or third
 *  child node depending on the truth value of it's first child.
 */
public class ConditionalNode	extends ExpressionNode
{
	//-------- constructors --------

	/**
	 *  Create a node.
	 *  @param p	The parser.
	 *  @param id	The id.
	 */
	public ConditionalNode(ParserImpl p, int id)
	{
		super(p, id);
	}

	//-------- evaluation --------

	/**
	 *  Check argument types, and precompute expression
	 *  when some children are constant.
	 */
	public void precompile()
	{
		// Check number of children and operator type.
		if(jjtGetNumChildren()!=3)
			throw new ParseException("Wrong number of subterms for expression: "+this);

		ExpressionNode	choice	= (ExpressionNode)jjtGetChild(0);
		ExpressionNode	node1	= (ExpressionNode)jjtGetChild(1);
		ExpressionNode	node2	= (ExpressionNode)jjtGetChild(2);

		// Check type of children.
		Class	clazz	= choice.getStaticType();
		if(clazz!=null && clazz.isPrimitive())
		{
			clazz	= SReflect.getWrappedType(clazz);
		}
		if(clazz!=null && clazz!=Boolean.class)
		{
			throw new ParseException("First term not boolean: "+this);
		}

		// Precompute constant value, when children are constant.
		if(choice.isConstant())
		{
			try
			{
				boolean	val	= ((Boolean)choice.getValue(null)).booleanValue();
				if(val)
				{
					if(node1.isConstant())
					{
						setConstantValue(node1.getValue(null));
						setConstant(true);
					}
					else
					{
						setStaticType(node1.getStaticType());
					}
				}
				else
				{
					if(node2.isConstant())
					{
						setConstantValue(node2.getValue(null));
						setConstant(true);
					}
					else
					{
						setStaticType(node2.getStaticType());
					}
				}
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 *  Evaluate the term.
	 *  @param params	The parameters (string, value).
	 *  @return	The value of the term.
	 * @throws Exception 
	 */
	public Object	getValue(Map params) throws Exception
	{
		if(isConstant())
		{
			return getConstantValue();
		}

		Object	ret;
		ExpressionNode	choice	= (ExpressionNode)jjtGetChild(0);
		ExpressionNode	node1	= (ExpressionNode)jjtGetChild(1);
		ExpressionNode	node2	= (ExpressionNode)jjtGetChild(2);
		boolean	val	= ((Boolean)choice.getValue(params)).booleanValue();
		if(val)
		{
			ret	= node1.getValue(params);
		}
		else
		{
			ret	= node2.getValue(params);
		}

		return ret;
	}

	/**
	 *  Create a string representation of this node and its subnodes.
	 *  @return A string representation of this node.
	 */
	public String toPlainString()
	{
		return subnodeToString(0) + " ? " + subnodeToString(1) + " : " + subnodeToString(2);
	}
}

