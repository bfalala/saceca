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

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;

/**
 * A class for expressions and their evaluation. It is not directly capable of handling compound
 * operators assignments.
 * 
 * The direct usage of this class is discouraged yet kept for advanced features.
 * 
 * @param <T>
 *            The type of the expected result (if any).
 * @author Sylvain Cambon
 */
public class U3duExpression<T> implements Statement<T> {
	
	/** The expression. */
	protected Expression expression;
	
	/** The raw expression. */
	protected String rawExpression;
	
	/**
	 * Instantiates a new expression statement. This is equivalent to
	 * <code>U3duExpression(string, string)</code>.
	 * 
	 * @param string
	 *            the string
	 */
	public U3duExpression(String string) {
		this(string, string);
	}
	
	/**
	 * Instantiates a new expression statement.
	 * 
	 * @param rawExpression
	 *            the raw expression for serialized versions.
	 * @param internalRepresentation
	 *            the internal representation
	 */
	public U3duExpression(String rawExpression, String internalRepresentation) {
		super();
		this.rawExpression = rawExpression;
		this.expression = ServiceAwareU3duJexlEngine.getInstance().createExpression(internalRepresentation);
	}
	
	/**
	 * Evaluates a statement.
	 * 
	 * @param context
	 *            The context.
	 * @return The result of the statement, if any.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T evaluate(JexlContext context) {
		return (T) this.expression.evaluate(context);
	}
	
	/**
	 * Gets the raw expression.
	 * 
	 * @return the raw expression
	 */
	public final String getRawExpression() {
		return this.rawExpression;
	}
	
	@Override
	public String toString() {
		return this.rawExpression;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.rawExpression == null) ? 0 : this.rawExpression.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		U3duExpression<?> other = (U3duExpression<?>) obj;
		if (this.rawExpression == null) {
			if (other.rawExpression != null) {
				return false;
			}
		} else if (!this.rawExpression.replace(" ", "").equals(other.rawExpression.replace(" ", ""))) {
			return false;
		}
		return true;
	}
}
