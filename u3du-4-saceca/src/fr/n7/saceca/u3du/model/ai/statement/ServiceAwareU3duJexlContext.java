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
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

/**
 * A service-aware implementation of JexlContext for expressions evaluation. The main aim is to be
 * able to correctly handle properties access such as:
 * <ul>
 * <li>"this_myPropertyName" for a provider's property;</li>
 * <li>"other_theirPropertyName" for a consumer's property.</li>
 * </ul>
 * 
 * @author Sylvain Cambon
 */
public class ServiceAwareU3duJexlContext extends U3duJexlContext {
	
	/** The prefix used for the provider's properties. */
	public static final String PROVIDER_PREFIX = "this";
	
	/** The prefix used for the consumer's properties. */
	public static final String CONSUMER_PREFIX = "other";
	
	/** The provider. */
	private WorldObject thisObject;
	
	/** The consumer. */
	private WorldObject otherObject;
	
	/** The execution mode. */
	private ExecutionMode executionMode;
	
	/**
	 * Instantiates a new Jexl Context using two World objects. The first one is the provider and
	 * the second one the consumer.
	 * 
	 * @param thisObject
	 *            The provider.
	 * @param otherObject
	 *            The consumer.
	 * @param parameters
	 *            the parameters
	 * @param mode
	 *            the mode
	 */
	public ServiceAwareU3duJexlContext(WorldObject thisObject, WorldObject otherObject, Map<String, Object> parameters,
			ExecutionMode mode) {
		super(parameters);
		this.thisObject = thisObject;
		this.otherObject = otherObject;
		this.executionMode = mode;
	}
	
	/**
	 * Instantiates a new Jexl Context using properties container. The consumer as well as the
	 * parameters are null. The execution mode is virtual.
	 * 
	 * @param thisObject
	 *            the object on which one wants to apply properties
	 */
	public ServiceAwareU3duJexlContext(WorldObject thisObject) {
		this(thisObject, null, null, ExecutionMode.VIRTUAL);
	}
	
	/**
	 * Gets the consumer.
	 * 
	 * @return the consumer
	 */
	public final WorldObject getConsumer() {
		return this.otherObject;
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
	 *             If the fullName does not start with "this_" or "other_"
	 */
	@Override
	protected Property<?> getProperty(String fullName) throws UnknownPropertyException,
			MalformedExpressionVariableException {
		PropertiesContainer container = null;
		Property<?> property = null;
		int indexOfFirstDot = fullName.indexOf(Property.SEPARATOR);
		if (indexOfFirstDot != -1) {
			String propertyName = fullName.substring(indexOfFirstDot + 1);
			if (fullName.startsWith(PROVIDER_PREFIX)) {
				container = this.thisObject.getPropertiesContainer();
			} else if (fullName.startsWith(CONSUMER_PREFIX)) {
				container = this.otherObject.getPropertiesContainer();
			} else {
				throw new MalformedExpressionVariableException(propertyName, PROVIDER_PREFIX + Property.SEPARATOR,
						CONSUMER_PREFIX + Property.SEPARATOR);
			}
			if (container != null) {
				property = container.getProperty(null, propertyName);
			}
		}
		return property;
	}
	
	/**
	 * Gets the provider.
	 * 
	 * @return the provider
	 */
	public final WorldObject getProvider() {
		return this.thisObject;
	}
	
	/**
	 * Gets the execution mode.
	 * 
	 * @return the execution mode
	 */
	public ExecutionMode getExecutionMode() {
		return this.executionMode;
	}
	
	/**
	 * Sets the execution mode.
	 * 
	 * @param executionMode
	 *            the new execution mode
	 */
	public final void setExecutionMode(ExecutionMode executionMode) {
		this.executionMode = executionMode;
	}
	
	/**
	 * Gets an element. Only "this" and "other" are supported.
	 * 
	 * @param name
	 *            the name
	 * @return the special
	 */
	@Override
	protected WorldObject getSpecial(String name) {
		if (name.equals(ServiceAwareU3duJexlContext.CONSUMER_PREFIX)) {
			return this.otherObject;
		} else if (name.equals(ServiceAwareU3duJexlContext.PROVIDER_PREFIX)) {
			return this.thisObject;
		}
		return null;
	}
	
	/**
	 * Can handle "this" and "other".
	 * 
	 * @param name
	 *            the name
	 * @return true, if the element can be handled.
	 */
	@Override
	protected boolean canHandleSpecial(String name) {
		return this.getSpecial(name) != null;
	}
	
}
