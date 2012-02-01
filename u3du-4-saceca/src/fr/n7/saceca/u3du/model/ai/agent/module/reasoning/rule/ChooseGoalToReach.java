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
import java.util.Collections;
import java.util.List;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.DefaultReasoningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;

/**
 * The Class ChooseGoalToReach.
 * 
 * The element on which the rule is applied is the agent's list of goals
 * 
 * @author Jérôme Dalbert
 */
public class ChooseGoalToReach extends UniqueElementRule<List<Goal>> {
	
	/** The goal to reach. */
	private Goal goalToReach;
	
	/**
	 * Instantiates a new find goal to reach.
	 * 
	 * @param agent
	 *            the agent
	 */
	public ChooseGoalToReach(Agent agent) {
		super(agent);
	}
	
	@Override
	public void reset() {
		super.reset();
		this.element = new ArrayList<Goal>(this.agent.getMemory().getGoals());
		
		// We shuffle the goals to have an unpredictable choice between equal priorities goals
		Collections.shuffle(this.element);
	}
	
	@Override
	protected boolean checkConditions() {
		if (this.element.isEmpty()) {
			return false;
		}
		
		Goal maxGoal = this.element.get(0);
		
		for (Goal goal : this.element) {
			if (goal.getPriority() >= DefaultReasoningModule.TOP_PRIORITY) {
				return false;
			}
			if (goal.getPriority() > maxGoal.getPriority()) {
				maxGoal = goal;
			}
		}
		
		this.goalToReach = maxGoal;
		return true;
	}
	
	@Override
	protected void applyEffects() {
		this.goalToReach.setPriority(DefaultReasoningModule.TOP_PRIORITY);
	}
	
}
