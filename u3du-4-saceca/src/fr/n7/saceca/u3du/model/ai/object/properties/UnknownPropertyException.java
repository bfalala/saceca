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
package fr.n7.saceca.u3du.model.ai.object.properties;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * The Class UnknownPropertyException. A null field may mean that the the information was not
 * available in the throwing context.
 * 
 * @author Sylvain Cambon
 */
public class UnknownPropertyException extends Exception {
	
	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** The property name. */
	private final String propertyName;
	
	/** The type. */
	private final Class<?> type;
	
	/** The object. */
	private final WorldObject object;
	
	/**
	 * Instantiates a new unknown property exception.
	 * 
	 * @param object
	 *            the object
	 * @param propertyName
	 *            the property name
	 * @param type
	 *            the type
	 */
	public UnknownPropertyException(WorldObject object, String propertyName, Class<?> type) {
		super("There is no property called " + propertyName + " of type " + type.getCanonicalName() + " in "
				+ object.toShortString() + ".");
		this.object = object;
		this.propertyName = propertyName;
		this.type = type;
	}
	
	/**
	 * Instantiates a new unknown property exception. The type will be null.
	 * 
	 * @param object
	 *            the object
	 * @param propertyName
	 *            the property name
	 */
	public UnknownPropertyException(WorldObject object, String propertyName) {
		super("There is no property called " + propertyName + " in " + object.toShortString() + ".");
		this.object = object;
		this.propertyName = propertyName;
		this.type = null;
	}
	
	/**
	 * Instantiates a new unknown property exception. The concerned object will be null.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param type
	 *            the type
	 */
	public UnknownPropertyException(String propertyName, Class<?> type) {
		super("There is no property called " + propertyName + " of type "
				+ (type == null ? "<Unknown type>" : type.getCanonicalName()) + " where expected.");
		this.object = null;
		this.propertyName = propertyName;
		this.type = type;
	}
	
	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public final String getPropertyName() {
		return this.propertyName;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public final Class<?> getType() {
		return this.type;
	}
	
	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	public final WorldObject getObject() {
		return this.object;
	}
	
}
