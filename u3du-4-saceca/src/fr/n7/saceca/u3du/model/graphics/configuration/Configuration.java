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
package fr.n7.saceca.u3du.model.graphics.configuration;

import fr.n7.saceca.u3du.model.util.io.storage.Storable;

// TODO: Auto-generated Javadoc
/**
 * A common class to configure 3D models.
 * 
 * @author Sylvain Cambon
 */
public class Configuration implements Storable {
	
	/** The name. */
	private String name;
	
	// Visibility
	/** If the generated element is visible at start. */
	private boolean visible;
	
	/** The perceptibility. */
	private boolean perceptible;
	
	// Position
	/** The elevation. */
	private float elevation;
	
	/** The corrective angle (rad) to normalize directions. */
	private float correctiveAngle;
	
	/**
	 * Instantiates a new configuration.
	 */
	public Configuration() {
		super();
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Checks if is if the generated element is visible.
	 * 
	 * @return the if the generated element is visible
	 */
	public final boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * Sets the if the generated element is visible.
	 * 
	 * @param visible
	 *            the new if the generated element is visible
	 */
	public final void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Gets the corrective angle (rad) to normalize directions.
	 * 
	 * @return the corrective angle (rad) to normalize directions
	 */
	public final float getCorrectiveAngle() {
		return this.correctiveAngle;
	}
	
	/**
	 * Sets the corrective angle (rad) to normalize directions.
	 * 
	 * @param correctiveAngle
	 *            the new corrective angle (rad) to normalize directions
	 */
	public final void setCorrectiveAngle(float correctiveAngle) {
		this.correctiveAngle = correctiveAngle;
	}
	
	/**
	 * Gets the elevation.
	 * 
	 * @return the elevation
	 */
	public final float getElevation() {
		return this.elevation;
	}
	
	/**
	 * Sets the elevation.
	 * 
	 * @param elevation
	 *            the new elevation
	 */
	public final void setElevation(float elevation) {
		this.elevation = elevation;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "name=" + this.name + ", visible=" + this.visible + ", zOffset=" + this.elevation + ", correctiveAngle="
				+ this.correctiveAngle;
	}
	
	/**
	 * Hash code.
	 * 
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.correctiveAngle);
		result = prime * result + Float.floatToIntBits(this.elevation);
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + (this.visible ? 1231 : 1237);
		return result;
	}
	
	/**
	 * Equals.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
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
		Configuration other = (Configuration) obj;
		if (Float.floatToIntBits(this.correctiveAngle) != Float.floatToIntBits(other.correctiveAngle)) {
			return false;
		}
		if (Float.floatToIntBits(this.elevation) != Float.floatToIntBits(other.elevation)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.visible != other.visible) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.getName();
	}
	
	/**
	 * Checks if is the perceptibility.
	 * 
	 * @return the perceptibility
	 */
	public final boolean isPerceptible() {
		return this.perceptible;
	}
	
	/**
	 * Sets the perceptibility.
	 * 
	 * @param perceptible
	 *            the new perceptibility
	 */
	public final void setPerceptible(boolean perceptible) {
		this.perceptible = perceptible;
	}
	
}