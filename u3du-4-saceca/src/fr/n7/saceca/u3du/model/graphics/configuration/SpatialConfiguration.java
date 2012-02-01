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

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A configuration class for spatials.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("spatial-3d")
public class SpatialConfiguration extends Configuration {
	
	/** The path model. */
	private String pathModel;
	
	// Scale
	/** The scale x. */
	private float scaleX;
	
	/** The scale y. */
	private float scaleY;
	
	/** The scale z. */
	private float scaleZ;
	
	/** The children. */
	private Map<String, SpatialConfiguration> children;
	
	/**
	 * Instantiates a new spatial configuration.
	 */
	public SpatialConfiguration() {
		super();
		this.children = new HashMap<String, SpatialConfiguration>();
	}
	
	/**
	 * Gets the path model.
	 * 
	 * @return the path model
	 */
	public final String getPathModel() {
		return this.pathModel;
	}
	
	/**
	 * Sets the path model.
	 * 
	 * @param pathModel
	 *            the new path model
	 */
	public final void setPathModel(String pathModel) {
		this.pathModel = pathModel;
	}
	
	/**
	 * Gets the scale x.
	 * 
	 * @return the scale x
	 */
	public final float getScaleX() {
		return this.scaleX;
	}
	
	/**
	 * Sets the scale x.
	 * 
	 * @param scaleX
	 *            the new scale x
	 */
	public final void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}
	
	/**
	 * Gets the scale y.
	 * 
	 * @return the scale y
	 */
	public final float getScaleY() {
		return this.scaleY;
	}
	
	/**
	 * Sets the scale y.
	 * 
	 * @param scaleY
	 *            the new scale y
	 */
	public final void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	/**
	 * Gets the scale z.
	 * 
	 * @return the scale z
	 */
	public final float getScaleZ() {
		return this.scaleZ;
	}
	
	/**
	 * Sets the scale z.
	 * 
	 * @param scaleZ
	 *            the new scale z
	 */
	public final void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}
	
	/**
	 * Gets the children.
	 * 
	 * @return the children
	 */
	public final Map<String, SpatialConfiguration> getChildren() {
		return this.children;
	}
	
	/**
	 * Sets the children.
	 * 
	 * @param children
	 *            the children to set
	 */
	public final void setChildren(Map<String, SpatialConfiguration> children) {
		this.children = children;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "SpatialConfiguration [" + this.toInnerString() + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.children == null) ? 0 : this.children.hashCode());
		result = prime * result + ((this.pathModel == null) ? 0 : this.pathModel.hashCode());
		result = prime * result + Float.floatToIntBits(this.scaleX);
		result = prime * result + Float.floatToIntBits(this.scaleY);
		result = prime * result + Float.floatToIntBits(this.scaleZ);
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
		SpatialConfiguration other = (SpatialConfiguration) obj;
		if (this.children == null) {
			if (other.children != null) {
				return false;
			}
		} else if (!this.children.equals(other.children)) {
			return false;
		}
		if (this.pathModel == null) {
			if (other.pathModel != null) {
				return false;
			}
		} else if (!this.pathModel.equals(other.pathModel)) {
			return false;
		}
		if (Float.floatToIntBits(this.scaleX) != Float.floatToIntBits(other.scaleX)) {
			return false;
		}
		if (Float.floatToIntBits(this.scaleY) != Float.floatToIntBits(other.scaleY)) {
			return false;
		}
		if (Float.floatToIntBits(this.scaleZ) != Float.floatToIntBits(other.scaleZ)) {
			return false;
		}
		return true;
	}
	
	/**
	 * To string aimed at integration for subclasses.
	 * 
	 * @return the string
	 */
	protected String toInnerString() {
		String string = super.toString() + ", pathModel=" + this.pathModel + ", scaleX=" + this.scaleX + ", scaleY="
				+ this.scaleY + ", scaleZ=" + this.scaleZ + ", children=[";
		StringBuilder builder = new StringBuilder();
		for (SpatialConfiguration conf : this.getChildren().values()) {
			builder.append('\n');
			builder.append(conf.toInnerString());
		}
		return string + builder.toString() + "]\n";
	}
	
}