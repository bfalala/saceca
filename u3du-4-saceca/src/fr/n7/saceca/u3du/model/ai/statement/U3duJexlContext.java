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
package fr.n7.saceca.u3du.model.ai.statement;

import java.util.Map;

import org.apache.commons.jexl2.JexlContext;
import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

/**
 * A common class to build U3du-specific JexlContext.
 * 
 * @author Sylvain Cambon
 */
public abstract class U3duJexlContext implements JexlContext {
	
	/** The logger. */
	protected static Logger logger = Logger.getLogger(U3duJexlContext.class);
	
	/** The Constant PARAMETERS_MAP_NAME. */
	public static final String PARAMETERS_MAP_NAME = "param";
	
	/** The parameters. */
	private final Map<String, Object> parameters;
	
	/**
	 * Instantiates a new u3du Jexl context. The parameters are null.
	 */
	public U3duJexlContext() {
		super();
		this.parameters = null;
	}
	
	/**
	 * Instantiates a new u3du jexl context.
	 * 
	 * @param params
	 *            the params
	 */
	public U3duJexlContext(Map<String, Object> params) {
		super();
		this.parameters = params;
	}
	
	/**
	 * Gets something, may it be a property or a whole object. Null is returned in case the name
	 * cannot be resolved.
	 * 
	 * @param name
	 *            the name
	 * @return the object
	 */
	@Override
	public Object get(String name) {
		if (this.parameters != null && name.equals(PARAMETERS_MAP_NAME)) {
			return this.parameters;
		}
		if (this.canHandleSpecial(name)) {
			Object special = null;
			try {
				special = this.getSpecial(name);
			} catch (MalformedExpressionVariableException e) {
				// This should be prevented by canHandleSpecial(...)
				logger.error(e);
				e.printStackTrace();
			}
			return special;
		}
		Property<?> property = null;
		try {
			property = this.getProperty(name);
		} catch (UnknownPropertyException upe) {
			logger.error(upe);
			upe.printStackTrace();
		} catch (MalformedExpressionVariableException e) {
			logger.error(e);
			e.printStackTrace();
		}
		if (property != null) {
			return property.getValue();
		}
		return null;
	}
	
	/**
	 * Gets the special element named "name". For instance, it can be "this" or "other" in a
	 * ServiceAwareJexlContext.
	 * 
	 * @param name
	 *            the name
	 * @return the special element.
	 * @throws MalformedExpressionVariableException
	 *             If the name was not compatible with the possible special names.
	 */
	protected abstract Object getSpecial(String name) throws MalformedExpressionVariableException;
	
	/**
	 * Tells whether there is an element named "name".
	 * 
	 * @param name
	 *            the name
	 * @return If this element can be handled.
	 */
	protected abstract boolean canHandleSpecial(String name);
	
	@Override
	public boolean has(String name) {
		try {
			return this.getProperty(name) != null || (PARAMETERS_MAP_NAME.equals(name) && this.parameters != null)
					|| this.canHandleSpecial(name);
		} catch (UnknownPropertyException upe) {
			logger.warn(upe);
			upe.printStackTrace();
			return false;
		} catch (MalformedExpressionVariableException e) {
			logger.warn(e);
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sets the properties but not the parameters.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void set(String name, Object value) {
		@SuppressWarnings("rawtypes")
		Property property;
		try {
			property = this.getProperty(name);
			if (property != null) {
				if (EnumElement.class.equals(property.getModel().getType())) {
					property.setValue(new EnumElement((String) value));
				} else {
					property.setValue(value);
				}
			}
		} catch (UnknownPropertyException upe) {
			logger.error(upe);
			upe.printStackTrace();
		} catch (MalformedExpressionVariableException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a property.
	 * 
	 * @param fullName
	 *            The full name of the property.
	 * @return The corresponding property, or null.
	 * @throws UnknownPropertyException
	 *             If the property cannot be found.
	 * @throws MalformedExpressionVariableException
	 *             If the fullNmame does not match the expected pattern.
	 */
	protected abstract Property<?> getProperty(String fullName) throws UnknownPropertyException,
			MalformedExpressionVariableException;
}