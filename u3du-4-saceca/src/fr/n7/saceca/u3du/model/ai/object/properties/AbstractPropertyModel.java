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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import fr.n7.saceca.u3du.model.ai.Visibility;

/**
 * An abstract common PropertyModel implementation, dealing with the following common properties of
 * the modeled parameter:
 * <ul>
 * <li>Type (runtime accessible yet the interface is generic);</li>
 * <li>Name;</li>
 * <li>Default value;</li>
 * <li>Visibility.</li>
 * </ul>
 * 
 * @author Sylvain Cambon
 * 
 * @param <T>
 *            The type of the modeled property, for instance Integer, Boolean, etc.
 */
public abstract class AbstractPropertyModel<T> implements PropertyModel<T> {
	
	/** The name of the property. */
	@XStreamAsAttribute
	private final String name;
	
	/** The default value of the property. */
	@XStreamAlias("default")
	@XStreamAsAttribute
	private T defaultValue;
	
	/** The visibility of the property. */
	private Visibility visibility;
	
	/**
	 * For children property model constructors.
	 * 
	 * @param name
	 *            the name.
	 * @param defaultValue
	 *            the default value.
	 * @param visibility
	 *            the visibility.
	 */
	public AbstractPropertyModel(String name, T defaultValue, Visibility visibility) {
		super();
		this.name = name;
		this.setDefaultValue(defaultValue);
		this.visibility = visibility;
	}
	
	@Override
	public final Visibility getVisibility() {
		return this.visibility;
	}
	
	/**
	 * Sets the visibility of the property.
	 * 
	 * @param visibility
	 *            the new visibility of the property
	 */
	public final void setVisibility(Visibility visibility) {
		this.visibility = visibility;
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
		PropertyModel<?> other = (AbstractPropertyModel<?>) obj;
		if (!this.getType().equals(other.getType())) {
			return false;
		}
		if (this.name == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!this.name.equals(other.getName())) {
			return false;
		}
		return true;
	};
	
	@Override
	public T getDefaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getType() {
		return (Class<T>) this.defaultValue.getClass();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
	
	/**
	 * Tells whether the value is acceptable. This default implementation always return true for
	 * convenience but overriding methods can have a different contract.
	 * 
	 * @param value
	 *            The value to check.
	 * @return true.
	 */
	@Override
	public boolean isAcceptable(T value) {
		return true;
	}
	
	/**
	 * Sets the default value if the given value is acceptable.
	 * 
	 * @param defaultValue
	 *            The new default value.
	 */
	protected void setDefaultValue(T defaultValue) {
		if (this.isAcceptable(defaultValue)) {
			this.defaultValue = defaultValue;
		} else {
			throw new IllegalValueException(defaultValue + " is not an acceptable default value for " + this.toString());
		}
	}
	
}
