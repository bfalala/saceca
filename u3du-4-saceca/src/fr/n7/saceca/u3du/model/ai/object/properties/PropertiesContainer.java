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
package fr.n7.saceca.u3du.model.ai.object.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A class to store properties.
 * 
 * @author Sylvain Cambon
 */
public class PropertiesContainer implements Cloneable {
	
	/** The Constant PROPERTY_TYPE_WILDCARD. */
	public static final Class<?> PROPERTY_TYPE_WILDCARD = null;
	
	/** The properties. */
	private Map<String, Property<?>> properties = new HashMap<String, Property<?>>();
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(PropertiesContainer.class);
	
	/**
	 * Instantiates a new properties property container.
	 */
	public PropertiesContainer() {
		super();
	}
	
	/**
	 * Adds a property.
	 * 
	 * @param property
	 *            the property
	 */
	public void addProperty(Property<?> property) {
		this.properties.put(property.getModel().getName(), property);
	}
	
	/**
	 * Gets a boolean property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the boolean
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public boolean getBoolean(String propertyName) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<Boolean> prop = (Property<Boolean>) this.getProperty(Boolean.class, propertyName);
		return prop.getValue();
	}
	
	/**
	 * Gets a double property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the double
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public double getDouble(String propertyName) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<Double> prop = (Property<Double>) this.getProperty(Double.class, propertyName);
		return prop.getValue();
	}
	
	/**
	 * Gets an enumeration element property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the Enumeration Element
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public EnumElement getEnumerationElement(String propertyName) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<EnumElement> prop = (Property<EnumElement>) this.getProperty(EnumElement.class, propertyName);
		return prop.getValue();
	}
	
	/**
	 * Gets an int property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the int
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public int getInt(String propertyName) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<Integer> prop = (Property<Integer>) this.getProperty(Integer.class, propertyName);
		return prop.getValue();
	}
	
	/**
	 * Gets the properties names.
	 * 
	 * @return the properties names
	 */
	public Set<String> getPropertiesNames() {
		return this.properties.keySet();
	}
	
	/**
	 * Gets a property.
	 * 
	 * @param clazz
	 *            The class of the value of the property. The constant PROPERTY_TYPE_WILDCARD of
	 *            this class can be used.
	 * @param propertyName
	 *            the property name
	 * @return the property
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public synchronized Property<?> getProperty(Class<?> clazz, String propertyName) throws UnknownPropertyException {
		Property<?> property = this.properties.get(propertyName);
		if (property == null || (clazz != PROPERTY_TYPE_WILDCARD && !clazz.equals(property.getModel().getType()))) {
			UnknownPropertyException upe = new UnknownPropertyException(propertyName, clazz);
			throw upe;
		}
		return property;
	}
	
	/**
	 * Gets the property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public Property<?> getProperty(String propertyName) throws UnknownPropertyException {
		return this.getProperty(null, propertyName);
	}
	
	/**
	 * Gets all the properties.
	 * 
	 * @return all the properties
	 */
	public Collection<Property<?>> getProperties() {
		return this.properties.values();
	}
	
	/**
	 * Gets the properties that were accepted by the filter.
	 * 
	 * @param filter
	 *            the filter
	 * @return the accepted properties
	 */
	public Collection<Property<?>> getProperties(PropertyFilter filter) {
		Collection<Property<?>> filteredProperties = new ArrayList<Property<?>>();
		for (Property<?> property : this.properties.values()) {
			if (filter.accept(property)) {
				filteredProperties.add(property);
			}
		}
		return filteredProperties;
	}
	
	/**
	 * Gets a string property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the string
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 * 
	 */
	public String getString(String propertyName) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<String> prop = (Property<String>) this.getProperty(String.class, propertyName);
		return prop.getValue();
	}
	
	/**
	 * Removes a property from the property container.
	 * 
	 * @param propertyName
	 *            The name of the property to be removed.
	 */
	public void removeProperty(String propertyName) {
		this.properties.remove(propertyName);
	}
	
	/**
	 * Sets a boolean property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 * 
	 */
	public void setBoolean(String propertyName, Boolean newValue) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<Boolean> prop = (Property<Boolean>) this.getProperty(Boolean.class, propertyName);
		prop.setValue(newValue);
	}
	
	/**
	 * Sets a double property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param newValue
	 *            the new value
	 * 
	 *            If the property does not exist.
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public void setDouble(String propertyName, Double newValue) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<Double> prop = (Property<Double>) this.getProperty(Double.class, propertyName);
		prop.setValue(newValue);
	}
	
	/**
	 * Sets an enumeration element property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 * 
	 */
	public void setEnumElement(String propertyName, EnumElement newValue) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<EnumElement> prop = (Property<EnumElement>) this.getProperty(EnumElement.class, propertyName);
		prop.setValue(newValue);
	}
	
	/**
	 * Sets an int property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 * 
	 */
	public void setInt(String propertyName, int newValue) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<Integer> prop = (Property<Integer>) this.getProperty(Integer.class, propertyName);
		prop.setValue(newValue);
	}
	
	/**
	 * Sets a string property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @throws UnknownPropertyException
	 *             If the property is not part of this container.
	 */
	public void setString(String propertyName, String newValue) throws UnknownPropertyException {
		@SuppressWarnings("unchecked")
		Property<String> prop = (Property<String>) this.getProperty(String.class, propertyName);
		prop.setValue(newValue);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("PropertiesContainer:");
		for (String key : this.properties.keySet()) {
			sb.append("\n\t" + key + " => " + this.properties.get(key).toString());
		}
		sb.append("\n");
		return sb.toString();
	}
	
	@Override
	public PropertiesContainer clone() {
		try {
			super.clone();
			PropertiesContainer container = new PropertiesContainer();
			for (Property<?> property : this.properties.values()) {
				container.addProperty(property.clone());
			}
			return container;
		} catch (CloneNotSupportedException e) {
			// Cannot happen as Object is cloneable.
			logger.error("Cannot clone an instance of object", e);
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.properties == null) ? 0 : this.properties.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		PropertiesContainer other = (PropertiesContainer) obj;
		if (this.properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!this.properties.equals(other.properties)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Helps to rebuild the object.
	 * 
	 * @return the corrected properties container.
	 */
	public PropertiesContainer readResolve() {
		if (this.properties == null) {
			this.properties = new HashMap<String, Property<?>>();
		}
		return this;
	}
}