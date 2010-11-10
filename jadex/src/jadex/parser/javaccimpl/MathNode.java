package jadex.parser.javaccimpl;

import jadex.util.SReflect;

import java.util.Map;


/**
 *  Math node performs mathematical operations on it's (one or two) child nodes.
 */
// Todo: support addition of string???
public class MathNode	extends ExpressionNode
{
	//-------- constants --------

	/** The plus operator. */
	public static final int	ADD	= 1;

	/** The minus operator. */
	public static final int	SUBSTRACT	= 2;

	/** The multiply operator. */
	public static final int	MULTIPLY	= 3;

	/** The divide operator. */
	public static final int	DIVIDE	= 4;

	/** The modula operator. */
	public static final int	MODULO	= 5;

	/** The bitwise and operator. */
	public static final int	AND	= 6;

	/** The bitwise or operator. */
	public static final int	OR	= 7;

	/** The bitwise xor operator. */
	public static final int	XOR = 8;

	/** The bitwise not operator (unary). */
	public static final int	NOT	= 9;

	/** The left shift operator. */
	public static final int	LSHIFT	= 10;

	/** The right shift operator. */
	public static final int	RSHIFT	= 11;

	/** The unsinged right shift operator. */
	public static final int	URSHIFT	= 12;

	//-------- attributes --------

	/** The operation. */
	protected int op;

	//-------- constructors --------

	/**
	 *  Create a node.
	 *  @param p	The parser.
	 *  @param id	The id.
	 */
	public MathNode(ParserImpl p, int id)
	{
		super(p, id);
	}

	//-------- attribute accessors --------

	/**
	 *  Set the token text.
	 *  @param text	The token text.
	 */
	public void	setText(String text)
	{
		super.setText(text);
		this.op	= fromString(text);
	}

	//-------- evaluation --------

