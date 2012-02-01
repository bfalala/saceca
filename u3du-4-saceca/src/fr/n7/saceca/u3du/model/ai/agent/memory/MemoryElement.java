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
package fr.n7.saceca.u3du.model.ai.agent.memory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * A class describing an object in an agent's memory.
 * 
 * @author Jérôme Dalbert
 */
@XStreamAlias("memory-element")
public class MemoryElement {
	
	/** The object. */
	private WorldObject worldObject;
	
	/** The forgettable. */
	@XStreamAlias("forgettable")
	@XStreamAsAttribute
	private boolean forgettable;
	
	/**
	 * Instantiates a new memory world object.
	 * 
	 * @param worldObject
	 *            the object
	 * @param forgettable
	 *            the forgettable
	 */
	public MemoryElement(WorldObject worldObject, boolean forgettable) {
		super();
		this.worldObject = worldObject;
		this.forgettable = forgettable;
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
		return new MemoryElement(this.worldObject.deepDataClone(), this.forgettable);
	}
	
}
