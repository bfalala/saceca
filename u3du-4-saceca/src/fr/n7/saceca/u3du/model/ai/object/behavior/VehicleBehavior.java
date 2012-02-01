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
package fr.n7.saceca.u3du.model.ai.object.behavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.World;
import fr.n7.saceca.u3du.model.ai.WorldObjectFilter;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.animation.MoveVehicleAnimation;
import fr.n7.saceca.u3du.model.util.MathUtil;

/**
 * The Class VehicleBehavior.
 * 
 * The vehicle circularly drives from place to place. This class is notably used by the cars.
 * 
 * @author Jérôme Dalbert
 */
public class VehicleBehavior implements Behavior {
	
	/** The circuit, containing the different destinations. */
	protected List<Point2f> circuit;
	
	/** The circuit size. */
	private int circuitSize;
	
	/** The index of the next destination */
	private int iNextDest;
	
	/** The vehicle. */
	protected WorldObject vehicle;
	
	/** The Constant TRAFFIC_LIGHT_DETECTION_RANGE. */
	private static final int TRAFFIC_LIGHT_RANGE = 15;
	
	/** The Constant COLLISION_DETECTION_RANGE. */
	private static final int OBSTACLE_RANGE = 15;
	
	@Override
	public void init(WorldObject object) {
		this.vehicle = object;
		
		// Circuit initialization
		this.circuit = new ArrayList<Point2f>();
		this.buildCircuit();
		this.circuitSize = this.circuit.size();
		
		this.iNextDest = -1;
	}
	
	@Override
	public void behave() {
		// First time initialization
		if (this.iNextDest == -1) {
			this.determineNextDestination();
		}
		
		Animation animation = this.vehicle.getAnimation();
		
		// Cases when the vehicle must pause or resume its animation
		if (animation != null) {
			if (this.mustStop()) {
				animation.pause();
			} else {
				animation.resume();
			}
		}
		
		// If the animation is finished, we go to the next destination
		if (animation == null) {
			Point2f destination = this.circuit.get(this.iNextDest);
			
			Animation a = new MoveVehicleAnimation(this.vehicle.getId(), destination);
			this.vehicle.setAnimation(a);
			Model.getInstance().getGraphics().sendAnimation(a);
			
			this.iNextDest = (this.iNextDest + 1) % this.circuitSize;
		}
	}
	
	/**
	 * Determines if the vehicle must stop.
	 * 
	 * @return true, if successful
	 */
	protected boolean mustStop() {
		return this.isLightRedOrOrange() || this.isObstacleInFront();
	}
	
	/**
	 * Checks if the traffic light on the vehicle's right side is red or orange.
	 * 
	 * @return true, if is the light is red or orange
	 */
	public boolean isLightRedOrOrange() {
		// We get the traffic lights around the vehicle
		World world = Model.getInstance().getAI().getWorld();
		Collection<WorldObject> trafficLights = world.getWorldObjects(new WorldObjectFilter() {
			@Override
			public boolean accept(WorldObject worldObject) {
				return worldObject.getModelName().toLowerCase().equals("trafficlight");
			}
		});
		Collection<WorldObject> trafficLightsAround = world.getWorldObjectsAround(this.vehicle, trafficLights,
				TRAFFIC_LIGHT_RANGE);
		if (trafficLightsAround.isEmpty()) {
			return false;
		}
		
		// We get the closest one
		WorldObject trafficLight = world.getClosestWorldObject(this.vehicle, trafficLightsAround);
		
		// We check that it is on the vehicle's front right side
		Vector2f vehicleDirection = this.getVehicleDirection();
		Vector2f vehicleToLight = new Vector2f(trafficLight.getPosition().x - this.vehicle.getPosition().x,
				trafficLight.getPosition().y - this.vehicle.getPosition().y);
		
		double angle1 = MathUtil.signedAngle(vehicleToLight, vehicleDirection);
		boolean rightAndFront = angle1 < 0 && angle1 > -Math.PI / 2;
		if (!rightAndFront) {
			return false;
		}
		
		// We check that the traffic light is facing the vehicle
		Vector2f trafficLightDirection = trafficLight.getInitialDirection();
		float angle2 = (vehicleDirection.angle(trafficLightDirection)) % (MathUtil.PI - MathUtil.BIG_EPSILON);
		if (angle2 < -2 * MathUtil.BIG_EPSILON || angle2 > 2 * MathUtil.BIG_EPSILON) {
			return false;
		}
		
		// We check its color
		try {
			EnumElement color = trafficLight.getPropertiesContainer().getEnumerationElement("m_TrafficLight_color");
			if (color.equals(new EnumElement("RED")) || color.equals(new EnumElement("ORANGE"))) {
				return true;
			}
		} catch (UnknownPropertyException e) {
			return false;
		}
		
		return false;
	}
	
