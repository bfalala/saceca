/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * Aurélien Chabot, Anthony Foulfoin, Jérôme Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.statement;

import org.apache.commons.jexl2.JexlContext;

/**
 * A class to add the "eval:string('myExpression')" method to the Jexl engine.
 * 
 * @author Sylvain Cambon
 */
public class StringEvaluationFunctor {
	
	/** The Constant NAMESPACE for context registering. */
	public static final String NAMESPACE = "eval";
	
	/** The context. */
	private JexlContext context;
	
	/**
	 * Instantiates a new string evaluation functor.
	 * 
	 * @param context
	 *            the context
	 */
	public StringEvaluationFunctor(JexlContext context) {
		super();
		this.context = context;
	}
	
	/**
	 * Evaluates an expression passed as a string within an expression. Other custom Jexl methods
	 * should not be mixed with this one.
	 * 
	 * @param string
	 *            the string
	 * @return the object
	 */
	public Object string(String string) {
		StatementFactory.getInstance().createActionStatement(string);
		return new U3duExpression<Object>(string).evaluate(this.context);
	}
	
}
