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
 * A class representing a numeric value bounded by a minimum and a maximum.
 * 
 * @author Sylvain Cambon
 * 
 * @param <T>
 *            A comparable number type
 */
public abstract class AbstractNumericPropertyModel<T extends Number & Comparable<T>> extends AbstractPropertyModel<T>
		implements NumericPropertyModel<T>
{
	
	/** The min value. */
	@XStreamAlias("min")
	@XStreamAsAttribute
	private final T minValue;
	
	/** The max value. */
	@XStreamAlias("max")
	@XStreamAsAttribute
	private final T maxValue;
	
	/**
	 * Instantiates a new numeric bounded model. If minimum>maximum, an IllegalValueException will
	 * be thrown.
	 * 
	 * @param name
	 *            The name.
	 * @param minValue
	 *            The minimum value.
	 * @param maxValue
	 *            The maximum value.
	 * @param defaultValue
	 *            The default value.
	 * @param visibility
	 *            the visibility.
	 */
	public AbstractNumericPropertyModel(String name, T minValue, T maxValue, T defaultValue, Visibility visibility) {
		super(name, defaultValue, visibility);
		this.minValue = minValue;
		this.maxValue = maxValue;
		if (minValue != null && maxValue != null && minValue.compareTo(maxValue) > 0) {
			throw new IllegalValueException("The following predicate is not verified: " + minValue + "<=" + maxValue);
		}
		this.setDefaultValue(defaultValue);
	}
	
	/**
	 * Gets the max value.
	 * 
	 * @return the max value
	 */
	@Override
	public T getMaxValue() {
		return this.maxValue;
	}
	
	/**
	 * Gets the min value.
	 * 
	 * @return the min value
	 */
	@Override
	public T getMinValue() {
		return this.minValue;
	}
	
	/**
	 * Checks whether the value is acceptable, i.e. is in the interval [min, max]
	 * 
	 * @param defaultValue
	 *            The new default value.
	 * @return true, if the value is acceptable.
	 */
	@Override
	public boolean isAcceptable(T defaultValue) {
		return (this.minValue == null || this.minValue.compareTo(defaultValue) <= 0)
				&& (this.maxValue == null || this.maxValue.compareTo(defaultValue) >= 0);
	}
	
	@Override
	public String toString() {
		return "NumericPropertyModel<" + this.getType().getSimpleName() + ">[" + this.getName() + ": defaultValue="
				+ this.getDefaultValue() + ", minValue=" + this.minValue + ", maxValue=" + this.maxValue + "]";
	}
	
}
