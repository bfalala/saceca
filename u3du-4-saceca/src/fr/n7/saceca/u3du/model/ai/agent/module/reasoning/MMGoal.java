/*
 * This work is licensed under the Creative Commons Attribimport fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.agent.module.reasoning;

import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;

/**
 * The MMGoal class - represents an instantiated service property with a certain level of priority
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class MMGoal {
	/** The success condition */
	private ServiceProperty successCondition;
	
	/** The priority */
	private Integer priority;
	
	/** If the goal is reached */
	private boolean reached;
	
	/** If the goal is reachable */
	private boolean reachable;
	
	/**
	 * Cosntructor
	 * 
	 * @param successCondition
	 *            the success condition
	 * @param priority
	 *            the priority
	 */
	public MMGoal(ServiceProperty successCondition, int priority) {
		this.successCondition = successCondition.deepDataClone();
		this.priority = priority;
		this.reached = false;
		this.reachable = true;
	}
	
	/**
	 * Cosntructor
	 * 
	 * @param successCondition
	 */
	public MMGoal(ServiceProperty successCondition) {
		this(successCondition, 0);
	}
	
	/**
	 * Default constructor
	 */
	public MMGoal() {
		this(new ServiceProperty(), 0);
	}
	
	/**
	 * Gets the success condition
	 * 
	 * @return
	 */
	public ServiceProperty getSuccessCondition() {
		return this.successCondition;
	}
	
	/**
	 * Sets the success condition
	 * 
	 * @param successCondition
	 *            the success condition
	 */
	public void setSuccessCondition(ServiceProperty successCondition) {
		this.successCondition = successCondition;
	}
	
	/**
	 * Gets the priority of the goal
	 * 
	 * @return
	 */
	public Integer getPriority() {
		return this.priority;
	}
	
	/**
	 * Sets the goal priority
	 * 
	 * @param priority
	 *            the priority
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	/**
	 * If the goal is reached
	 * 
	 * @return
	 */
	public boolean isReached() {
		return this.reached;
	}
	
	/**
	 * Sets the reached attribute
	 * 
	 * @param reached
	 *            the reached attribute
	 */
	public void setReached(boolean reached) {
		this.reached = reached;
	}
	
	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}
	
	public boolean isReachable() {
		return this.reachable;
	}
	
	@Override
	public String toString() {
		return "Goal [successCondition=" + this.successCondition.getTreatment_precond() + ", priority=" + this.priority
				+ ", reachable=" + this.reachable + ", reached=" + this.reached + "]";
	}
	
	/**
	 * Clones the goal
	 * 
	 * @return
	 */
	public MMGoal deepDataClone() {
		MMGoal goal = new MMGoal(this.successCondition, this.priority);
		goal.setReached(this.reached);
		goal.setReachable(this.reachable);
		
		return goal;
	}
	
	/**
	 * The name of the goal to be displayed is the interface
	 * 
	 * @return the name if the service
	 */
	public String displayName() {
		if (!this.successCondition.getTreatment_precond().equals("")) {
			StringBuilder nameBuilder = new StringBuilder();
			nameBuilder.append(this.successCondition.getTreatment_precond().split("__")[0]);
			nameBuilder.append(" >= ");
			nameBuilder.append(this.successCondition.getParameter(
					this.successCondition.getTreatment_precond().split("__")[2]).getParamValue());
			return nameBuilder.toString();
		}
		return this.successCondition.getPropertyName();
	}
}