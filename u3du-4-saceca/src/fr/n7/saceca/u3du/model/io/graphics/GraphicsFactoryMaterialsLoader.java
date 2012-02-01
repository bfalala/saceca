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
package fr.n7.saceca.u3du.model.io.graphics;

import java.util.Date;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.graphics.configuration.GraphicsFactoryMaterials;

/**
 * A class to read various files in a directory. The list currently contains:
 * <ul>
 * <li>graphical agent configuration;</li>
 * <li>graphical box configuration;</li>
 * <li>graphical agent configuration.</li>
 * </ul>
 * 
 * 
 * @author Sylvain Cambon
 */
public class GraphicsFactoryMaterialsLoader {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(GraphicsFactoryMaterialsLoader.class);
	
	/**
	 * Read all the compatible files in the given directory. The organization has to respect the
	 * official requirements.
	 * 
	 * @param path
	 *            the path
	 * @return the graphics factory materials
	 */
	public GraphicsFactoryMaterials readAll(String path) {
		final long before = new Date().getTime();
		
		final GraphicsFactoryMaterials materials = new GraphicsFactoryMaterials();
		
		AgentConfigurationRepositoryLoader acLoader = new AgentConfigurationRepositoryLoader();
		materials.setAgentConfigurationRepository(acLoader.loadFilesToRepository(path));
		
		VehicleConfigurationRepositoryLoader vcLoader = new VehicleConfigurationRepositoryLoader();
		materials.setVehicleConfigurationRepository(vcLoader.loadFilesToRepository(path));
		
		SpatialConfigurationRepositoryLoader spaLoader = new SpatialConfigurationRepositoryLoader();
		materials.setSpatialConfigurationRepository(spaLoader.loadFilesToRepository(path));
		
		BoxConfigurationRepositoryLoader bcLoader = new BoxConfigurationRepositoryLoader();
		materials.setBoxConfigurationRepository(bcLoader.loadFilesToRepository(path));
		
		final long after = new Date().getTime();
		final long diff = after - before;
		logger.info("Loading finished in " + diff + "ms.");
		return materials;
	}
	
}
