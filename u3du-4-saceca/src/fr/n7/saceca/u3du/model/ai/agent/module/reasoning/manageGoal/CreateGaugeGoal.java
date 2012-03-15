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
import fr.n7.saceca.u3du.model.ai.agent.module.planning.MatrixMethodPlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMReasoningModule;
import fr.n7.saceca.u3du.model.ai.service.Param;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The CreateGaugeGoal class - it creates goals from gauges values
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris, Bertrand Deguelle
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
		this.elementsToCheckWithType = new ArrayList<Couple<Gauge, gaugeType>>();
		ArrayList<Gauge> liste_gauges = new ArrayList<Gauge>(knowledgeAboutOwner.getGauges());
		for (Gauge gauge : liste_gauges) {
			this.elementsToCheckWithType.add(new Couple<Gauge, gaugeType>(gauge, gaugeType.SAFE));
		}
	}
	
	@Override
	protected boolean checkConditions() {
		
		Gauge gauge_next_no_safe = null;
		double minTimeToBeCritical = -1.0;
		double timeToBeCritical = 0;
		
		if (!this.hasElementsToCheckWithType()) {
			return false;
		}
		// take only the gauges whose values are not bigger than the gauge_refill level
		for (Iterator<Couple<Gauge, gaugeType>> it_gauge = this.elementsToCheckWithType.iterator(); it_gauge.hasNext();) {
			Couple<Gauge, gaugeType> couple_gauge = it_gauge.next();
			Gauge gauge = couple_gauge.getFirstElement();
			if (gauge.getValue() >= MMReasoningModule.GAUGE_REFILL * gauge.getMaxValue()) {
				it_gauge.remove();
				// We conserved the next critical gauge
				if (gauge.isDecreased()) {
					
					timeToBeCritical = (gauge.getValue() - MMReasoningModule.GAUGE_REFILL * gauge.getMaxValue())
							* gauge.getDecrementPeriod(this.agent) + gauge.getDecrementPeriod(this.agent) - 1
							- gauge.getGaugeDecrementTime();
					
					if (minTimeToBeCritical == -1 || minTimeToBeCritical >= timeToBeCritical) {
						minTimeToBeCritical = timeToBeCritical;
						gauge_next_no_safe = new Gauge(gauge);
					}
				}
			} else {
				couple_gauge.setSecondElement(gaugeType.CRITICAL);
			}
		}
		
		if (gauge_next_no_safe != null) {
			this.elementsToCheckWithType.add(new Couple<Gauge, gaugeType>(gauge_next_no_safe, gaugeType.SAFE));
		}
		
		return true;
	}
	
	@Override
	protected void applyEffects() {
		MMGoal goalToAdd;
		
		double goalValue = 0;
		
		// take every gauge from the list adn create a goal
		for (Couple<Gauge, gaugeType> couple_gauge : this.elementsToCheckWithType) {
			Gauge gauge = couple_gauge.getFirstElement();
			gaugeType type = couple_gauge.getSecondElement();
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
			// set the goal priority, fonction of gauge type
			if (type == gaugeType.CRITICAL) {
				goalToAdd.setPriority((gauge.getValue() < goalValue) ? (int) Math.round(gauge.getMaxValue()
						- gauge.getValue()) : 0);
				// we compare necessary time to realize the plan and time before the gauge become
				// CRITICAL
			} else {
				MatrixMethodPlanningModule MMPM = new MatrixMethodPlanningModule(this.agent);
				double timeToBeCritical = (gauge.getValue() - goalValue) * gauge.getDecrementPeriod(this.agent)
						+ gauge.getDecrementPeriod(this.agent) - 1
						- (Gauge.getGaugeFromList(this.agent.getGauges(), gauge.getName()).getGaugeDecrementTime());
				
				double timeEstimation = MMPM.timeEstimationForAGoal(goalToAdd);
				if (timeEstimation > timeToBeCritical) {
					goalToAdd.setPriority((int) Math.round(gauge.getMaxValue() - gauge.getValue()));
				} else {
					goalToAdd.setPriority(-1);
				}
			}
			// add the goal in the stack
			this.agent.getMemory().getGoalStack().addMiddle(goalToAdd, "gauge");
		}
	}
}
