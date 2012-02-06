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

import java.util.ArrayList;
import java.util.Iterator;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMReasoningModule;
import fr.n7.saceca.u3du.model.ai.service.Param;

/**
 * The CreateGaugeGoal class - it creates goals from gauges values
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class CreateGaugeGoal extends SameElementsRule<Gauge> {
	/**
	 * Cosntructor
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
		// take all the gauges
		this.elementsToCheck = new ArrayList<Gauge>(knowledgeAboutOwner.getGauges());
	}
	
	@Override
	protected boolean checkConditions() {
		if (!this.hasElementsToCheck()) {
			return false;
		}
		// take only the gauges whose values are not bigger than the gauge_refill level
		for (Iterator<Gauge> it_gauge = this.elementsToCheck.iterator(); it_gauge.hasNext();) {
			Gauge gauge = it_gauge.next();
			if (gauge.getValue() >= MMReasoningModule.GAUGE_REFILL * gauge.getMaxValue()) {
				it_gauge.remove();
			}
		}
		return true;
	}
	
	@Override
	protected void applyEffects() {
		MMGoal goalToAdd;
		
		double goalValue = 0;
		// take every gauge from the list adn create a goal
		for (Gauge gauge : this.elementsToCheck) {
			goalValue = MMReasoningModule.GAUGE_REFILL * gauge.getMaxValue();
			
			goalToAdd = new MMGoal();
			goalToAdd.getSuccessCondition().setPropertyName("biggerThan");
			goalToAdd.getSuccessCondition().setPropertyParameters(new ArrayList<Param>());
			goalToAdd.getSuccessCondition().getPropertyParameters()
					.add(new Param(gauge.getName(), "", "double", "", "false"));
			goalToAdd.getSuccessCondition().getPropertyParameters()
					.add(new Param("amount", String.valueOf(goalValue), "double", "", "false"));
			// set the treatment precondition to know how to check if the goal is satisfied
			goalToAdd.getSuccessCondition().setTreatment_precond(gauge.getName() + "__>__amount");
			// set the goal priority
			goalToAdd.setPriority((gauge.getValue() < goalValue) ? (int) Math.round(goalValue - gauge.getValue()) : 0);
			// add the goal in the stack
			this.agent.getMemory().getGoalStack().addMiddle(goalToAdd, "gauge");
		}
	}
}
