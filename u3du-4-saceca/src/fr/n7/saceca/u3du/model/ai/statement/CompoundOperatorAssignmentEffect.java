/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.statement;

/**
 * A class to handle compound operators assignment. For instance, 'a+=3' is a compound assignment.
 */
public final class CompoundOperatorAssignmentEffect extends DefaultEffect {
	
	/**
	 * The array of the operators that may be used in compound operator assignment.
	 */
	protected static final char[] ALLOWED_COMPOUND_OPERATORS = { '+', '-', '*', '/', '&', '^', '|', '~' };
	
	/**
	 * Instantiates a new enhanced assignment statement capable of handling compound operators
	 * assignment.
	 * 
	 * @param expression
	 *            The expression.
	 */
	public CompoundOperatorAssignmentEffect(String expression) {
		super(expression, transform(expression));
	}
	
	/**
	 * Transforms an expression using one or less compound operator assignment to fit Jexl syntax.
	 * 
	 * @param initExpr
	 *            the initial expr
	 * @return the transformed expression.
	 */
	private static final String transform(String initExpr) {
		String compoundOp;
		int index;
		for (char operator : ALLOWED_COMPOUND_OPERATORS) {
			compoundOp = operator + "=";
			index = initExpr.indexOf(compoundOp);
			if (index != -1) {
				String beforeOp = initExpr.substring(0, index);
				String afterOp = initExpr.substring(index + 2);
				StringBuilder sb = new StringBuilder(beforeOp);
				sb.append('=');
				sb.append(beforeOp);
				sb.append(operator);
				sb.append(afterOp);
				return sb.toString();
			}
		}
		return initExpr;
	}
}