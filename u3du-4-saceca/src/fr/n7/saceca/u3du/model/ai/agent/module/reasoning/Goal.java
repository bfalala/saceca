/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
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

import org.apache.commons.jexl2.JexlContext;

import fr.n7.saceca.u3du.model.ai.statement.DefaultCondition;

/**
 * The Class Goal.
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public class Goal extends DefaultCondition {
	
	/** The success condition. */
	private String successCondition;
	
	/** The priority. */
	private Integer priority;
	
	/** The reachable. */
	private boolean reachable;
	
	/** The reached. */
	private boolean reached;
	
	/**
	 * Instantiates a new goal.
	 * 
	 * @param priority
	 *            the priority
	 * 
	 * @param successCondition
	 *            the success condition
	 */
	public Goal(String successCondition, int priority) {
		super(successCondition);
		this.successCondition = successCondition;
		this.priority = priority;
		this.reachable = true;
		this.reached = false;
	}
	
	/**
	 * Instantiates a new goal with a minimal priority.
	 * 
	 * @param successCondition
	 *            the success condition
	 */
	public Goal(String successCondition) {
		this(successCondition, 0);
	}
	
	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public Integer getPriority() {
		return this.priority;
	}
	
	/**
	 * Sets the priority.
	 * 
	 * @param priority
	 *            the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * Sets the priority.
	 * 
	 * @param priority
	 *            the new priority
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	/**
	 * Checks if this goal seems reached.
	 * 
	 * @param context
	 *            the context
	 * @return true, if it seems reached
	 */
	public boolean seemsReached(JexlContext context) {
		boolean result = this.check(context);
		return result;
	}
	
	/**
	 * Checks if is the reached.
	 * 
	 * @return the reached
	 */
	public final boolean isReached() {
		return this.reached;
	}
	
	/**
	 * Sets the reached.
	 * 
	 * @param reached
	 *            the new reached
	 */
	public final void setReached(boolean reached) {
		this.reached = reached;
	}
	
	/**
	 * Checks if is the reachable.
	 * 
	 * @return the reachable
	 */
	public boolean isReachable() {
		return this.reachable;
	}
	
	/**
	 * Sets the reachable.
	 * 
	 * @param reachable
	 *            the new reachable
	 */
	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}
	
	/**
	 * Gets the success condition.
	 * 
	 * @return the success condition
	 */
	public String getSuccessCondition() {
		return this.successCondition;
	}
	
	/**
	 * Sets the success condition.
	 * 
	 * @param successCondition
	 *            the new success condition
	 */
	public void setSuccessCondition(String successCondition) {
		this.successCondition = successCondition;
		this.rawExpression = successCondition;
	}
	
	@Override
	public String toString() {
		return "Goal [successCondition=" + this.successCondition + ", priority=" + this.priority + ", reachable="
				+ this.reachable + ", reached=" + this.reached + "]";
	}
	
	@Override
	public Goal clone() {
		Goal g = new Goal(this.successCondition, this.priority);
		g.setReachable(this.reachable);
		
		return g;
	}
	
}