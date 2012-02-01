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

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.graphics.animation.ChangeTrafficLightAnimation;

/**
 * The Class TrafficLightBehavior.
 * 
 * @author Jérôme Dalbert, Sylvain Cambon
 */
public class TrafficLightBehavior implements Behavior {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(TrafficLightBehavior.class);
	
	/**
	 * The connection mode
	 * 
	 * @author Sylvain Cambon
	 */
	private enum Connection {
		
		/** The traffic light is not yet connected to its group. */
		NOT_CONNECTED,
		/** The traffic light is connected to its group. */
		CONNECTED
	}
	
	/** The state of the light. */
	private LightState state;
	
	/** The connection mode. */
	private Connection connection;
	
	/** The group of objects to send tics. */
	private Collection<WorldObject> group;
	
	/** The current object. */
	private WorldObject object;
	
	/** The timer. */
	private int timer;
	
	/**
	 * Instantiates a new car behavior with predefined places.
	 */
	public TrafficLightBehavior() {
		this.connection = Connection.NOT_CONNECTED;
		this.group = null;
		this.object = null;
		this.group = new ArrayList<WorldObject>();
	}
	
	/**
	 * Behave.
	 * 
	 */
	@Override
	public synchronized void behave() {
		if (this.connection != Connection.CONNECTED) {
			// Case where the group is not yet connected
			// Timer initialization
			try {
				this.timer = this.object.getPropertiesContainer().getInt("m_TrafficLight_deltaPhase");
				// Initial color
				this.state = LightState.valueOf(this.object.getPropertiesContainer()
						.getEnumerationElement("m_TrafficLight_color").toString());
				// The graphical color has to be set
				this.updateGraphicalColor();
				
				// Master traffic light election
				WorldObject master = this.electMaster();
				// Now the connection can be created
				this.connection = Connection.CONNECTED;
				if (!this.object.equals(master)) {
					// Registering with the master
					((TrafficLightBehavior) master.getBehavior()).register(this.object);
					// Setting the object as non-threaded
					this.object.getPropertiesContainer().setBoolean(Internal.Object.THREADED, false);
					
					// Extinction of the thread
					this.object.setAlive(false);
				} else {
					// The master is part of its group
					this.group.add(this.object);
				}
			} catch (UnknownPropertyException upe) {
				this.timer = 0;
				this.connection = Connection.NOT_CONNECTED;
				this.state = LightState.GREEN;
				logger.warn("The Traffic Light Behavior of " + this.object.toShortString()
						+ " cannot find a required property.", upe);
			}
		}
		
		if (this.connection == Connection.CONNECTED) {
			// Only the master will warn the group, one member by one
			for (WorldObject groupMember : this.group) {
				try {
					((TrafficLightBehavior) groupMember.getBehavior()).tic();
				} catch (MalformedObjectException moe) {
					logger.warn("The Traffic Light Behavior of " + this.object.toShortString()
							+ " cannot find a required property.", moe);
				}
			}
		}
	}
	
	/**
	 * Registers the given object in the tic receiver list.
	 * 
	 * @param object
	 *            the object to send tics to.
	 */
	private synchronized void register(WorldObject object) {
		this.group.add(object);
	}
	
	/**
	 * Elects a master. The first traffic light of the group is chosen.
	 * 
	 * @return the world object
	 * @throws UnknownPropertyException
	 *             If the property m_TrafficLight_groupNumber is not present.
	 */
	private WorldObject electMaster() throws UnknownPropertyException {
		int groupNumber = this.object.getPropertiesContainer().getInt("m_TrafficLight_groupNumber");
		for (WorldObject other : Model.getInstance().getAI().getWorld().getWorldObjects().values()) {
			if (other.getModelName().equals("TrafficLight")
					&& other.getPropertiesContainer().getInt("m_TrafficLight_groupNumber") == groupNumber) {
				// The first one has been found; it is the master
				return other;
			}
		}
		// This cannot happen as this.object should be a solution.
		return null;
	}
	
	/**
	 * Makes the traffic light advance in its cycle.
	 * 
	 * @throws MalformedObjectException
	 *             If the property m_TrafficLight_color is not present.
	 */
	private void tic() throws MalformedObjectException {
		this.timer = (this.timer + 1) % LightState.NUMBER_OF_ELEMENTS_IN_A_PERIOD;
		if (this.timer >= this.state.getPhaseEnd()) {
			this.state = this.state.getNext();
			this.updateGraphicalColor();
			try {
				this.object.getPropertiesContainer()
						.setEnumElement("m_TrafficLight_color", this.state.getEnumElement());
			} catch (UnknownPropertyException upe) {
				throw new MalformedObjectException(this.object, "m_TrafficLight_color", upe);
			}
		}
	}
	
	/**
	 * Updates the color of the graphical object.
	 */
	private void updateGraphicalColor() {
		Model.getInstance().getGraphics()
				.sendAnimation(new ChangeTrafficLightAnimation(this.object.getId(), this.state));
	}
	
	@Override
	public String getStorageLabel() {
		return TrafficLightBehavior.class.getCanonicalName();
	}
	
	@Override
	public void init(WorldObject object) {
		this.object = object;
	}
}
