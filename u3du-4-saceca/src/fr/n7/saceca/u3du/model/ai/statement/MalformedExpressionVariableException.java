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

import java.util.Arrays;

/**
 * A class for problems with variable names in Jexl Contexts.
 * 
 * @author Sylvain Cambon
 */
public class MalformedExpressionVariableException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The variable name. */
	private final String variableName;
	
	/** The possible names. */
	private final String[] possibleNames;
	
	/**
	 * Instantiates a new malformed expression variable exception.
	 * 
	 * @param variableName
	 *            the variable name
	 * @param possibleNames
	 *            the possible names
	 */
	public MalformedExpressionVariableException(String variableName, String... possibleNames) {
		super("The variable name \"" + variableName + "\" does not start with one among: "
				+ Arrays.toString(possibleNames));
		this.variableName = variableName;
		this.possibleNames = possibleNames;
	}
	
	/**
	 * Instantiates a new malformed expression variable exception.
	 * 
	 * @param variableName
	 *            the variable name
	 * @param cause
	 *            the cause
	 * @param possibleNames
	 *            the possible names
	 */
	public MalformedExpressionVariableException(String variableName, Exception cause, String... possibleNames) {
		super("The variable name \"" + variableName + "\" does not start with one among: "
				+ Arrays.toString(possibleNames), cause);
		this.variableName = variableName;
		this.possibleNames = possibleNames;
	}
	
	/**
	 * Gets the variable name.
	 * 
	 * @return the variable name
	 */
	public final String getVariableName() {
		return this.variableName;
	}
	
	/**
	 * Gets the possible names.
	 * 
	 * @return the possible names
	 */
	public final String[] getPossibleNames() {
		return this.possibleNames;
	}
	
}
