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
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.DefaultReasoningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;

/**
 * The Class CreateGaugeGoal.
 * 
 * @author Jérôme Dalbert
 */
public class CreateGaugeGoal extends SameElementsRule<Gauge> {
	
	/**
	 * Instantiates a new CreateGaugeGoal.
	 * 
	 * @param agent
	 *            the agent
	 */
	public CreateGaugeGoal(Agent agent) {
		super(agent);
	}
	
	@Override
	public void reset() {
		Memory memory = this.agent.getMemory();
		Agent knowledgeAboutOwner = memory.getKnowledgeAboutOwner();
		this.elementsToCheck = new ArrayList<Gauge>(knowledgeAboutOwner.getGauges());
	}
	
	@Override
	public boolean checkConditions() {
		Gauge gauge = this.elementsToCheck.get(0);
		
		boolean gaugeFull = gauge.getValue() == gauge.getMaxValue();
		
		boolean createdBefore = false;
		String successCondition = this.createSuccessCondition(gauge);
		for (Goal goal : this.agent.getMemory().getGoals()) {
			if (goal.getSuccessCondition().equals(successCondition)) {
				createdBefore = true;
			}
		}
		
		return !gaugeFull && !createdBefore;
	}
	
	@Override
	public void applyEffects() {
		Gauge gauge = this.elementsToCheck.get(0);
		
		// If a survival gauge is below the threshold, we give the goal maximum priority
		int priority;
		if (gauge.isSurvival()
				&& gauge.getValue() < DefaultReasoningModule.SURVIVAL_GAUGE_THRESHOLD * gauge.getMaxValue()) {
			priority = DefaultReasoningModule.EMERGENCY_PRIORITY;
		} else {
			priority = (int) (gauge.getMaxValue() - gauge.getValue());
		}
		
		String successCondition = this.createSuccessCondition(gauge);
		
		this.agent.getMemory().getGoals().add(new Goal(successCondition, priority));
	}
	
	/**
	 * Creates the goal success condition relative to a gauge.
	 * 
	 * @param gauge
	 *            the gauge
	 * @return the string
	 */
	private String createSuccessCondition(Gauge gauge) {
		int refillValue = (int) (DefaultReasoningModule.GAUGE_REFILL * gauge.getMaxValue());
		if (gauge.getValue() > refillValue) {
			refillValue = (int) gauge.getMaxValue();
		}
		
		return "id" + this.agent.getId() + "_" + gauge.getName() + ">=" + refillValue;
	}
}