	/**
	 * Gets the vehicle orientation.
	 * 
	 * @return the vehicle orientation
	 */
	protected Vector2f getVehicleDirection() {
		Point2f destination = this.circuit.get((this.iNextDest - 1 + this.circuitSize) % this.circuitSize);
		return new Vector2f(destination.x - this.vehicle.getPosition().x, destination.y - this.vehicle.getPosition().y);
	}
	
	/**
	 * Checks if the object is an obstacle.
	 * 
	 * @param worldObject
	 *            the world object
	 * @return true, if is obstacle
	 */
	public boolean isObstacle(WorldObject worldObject) {
		Collection<Category> categories = worldObject.getCategories();
		
		return categories.contains(WorldObject.VEHICLE) || categories.contains(WorldObject.HUMAN);
	}
	
	/**
	 * Checks if there is an obstacle in front.
	 * 
	 * @return true, if a vehicle is in front
	 */
	public boolean isObstacleInFront() {
		// We get the obstacles around
		World world = Model.getInstance().getAI().getWorld();
		Collection<WorldObject> obstacles = world.getWorldObjects(new WorldObjectFilter() {
			@Override
			public boolean accept(WorldObject worldObject) {
				return worldObject != VehicleBehavior.this.vehicle && VehicleBehavior.this.isObstacle(worldObject);
			}
		});
		Collection<WorldObject> obstaclesAround = world.getWorldObjectsAround(this.vehicle, obstacles, OBSTACLE_RANGE);
		if (obstaclesAround.isEmpty()) {
			return false;
		}
		
		// We return true if an obstacle is in front and close enough
		Vector2f vehicleOrientation = this.getVehicleDirection();
		for (WorldObject obstacle : obstaclesAround) {
			Vector2f vehicleToObstacle = new Vector2f(obstacle.getPosition().x - this.vehicle.getPosition().x,
					obstacle.getPosition().y - this.vehicle.getPosition().y);
			
			if (vehicleOrientation.angle(vehicleToObstacle) < MathUtil.BIG_EPSILON) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Builds the circuit.
	 */
	protected void buildCircuit() {
		// Loop, bottom-right block (step 1)
		this.circuit.add(new Point2f(97, -98));
		this.circuit.add(new Point2f(-3, -98));
		this.circuit.add(new Point2f(-3, 2));
		this.circuit.add(new Point2f(93, 2));
		
		// Semi-Loop, bottom-left block (step 2)
		this.circuit.add(new Point2f(93, 102));
		this.circuit.add(new Point2f(1, 102));
		this.circuit.add(new Point2f(1, -2));
		
		// U-turn (step 3)
		this.circuit.add(new Point2f(-111, -2));
		this.circuit.add(new Point2f(-111, 2));
		
		// Loop, bottom-right block (step 4)
		this.circuit.add(new Point2f(1, 2));
		this.circuit.add(new Point2f(1, -94));
		this.circuit.add(new Point2f(93, -94));
		this.circuit.add(new Point2f(93, -2));
		
		// Semi-Loop, bottom-left block (step 5)
		this.circuit.add(new Point2f(-3, -2));
		this.circuit.add(new Point2f(-3, 106));
		this.circuit.add(new Point2f(97, 106));
		this.circuit.add(new Point2f(97, -2));
		
		// U-turn (step 6)
		this.circuit.add(new Point2f(-111, -2));
		this.circuit.add(new Point2f(-111, 2));
		
		// Final destination (step 7)
		this.circuit.add(new Point2f(97, 2));
	}
	
	/**
	 * Sets the index of the next destination.
	 */
	private void determineNextDestination() {
		this.iNextDest = 0;
		
		for (int j = 0; j < this.circuitSize; j++) {
			Point2f destination = this.circuit.get(j);
			Point2f previousDestination = this.circuit.get((j - 1 + this.circuitSize) % this.circuitSize);
			
			if (MathUtil.areAlmostAligned(previousDestination, this.vehicle.getPosition(), destination)) {
				this.iNextDest = j;
				return;
			}
		}
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return VehicleBehavior.class.getCanonicalName();
	}
	
}
