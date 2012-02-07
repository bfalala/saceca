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
package fr.n7.saceca.u3du.model.ai.agent.module.planning;

import java.util.List;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * The Interface PlanningModule.
 * 
 * @author Sylvain Cambon & Jérôme Dalbert
 */
public interface PlanningModule extends Storable {
	/**
	 * Plan and execute.
	 */
	public void planAndExecute();
	
	/**
	 * Builds the plan.
	 * 
	 * @param agent
	 *            the agent
	 * @param goal
	 *            the goal
	 * @param currentDepth
	 *            the current depth
	 * @param maxDepth
	 *            the max depth
	 * @return the plan
	 */
	public Couple<Boolean, Plan> buildPlan(Agent agent, MMGoal goal, int currentDepth, int maxDepth);
	
	/**
	 * Gets the plan.
	 * 
	 * @return the plan
	 */
	public Plan getPlan();
	
	/**
	 * Gets the current goal.
	 * 
	 * @return the current goal
	 */
	public MMGoal getCurrentGoal();
	
	/**
	 * Gets the current plan.
	 * 
	 * @return the current plan
	 */
	public Plan getCurrentPlan();
	
	/**
	 * Gets the current index.
	 * 
	 * @return the current index
	 */
	public int getCurrentIndex();
	
	/**
	 * Forces the execution of a service, disabling the planning.
	 * 
	 * @param serviceName
	 *            the service name
	 * @param params
	 *            the params
	 * @param clearPreviousPlan
	 *            the clear previous plan
	 * @throws CommandException
	 *             the command exception
	 */
	public void forceExecution(String serviceName, List<Object> params, boolean clearPreviousPlan)
			throws CommandException;
	
	/**
	 * Enables the planning.
	 */
	public void enablePlanning();
}
