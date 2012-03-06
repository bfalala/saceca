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
package fr.n7.saceca.u3du.exception;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * An exception for malformed objects.
 * 
 * @author Sylvain Cambon
 */
public class MalformedObjectException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The object. */
	private final WorldObject object;
	
	/** The property name. */
	private final String propertyName;
	
	/**
	 * Instantiates a new malformed object exception.
	 * 
	 * @param object
	 *            the object
	 * @param propertyName
	 *            the property name
	 * @param cause
	 *            the cause
	 */
	public MalformedObjectException(WorldObject object, String propertyName, Throwable cause) {
		super("The object (model="
				+ (object == null ? "<Null object>" : (object.getModelName() == null ? "<Null model name>" : object
						.getModelName())) + ", id=" + (object == null ? "<Null object>" : object.getId())
				+ ") has a missing property: " + propertyName + ".", cause);
		this.object = object;
		this.propertyName = propertyName;
	}
	
	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	public final WorldObject getObject() {
		return this.object;
	}
	
	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public final String getPropertyName() {
		return this.propertyName;
	}
	
}