/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.agent.memory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.util.Periodic;

/**
 * A class describing an object in an agent's memory.
 * 
 * @author JÃ©rÃ´me Dalbert, Ciprian Munteanu
 */
@XStreamAlias("memory-element")
public class MemoryElement {
	
	/** The object. */
	private WorldObject worldObject;
	
	/** The forgettable. */
	@XStreamAlias("forgettable")
	@XStreamAsAttribute
	private boolean forgettable;
	
	/** The place in the memory: short or long - term memory */
	@XStreamAlias("place")
	@XStreamAsAttribute
	private String place;
	
	/** The number of references */
	@XStreamAlias("nbRef")
	@XStreamAsAttribute
	private Integer nbReferences;
	
	/** The number of references decrement time */
	@XStreamOmitField
	private int nbReferencesDecrementTime;
	
	/**
	 * Instantiates a new memory world object.
	 * 
	 * @param worldObject
	 *            the object
	 * @param forgettable
	 *            the forgettable
	 * @param place
	 *            the place in the memory (short or long - term)
	 * @param nbReferences
	 *            number of references
	 */
	public MemoryElement(WorldObject worldObject, boolean forgettable, String place, int nbReferences) {
		super();
		this.worldObject = worldObject;
		this.forgettable = forgettable;
		this.setPlace(place);
		this.setNbReferences(nbReferences);
	}
	
	/**
	 * Instantiates a new memory world object.
	 * 
	 * @param worldObject
	 *            the object
	 * @param forgettable
	 *            the forgettable
	 */
	public MemoryElement(WorldObject worldObject, boolean forgettable) {
		this(worldObject, forgettable, "short", Memory.INITIAL_ENTRY_NB_REFERENCES);
	}
	
	/**
	 * Checks if is forgettable.
	 * 
	 * @return true, if is forgettable
	 */
	public boolean isForgettable() {
		return this.forgettable;
	}
	
	/**
	 * Sets the forgettable.
	 * 
	 * @param forgettable
	 *            the new forgettable
	 */
	public void setForgettable(boolean forgettable) {
		this.forgettable = forgettable;
	}
	
	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	public final WorldObject getWorldObject() {
		return this.worldObject;
	}
	
	/**
	 * Sets the object.
	 * 
	 * @param object
	 *            the new object
	 */
	public final void setWorldObject(WorldObject object) {
		this.worldObject = object;
	}
	
	/**
	 * Sets the place in the memory
	 * 
	 * @param place
	 *            the place
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	
	/**
	 * Gets the place in the memory
	 * 
	 * @return
	 */
	public String getPlace() {
		return this.place;
	}
	
	/**
	 * Sets the number of references
	 * 
	 * @param nbReferences
	 *            the new number of references
	 */
	public void setNbReferences(Integer nbReferences) {
		this.nbReferences = nbReferences;
	}
	
	/**
	 * Gets the number of references
	 * 
	 * @return
	 */
	public Integer getNbReferences() {
		return this.nbReferences;
	}
	
	/**
	 * Increases the number of references
	 * 
	 * @param value
	 *            the increment value
	 */
	public void increaseNbReferences(Integer value) {
		this.nbReferences += value;
	}
	
	/**
	 * Decreases the number of references
	 * 
	 * @param value
	 *            the decrement value
	 */
	public void decreaseNbReferences(Integer value) {
		this.nbReferences -= value;
	}
	
	@Override
	public String toString() {
		if (this.worldObject == null) {
			return "Reflexive memory";
		}
		return "Memory about " + this.worldObject.toShortString();
	}
	
	/**
	 * Creates a deep data clone.
	 * 
	 * @return the memory element
	 */
	public MemoryElement deepDataClone() {
		return new MemoryElement(this.worldObject.deepDataClone(), this.forgettable, this.place, this.nbReferences);
	}
	
	private Integer getDecrementPeriod(Agent agent) {
		Integer nbReferencesDecrementPeriod = null;
		
		// Case when there is a decrement period for the current gauge
		try {
			nbReferencesDecrementPeriod = agent.getPropertiesContainer().getInt("i_decrement_period_references");
		}
		// Case when there the decrement period is not found. We use the default decrement period.
		catch (UnknownPropertyException e) {
			try {
				nbReferencesDecrementPeriod = agent.getPropertiesContainer().getInt("i_decrement_period_default");
			} catch (UnknownPropertyException e1) {
				e.printStackTrace();
			}
		}
		
		return nbReferencesDecrementPeriod;
	}
	
	/**  */
	
	/**
	 * Decrements periodically the number of references at every nbReferencesDecrementTime
	 * 
	 * @param agent
	 *            the agent
	 */
	public void periodicDecrement(Agent agent) {
		int decrementPeriod = this.getDecrementPeriod(agent);
		if (this.nbReferencesDecrementTime == decrementPeriod - 1) {
			this.decreaseNbReferences(1);
		}
		
		this.nbReferencesDecrementTime = Periodic.incrementPeriodTime(this.nbReferencesDecrementTime, decrementPeriod);
	}
	
}