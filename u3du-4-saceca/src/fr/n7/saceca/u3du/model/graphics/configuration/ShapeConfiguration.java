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

/**
 * A class to configure shpaes that have textures.
 * 
 * @author Sylvain Cambon
 */
public abstract class ShapeConfiguration extends Configuration {
	
	/** The path to texture. */
	private String pathTexture;
	
	/**
	 * Instantiates a new shape configuration.
	 */
	public ShapeConfiguration() {
		super();
	}
	
	/**
	 * Gets the path to texture.
	 * 
	 * @return the path to texture
	 */
	public final String getPathTexture() {
		return this.pathTexture;
	}
	
	/**
	 * Sets the path to texture.
	 * 
	 * @param pathTexture
	 *            the new path to texture
	 */
	public final void setPathTexture(String pathTexture) {
		this.pathTexture = pathTexture;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return super.toString() + ", pathTexture=" + this.pathTexture;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.pathTexture == null) ? 0 : this.pathTexture.hashCode());
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
		ShapeConfiguration other = (ShapeConfiguration) obj;
		if (this.pathTexture == null) {
			if (other.pathTexture != null) {
				return false;
			}
		} else if (!this.pathTexture.equals(other.pathTexture)) {
			return false;
		}
		return true;
	}
	
}