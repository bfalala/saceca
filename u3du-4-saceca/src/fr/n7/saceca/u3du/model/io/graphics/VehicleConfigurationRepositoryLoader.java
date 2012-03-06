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
package fr.n7.saceca.u3du.model.io.graphics;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.graphics.configuration.VehicleConfiguration;
import fr.n7.saceca.u3du.model.io.common.HighLevelImporter;
import fr.n7.saceca.u3du.model.io.common.XMLRepositoryLoader;

/**
 * A repository for 3D vehicles configurations.
 * 
 * @author Sylvain Cambon
 */
public class VehicleConfigurationRepositoryLoader extends XMLRepositoryLoader<VehicleConfiguration> {
	
	@Override
	protected String getExtension() {
		return Constants.VEHICLE_3D_EXTENSION;
	}
	
	@Override
	protected HighLevelImporter<VehicleConfiguration> getImporter() {
		return new VehicleConfigurationIO();
	}
	
	@Override
	protected String getRepositoryName() {
		return VehicleConfiguration.class.getSimpleName() + " Repository";
	}
	
}