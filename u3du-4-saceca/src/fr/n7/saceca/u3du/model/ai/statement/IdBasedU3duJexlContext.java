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
package fr.n7.saceca.u3du.model.ai.statement;

import java.util.Map;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

/**
 * An abstract class for id-based U3DU Jexl contexts. It accepts the "id<myId>_<myProperty>" syntax.
 * 
 * @author Sylvain Cambon
 */
public abstract class IdBasedU3duJexlContext extends U3duJexlContext {
	
	/**
	 * Instantiates a new id based u3du jexl context.
	 */
	public IdBasedU3duJexlContext() {
		super();
	}
	
	/**
	 * Gets a property.
	 * 
	 * @param name
	 *            The full name of the property.
	 * @return The corresponding property, or null.
	 * @throws UnknownPropertyException
	 *             If the property cannot be found.
	 * @throws MalformedExpressionVariableException
	 *             If the name does not match the expected pattern.
	 */
	@Override
	protected Property<?> getProperty(String name) throws UnknownPropertyException,
			MalformedExpressionVariableException {
		WorldObject object = null;
		String propertyName = null;
		try {
			int indexOfFirstSeparator = name.indexOf(Property.SEPARATOR);
			if (indexOfFirstSeparator < 0) {
				throw new MalformedExpressionVariableException(name, "id<number>_...");
			}
			propertyName = name.substring(indexOfFirstSeparator + 1);
			object = this.getSpecial(name.substring(0, indexOfFirstSeparator));
			return object.getPropertiesContainer().getProperty(propertyName);
		} catch (UnknownPropertyException upe) {
			throw new UnknownPropertyException(object, propertyName);
		}
	}
	
	/**
	 * Gets the world object map where to find items.
	 * 
	 * @return the world object map
	 */
	protected abstract Map<Long, WorldObject> getWorldObjectMap();
	
	@Override
	protected WorldObject getSpecial(String name) throws MalformedExpressionVariableException {
		if (!name.startsWith("id")) {
			throw new MalformedExpressionVariableException(name, "id<number>_");
		}
		String idString = name.substring(2);
		WorldObject object = null;
		try {
			long id = Long.parseLong(idString);
			object = this.getWorldObjectMap().get(id);
		} catch (NumberFormatException nfe) {
			throw new MalformedExpressionVariableException(name, nfe, "id<number>_");
		}
		if (object == null) {
			throw new MalformedExpressionVariableException(name, "id<number>_");
		}
		return object;
	}
	
	@Override
	protected boolean canHandleSpecial(String name) {
		try {
			return this.getSpecial(name) != null;
		} catch (MalformedExpressionVariableException e) {
			return false;
		}
	}
	
}