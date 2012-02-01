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

import java.util.ArrayList;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;

/**
 * The Class DeleteReachedGoal.
 * 
 * @author Jérôme Dalbert
 */
public class RemoveReachedGoal extends SameElementsRule<Goal> {
	
	/**
	 * Instantiates a new DeleteReachedGoal.
	 * 
	 * @param agent
	 *            the agent
	 */
	public RemoveReachedGoal(Agent agent) {
		super(agent);
	}
	
	@Override
	public void reset() {
		this.elementsToCheck = new ArrayList<Goal>(this.agent.getMemory().getGoals());
	}
	
	@Override
	public boolean checkConditions() {
		Goal goal = this.elementsToCheck.get(0);
		return goal.isReached();
	}
	
	@Override
	public void applyEffects() {
		Goal goal = this.elementsToCheck.get(0);
		this.agent.getMemory().getGoals().remove(goal);
	}
	
}
