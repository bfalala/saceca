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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A class to configure a box model.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("box-3d")
public class BoxConfiguration extends ShapeConfiguration {
	
	/** The width. */
	private float width;
	
	/** The length. */
	private float length;
	
	/** The height. */
	private float height;
	
	/**
	 * Instantiates a new box configuration.
	 */
	public BoxConfiguration() {
		super();
	}
	
	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public final float getWidth() {
		return this.width;
	}
	
	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the new width
	 */
	public final void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public final float getLength() {
		return this.length;
	}
	
	/**
	 * Sets the length.
	 * 
	 * @param length
	 *            the new length
	 */
	public final void setLength(float length) {
		this.length = length;
	}
	
	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public final float getHeight() {
		return this.height;
	}
	
	/**
	 * Sets the height.
	 * 
	 * @param height
	 *            the new height
	 */
	public final void setHeight(float height) {
		this.height = height;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(this.height);
		result = prime * result + Float.floatToIntBits(this.length);
		result = prime * result + Float.floatToIntBits(this.width);
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
		BoxConfiguration other = (BoxConfiguration) obj;
		if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height)) {
			return false;
		}
		if (Float.floatToIntBits(this.length) != Float.floatToIntBits(other.length)) {
			return false;
		}
		if (Float.floatToIntBits(this.width) != Float.floatToIntBits(other.width)) {
			return false;
		}
		return true;
	}
	
}