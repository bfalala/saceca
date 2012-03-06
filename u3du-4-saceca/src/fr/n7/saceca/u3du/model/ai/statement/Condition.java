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

import org.apache.commons.jexl2.JexlContext;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * An interface for test statements.
 */
@XStreamAlias("condition")
public interface Condition {
	
	/**
	 * Checks whether the statement is true.
	 * 
	 * @param context
	 *            The context.
	 * @return true, if the statement is true.
	 */
	public boolean check(JexlContext context);
	
	/**
	 * This method has to be called when the possible values kept by the expression have to be
	 * reseted. For instance, time aware test statements may need it to reset their inner clock.
	 */
	public void reset();
	
}