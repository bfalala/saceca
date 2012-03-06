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
package fr.n7.saceca.u3du.model.io.ai.clazz;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;
import fr.n7.saceca.u3du.model.util.io.clazz.management.ClassFileDirectory;

/**
 * A class to handle yellow pages IO.
 * 
 * @author Sylvain Cambon
 */
public class ClassFileDirectoryIO extends XStreamIO<ClassFileDirectory> {
	
	/** Whether XStream was configured for dealing with ClassFileDirectory . */
	private static boolean configured = false;
	
	/**
	 * Configure if necessary to deal with ClassFileDirectory.
	 * 
	 * @param xStream
	 *            the x stream
	 */
	@Override
	protected void configureIfNecessary(XStream xStream) {
		if (!configured) {
			xStream.processAnnotations(ClassFileDirectory.class);
			ClassFileDirectoryIO.configured = true;
		}
	}
	
	/**
	 * Checks Whether XStream was configured for dealing with ClassFileDirectory.
	 * 
	 * @return true, if is configured
	 */
	@Override
	protected boolean isConfigured() {
		return configured;
	}
}