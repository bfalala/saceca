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
package fr.n7.saceca.u3du.model.ai.service.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.World;
import fr.n7.saceca.u3du.model.ai.WorldObjectFilter;
import fr.n7.saceca.u3du.model.ai.graph.GraphSolver;
import fr.n7.saceca.u3du.model.ai.graph.WeightedEdge;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.animation.MoveHumanAnimation;
import fr.n7.saceca.u3du.model.util.MathUtil;

/**
 * The Class WalkTo.
 * 
 * This class is the code linked to the service "walkTo".
 * 
 * @author Jérôme Dalbert
 */
public class WalkTo implements Action {
	
	/** The path. It is a list of walkable objects, ordered from the source to the destination. */
	private List<WorldObject> path;
	
	/** The current place to go. */
	private WorldObject nextPlace;
	
	/** The can walk. */
	private boolean canWalk;
	
	/** The Constant TRAFFIC_LIGHT_CHECK_RANGE. */
	private static final int TRAFFIC_LIGHT_RANGE = 10;
	
	/**
	 * Builds the path to go from source to destination.
	 * 
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 */
	public void initPath(WorldObject source, WorldObject destination) {
		this.path = new ArrayList<WorldObject>();
		this.path.add(source);
		
		UndirectedSparseGraph<WorldObject, WeightedEdge> graph = Model.getInstance().getAI().getWorld()
				.getWalkableGraph();
		this.path = GraphSolver.forGraph(graph).getShortestPath(source, destination);
		this.path.size();
	}
	
	public static int getNumberOfPavements(WorldObject source, WorldObject destination) {
		List<WorldObject> path = new ArrayList<WorldObject>();
		path.add(source);
		
		UndirectedSparseGraph<WorldObject, WeightedEdge> graph = Model.getInstance().getAI().getWorld()
				.getWalkableGraph();
		path = GraphSolver.forGraph(graph).getShortestPath(source, destination);
		return path.size();
	}
	
	@Override
	public ExecutionStatus executeStep(WorldObject provider, WorldObject consumer, Map<String, Object> parameters) {
		WorldObject destination = (WorldObject) parameters.get("destination");
		
		if (destination == null) {
			return ExecutionStatus.FAILURE;
		}
		if (provider == null) {
			return ExecutionStatus.SUCCESSFUL_TERMINATION;
		}
		
		if (provider.getPosition().egal(destination.getPosition())) {
			return ExecutionStatus.SUCCESSFUL_TERMINATION;
		}
		
		// Initialization
		if (this.path == null || (!this.path.isEmpty() && !destination.equals(this.path.get(this.path.size() - 1)))) {
			this.initPath(provider, destination);
		}
		
		if (this.path == null) {
			return ExecutionStatus.CONTINUE_NEXT_TIME;
		}
		
		Animation animation = consumer.getAnimation();
		
		// If the animation is finished, we decide to walk to the next destination
		if (animation == null) {
			this.canWalk = false;
			if (this.path.isEmpty()) {
				this.path = null;
				return ExecutionStatus.SUCCESSFUL_TERMINATION;
			}
			
			this.nextPlace = this.path.remove(0);
			
			animation = new MoveHumanAnimation(consumer.getId(), this.nextPlace.getPosition());
			animation.pause();
			consumer.setAnimation(animation);
			Model.getInstance().getGraphics().sendAnimation(animation);
		}
		
		// We don't walk if we must stop at a pedestrian crossing
		if (animation != null) {
			if (this.stopAtPedestrianCrossing(consumer, animation)) {
				animation.pause();
			} else {
				this.canWalk = true;
				animation.resume();
			}
		}
		
		return ExecutionStatus.CONTINUE_NEXT_TIME;
	}
	
