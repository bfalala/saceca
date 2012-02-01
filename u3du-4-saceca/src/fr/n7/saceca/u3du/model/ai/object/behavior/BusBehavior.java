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

import java.util.Collection;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.World;
import fr.n7.saceca.u3du.model.ai.WorldObjectFilter;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.util.MathUtil;
import fr.n7.saceca.u3du.model.util.Periodic.Clock;

/**
 * The Class BusBehavior.
 * 
 * @author Jérôme Dalbert
 */
public class BusBehavior extends VehicleBehavior {
	
	/** The Constant ROADSIGN_WAIT_TIME, in ms */
	private static final long ROADSIGN_WAIT_TIME = 5000;
	
	/** The wait clock. */
	private Clock waitClock;
	
	/** The finished waiting. */
	private boolean finishedWaiting;
	
	/** The Constant BUS_ROADSIGN_RANGE. */
	private static final int BUS_ROADSIGN_RANGE = 8;
	
	/** The latest sign where the bus stopped (includes the current one). */
	private WorldObject latestBusSign;
	
	@Override
	public void init(WorldObject object) {
		super.init(object);
		this.latestBusSign = null;
		this.waitClock = new Clock(ROADSIGN_WAIT_TIME);
		this.finishedWaiting = false;
	}
	
	@Override
	protected boolean mustStop() {
		return this.stopAtRoadSign() || super.mustStop();
	}
	
	/**
	 * Checks if the bus can stop at a roadsign.
	 * 
	 * @return true, if is bus road sign
	 */
	private boolean stopAtRoadSign() {
		// We get the bus signs around the car
		World world = Model.getInstance().getAI().getWorld();
		Collection<WorldObject> busSigns = world.getWorldObjects(new WorldObjectFilter() {
			@Override
			public boolean accept(WorldObject worldObject) {
				return worldObject.getModelName().toLowerCase().equals("busroadsign");
			}
		});
		Collection<WorldObject> busSignsAround = world
				.getWorldObjectsAround(this.vehicle, busSigns, BUS_ROADSIGN_RANGE);
		if (busSignsAround.isEmpty()) {
			return false;
		}
		
		// We get the closest one
		WorldObject busSign = world.getClosestWorldObject(this.vehicle, busSignsAround);
		
		// We check that it is on the car's right side
		Vector2f carOrientation = this.getVehicleDirection();
		Vector2f carToLight = new Vector2f(busSign.getPosition().x - this.vehicle.getPosition().x,
				busSign.getPosition().y - this.vehicle.getPosition().y);
		
		double angle1 = MathUtil.signedAngle(carToLight, carOrientation);
		boolean right = angle1 < 15 && angle1 > -Math.PI;
		if (!right) {
			return false;
		}
		
		// If the bus is facing a new sign, it stops
		if (busSign != this.latestBusSign) {
			this.latestBusSign = busSign;
			this.finishedWaiting = false;
			this.waitClock.reset();
			return true;
		}
		// If it is the previous sign, the bus waits (i.e. it keeps on stopping) only if he hasn't
		// already finished waiting
		else {
			if (this.finishedWaiting) {
				return false;
			}
			
			this.finishedWaiting = this.waitClock.tic();
			return !this.finishedWaiting;
		}
	}
	
	@Override
	protected void buildCircuit() {
		// Semi-Loop, bottom-right block (step 1)
		this.circuit.add(new Point2f(97, -98));
		this.circuit.add(new Point2f(-3, -98));
		this.circuit.add(new Point2f(-3, -2));
		
		// U-turn (step 2)
		this.circuit.add(new Point2f(-111, -2));
		this.circuit.add(new Point2f(-111, 2));
		
		// Semi-Loop, bottom-left block (step 3)
		this.circuit.add(new Point2f(-3, 2));
		this.circuit.add(new Point2f(-3, 106));
		this.circuit.add(new Point2f(97, 106));
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return BusBehavior.class.getCanonicalName();
	}
	
}
