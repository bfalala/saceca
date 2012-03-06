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
 * The Class WorldConfiguration.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("world-conf")
public class WorldConfiguration {
	
	/** The terrain size. */
	@XStreamAlias("terrain-size")
	private int terrainSize;
	
	/**
	 * Gets the terrain size.
	 * 
	 * @return the terrain size
	 */
	public final int getTerrainSize() {
		return this.terrainSize;
	}
	
	/**
	 * Sets the terrain size.
	 * 
	 * @param terrainSize
	 *            the new terrain size
	 */
	public final void setTerrainSize(int terrainSize) {
		this.terrainSize = terrainSize;
	}
	
	/**
	 * Instantiates a new world configuration.
	 * 
	 * @param terrainSize
	 *            the terrain size
	 */
	public WorldConfiguration(int terrainSize) {
		super();
		this.terrainSize = terrainSize;
	}
	
}