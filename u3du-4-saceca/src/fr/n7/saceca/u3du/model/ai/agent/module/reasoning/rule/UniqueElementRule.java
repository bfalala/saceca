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
package fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule;

import fr.n7.saceca.u3du.model.ai.agent.Agent;

/**
 * The abstract class UniqueElementRule represents a rule that checks only one element.
 * 
 * @param <T>
 *            the generic type of the element
 * 
 * @author Jérôme Dalbert
 */
public abstract class UniqueElementRule<T> extends Rule {
	
	/** The element to check. */
	protected T element;
	
	/** Is the element checked ?. */
	private boolean elementChecked;
	
	/**
	 * Instantiates a new UniqueElementRule.
	 * 
	 * @param agent
	 *            the agent
	 */
	public UniqueElementRule(Agent agent) {
		super(agent);
	}
	
	/**
	 * Reset.
	 */
	@Override
	public void reset() {
		this.elementChecked = false;
	}
	
	/**
	 * Checks for elements to check.
	 * 
	 * @return true, if successful
	 */
	@Override
	protected boolean hasElementsToCheck() {
		return !this.elementChecked;
	}
	
	/**
	 * Removes the elements.
	 */
	@Override
	protected void removeElements() {
		this.elementChecked = true;
	}
	
}
