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
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;

/**
 * The Class DeleteGoalSameGauge.
 * 
 * @author Jérôme Dalbert
 */
public class RemoveGoalSameGauge extends SameElementsRule<Goal> {
	
	/** The goal to delete. */
	private Goal goalToDelete;
	
	/**
	 * Instantiates a new DeleteGoalSameGauge.
	 * 
	 * @param agent
	 *            the agent
	 */
	public RemoveGoalSameGauge(Agent agent) {
		super(agent);
	}
	
	@Override
	public void reset() {
		this.elementsToCheck = new ArrayList<Goal>(this.agent.getMemory().getGoals());
	}
	
	@Override
	public boolean checkConditions() {
		Goal goal = this.elementsToCheck.get(0);
		String goalCondition = goal.getSuccessCondition();
		if (!goalCondition.contains(Gauge.PREFIX)) {
			return false;
		}
		
		String gaugeCondition = goalCondition.split(Gauge.PREFIX)[1];
		String gaugeName = gaugeCondition.split(">=")[0];
		
		for (Goal otherGoal : this.agent.getMemory().getGoals()) {
			if (otherGoal != goal) {
				String otherGoalCondition = otherGoal.getSuccessCondition();
				if (otherGoalCondition.contains(Gauge.PREFIX)) {
					String otherGaugeCondition = otherGoalCondition.split(Gauge.PREFIX)[1];
					String otherGaugeName = otherGaugeCondition.split(">=")[0];
					
					if (gaugeName.equals(otherGaugeName)) {
						if (otherGoal.getPriority() < goal.getPriority()) {
							this.goalToDelete = otherGoal;
						} else {
							this.goalToDelete = goal;
						}
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void applyEffects() {
		this.agent.getMemory().getGoals().remove(this.goalToDelete);
	}
	
}
