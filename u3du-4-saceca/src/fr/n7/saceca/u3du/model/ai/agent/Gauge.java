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
package fr.n7.saceca.u3du.model.ai.agent;

import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.util.Periodic;

/**
 * The Class Gauge. It is used for "i_gauge_X" property handling.
 * 
 * @author Jérôme Dalbert
 */
public class Gauge extends Property<Double> {
	
	/** The Constant PREFIX. */
	public static final String PREFIX = "i_gauge_";
	
	/** The Constant DECREMENT_PERIOD_PREFIX. */
	public static final String DECREMENT_PERIOD_PREFIX = "i_decrement_period_";
	
	/** The Constant SURVIVAL_SUFFIXES. */
	public static final String[] SURVIVAL_SUFFIXES = { "hunger", "tiredness", "thirst" };
	
	/** The gauge decrement time. */
	private int gaugeDecrementTime;
	
	/**
	 * Instantiates a new gauge.
	 * 
	 * @param prop
	 *            the prop
	 */
	public Gauge(Property<Double> prop) {
		super(prop.getModel());
		this.setValue(prop.getValue());
		this.gaugeDecrementTime = 0;
	}
	
	/**
	 * Checks if is survival.
	 * 
	 * @return true, if is survival
	 */
	public boolean isSurvival() {
		String gaugeNameSuffix = this.getNameSuffix();
		
		for (String survivalGaugeNameSuffix : SURVIVAL_SUFFIXES) {
			if (gaugeNameSuffix.equals(survivalGaugeNameSuffix)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the min value.
	 * 
	 * @return the min value
	 */
	public double getMinValue() {
		DoublePropertyModel model = (DoublePropertyModel) this.getModel();
		return model.getMinValue();
	}
	
	/**
	 * Gets the max value.
	 * 
	 * @return the max value
	 */
	public double getMaxValue() {
		DoublePropertyModel model = (DoublePropertyModel) this.getModel();
		return model.getMaxValue();
	}
	
	/**
	 * Decrement.
	 * 
	 * @param decrementValue
	 *            the decrement value
	 */
	public void decrement(double decrementValue) {
		this.setValue(this.getValue() - decrementValue);
		if (this.getValue() < this.getMinValue()) {
			this.setValue(this.getMinValue());
		}
	}
	
	/**
	 * Decrement.
	 */
	public void decrement() {
		this.decrement(1);
	}
	
	/**
	 * Increment.
	 * 
	 * @param incrementValue
	 *            the increment value
	 */
	public void increment(double incrementValue) {
		this.setValue(this.getValue() + incrementValue);
		if (this.getValue() > this.getMaxValue()) {
			this.setValue(this.getMaxValue());
		}
	}
	
	/**
	 * Increment.
	 */
	public void increment() {
		this.increment(1);
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.getModel().getName();
	}
	
	@Override
	public String toString() {
		return super.toString() + " (Gauge)";
	}
	
	/**
	 * Gets the gauge name suffix.
	 * 
	 * @return the name suffix
	 */
	public String getNameSuffix() {
		return this.getModel().getName().replace(PREFIX, "");
	}
	
	/**
	 * Gets the gauge decrement period.
	 * 
	 * @param agent
	 *            the agent who owns this gauge
	 * @return the decrement period
	 */
	private Integer getDecrementPeriod(Agent agent) {
		Integer gaugeDecrementPeriod = null;
		
		// Case when there is a decrement period for the current gauge
		try {
			gaugeDecrementPeriod = agent.getPropertiesContainer()
					.getInt(DECREMENT_PERIOD_PREFIX + this.getNameSuffix());
		}
		// Case when there the decrement period is not found. We use the default decrement period.
		catch (UnknownPropertyException e) {
			try {
				gaugeDecrementPeriod = agent.getPropertiesContainer().getInt(DECREMENT_PERIOD_PREFIX + "default");
			} catch (UnknownPropertyException e1) {
				e.printStackTrace();
			}
		}
		
		return gaugeDecrementPeriod;
	}
	
	/**
	 * Decrements the gauge periodically.
	 * 
	 * @param agent
	 *            the agent
	 */
	public void periodicDecrement(Agent agent) {
		int decrementPeriod = this.getDecrementPeriod(agent);
		
		if (this.gaugeDecrementTime == decrementPeriod - 1) {
			this.decrement();
		}
		
		this.gaugeDecrementTime = Periodic.incrementPeriodTime(this.gaugeDecrementTime, decrementPeriod);
	}
}
