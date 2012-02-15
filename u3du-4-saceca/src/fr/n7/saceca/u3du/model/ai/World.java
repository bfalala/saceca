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
package fr.n7.saceca.u3du.model.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point2f;

import org.apache.log4j.Logger;

import Emotion_primary.matrix_rule;
import Emotion_primary.property_rule;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.graph.WeightedEdge;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The Class World contains all the world objects.
 * 
 * @author Jérôme Dalbert
 */
public class World {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(World.class);
	
	/** The world objects. */
	private Map<Long, WorldObject> worldObjects;
	
	/** The walkable graph. */
	private UndirectedSparseGraph<WorldObject, WeightedEdge> walkableGraph;
	/** matrix_emotion_effect */
	private int[][] matrix_effect_emotions;
	
	private ArrayList<PropertyLink> linkList;
	
	private double[][] matrix_primary_secondary;
	
	private Hashtable<String, Class<? extends Action>> stimulusReactionTable;
	
	private matrix_rule matrule;
	
	private ArrayList<property_rule> property_rule_tab;
	
	private Hashtable<String, Integer> emotion_index_tab;
	
	/** the effectlist */
	private ArrayList<Couple<String, ServiceProperty>> serviceEffectList;
	
	public ArrayList<property_rule> getProperty_rule_tab() {
		return this.property_rule_tab;
	}
	
	public void setProperty_rule_tab(ArrayList<property_rule> property_rule_tab) {
		this.property_rule_tab = property_rule_tab;
	}
	
	public matrix_rule getMatrule() {
		return this.matrule;
	}
	
	public void setMatrule(matrix_rule matrule) {
		this.matrule = matrule;
	}
	
	/**
	 * Instantiates a new world.
	 */
	public World() {
		this.worldObjects = new HashMap<Long, WorldObject>();
		
	}
	
	/**
	 * Removes properly an object from the world.
	 * 
	 * @param objectId
	 *            the id of the object to destroy
	 */
	public void remove(long id) {
		
		for (Agent a : this.getAgents()) {
			a.getMemory().forget(id);
		}
		
		this.worldObjects.remove(id);
		
	}
	
	/**
	 * Gets the world objects.
	 * 
	 * @return the entities
	 */
	public Map<Long, WorldObject> getWorldObjects() {
		return this.worldObjects;
	}
	
	/**
	 * Gets the world objects.
	 * 
	 * @param filter
	 *            the filter
	 * @return the world objects
	 */
	public Collection<WorldObject> getWorldObjects(WorldObjectFilter filter) {
		List<WorldObject> filteredWorldObjects = new ArrayList<WorldObject>();
		
		for (WorldObject worldObject : this.worldObjects.values()) {
			if (filter.accept(worldObject)) {
				filteredWorldObjects.add(worldObject);
			}
		}
		
		return filteredWorldObjects;
	}
	
	/**
	 * Sets the entities.
	 * 
	 * @param objects
	 *            the new entities
	 */
	public void setObjects(Map<Long, WorldObject> objects) {
		this.worldObjects = objects;
	}
	
	/**
	 * Broadcasts a message to surrounding agents. The message is not sent to the sender. The
	 * message is also sent to the graphics.
	 * 
	 * @param sender
	 *            the sender
	 * @param message
	 *            the message
	 */
	public void locallyBroadcastMessage(Agent sender, Message message) {
		for (Agent receiver : this.getAgents()) {
			boolean distanceOk;
			try {
				distanceOk = sender.getPosition().distance(receiver.getPosition()) <= receiver.getPropertiesContainer()
						.getInt(Internal.Agent.PERCEPTION_MAX_HEARING_DISTANCE);
				boolean notToOneSelf = !sender.equals(receiver);
				if (distanceOk && notToOneSelf) {
					receiver.sendMessage(message);
				}
			} catch (UnknownPropertyException e) {
				logger.error(e);
			}
			
		}
		Model.getInstance().getGraphics().showMessage(sender.getId(), message.toString());
	}
	
