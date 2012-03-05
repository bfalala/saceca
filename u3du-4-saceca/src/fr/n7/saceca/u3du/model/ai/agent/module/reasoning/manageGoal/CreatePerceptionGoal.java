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

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMReasoningModule;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.Param;
import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The CreatePerceptionGoal class - generates goals using the perceived objects
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class CreatePerceptionGoal extends SameElementsRule<WorldObject> {
	/**
	 * Cosntructor
	 * 
	 * @param agent
	 *            the agent
	 */
	public CreatePerceptionGoal(Agent agent) {
		super(agent);
	}
	
	@Override
	public void reset() {
		this.elementsToCheck = new ArrayList<WorldObject>();
		// we get only the objects from the perceived objects list that weren't perceived in the
		// previous perception
		for (Couple<WorldObject, Boolean> couple : (ArrayList<Couple<WorldObject, Boolean>>) this.agent.getMemory()
				.getPerceivedObjects().clone()) {
			
			if (couple != null && couple.getSecondElement()) {
				this.elementsToCheck.add(couple.getFirstElement());
			}
		}
		
	}
	
	@Override
	protected boolean checkConditions() {
		return this.hasElementsToCheck();
	}
	
	@Override
	protected void applyEffects() {
		if (this.elementsToCheck == null || this.elementsToCheck.size() == 0) {
			return;
		}
		MMGoal goalToAdd;
		// take every object in the list
		for (WorldObject worldObject : this.elementsToCheck) {
			// take every service provided by the object
			for (Service service : worldObject.getServices()) {
				// take all the plus effects
				for (ServiceProperty effectPlus : service.getServiceEffectsPlus()) {
					// if the attractivity of that effect is greater than 0, we create a new goal
					if (effectPlus.getAttractivity() > 0) {
						goalToAdd = this.generateGoalFromProperty(effectPlus);
						if (goalToAdd != null) {
							// add the generated goal in the stack
							this.agent.getMemory().getGoalStack().addMiddle(goalToAdd, "perception");
						}
					}
				}
			}
		}
	}
	
	/**
	 * Generates a goal from a positive effect
	 * 
	 * @param effectPlus
	 *            the service property
	 * @return the generated goal
	 */
	private MMGoal generateGoalFromProperty(ServiceProperty effectPlus) {
		MMGoal goal = new MMGoal();
		double goalValue = 0;
		
		if (!effectPlus.getTreatment_effect().equals("")) {
			String name = effectPlus.getTreatment_effect().split("__")[0];
			for (Gauge gauge : this.agent.getGauges()) {
				if (gauge.getName().equals(name)) {
					goalValue = MMReasoningModule.GAUGE_REFILL * gauge.getMaxValue();
					
					goal = new MMGoal();
					
					goal.getSuccessCondition().setPropertyName("biggerThan");
					goal.getSuccessCondition().setPropertyParameters(new ArrayList<Param>());
					goal.getSuccessCondition().getPropertyParameters()
							.add(new Param(gauge.getName(), "", "double", "", "false"));
					goal.getSuccessCondition().getPropertyParameters()
							.add(new Param("amount", String.valueOf(goalValue), "double", "", "false"));
					goal.getSuccessCondition().setTreatment_precond(gauge.getName() + "__>__amount");
					
					goal.setPriority(effectPlus.getAttractivity());
					
					return goal;
				}
			}
		}
		
		ArrayList<PropertyLink> linkList = Model.getInstance().getAI().getWorld().getLinkList();
		
		ServiceProperty premise = null;
		
		if ((premise = effectPlus.getPremise(linkList)) != null) {
			for (Param parameter : premise.getPropertyParameters()) {
				if (parameter.getFixed().equals("false") && !parameter.getFindValue().equals("")) {
					try {
						parameter.setParamValue(String.valueOf(this.agent.getMemory().getKnowledgeAboutOwner()
								.getPropertiesContainer().getProperty(parameter.getFindValue()).getValue()));
					} catch (Exception e) {
						
					}
				}
			}
			goal.setSuccessCondition(premise);
			goal.setPriority(effectPlus.getAttractivity());
			
			return goal;
		}
		
		return null;
	}
}
