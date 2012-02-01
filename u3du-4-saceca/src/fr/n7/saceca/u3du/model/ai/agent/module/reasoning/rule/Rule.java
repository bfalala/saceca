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
 * The abstract class Rule.
 * 
 * @author Jérôme Dalbert
 */
public abstract class Rule {
	
	/** The agent. */
	protected Agent agent;
	
	/**
	 * Instantiates a new rule.
	 * 
	 * @param agent
	 *            the agent
	 */
	public Rule(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Checks if the rule is applicable.
	 * 
	 * @return true, if is applicable
	 */
	public final boolean isApplicable() {
		while (this.hasElementsToCheck()) {
			if (this.checkConditions()) {
				return true;
			} else {
				this.removeElements();
			}
		}
		
		return false;
	}
	
	/**
	 * Re-initializes the elements to check.
	 */
	public abstract void reset();
	
	/**
	 * Checks for elements to check.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean hasElementsToCheck();
	
	/**
	 * Check the Rule conditions on some elements to check.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean checkConditions();
	
	/**
	 * Apply the rule.
	 */
	public final void apply() {
		this.applyEffects();
		this.removeElements();
	}
	
	/**
	 * Apply the Rule effects.
	 */
	protected abstract void applyEffects();
	
	/**
	 * Removes elements relative to either a previous failed conditions check or a previous apply
	 * call
	 */
	protected abstract void removeElements();
	
}
