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

import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;

/**
 * A class to gather elements required by the graphics factory to run.
 * 
 * @author Sylvain Cambon
 */
public class GraphicsFactoryMaterials {
	
	/** The agent configuration repository. */
	private Repository<AgentConfiguration> agentConfigurationRepository;
	
	/** The box configuration repository. */
	private Repository<BoxConfiguration> boxConfigurationRepository;
	
	/** The vehicle configuration repository. */
	private Repository<VehicleConfiguration> vehicleConfigurationRepository;
	
	/** The spatial configuration repository. */
	private Repository<SpatialConfiguration> spatialConfigurationRepository;
	
	/** The engine. */
	private Engine3D engine;
	
	/**
	 * Gets the engine.
	 * 
	 * @return the engine
	 */
	public final Engine3D getEngine() {
		return this.engine;
	}
	
	/**
	 * Sets the engine.
	 * 
	 * @param engine
	 *            the new engine
	 */
	public final void setEngine(Engine3D engine) {
		this.engine = engine;
	}
	
	/**
	 * Instantiates a new entities factory materials.
	 */
	public GraphicsFactoryMaterials() {
		super();
	}
	
	/**
	 * Gets the agent configuration repository.
	 * 
	 * @return the agent configuration repository
	 */
	public final Repository<AgentConfiguration> getAgentConfigurationRepository() {
		return this.agentConfigurationRepository;
	}
	
	/**
	 * Gets the spatial configuration repository.
	 * 
	 * @return the spatial configuration repository
	 */
	public final Repository<SpatialConfiguration> getSpatialConfigurationRepository() {
		return this.spatialConfigurationRepository;
	}
	
	/**
	 * Sets the spatial configuration repository.
	 * 
	 * @param spatialConfigurationRepository
	 *            the new spatial configuration repository
	 */
	public final void setSpatialConfigurationRepository(Repository<SpatialConfiguration> spatialConfigurationRepository) {
		this.spatialConfigurationRepository = spatialConfigurationRepository;
	}
	
	/**
	 * Sets the agent configuration repository.
	 * 
	 * @param agentConfigurationRepository
	 *            the new agent configuration repository
	 */
	public final void setAgentConfigurationRepository(Repository<AgentConfiguration> agentConfigurationRepository) {
		this.agentConfigurationRepository = agentConfigurationRepository;
	}
	
	/**
	 * Gets the box configuration repository.
	 * 
	 * @return the box configuration repository
	 */
	public final Repository<BoxConfiguration> getBoxConfigurationRepository() {
		return this.boxConfigurationRepository;
	}
	
	/**
	 * Sets the box configuration repository.
	 * 
	 * @param boxConfigurationRepository
	 *            the new box configuration repository
	 */
	public final void setBoxConfigurationRepository(Repository<BoxConfiguration> boxConfigurationRepository) {
		this.boxConfigurationRepository = boxConfigurationRepository;
	}
	
	/**
	 * Gets the vehicle configuration repository.
	 * 
	 * @return the vehicle configuration repository
	 */
	public final Repository<VehicleConfiguration> getVehicleConfigurationRepository() {
		return this.vehicleConfigurationRepository;
	}
	
	/**
	 * Sets the vehicle configuration repository.
	 * 
	 * @param vehicleConfigurationRepository
	 *            the new vehicle configuration repository
	 */
	public final void setVehicleConfigurationRepository(Repository<VehicleConfiguration> vehicleConfigurationRepository) {
		this.vehicleConfigurationRepository = vehicleConfigurationRepository;
	}
	
}