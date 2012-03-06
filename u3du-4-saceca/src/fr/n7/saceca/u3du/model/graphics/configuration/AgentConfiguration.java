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
 * A class to describe an agent graphical model.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("agent-3d")
public class AgentConfiguration extends SpatialConfiguration {
	
	/** The height of the eyes of the Agent. */
	private float eyesHeight;
	
	/**
	 * Gets the height of the eyes of the Agent.
	 * 
	 * @return the height of the eyes of the Agent
	 */
	public final float getEyesHeight() {
		return this.eyesHeight;
	}
	
	/**
	 * Sets the height of the eyes of the Agent.
	 * 
	 * @param eyesHeight
	 *            the new height of the eyes of the Agent
	 */
	public final void setEyesHeight(float eyesHeight) {
		this.eyesHeight = eyesHeight;
	}
	
	/**
	 * Instantiates a new agent configuration.
	 */
	public AgentConfiguration() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(this.eyesHeight);
		return result;
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
		AgentConfiguration other = (AgentConfiguration) obj;
		if (Float.floatToIntBits(this.eyesHeight) != Float.floatToIntBits(other.eyesHeight)) {
			return false;
		}
		return true;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "AgentConfiguration [" + this.toInnerString() + ", eyesHeight=" + this.eyesHeight + "]";
	}
	
}