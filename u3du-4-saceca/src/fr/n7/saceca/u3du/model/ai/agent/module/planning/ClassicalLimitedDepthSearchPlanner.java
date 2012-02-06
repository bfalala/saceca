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

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.graph.GraphSolver;
import fr.n7.saceca.u3du.model.ai.graph.WeightedEdge;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * The classical planner class
 * 
 * @author Ciprian Munteanu
 * 
 */
public class ClassicalLimitedDepthSearchPlanner {
	
	public ClassicalLimitedDepthSearchPlanner() {
		super();
	}
	
	public Couple<Boolean, Plan> buildPlan(Agent agent, MMGoal goal, int currentDepth, int maxDepth) {
		if (maxDepth < currentDepth) {
			return new Couple<Boolean, Plan>(false, ClassicalPlanningModule.NO_PLAN_FOUND);
		}
		if (!agent.getMemory().checkVirtualMemory(goal.getSuccessCondition())) {
			// According to the memory, the goal seems not yet reached
			
			// Objects order randomization to avoid having always the same scene
			Collection<WorldObject> objects = agent.getMemory().getKnowledges();
			if (ClassicalPlanningModule.COLLECTION_SHUFFLE_ENABLED) {
				objects = new ArrayList<WorldObject>(objects);
				Collections.shuffle((List<WorldObject>) objects);
			}
			
			for (WorldObject object : objects) {
				// Services order randomization
				Collection<Service> services = object.getServices();
				if (ClassicalPlanningModule.COLLECTION_SHUFFLE_ENABLED) {
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
					
					if (service.isClassicUsable(object, clonedAgentMemory)) {
						// The service is usable, according to the memory
						// What about its accessibility ?
						try {
							PlanElement walkTo = this.handleWalkTo(object, clonedAgent,
									service.getMaxDistanceForUsage());
							// service.execute(object, clonedAgentMemory, null,
							// ExecutionMode.VIRTUAL);
							
							clonedAgentMemory.getMemory().updateVirtualMemory(service);
							
							MMGoal goalClone = new MMGoal(goal.getSuccessCondition(), goal.getPriority());
							// Computing the next step of the plan
							Couple<Boolean, Plan> resultedPlan = this.buildPlan(clonedAgent, goalClone,
									currentDepth + 1, maxDepth);
							if (resultedPlan.getSecondElement() != ClassicalPlanningModule.NO_PLAN_FOUND) {
								// The computed plan works
								resultedPlan.getSecondElement().cons(new PlanElement(service, object, null));
								if (walkTo != null) {
									resultedPlan.getSecondElement().cons(walkTo);
								}
								return resultedPlan;
							}
						} catch (UnreachableObjectExpcetion e) {
							// The object cannot be reached
							continue;
						}
					}
				}
			}
			return new Couple<Boolean, Plan>(false, ClassicalPlanningModule.NO_PLAN_FOUND);
		} else {
			return new Couple<Boolean, Plan>(true, new ClassicalPlan());
		}
	}
	
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
				
			}
			
			// Too far and unreachable or error
			
		}
		
		throw new UnreachableObjectExpcetion();
	}
	
}