	/**
	 *  Precompute the static type and constant value if possible.
	 */
	public void precompile()
	{
		if(jjtGetNumChildren()==1)
		{
			// Unary.
			precompileUnary();
		}
		else if(jjtGetNumChildren()==2)
		{
			// Binary: Check operator.
			switch(op)
			{
				case ADD:
				case SUBSTRACT:
				case MULTIPLY:
				case DIVIDE:
				case MODULO:
				case AND:
				case OR:
				case XOR:
				case LSHIFT:
				case RSHIFT:
				case URSHIFT:
					break;
				default:
					throw new ParseException("Unsupported binary operator: "+this);
			}

			// Determine static type.
			ExpressionNode	left	= (ExpressionNode)jjtGetChild(0);
			ExpressionNode	right	= (ExpressionNode)jjtGetChild(1);
			// Hack??? No distinction between wrapper and primitive types?
			Class	cleft	= left.getStaticType();
			cleft	= cleft!=null ? SReflect.getWrappedType(cleft) : null;
			Class	cright	= right.getStaticType();
			cright	= cright!=null ? SReflect.getWrappedType(cright) : null;
			if(cleft==null && cright==null)
 			{
				// Check ok, static type undeterminable.
			}
			else if((cleft==null || Number.class.isAssignableFrom(cleft))
			&& (cright==null || Number.class.isAssignableFrom(cright)))
			{
				// Check ok, must be some kind of number.
				// If both types are known, determine static type,
				// and check for operator applicability.
				if(cleft!=null && cright!=null)
				{
					if(cleft==Double.class || cright==Double.class)
					{
						switch(op)
						{
							case AND:
							case OR:
							case XOR:
							case LSHIFT:
							case RSHIFT:
							case URSHIFT:
								throw new ParseException("Operator "+toString(op)+" cannot be applied to floats: "+this);
						}
						setStaticType(Double.class);
					}
					else if(cleft==Float.class || cright==Float.class)
					{
						switch(op)
						{
							case AND:
							case OR:
							case XOR:
							case LSHIFT:
							case RSHIFT:
							case URSHIFT:
								throw new ParseException("Operator "+toString(op)+" cannot be applied to floats: "+this);
						}
						setStaticType(Float.class);
					}
					else if(cleft==Long.class || cright==Long.class)
					{
						setStaticType(Long.class);
					}
					else
					{
						setStaticType(Integer.class);
					}
				}
			}
			else if((cleft==null || Boolean.class==cleft)
			&& (cright==null || Boolean.class==cright))
			{
				// Check ok, must be some kind of boolean.
				if(cleft!=null && cright!=null)
				{
					setStaticType(Boolean.class);
				}
			}
			else if((cleft==null || Character.class==cleft)
			&& (cright==null || Character.class==cright))
			{
				// Check ok, must be some kind of number.
				if(cleft!=null && cright!=null)
				{
					setStaticType(Integer.class);
				}
			}
			else if(cleft==String.class && op==ADD)
			{
				// String concatenation: ok.
				setStaticType(String.class);
			}
			else
			{
				throw new ParseException("Incompatible types of subterms: "+this);
			}

			// Precompute constant value.
			if(left.isConstant() && right.isConstant())
			{
				try
				{
					this.setConstantValue(this.getValue(null));
					this.setConstant(true);
					if(getStaticType()==null && getConstantValue()!=null)
					{
						// Does this happen???
						setStaticType(getConstantValue().getClass());
					}
				}
				catch(Exception e)
				{
				}
			}
		}
		else
		{
			throw new ParseException("Wrong number of subterms: "+this);
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

		if(jjtGetNumChildren()==1)
		{
			return getUnaryValue(params);
		}
		// Get subterms.
		Object	left	= ((ExpressionNode)jjtGetChild(0)).getValue(params);
		Object	right	= ((ExpressionNode)jjtGetChild(1)).getValue(params);

		// Support concatenation of strings (Hack???).
		if(left instanceof String && op==ADD)
		{
			return ((String)left) + right;
		}
		// System.out.println("left: "+left);
		// System.out.println("right: "+right);
		// Hack !!! Support booleans for bitwise AND, OR, XOR.
		boolean	bool	= false;
		if((left instanceof Boolean) && (right instanceof Boolean)
			&& (op==AND || op==OR || op==XOR))
		{
			left	= new Integer(((Boolean)left).booleanValue()?1:0);
			right	= new Integer(((Boolean)right).booleanValue()?1:0);
			bool	= true;
		}
		// Hack ??? Support Character.
		if((left instanceof Character))
		{
			left	= new Integer(((Character)left).charValue());
		}
		if((right instanceof Character))
		{
			right	= new Integer(((Character)right).charValue());
		}
		if(!(left instanceof Number) && left!=null)	// Support null as number???
		{
			throw new RuntimeException("Left hand side of expression not number: "+this);
		}
		if(!(right instanceof Number) && right!=null)	// Support null as number???
		{
			throw new RuntimeException("Right hand side of expression not number: "+this);
		}

		// Determine return type and calculate value.
		Number	numleft	= left!=null ? (Number)left : new Integer(0);	// Support null as number???
		Number	numright	= right!=null ? (Number)right : new Integer(0);	// Support null as number???
		Object	value	= null;
		if(numleft instanceof Double || numright instanceof Double)
		{
			switch(op)
			{
				case ADD:
					value	= new Double(numleft.doubleValue() + numright.doubleValue());
					break;
				case SUBSTRACT:
					value	= new Double(numleft.doubleValue() - numright.doubleValue());
					break;
				case MULTIPLY:
					value	= new Double(numleft.doubleValue() * numright.doubleValue());
					break;
				case DIVIDE:
					value	= new Double(numleft.doubleValue() / numright.doubleValue());
					break;
				case MODULO:
					value	= new Double(numleft.doubleValue() % numright.doubleValue());
					break;
				case AND:
				case OR:
				case XOR:
				case LSHIFT:
				case RSHIFT:
				case URSHIFT:
					throw new RuntimeException("Operator "+toString(op)+" cannot be applied to floats: "+this);
			}
		}
		else if(numleft instanceof Float || numright instanceof Float)
		{
			switch(op)
			{
				case ADD:
					value	= new Float(numleft.floatValue() + numright.floatValue());
					break;
				case SUBSTRACT:
					value	= new Float(numleft.floatValue() - numright.floatValue());
					break;
				case MULTIPLY:
					value	= new Float(numleft.floatValue() * numright.floatValue());
					break;
				case DIVIDE:
					value	= new Float(numleft.floatValue() / numright.floatValue());
					break;
				case MODULO:
					value	= new Float(numleft.floatValue() % numright.floatValue());
					break;
				case AND:
				case OR:
				case XOR:
				case LSHIFT:
				case RSHIFT:
				case URSHIFT:
					throw new RuntimeException("Operator "+toString(op)+" cannot be applied to floats: "+this);
			}
		}
		else if(numleft instanceof Long || numright instanceof Long)
		{
			switch(op)
			{
				case ADD:
					value	= new Long(numleft.longValue() + numright.longValue());
					break;
				case SUBSTRACT:
					value	= new Long(numleft.longValue() - numright.longValue());
					break;
				case MULTIPLY:
					value	= new Long(numleft.longValue() * numright.longValue());
					break;
				case DIVIDE:
					value	= new Long(numleft.longValue() / numright.longValue());
					break;
				case MODULO:
					value	= new Long(numleft.longValue() % numright.longValue());
					break;
				case AND:
					value	= new Long(numleft.longValue() & numright.longValue());
					break;
				case OR:
					value	= new Long(numleft.longValue() | numright.longValue());
					break;
				case XOR:
					value	= new Long(numleft.longValue() ^ numright.longValue());
					break;
				case LSHIFT:
					value	= new Long(numleft.longValue() << numright.longValue());
					break;
				case RSHIFT:
					value	= new Long(numleft.longValue() >> numright.longValue());
					break;
				case URSHIFT:
					value	= new Long(numleft.longValue() >>> numright.longValue());
					break;
			}
		}
		else
		{
			switch(op)
			{
				case ADD:
					value	= new Integer(numleft.intValue() + numright.intValue());
					break;
				case SUBSTRACT:
					value	= new Integer(numleft.intValue() - numright.intValue());
					break;
				case MULTIPLY:
					value	= new Integer(numleft.intValue() * numright.intValue());
					break;
				case DIVIDE:
					value	= new Integer(numleft.intValue() / numright.intValue());
					break;
				case MODULO:
					value	= new Integer(numleft.intValue() % numright.intValue());
					break;
				case AND:
					value	= new Integer(numleft.intValue() & numright.intValue());
					break;
				case OR:
					value	= new Integer(numleft.intValue() | numright.intValue());
					break;
				case XOR:
					value	= new Integer(numleft.intValue() ^ numright.intValue());
					break;
				case LSHIFT:
					value	= new Integer(numleft.intValue() << numright.intValue());
					break;
				case RSHIFT:
					value	= new Integer(numleft.intValue() >> numright.intValue());
					break;
				case URSHIFT:
					value	= new Integer(numleft.intValue() >>> numright.intValue());
					break;
			}
		}

		if(bool)
		{
			value	= new Boolean(((Number)value).intValue()==1?true:false);
		}
		return value;
	}

	//-------- helper methods --------

	/**
	 *  Precompile unary node.
	 */
	protected void	precompileUnary()
	{
		// Check operator.
		switch(op)
		{
			case ADD:
			case SUBSTRACT:
			case NOT:
				break;
			default:
				throw new ParseException("Unsupported unary operator: "+toString(op));
		}

		// Determine static type.
		ExpressionNode	child	= (ExpressionNode)jjtGetChild(0);
		Class	clazz	= child.getStaticType();
		if(clazz!=null)
		{
			clazz	= SReflect.getWrappedType(clazz);
			// Double/ Float
			if(clazz.isAssignableFrom(Double.class)
			|| clazz.isAssignableFrom(Float.class))
			{
				if(op==NOT)
				{
					throw new ParseException("Operator ~ cannot be applied to floats: "+this);
				}
				else
				{
					setStaticType(clazz);
				}
			}

			// Long/ Integer
			else if(clazz.isAssignableFrom(Long.class)
			|| clazz.isAssignableFrom(Integer.class))
			{
				setStaticType(clazz);
			}

			// Short/ Byte/ Char (wrapped)
			else if(clazz.isAssignableFrom(Short.class)
				|| clazz.isAssignableFrom(Byte.class)
				|| clazz.isAssignableFrom(Character.class))
			{
				setStaticType(Integer.class);
			}

			// Unsupported type.
			else
			{
				throw new ParseException("Incompatible type of subterm: "+this);
			}
		}

		// Precompute constant value.
		if(child.isConstant())
		{
			try
			{
				this.setConstantValue(this.getValue(null));
				this.setConstant(true);
				if(getStaticType()==null && getConstantValue()!=null)
				{
					// Does this happen???
					setStaticType(getConstantValue().getClass());
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
	public Object	getUnaryValue(Map params) throws Exception
	{
		// Get subterm.
		Object	val	= ((ExpressionNode)jjtGetChild(0)).getValue(params);
		// System.out.println("term: "+val);
		if(!(val instanceof Number))
		{
			throw new RuntimeException("Left hand side of expression not number: "+this);
		}

		// Determine return type and calculate value.
		Number	numval	= (Number)val;
		Number	value	= null;
		switch(op)
		{
			case ADD:
				value	= numval;
				break;
			case SUBSTRACT:
				if(numval instanceof Double)
				{
					value	= new Double(- numval.doubleValue());
				}
				else if(numval instanceof Float)
				{
					value	= new Float(- numval.floatValue());
					}
				else if(numval instanceof Long)
				{
					value	= new Long(- numval.longValue());
				}
				else
				{
					value	= new Integer(- numval.intValue());
				}
				break;
			case NOT:
				if((val instanceof Double) || (val instanceof Float))
				{
					throw new RuntimeException("Operator ~ cannot be applied to floats: "+this);
				}
				else if(numval instanceof Long)
				{
					value	= new Long(~ numval.longValue());
				}
				else
				{
					value	= new Integer(~ numval.intValue());
				}
				break;
		}

		return value;
	}

	/**
	 *  Create a string representation of this node and its subnodes.
	 *  @return A string representation of this node.
	 */
	public String toPlainString()
	{
		if(jjtGetNumChildren()==2)
			return subnodeToString(0) + toString(op) + subnodeToString(1);
		else
			return toString(op) + subnodeToString(0);			
	}

	//-------- static part --------

	/**
	 *  Convert an operator to a string representation.
	 *  @param operator	The operator
	 *  @return A string representation of the operator.
	 */
	public static String	toString(int operator)
	{
		switch(operator)
		{
			case ADD:
				return "+";
			case SUBSTRACT:
				return "-";
			case MULTIPLY:
				return "*";
			case DIVIDE:
				return "/";
			case MODULO:
				return "%";
			case AND:
				return "&";
			case OR:
				return "|";
			case XOR:
				return "^";
			case NOT:
				return "~";
			case LSHIFT:
				return "<<";
			case RSHIFT:
				return ">>";
			case URSHIFT:
				return ">>>";
			default:
				return ""+operator;
		}
	}

	/**
	 *  Convert an operator from a string representation.
	 *  @param operator	The operator as string.
	 *  @return The int value of the operator.
	 */
	public static int	fromString(String operator)
	{
		if("+".equals(operator))
		{
			return ADD;
		}
		else if("-".equals(operator))
		{
			return SUBSTRACT;
		}
		else if("*".equals(operator))
		{
			return MULTIPLY;
		}
		else if("/".equals(operator))
		{
			return DIVIDE;
		}
		else if("%".equals(operator))
		{
			return MODULO;
		}
		else if("&".equals(operator))
		{
			return AND;
		}
		else if("|".equals(operator))
		{
			return OR;
		}
		else if("^".equals(operator))
		{
			return XOR;
		}
		else if("~".equals(operator))
		{
			return NOT;
		}
		else if("<<".equals(operator))
		{
			return LSHIFT;
		}
		else if(">>".equals(operator))
		{
			return RSHIFT;
		}
		else if(">>>".equals(operator))
		{
			return URSHIFT;
		}
		else
		{
			throw new ParseException("Unknown operator: "+operator);
		}
	}
}
