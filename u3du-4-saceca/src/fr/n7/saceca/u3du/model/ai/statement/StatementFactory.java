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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A factory for creating Statement objects. The implementation is chosen after the contained
 * expression for maximum performance.
 */
public class StatementFactory {
	
	/** The singleton instance. */
	protected static StatementFactory instance = null;
	
	/** The pattern to detect compound operators assignment usage. */
	protected static final Pattern COMPOUND_OPS_PATTERN = Pattern
			.compile(".+[" + getRegExpFriendlyOperators() + "]=.+");
	
	/** The pattern to detect timer:wait(time) usage. */
	protected static final Pattern WAIT_PATTERN = Pattern.compile(".*timer\\:wait\\(.*\\).*");
	
	/**
	 * Instantiates a new statement factory.
	 */
	protected StatementFactory() {
		// NOP
	}
	
	/**
	 * Gets the single instance of StatementFactory.
	 * 
	 * @return single instance of StatementFactory.
	 */
	public static StatementFactory getInstance() {
		if (instance == null) {
			instance = new StatementFactory();
		}
		return instance;
	}
	
	/**
	 * Gets the reg exp friendly operators.
	 * 
	 * @return the reg exp friendly operators
	 */
	private static final String getRegExpFriendlyOperators() {
		StringBuilder builder = new StringBuilder();
		for (char op : CompoundOperatorAssignmentEffect.ALLOWED_COMPOUND_OPERATORS) {
			builder.append("\\");
			builder.append(op);
		}
		return builder.toString();
	}
	
	/**
	 * Creates a new executable Statement object.
	 * 
	 * @param expression
	 *            the expression
	 * @return the executable statement
	 */
	public Effect createActionStatement(String expression) {
		// Checks whether extra processing for compound operators assignment
		// needs to be done.
		Matcher matcher = StatementFactory.COMPOUND_OPS_PATTERN.matcher(expression);
		if (matcher.matches()) {
			// The expression contains a compound operator assignment, thus an
			// CompoundOperatorAssignmentEffect is required.
			return new CompoundOperatorAssignmentEffect(expression);
		} else {
			// The operation does not contain a compound operator assignment,
			// thus optimal creation is to build an U3duExpression
			return new DefaultEffect(expression);
		}
	}
	
	/**
	 * Creates a new test statement.
	 * 
	 * @param expression
	 *            the expression
	 * @return the test statement
	 */
	public Condition createTestStatement(String expression) {
		// Checks whether time aware implementation is required
		// Matcher matcher = StatementFactory.WAIT_PATTERN.matcher(expression);
		// if (matcher.matches()) {
		// // The expression contains a timer:wait(time) statement and thus
		// // requires time.
		// // TODO : see FIX ME about time conditions
		// return new DoNotUseTemporalCondition(expression);
		// } else {
		// The expression is wait pattern free and a mere boolean statement
		// is enough.
		return new DefaultCondition(expression);
		// }
	}
}
