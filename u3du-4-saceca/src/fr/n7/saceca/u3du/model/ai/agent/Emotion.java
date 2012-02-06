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
 * The emotion class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class Emotion extends Property<Double> {
	
	/** The Constant PREFIX. */
	public static final String PREFIX = "i_emotion_";
	
	/** The Constant DECREMENT_PERIOD_PREFIX. */
	public static final String DECREMENT_PERIOD_PREFIX = "i_decrement_period_";
	
	/** The constant ALERT_LEVEL */
	public static final Double ALERT_LEVEL = 80.0;
	
	/** The emotion decrement time. */
	private int emotionDecrementTime;
	
	/**
	 * Instantiates a new gauge.
	 * 
	 * @param prop
	 *            the prop
	 */
	public Emotion(Property<Double> prop) {
		super(prop.getModel());
		this.setValue(prop.getValue());
		this.emotionDecrementTime = 0;
	}
	
	/**
	 * Checks if it is a primary emotion
	 * 
	 * @return true, if primary
	 */
	public boolean isPrimary() {
		String[] str = this.getModel().getName().split("_");
		return (str.length == 3);
	}
	
	/**
	 * Checks if it is a secondary emotion
	 * 
	 * @return true, if secondary
	 */
	public boolean isSecondary() {
		return this.getModel().getName().startsWith("i_emotion_secondary");
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
		this.decrement(5);
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
		return super.toString() + " (Emotion)";
	}
	
	/**
	 * Gets the emotion name suffix.
	 * 
	 * @return the name suffix
	 */
	public String getNameSuffix() {
		return this.getModel().getName().replace(PREFIX, "");
	}
	
	public String getSecondaryEmotionName() {
		if (this.isSecondary()) {
			return this.getModel().getName().replace("i_emotion_secondary_", "");
		}
		return "";
	}
	
	/**
	 * Gets the emotion decrement period.
	 * 
	 * @param agent
	 *            the agent who owns this emotion
	 * @return the decrement period
	 */
	private Integer getDecrementPeriod(Agent agent) {
		Integer emotionDecrementPeriod = null;
		
		// Case when there is a decrement period for the current emotion
		try {
			emotionDecrementPeriod = agent.getPropertiesContainer().getInt(
					DECREMENT_PERIOD_PREFIX + this.getNameSuffix());
		}
		// Case when there the decrement period is not found. We use the default decrement period.
		catch (UnknownPropertyException e) {
			try {
				emotionDecrementPeriod = agent.getPropertiesContainer().getInt(DECREMENT_PERIOD_PREFIX + "default");
			} catch (UnknownPropertyException e1) {
				e.printStackTrace();
			}
		}
		
		return emotionDecrementPeriod;
	}
	
	/**
	 * Decrements the emotion periodically.
	 * 
	 * @param agent
	 *            the agent
	 */
	public void periodicDecrement(Agent agent) {
		int decrementPeriod = this.getDecrementPeriod(agent);
		
		if (this.emotionDecrementTime == decrementPeriod - 1) {
			this.decrement();
		}
		
		this.emotionDecrementTime = Periodic.incrementPeriodTime(this.emotionDecrementTime, decrementPeriod);
	}
}