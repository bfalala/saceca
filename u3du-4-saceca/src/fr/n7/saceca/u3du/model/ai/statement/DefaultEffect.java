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

/**
 * The Class DefaultEffect.
 * 
 * @author Sylvain Cambon
 */
public class DefaultEffect extends U3duExpression<Void> implements Effect {

    /**
     * Instantiates a new action statement.
     * 
     * @param expression
     *            the expression.
     */
    public DefaultEffect(String expression) {
        super(expression);
    }


    /**
     * Instantiates a new default action statement.
     * 
     * @param rawExpression
     *            the raw expression for serialized versions
     * @param internalRepresentation
     *            the internal representation
     */
    public DefaultEffect(String rawExpression, String internalRepresentation) {
        super(rawExpression, internalRepresentation);
    }

}
