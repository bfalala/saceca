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
package fr.n7.saceca.u3du.model.graphics.configuration;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A class to describe a vehicle graphical model.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("vehicle-3d")
public class VehicleConfiguration extends SpatialConfiguration {
	
	/** The collisionShapeRadius value. */
	private float collisionShapeRadius;
	
	/** The collisionShapeHeight value. */
	private float collisionShapeHeight;
	
	/** The stepHeight value. */
	private float stepHeight;
	
	/**
	 * Instantiates a new vehicle configuration.
	 */
	public VehicleConfiguration() {
		super();
	}
	
	/**
	 * Gets the collisionShapeRadius.
	 * 
	 * @return the collisionShapeRadius
	 */
	public final float getCollisionShapeRadius() {
		return this.collisionShapeRadius;
	}
	
	/**
	 * Sets the stiffness.
	 * 
	 * @param collisionShapeRadius
	 *            the new collisionShapeRadius
	 */
	public final void setCollisionShapeRadius(float collisionShapeRadius) {
		this.collisionShapeRadius = collisionShapeRadius;
	}
	
	/**
	 * Gets the collisionShapeHeight value.
	 * 
	 * @return the collisionShapeHeight value
	 */
	public final float getCollisionShapeHeight() {
		return this.collisionShapeHeight;
	}
	
	/**
	 * Sets the collisionShapeHeight value.
	 * 
	 * @param collisionShapeHeight
	 *            the new collisionShapeHeight value
	 */
	public final void setCollisionShapeHeight(float collisionShapeHeight) {
		this.collisionShapeHeight = collisionShapeHeight;
	}
	
	/**
	 * Gets the stepHeight value.
	 * 
	 * @return the stepHeight value
	 */
	public final float getStepHeight() {
		return this.stepHeight;
	}
	
	/**
	 * Sets the stepHeight value.
	 * 
	 * @param stepHeight
	 *            the new stepHeight value
	 */
	public final void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(this.collisionShapeHeight);
		result = prime * result + Float.floatToIntBits(this.stepHeight);
		result = prime * result + Float.floatToIntBits(this.collisionShapeRadius);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		VehicleConfiguration other = (VehicleConfiguration) obj;
		if (Float.floatToIntBits(this.collisionShapeHeight) != Float.floatToIntBits(other.collisionShapeHeight)) {
			return false;
		}
		if (Float.floatToIntBits(this.stepHeight) != Float.floatToIntBits(other.stepHeight)) {
			return false;
		}
		if (Float.floatToIntBits(this.collisionShapeRadius) != Float.floatToIntBits(other.collisionShapeRadius)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "VehicleConfiguration [" + super.toInnerString() + ", collisionShapeRadius=" + this.collisionShapeRadius
				+ ", collisionShapeHeight=" + this.collisionShapeHeight + ", stepHeight=" + this.stepHeight + "]";
	}
}