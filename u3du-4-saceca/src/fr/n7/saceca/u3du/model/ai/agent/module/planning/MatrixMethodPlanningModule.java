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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.World;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization.GenerateProperties;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization.Matrix;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;
import fr.n7.saceca.u3du.util.Log;

/**
 * The MatrixMethodPlanningModule class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class MatrixMethodPlanningModule implements PlanningModule {
	/** The Constant logger. */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MatrixMethodPlanningModule.class);
	
	/** The Constant COLLECTION_SHUFFLE_ENABLED. */
	public static final boolean COLLECTION_SHUFFLE_ENABLED = true;
	
	/** The Constant NO_PLAN_FOUND. */
	public static final Plan NO_PLAN_FOUND = null;
	
	public static final Plan GOAL_ALREADY_ACHIEVED = null;
	
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
	public MatrixMethodPlanningModule(Agent agent) {
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
		else if (this.forcePlan || this.currentGoal != null && !this.agent.getMemory().getGoalStack().isEmpty()
				&& this.currentGoal.equals(this.agent.getMemory().getGoalStack().get(0)) && this.currentPlan != null
				&& this.currentPlan.size() >= this.currentIndex) {
			this.execute();
			// There is a goal in the stack
		} else if (!this.agent.getMemory().getGoalStack().isEmpty()) {
			this.currentGoal = this.agent.getMemory().getGoalStack().get(0);
			// if the goal is a reactive one, we execute it without planning
			if (this.currentGoal.getSuccessCondition().getPropertyName().startsWith("reactive")) {
				this.executeStimulusReaction();
				// we plan to reach the currentGoal
			} else {
				this.currentIndex = 0;
				this.currentPlan = new MatrixMethodPlan();
				// if the current goal is reacheable we plan for it
				if (this.currentGoal.isReachable()) {
					Couple<Boolean, Plan> resultedPlan = this.buildPlan(this.agent, this.currentGoal, 0, 0);
					this.currentPlan = resultedPlan.getSecondElement();
					
					if (this.currentPlan == null) {
						if (!resultedPlan.getFirstElement()) {
							this.currentGoal.setReachable(false);
							return;
							// if the current plan is null because the goal is already reached
						} else {
							this.currentGoal.setReachable(true);
							this.currentGoal.setReached(true);
							return;
						}
					}
					// prepare the plan for execution
					this.preparePlanForExecution();
				}
			}
		}
	}
	
	/**
	 * Executes a stimulus-reaction
	 */
	private void executeStimulusReaction() {
		Hashtable<String, Class<? extends Action>> stimulusReactionTable = Model.getInstance().getAI().getWorld()
				.getStimulusReactionTable();
		
		if (stimulusReactionTable == null) {
			return;
		}
		
		try {
			Class<? extends Action> actionClass = stimulusReactionTable.get(this.currentGoal.getSuccessCondition()
					.getPropertyName());
			Action action = null;
			
			action = actionClass.newInstance();
			
			if (action != null) {
				action.executeStep(null, this.agent, null);
			}
		} catch (Exception e) {
			
		}
		
		this.currentGoal.setReachable(true);
		this.currentGoal.setReached(true);
		
		this.currentIndex = 0;
		
		this.currentPlan = null;
		
	}
	
	/**
	 * Builds a plan. The currentDepth and the maxDepth parameters are useless but they are kept in
	 * order to implement correctly the PlanningModule interface
	 * 
	 * @param agent
	 *            the agent
	 * @param goal
	 *            the goal
	 * @param currentDepth
	 *            the current depth - unused
	 * @param maxDepth
	 *            the max depth - unused
	 * @return the plan
	 */
	@Override
	public Couple<Boolean, Plan> buildPlan(Agent agent, MMGoal currentGoal, int currentDepth, int maxDepth) {
		ArrayList<Service> serviceList = new ArrayList<Service>();
		
		boolean serviceAdded = false;
		
		for (WorldObject object : agent.getMemory().getKnowledges()) {
			for (Service service : object.getServices()) {
				service.setProviderId(object.getId());
				if (service.getName().equals("walkTo")) {
					if (!serviceAdded) {
						serviceList.add(service.deepDataClone());
						serviceAdded = true;
					}
				} else {
					serviceList.add(service.deepDataClone());
				}
			}
		}
		
		GenerateProperties propertyGenerator = new GenerateProperties(serviceList, new ArrayList<PropertyLink>());
		ArrayList<ServiceProperty> servicePropertyList = propertyGenerator.getPropertiesFromServiceList();
		
		ArrayList<PropertyLink> linkList = Model.getInstance().getAI().getWorld().getLinkList();
		
		Memory virtualMemory = agent.getMemory().deepDataClone();
		virtualMemory.setAgent(agent);
		
		Matrix matrixServProp = new Matrix();
		matrixServProp.createMatrixA(serviceList, servicePropertyList);
		
		Matrix matrixCondServ = new Matrix();
		matrixCondServ.createMatrixP(servicePropertyList, serviceList);
		
		MatrixMethodPlanner mmPlanner = new MatrixMethodPlanner(this.currentGoal, serviceList, servicePropertyList,
				virtualMemory, linkList, matrixServProp, matrixCondServ);
		
		return mmPlanner.buildPlan();
	}
	
	/**
	 * Prepares the plan for execution
	 */
	public void preparePlanForExecution() {
		if (this.currentPlan == null) {
			return;
		}
		World world = Model.getInstance().getAI().getWorld();
		for (int i = 0; i < this.currentPlan.size(); i++) {
			PlanElement planElement = this.currentPlan.get(i);
			if (!planElement.getService().getName().equals("walkTo")) {
				planElement.setProvider(world.getWorldObjects().get(planElement.getService().getProviderId()));
			} else {
				String[] coordonates = planElement.getService().getParamValue("position").split("_");
				Oriented2DPosition initialConsumerPosition = new Oriented2DPosition(Float.parseFloat(coordonates[0]),
						Float.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2]));
				
				coordonates = planElement.getService().getParamValue("destination").split("_");
				Oriented2DPosition providerPosition = new Oriented2DPosition(Float.parseFloat(coordonates[0]),
						Float.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2]));
				
				int maxDist = 0;
				if (i < this.currentPlan.size() - 1) {
					maxDist = this.currentPlan.get(i + 1).getService().getMaxDistanceForUsage();
				}
				
				final float distance = initialConsumerPosition.distance(providerPosition);
				if (maxDist > distance) {
					// The service can directly be used
					planElement = null;
				} else {
					WorldObject provider = world.getClosestWalkable(initialConsumerPosition);
					planElement.setProvider(provider);
					planElement.setProviderId(provider.getId());
				}
			}
		}
	}
	
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
						this.agent.getMemory().getPastPlans().put(this.currentGoal, this.currentPlan);
					}
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
		return MatrixMethodPlanningModule.class.getCanonicalName();
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "MatrixMethodPlanningModule [currentGoal=" + this.currentGoal + ", currentPlan=" + this.currentPlan
				+ ", currentIndex=" + this.currentIndex + "]";
	}
	
	/**
	 * Returns the current plan
	 */
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
	
	/**
	 * We force the execution of a command received through the console and we disable the planning
	 * process
	 */
	@Override
	public synchronized void forceExecution(String serviceName, List<Object> params, boolean clearPreviousPlan)
			throws CommandException {
		
		Agent agent = this.agent;
		
		// We find the nearest service around the "agent" variable
		Couple<Service, WorldObject> serviceAndProvider = Model.getInstance().getAI().getWorld()
				.getFirstNearServiceAndProvider(serviceName, agent, PerceptionModule.NEARBY_OBJECTS_RANGE);
		if (serviceAndProvider == null) {
			throw new CommandException("There is no service called \"" + serviceName + "\" near the agent.");
		}
		
		Service service = serviceAndProvider.getFirstElement();
		WorldObject provider = serviceAndProvider.getSecondElement();
		
		// We interrupt the possible current agent animation
		agent.interruptAnimation();
		
		// We clear the current plan, and we add the service
		this.forcePlan = true;
		if (clearPreviousPlan) {
			this.currentPlan = new MatrixMethodPlan();
			this.currentIndex = 0;
			this.currentGoal = null;
		}
		
		if (service.getName().equals("walkTo")) {
			if (params.isEmpty()) {
				throw new CommandException("The service \"" + serviceName + "\" must have a position parameter.");
			}
			
			Oriented2DPosition position = this.agent.getPosition();
			
			service.getParameter("position").setParamValue(position.x + "_" + position.y + "_" + position.theta);
			
			position = ((WorldObject) params.get(0)).getPosition();
			
			service.getParameter("destination").setParamValue(position.x + "_" + position.y + "_" + position.theta);
		}
		
		this.currentPlan.append(new PlanElement(service, provider, null));
	}
	
	/**
	 * Enables the planning process
	 */
	@Override
	public synchronized void enablePlanning() {
		this.forcePlan = false;
	}
	
}
