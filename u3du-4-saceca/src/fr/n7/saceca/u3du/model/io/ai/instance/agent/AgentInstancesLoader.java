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
package fr.n7.saceca.u3du.model.io.ai.instance.agent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.util.io.FilenameExtensionFilter;

/**
 * A class to load all the agents instances saved as XML files in a directory.
 * 
 * @author Sylvain Cambon
 */
public class AgentInstancesLoader {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(AgentInstancesLoader.class);
	
	/** The factory. */
	private EntitiesFactory factory;
	
	/**
	 * Instantiates a new agent instances loader.
	 * 
	 * @param factory
	 *            the factory
	 */
	public AgentInstancesLoader(EntitiesFactory factory) {
		super();
		this.factory = factory;
	}
	
	/**
	 * Reads all the available agent instances in the provided directory. The factory provided to
	 * the constructor is used to instantiate the objects.
	 * 
	 * @param path
	 *            the path to read files from (a directory).
	 * @return the collection of read agents
	 */
	public Collection<Agent> readAll(String path) {
		Collection<Agent> objects = new LinkedList<Agent>();
		File dir = new File(path);
		FilenameFilter filter = new FilenameExtensionFilter(Constants.AGENTS_EXTENSION);
		AgentIO io = new AgentIO(this.factory);
		for (File file : dir.listFiles(filter)) {
			try {
				Agent object = io.importObject(file.getAbsolutePath());
				objects.add(object);
			} catch (IOException e) {
				logger.warn("The agent instance in " + file.getAbsolutePath() + " cannot be loaded. File skipped.");
			}
		}
		return objects;
	}
}