	/**
	 * Determines if the agent must stop at a pedestrian crossing. We determine only thanks the
	 * traffic lights surrounding the pedestrian crossing.
	 * 
	 * @param agent
	 *            the agent
	 * @param animation
	 *            the animation
	 * @return true, if successful
	 */
	private boolean stopAtPedestrianCrossing(WorldObject agent, Animation animation) {
		/* We don't stop if we have previously decided to walk */
		if (this.canWalk) {
			return false;
		}
		
		if (this.nextPlace == null) {
			return false;
		}
		/* We don't stop if the next destination is not a pedestrian crossing */
		if (!this.nextPlace.getModelName().toLowerCase().equals("pedestriancrossing")) {
			return false;
		}
		
		/* We get the traffic lights around the pedestrian crossing */
		World world = Model.getInstance().getAI().getWorld();
		Collection<WorldObject> trafficLights = world.getWorldObjects(new WorldObjectFilter() {
			@Override
			public boolean accept(WorldObject worldObject) {
				return worldObject.getModelName().toLowerCase().equals("trafficlight");
			}
		});
		Collection<WorldObject> trafficLightsAround = world.getWorldObjectsAround(this.nextPlace, trafficLights,
				TRAFFIC_LIGHT_RANGE);
		if (trafficLightsAround.isEmpty()) {
			return false;
		}
		
		/* Case when there are 2 traffic lights around the pedestrian crossing */
		if (trafficLightsAround.size() == 2) {
			// We get the close and far traffic light
			WorldObject closeTrafficLight = null;
			WorldObject farTrafficLight = null;
			float minDist = Float.MAX_VALUE;
			float maxDist = Float.MIN_VALUE;
			for (WorldObject trafficLight : trafficLightsAround) {
				float dist = trafficLight.getPosition().distance(agent.getPosition());
				if (dist < minDist) {
					minDist = dist;
					closeTrafficLight = trafficLight;
				}
				if (dist > maxDist) {
					maxDist = dist;
					farTrafficLight = trafficLight;
				}
			}
			
			// We get the far traffic light color
			EnumElement color = null;
			try {
				color = farTrafficLight.getPropertiesContainer().getEnumerationElement("m_TrafficLight_color");
			} catch (UnknownPropertyException e) {
				return false;
			}
			
			// We get the angle between the close and far traffic light
			Vector2f closeTLDirection = closeTrafficLight.getInitialDirection();
			Vector2f farTLDirection = farTrafficLight.getInitialDirection();
			double angle = MathUtil.signedAngle(closeTLDirection, farTLDirection);
			double halfPi = Math.PI / 2;
			
			// +45° angle : the far traffic light must be green or orange to stop
			if (angle > halfPi - MathUtil.BIG_EPSILON && angle < halfPi + MathUtil.BIG_EPSILON) {
				return color.equals(new EnumElement("GREEN")) || color.equals(new EnumElement("ORANGE"));
			}
			// -45° angle : the far traffic light must be red or orange to stop
			else if (angle > -halfPi - MathUtil.BIG_EPSILON && angle < -halfPi + MathUtil.BIG_EPSILON) {
				return color.equals(new EnumElement("RED")) || color.equals(new EnumElement("ORANGE"));
			}
		}
		/* Case when there is only one traffic light */
		else if (trafficLightsAround.size() == 1) {
			// Because trafficLightsAround is a collection, we access its unique element only by
			// iterating...
			WorldObject trafficLight = null;
			for (WorldObject tf : trafficLightsAround) {
				trafficLight = tf;
			}
			
			// We get the far traffic light color
			EnumElement color = null;
			try {
				color = trafficLight.getPropertiesContainer().getEnumerationElement("m_TrafficLight_color");
			} catch (UnknownPropertyException e) {
				return false;
			}
			
			// The traffic light must be green or orange to stop
			return color.equals(new EnumElement("GREEN")) || color.equals(new EnumElement("ORANGE"));
		}
		
		return false;
	}
	
	@Override
	public String getStorageLabel() {
		return WalkTo.class.getCanonicalName();
	}
	
	@Override
	public int getDuration(WorldObject provider, WorldObject consumer, Map<String, Object> parameters)
			throws UnknownPropertyException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
