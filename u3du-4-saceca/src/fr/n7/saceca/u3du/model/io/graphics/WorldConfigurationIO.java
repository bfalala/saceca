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

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.graphics.configuration.WorldConfiguration;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;

/**
 * A class for world configuration IO.
 * 
 * @author Sylvain Cambon
 */
public class WorldConfigurationIO extends XStreamIO<WorldConfiguration> {
	
	/** The configured. */
	private static boolean configured = false;
	
	@Override
	protected void configureIfNecessary(XStream xStream) {
		if (!configured) {
			xStream.processAnnotations(WorldConfiguration.class);
		}
	}
	
	@Override
	protected boolean isConfigured() {
		return configured;
	}
	
}