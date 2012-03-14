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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.util.Log;

/**
 * The Class DefaultPlanningModule.
 * 
 * @author Sylvain Cambon
 */
public class ClassicalPlanningModule implements PlanningModule {
	
	/** The Constant logger. */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ClassicalPlanningModule.class);
	
	/** The Constant COLLECTION_SHUFFLE_ENABLED. */
	public static final boolean COLLECTION_SHUFFLE_ENABLED = true;
	
	/** The Constant NO_PLAN_FOUND. */
	public static final Plan NO_PLAN_FOUND = null;
	
	/** The Constant PLAN_MAX_DEPTH. */
	public static final int PLAN_MAX_DEPTH = 50;
	
	/** The agent. */
	private Agent agent;
	
	/** The current goal. */
	private MMGoal currentGoal = null;
	
	/** The current plan. */
	private Plan currentPlan = null;
	
	/** The current index. */
	private int currentIndex = 0;
	
	/**
	 * This boolean is true if there was a command that forces the execution of the corresponding
	 * plan.
	 */
	private boolean forcePlan = false;
	
	/**
	 * Instantiates a new default planning module.
	 * 
	 * @param agent
	 *            the agent
	 */
	public ClassicalPlanningModule(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Plans and executes.
	 */
	@Override
	public synchronized void planAndExecute() {
		// We forced a plan execution and there is no element in it
		if (this.forcePlan && this.currentPlan == null || (this.currentPlan != null && this.currentPlan.size() == 0)) {
			return;
		}
		// There is an element in the plan
		else if (this.forcePlan || this.currentGoal != null && !this.agent.getMemory().getGoals().isEmpty()
				&& this.currentGoal.equals(this.agent.getMemory().getGoals().get(0)) && this.currentPlan != null
				&& this.currentPlan.size() >= this.currentIndex) {
			this.execute();
		} else if (!this.agent.getMemory().getGoalStack().isEmpty()) {
			this.currentGoal = this.agent.getMemory().getGoalStack().get(0);
			Couple<Boolean, Plan> resultedPlan = this.buildPlan(this.agent, this.currentGoal, 0, PLAN_MAX_DEPTH);
			this.currentPlan = resultedPlan.getSecondElement();
		}
	}
	
	/**
	 * Builds a plan.
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
	@Override
	public Couple<Boolean, Plan> buildPlan(Agent agent, MMGoal goal, int currentDepth, int maxDepth) {
		ClassicalLimitedDepthSearchPlanner planner = new ClassicalLimitedDepthSearchPlanner();
		
		return planner.buildPlan(agent, goal, currentDepth, maxDepth);
	}
	
	/**
	 * Handles walk to service setup, if needed. The memory consumer is directly accessed. The agent
	 * as well as its memory are moved (if needed).
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param maxDist
	 *            the max distance
	 * @return The plan element to be added to move to the next place. Null means there is no need
	 *         to move.
	 * @throws UnreachableObjectExpcetion
	 *             If no path is found
	 */
	
	/**
	 * Execution part.
	 */
	public void execute() {
		PlanElement planElement = this.currentPlan.get(this.currentIndex);
		ExecutionStatus status = planElement.execute(this.agent, ExecutionMode.REAL);
		
		switch (status) {
			case CONTINUE_NEXT_TIME:
				// NOP
				break;
			
			case SUCCESSFUL_TERMINATION:
				this.currentIndex++;
				if (this.currentIndex == this.currentPlan.size()) {
					if (!this.forcePlan) {
						this.currentGoal.setReachable(true);
						this.currentGoal.setReached(true);
						// this.agent.getMemory().getPastPlans().put(this.currentGoal,
						// this.currentPlan);
					}
					this.currentGoal = null;
					this.currentPlan = null;
					this.currentIndex = 0;
				}
				break;
			
			case FAILURE:
				this.currentPlan = null;
				this.currentIndex = 0;
				Log.debug("SERVICE UNUSABLE");
				break;
			
			default:
				break;
		}
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return ClassicalPlanningModule.class.getCanonicalName();
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "DefaultPlanningModule [currentGoal=" + this.currentGoal + ", currentPlan=" + this.currentPlan
				+ ", currentIndex=" + this.currentIndex + "]";
	}
	
	@Override
	public Plan getPlan() {
		return this.currentPlan;
	}
	
	/**
	 * Gets the current goal.
	 * 
	 * @return the current goal
	 */
	@Override
	public final MMGoal getCurrentGoal() {
		return this.currentGoal;
	}
	
	/**
	 * Gets the current plan.
	 * 
	 * @return the current plan
	 */
	@Override
	public final Plan getCurrentPlan() {
		return this.currentPlan;
	}
	
	/**
	 * Gets the current index.
	 * 
	 * @return the current index
	 */
	@Override
	public final int getCurrentIndex() {
		return this.currentIndex;
	}
	
	@Override
	public synchronized void forceExecution(String serviceName, List<Object> params, boolean clearPreviousPlan)
			throws CommandException {
		
		Agent agent = this.agent;
		// If previous forced services were added to the current plan, we virtually apply their
		// effects to the "agent" variable.
		if (this.forcePlan && this.currentPlan != null && this.currentPlan.size() > 0) {
			agent = this.agent.deepDataClone();
			
			for (PlanElement planElement : this.currentPlan) {
				planElement.getService().execute(planElement.getProvider(), agent, planElement.getParameters(),
						ExecutionMode.VIRTUAL);
			}
		}
		
		// We find the nearest service around the "agent" variable
		Couple<Service, WorldObject> serviceAndProvider = Model.getInstance().getAI().getWorld()
				.getFirstNearServiceAndProvider(serviceName, agent, PerceptionModule.NEARBY_OBJECTS_RANGE);
		if (serviceAndProvider == null) {
			throw new CommandException("There is no service called \"" + serviceName + "\" near the agent.");
		}
		
		Service service = serviceAndProvider.getFirstElement();
		WorldObject provider = serviceAndProvider.getSecondElement();
		
		// We fill its parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (serviceName.equalsIgnoreCase("walkto")) {
			if (params.isEmpty()) {
				throw new CommandException("The service \"" + serviceName + "\" must have a position parameter.");
			}
			
			parameters.put("destination", params.get(0));
		}
		
		// We interrupt the possible current agent animation
		agent.interruptAnimation();
		
		// We clear the current plan, and we add the service
		this.forcePlan = true;
		if (clearPreviousPlan) {
			this.currentPlan = new ClassicalPlan();
			this.currentIndex = 0;
			this.currentGoal = null;
		}
		
		this.currentPlan.append(new PlanElement(service, provider, parameters));
	}
	
	@Override
	public synchronized void enablePlanning() {
		this.forcePlan = false;
	}
	
	@Override
	public boolean isAlive() {
		return false;
	}
	
	@Override
	public void start() {
		this.planAndExecute();
	}
}