	/**
	 * Builds the walkableGraph.
	 */
	public synchronized void buildWalkableGraph() {
		this.walkableGraph = new UndirectedSparseGraph<WorldObject, WeightedEdge>();
		double maxPavementDist = (1 + 10 * Constants.EPSILON) * Math.sqrt(2) * 4;
		double maxPedestrianCrossingDist = (2 + 10 * Constants.EPSILON) * Math.sqrt(2) * 3;
		
		for (WorldObject object : this.worldObjects.values()) {
			if (object.isWalkable()) {
				// It can be added to the walkableGraph
				this.walkableGraph.addVertex(object);
				for (WorldObject node : this.walkableGraph.getVertices()) {
					if (!node.equals(object)) {
						// We determine the maxDistance (it is bigger when there are pedestrian
						// crossings)
						double maxDist = node.getModelName().equals("Pavement") ? maxPavementDist
								: maxPedestrianCrossingDist;
						if (object.getModelName().equals("PedestrianCrossing")) {
							maxDist = maxPedestrianCrossingDist;
						}
						
						// The weight is the distance between the two nodes, which is larger for
						// pedestrian crossings and diagonal walk.
						final float distance = node.getPosition().distance(object.getPosition());
						if (distance <= maxDist) {
							// Integers are used as weight to speed up calculations
							this.walkableGraph.addEdge(new WeightedEdge((int) distance), node, object);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gets the sidewalk graph.
	 * 
	 * @return the walkableGraph
	 */
	public synchronized final UndirectedSparseGraph<WorldObject, WeightedEdge> getWalkableGraph() {
		if (this.walkableGraph == null) {
			this.buildWalkableGraph();
		}
		return this.walkableGraph;
	}
	
	/**
	 * Gets the objects around the given world object, for the specified range.
	 * 
	 * @param sourceObject
	 *            the world object around which we find the nearest objects
	 * @param worldObjects
	 *            the world objects
	 * @param range
	 *            the range
	 * @return the nearest objects
	 */
	public Collection<WorldObject> getWorldObjectsAround(WorldObject sourceObject,
			Collection<WorldObject> worldObjects, int range) {
		List<WorldObject> nearestObjects = new ArrayList<WorldObject>();
		
		for (WorldObject nearObject : worldObjects) {
			if (nearObject.getPosition().distance(sourceObject.getPosition()) < range) {
				nearestObjects.add(nearObject);
			}
		}
		
		return nearestObjects;
	}
	
	/**
	 * Gets the world objects around.
	 * 
	 * @param sourceObject
	 *            the source object
	 * @param range
	 *            the range
	 * @return the world objects around
	 */
	public Collection<WorldObject> getWorldObjectsAround(WorldObject sourceObject, int range) {
		return this.getWorldObjectsAround(sourceObject, this.worldObjects.values(), range);
	}
	
	/**
	 * Gets the closest object.
	 * 
	 * @param sourceObject
	 *            the source object
	 * @param worldObjects
	 *            the world objects
	 * @return the closest object
	 */
	public WorldObject getClosestWorldObject(WorldObject sourceObject, Collection<WorldObject> worldObjects) {
		WorldObject closestObject = null;
		float minDist = Float.MAX_VALUE;
		
		for (WorldObject worldObject : worldObjects) {
			float dist = sourceObject.getPosition().distance(worldObject.getPosition());
			
			if (dist < minDist) {
				minDist = dist;
				closestObject = worldObject;
			}
		}
		
		return closestObject;
	}
	
	/**
	 * Gets the nearest service and its provider. The service must have the "serviceName" name and
	 * the search is performed around "givenWorldObject"
	 * 
	 * @param serviceName
	 *            the service name
	 * @param worldObject
	 *            the world object around which we find the nearest service
	 * @param range
	 *            the range
	 * @return the nearest service and its provider
	 */
	public Couple<Service, WorldObject> getFirstNearServiceAndProvider(String serviceName, WorldObject worldObject,
			int range) {
		for (WorldObject object : this.getWorldObjectsAround(worldObject, this.worldObjects.values(), range)) {
			for (Service service : object.getServices()) {
				if (service.getName().toLowerCase().equals(serviceName.toLowerCase())) {
					return new Couple<Service, WorldObject>(service, object);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the closest walkable item to a certain position, among given possible items
	 * 
	 * @param among
	 *            the items to iterate over
	 * @param to
	 *            the position to be close to
	 * @return the closest walkable
	 */
	public WorldObject getClosestWalkable(Collection<WorldObject> among, Point2f to) {
		float minDist = 100000;
		WorldObject closestWalkableToAgent = null;
		for (WorldObject item : among) {
			if (item.isWalkable()) {
				
				float dist = to.distance(item.getPosition());
				if (dist < minDist) {
					minDist = dist;
					closestWalkableToAgent = item;
				}
			}
		}
		return closestWalkableToAgent;
	}
	
	/**
	 * Gets the closest sidewalk to a certain position.
	 * 
	 * @param to
	 *            the position to be close to
	 * @return the closest sidewalk
	 */
	public WorldObject getClosestWalkable(Point2f to) {
		return this.getClosestWalkable(this.getWalkableGraph().getVertices(), to);
	}
	
	/**
	 * Gets the agents.
	 * 
	 * @return a read-only colletion of reactive agents
	 */
	public Collection<Agent> getAgents() {
		List<Agent> agents = new ArrayList<Agent>();
		
		for (WorldObject object : this.worldObjects.values()) {
			if (object instanceof Agent) {
				agents.add((Agent) object);
			}
		}
		
		return Collections.unmodifiableCollection(agents);
	}
	
	/**
	 * Gets the reactive objects, i.e. objects that are not agents.
	 * 
	 * @return a read-only colletion of reactive objects
	 */
	public Collection<WorldObject> getReactiveObjects() {
		List<WorldObject> reactiveObjects = new ArrayList<WorldObject>();
		
		for (WorldObject object : this.worldObjects.values()) {
			if (!(object instanceof Agent)) {
				reactiveObjects.add(object);
			}
		}
		
		return Collections.unmodifiableCollection(reactiveObjects);
	}
	
	public int[][] getMatrix_effect_emotions() {
		return this.matrix_effect_emotions;
	}
	
	public void setMatrix_effect_emotions(int[][] matrix_effect_emotions) {
		this.matrix_effect_emotions = matrix_effect_emotions;
	}
	
	public ArrayList<PropertyLink> getLinkList() {
		return this.linkList;
	}
	
	public void setLinkList(ArrayList<PropertyLink> linkList) {
		this.linkList = linkList;
	}
	
	public void setMatrix_primary_secondary(double[][] matrix_primary_secondary) {
		this.matrix_primary_secondary = matrix_primary_secondary;
	}
	
	public double[][] getMatrix_primary_secondary() {
		return this.matrix_primary_secondary;
	}
	
	public void setStimulusReactionTable(Hashtable<String, Class<? extends Action>> stimulusReactionTable) {
		this.stimulusReactionTable = stimulusReactionTable;
	}
	
	public Hashtable<String, Class<? extends Action>> getStimulusReactionTable() {
		return this.stimulusReactionTable;
	}
	
	public void setEmotion_index_tab(Hashtable<String, Integer> emotion_index_tab) {
		this.emotion_index_tab = emotion_index_tab;
	}
	
	public Hashtable<String, Integer> getEmotion_index_tab() {
		return this.emotion_index_tab;
	}
	
	public void setServiceEffectList(ArrayList<Couple<String, ServiceProperty>> serviceEffectList) {
		this.serviceEffectList = serviceEffectList;
	}
	
	public ArrayList<Couple<String, ServiceProperty>> getServiceEffectList() {
		return this.serviceEffectList;
	}
}
