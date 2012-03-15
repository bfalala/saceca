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
package fr.n7.saceca.u3du.model.ai.agent.module.reasoning.manageGoal;

import java.util.List;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The abstract class SameElementsRule represents a rule that checks elements that have the same
 * type.
 * 
 * @param <T>
 *            the generic type of the elements
 * 
 * @author Jérôme Dalbert, Bertrand Deguelle
 */
public abstract class SameElementsRule<T> extends Rule {
	
	/**
	 * Many types in order to implement anticipation ANTICIPATED and SOONCRITICAL are not explicited
	 * used but can be useful for futures codes
	 */
	
	enum gaugeType {
		SAFE, CRITICAL, ANTICIPATED, SOONCRITICAL
	};
	
	/** The remaining elements to check with type of Gauge. */
	protected List<Couple<T, gaugeType>> elementsToCheckWithType;
	
	/** The remaining elements to check. */
	protected List<T> elementsToCheck;
	
	/**
	 * Instantiates a new gauge rule.
	 * 
	 * @param agent
	 *            the agent
	 */
	public SameElementsRule(Agent agent) {
		super(agent);
	}
	
	@Override
	protected boolean hasElementsToCheck() {
		return !this.elementsToCheck.isEmpty();
	}
	
	@Override
	protected boolean hasElementsToCheckWithType() {
		return !this.elementsToCheckWithType.isEmpty();
	}
	
	@Override
	protected void removeElements() {
		this.elementsToCheck.remove(0);
	}
	
}
