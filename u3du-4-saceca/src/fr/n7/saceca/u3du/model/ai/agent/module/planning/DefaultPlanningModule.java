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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;
import fr.n7.saceca.u3du.model.ai.graph.GraphSolver;
import fr.n7.saceca.u3du.model.ai.graph.WeightedEdge;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.ai.statement.MemoryAwareU3duJexlContext;
import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;
import fr.n7.saceca.u3du.util.Log;

/**
 * The Class DefaultPlanningModule.
 * 
 * @author Sylvain Cambon
 */
public class DefaultPlanningModule implements PlanningModule {
	
	/** The Constant logger. */
	private static Logger logger = Logger.getLogger(DefaultPlanningModule.class);
	
	/** The Constant COLLECTION_SHUFFLE_ENABLED. */
	public static final boolean COLLECTION_SHUFFLE_ENABLED = true;
	
	/** The Constant NO_PLAN_FOUND. */
	private static final Plan NO_PLAN_FOUND = null;
	
	/** The Constant PLAN_MAX_DEPTH. */
	public static final int PLAN_MAX_DEPTH = 50;
	
	/** The agent. */
	private Agent agent;
	
	/** The current goal. */
	private Goal currentGoal = null;
	
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
	public DefaultPlanningModule(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Plans and executes.
	 */
	@Override
	public synchronized void planAndExecute() {
		// We forced a plan execution and thhere is no element in it
		if (this.forcePlan && this.currentPlan == null || (this.currentPlan != null && this.currentPlan.size() == 0)) {
			return;
		}
		// There is an element in the plan
		else if (this.forcePlan || this.currentGoal != null && !this.agent.getMemory().getGoals().isEmpty()
				&& this.currentGoal.equals(this.agent.getMemory().getGoals().get(0)) && this.currentPlan != null
				&& this.currentPlan.size() >= this.currentIndex) {
			this.execute();
		} else if (!this.agent.getMemory().getGoals().isEmpty()) {
			this.currentGoal = this.agent.getMemory().getGoals().get(0);
			this.currentPlan = this.buildPlan(this.agent, this.currentGoal, 0, PLAN_MAX_DEPTH);
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
	public Plan buildPlan(Agent agent, Goal goal, int currentDepth, int maxDepth) {
		if (maxDepth < currentDepth) {
			return NO_PLAN_FOUND;
		}
		if (!goal.seemsReached(new MemoryAwareU3duJexlContext(agent))) {
			// According to the memory, the goal seems not yet reached
			
			// Objects order randomization to avoid having always the same scene
			Collection<WorldObject> objects = agent.getMemory().getKnowledges();
			if (COLLECTION_SHUFFLE_ENABLED) {
				objects = new ArrayList<WorldObject>(objects);
				Collections.shuffle((List<WorldObject>) objects);
			}
			
			for (WorldObject object : objects) {
				// Services order randomization
				Collection<Service> services = object.getServices();
				if (COLLECTION_SHUFFLE_ENABLED) {
					services = new ArrayList<Service>(services);
					Collections.shuffle((List<Service>) services);
				}
				
				for (Service service : services) {
					// for (int i = 0; i < currentDepth; i++) {
					// System.out.print('\t');
					// }
					// System.out.println(service.getName());
					Agent clonedAgent = agent.deepDataClone();
					Agent clonedAgentMemory = clonedAgent.getMemory().getKnowledgeAboutOwner();
					if (service.getName().equals("walkTo")) {
						continue;
					}
					if (service.isUsable(object, clonedAgentMemory, null)) {
						// The service is usable, according to the memory
						// What about its accessibility ?
						try {
							PlanElement walkTo = this.handleWalkTo(object, clonedAgent,
									service.getMaxDistanceForUsage());
							service.execute(object, clonedAgentMemory, null, ExecutionMode.VIRTUAL);
							Goal goalClone = new Goal(goal.getSuccessCondition(), goal.getPriority());
							// Computing the next step of the plan
							Plan plan = this.buildPlan(clonedAgent, goalClone, currentDepth + 1, maxDepth);
							if (plan != NO_PLAN_FOUND) {
								// The computed plan works
								plan.cons(new PlanElement(service, object, null));
								if (walkTo != null) {
									plan.cons(walkTo);
								}
								return plan;
							}
						} catch (UnreachableObjectExpcetion e) {
							// The object cannot be reached
							continue;
						}
					}
				}
			}
			return NO_PLAN_FOUND;
		} else {
			return new DefaultPlan();
		}
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
	private PlanElement handleWalkTo(WorldObject provider, Agent consumer, int maxDist)
			throws UnreachableObjectExpcetion {
		Oriented2DPosition initialConsumerPosition = consumer.getMemory().getKnowledgeAboutOwner().getPosition();
		Oriented2DPosition providerPosition = provider.getPosition();
		
		final float distance = initialConsumerPosition.distance(providerPosition);
		if (maxDist > distance) {
			// The service can directly be used
			return null;
			
		} else {
			// The service cannot directly be used, a path has to be found first
			AI ai = Model.getInstance().getAI();
			UndirectedSparseGraph<WorldObject, WeightedEdge> walkableGraph = ai.getWorld().getWalkableGraph();
			
			// Looking for the closest walkable item to the agent
			WorldObject closestWalkableToAgent = ai.getWorld().getClosestWalkable(consumer.getPosition());
			
			// Looking for the closest walkable item to the object
			WorldObject closestWalkableToObject = ai.getWorld().getClosestWalkable(provider.getPosition());
			
			try {
				boolean existsPath = GraphSolver.forGraph(walkableGraph).existPath(closestWalkableToAgent,
						closestWalkableToObject);
				final float distanceAfterWalk = closestWalkableToObject.getPosition().distance(providerPosition);
				final boolean distanceOk = distanceAfterWalk <= maxDist;
				if (existsPath && distanceOk) {
					// A path exists to a place close enough to the object
					Service walkTo = ai.getEntitiesFactoryMaterials().getServiceRepository().get("walkTo"); // TODO:
					// constant
					Map<String, Object> parameters = new HashMap<String, Object>();
					// parameters.put("source", closestWalkableToAgent);
					parameters.put("destination", closestWalkableToObject);
					PlanElement planElement = new PlanElement(walkTo, closestWalkableToAgent, parameters);
					walkTo.execute(closestWalkableToAgent, consumer, parameters, ExecutionMode.VIRTUAL);
					// Ensures that the job is well done
					consumer.setPosition(closestWalkableToObject.getPosition());
					consumer.getMemory().getKnowledgeAboutOwner().setPosition(closestWalkableToObject.getPosition());
					return planElement;
				}
			} catch (IllegalArgumentException iae) {
				// Should not occur
				logger.error("A node was not in the sidewalk graph...", iae);
			}
			
			// Too far and unreachable or error
			
		}
		
		throw new UnreachableObjectExpcetion();
	}
	
	/**
	 * Execution part.
	 */
	public void execute() {
		PlanElement planElement = this.currentPlan.get(this.currentIndex);
		// FIXME : pas d'exec
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
		return DefaultPlanningModule.class.getCanonicalName();
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
	public final Goal getCurrentGoal() {
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
			this.currentPlan = new DefaultPlan();
			this.currentIndex = 0;
			this.currentGoal = null;
		}
		
		this.currentPlan.append(new PlanElement(service, provider, parameters));
	}
	
	@Override
	public synchronized void enablePlanning() {
		this.forcePlan = false;
	}
}
