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

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;

/**
 * The Class Property.
 * 
 * @param <T>
 *            the generic type
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("prop")
public class Property<T> implements Cloneable {
	
	/** The LOGGER. */
	private static Logger logger = Logger.getLogger(Property.class);
	
	/** A separator to be used for virtual paths in properties names. */
	public static final String SEPARATOR = "_";
	
	/** The model. */
	private final PropertyModel<T> model;
	
	/** The value. */
	private T value;
	
	/**
	 * Instantiates a new property and sets its default value.
	 * 
	 * @param model
	 *            the model
	 */
	public Property(PropertyModel<T> model) {
		super();
		this.model = model;
		this.value = model.getDefaultValue();
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
		Property<?> other = (Property<?>) obj;
		if (this.model == null) {
			if (other.model != null) {
				return false;
			}
		} else if (!this.model.equals(other.model)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public final PropertyModel<T> getModel() {
		return this.model;
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public T getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.model == null) ? 0 : this.model.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param newValue
	 *            the new value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setValue(T newValue) {
		if (this.model.isAcceptable(newValue)) {
			this.value = newValue;
		} else if (this.model instanceof NumericPropertyModel<?>) {
			// Case of a variation out of the interval. A border value is then
			// assigned
			NumericPropertyModel numericModel = (NumericPropertyModel) this.model;
			if (((Comparable) numericModel.getMinValue()).compareTo(newValue) > 0) {
				this.value = (T) numericModel.getMinValue();
			} else if (((Comparable) numericModel.getMaxValue()).compareTo(newValue) < 0) {
				this.value = (T) numericModel.getMaxValue();
			}
		} else {
			throw new IllegalValueException(newValue + " is not a legal value for " + this.toString());
		}
	}
	
	@Override
	public String toString() {
		return this.value + " (" + this.model + ")";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Property<T> clone() {
		try {
			super.clone();
			if (this.getModel().getName().startsWith(Gauge.PREFIX)) {
				Property<Double> prop = new Property<Double>((PropertyModel<Double>) this.model);
				prop.setValue((Double) this.value);
				prop = new Gauge(prop);
				return (Property<T>) prop;
			}
			if (this.getModel().getName().startsWith(Emotion.PREFIX)) {
				Property<Double> prop = new Property<Double>((PropertyModel<Double>) this.model);
				prop.setValue((Double) this.value);
				prop = new Emotion(prop);
				return (Property<T>) prop;
			} else {
				Property<T> prop = new Property<T>(this.model);
				prop.setValue(this.value);
				return prop;
			}
		} catch (CloneNotSupportedException e) {
			// Should not happen
			logger.error("An Object instance cannot be cloned!", e);
		}
		return null;
	}
}