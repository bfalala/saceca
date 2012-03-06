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
package fr.n7.saceca.u3du.model.ai;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.exception.SacecaStrictException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.graphics.configuration.WorldConfiguration;
import fr.n7.saceca.u3du.model.io.ai.EntitiesFactoryMaterialsLoader;
import fr.n7.saceca.u3du.model.io.ai.instance.agent.AgentIO;
import fr.n7.saceca.u3du.model.io.ai.instance.object.WorldObjectIO;
import fr.n7.saceca.u3du.model.io.graphics.WorldConfigurationIO;
import fr.n7.saceca.u3du.model.util.ProgressMonitor;
import fr.n7.saceca.u3du.model.util.io.FilenameExtensionFilter;

/**
 * The Class IOManager manages the IO streams of the AI.
 * 
 * @author Sylvain Cambon
 */
public class IOManager {
	
	/** The model path. */
	private String modelPath;
	
	/** The instances path. */
	private String instancesPath;
	
	/** The Constant logger. */
	private static final Logger LOGGER = Logger.getLogger(IOManager.class);
	
	/**
	 * Inits the AI.
	 * 
	 * @param modelPath
	 *            the model path
	 * @param instancePath
	 *            the instance path
	 * @throws SacecaStrictException
	 *             If a problem occurs during the initialization of the AI.
	 */
	public void loadAI(String modelPath, String instancePath) throws SacecaStrictException {
		this.loadWorldConf(instancePath);
		
		final AI ai = Model.getInstance().getAI();
		this.modelPath = modelPath;
		this.instancesPath = instancePath;
		
		LOGGER.info("Started loading AI files...");
		long before = new Date().getTime();
		
		// Loading models
		LOGGER.info("Started loading AI models...");
		EntitiesFactoryMaterialsLoader reader = new EntitiesFactoryMaterialsLoader();
		final EntitiesFactoryMaterials entitiesFactoryMaterials = reader.readAll(modelPath);
		ai.setEntitiesFactoryMaterials(entitiesFactoryMaterials);
		LOGGER.info("Finished loading AI models.");
		
		// Building the factory
		final EntitiesFactory entitiesFactory = new EntitiesFactory(entitiesFactoryMaterials);
		ai.setEntitiesFactory(entitiesFactory);
		
		// Getting the world content collection
		Map<Long, WorldObject> worldContent = ai.getWorld().getWorldObjects();
		
		// loading the world objects
		WorldObjectIO woio = new WorldObjectIO(entitiesFactory);
		String worldObjectDirName = instancePath + File.separatorChar + Constants.WORLD_OBJECTS_FOLDER_NAME;
		LOGGER.info("Started loading AI objects instances...");
		this.loadAllWorldObjectInstancesInFolder(worldContent, woio, worldObjectDirName);
		final String worldObjectNonPerceptibleDirName = worldObjectDirName + File.separatorChar
				+ Constants.MISC_SUB_FOLDER_NAME;
		File nonPerceptibleDir = new File(worldObjectNonPerceptibleDirName);
		if (nonPerceptibleDir.exists() && nonPerceptibleDir.isDirectory()) {
			this.loadAllWorldObjectInstancesInFolder(worldContent, woio, worldObjectNonPerceptibleDirName);
		}
		LOGGER.info("Finished loading AI objects instances.");
		
		// loading the agents
		AgentIO aio = new AgentIO(entitiesFactory);
		String agentDirName = instancePath + File.separatorChar + Constants.AGENTS_FOLDER_NAME;
		LOGGER.info("Started loading AI agents instances...");
		this.loadAllAgentInstancesInFolder(worldContent, aio, agentDirName);
		LOGGER.info("Finished loading AI agents instances...");
		
		// Repairing the links between objects
		LOGGER.info("Started loading AI files post-processing...");
		EntitiesFactory.repairBelongings(worldContent.values());
		
		long timeDelta = new Date().getTime() - before;
		LOGGER.info("Finished loading AI files in " + timeDelta + "ms.");
	}
	
	/**
	 * Loads the world configuration.
	 * 
	 * @param instancePath
	 *            the instance path
	 * @throws SacecaStrictException
	 *             the saceca strict exception
	 */
	private void loadWorldConf(String instancePath) throws SacecaStrictException {
		Model m = Model.getInstance();
		WorldConfigurationIO wcio = new WorldConfigurationIO();
		WorldConfiguration worldConf;
		try {
			worldConf = wcio.importObject(instancePath + File.separatorChar + Constants.WORLD_CONFIGURATION_FILE_NAME);
		} catch (IOException e) {
			throw new SacecaStrictException(e);
		}
		
		m.getGraphics().getEngine3D().setTerrainSize(worldConf.getTerrainSize());
	}
	
	/**
	 * Load all agent instances in a folder.
	 * 
	 * @param worldContent
	 *            the world content
	 * @param aio
	 *            the aio
	 * @param agentDirName
	 *            the agent dir name
	 */
	private void loadAllAgentInstancesInFolder(Map<Long, WorldObject> worldContent, AgentIO aio, String agentDirName) {
		File agentsDir = new File(agentDirName);
		String[] agentList = agentsDir.list(new FilenameExtensionFilter(Constants.AGENTS_EXTENSION));
		if (agentList != null) {
			for (String filename : agentList) {
				String completeFilename = agentDirName + File.separatorChar + filename;
				LOGGER.info("Attempt to load: " + completeFilename);
				try {
					Agent agent = aio.importObject(completeFilename);
					if (agent != null) {
						worldContent.put(agent.getId(), agent);
						LOGGER.info("Successfully loaded: " + completeFilename);
					}
				} catch (IOException e) {
					LOGGER.warn(completeFilename + " cannot be loaded, this file is skipped.", e);
				}
			}
		} else {
			LOGGER.warn("The directory \"" + agentDirName
					+ "\"seems not to exist or not to be a directory. Agent instances load skipped.");
		}
	}
	
	/**
	 * Load all world object instances a in folder.
	 * 
	 * @param worldContent
	 *            the world content
	 * @param woio
	 *            the woio
	 * @param worldObjectDirName
	 *            the world object dir name
	 */
	private void loadAllWorldObjectInstancesInFolder(Map<Long, WorldObject> worldContent, WorldObjectIO woio,
			String worldObjectDirName) {
		File worldObjectDir = new File(worldObjectDirName);
		String[] list = worldObjectDir.list(new FilenameExtensionFilter(Constants.WORLD_OBJECTS_EXTENSION));
		if (list != null) {
			for (String filename : list) {
				String completeFilename = worldObjectDirName + File.separatorChar + filename;
				LOGGER.info("Attempt to load: " + completeFilename);
				try {
					WorldObject object = woio.importObject(completeFilename);
					if (object != null) {
						worldContent.put(object.getId(), object);
						LOGGER.info("Successfully loaded: " + completeFilename);
					}
				} catch (IOException e) {
					LOGGER.warn(completeFilename + " cannot be loaded, this file is skipped.", e);
				}
			}
		} else {
			LOGGER.warn("The directory \"" + worldObjectDirName
					+ "\"seems not to exist or not to be a directory. Object instances load skipped.");
		}
	}
	
	/**
	 * Gets the model path.
	 * 
	 * @return the model path
	 */
	public final String getModelPath() {
		return this.modelPath;
	}
	
	/**
	 * Sets the model path.
	 * 
	 * @param modelPath
	 *            the new model path
	 */
	public final void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	
	/**
	 * Gets the instances path.
	 * 
	 * @return the instances path
	 */
	public final String getInstancesPath() {
		return this.instancesPath;
	}
	
	/**
	 * Sets the instances path.
	 * 
	 * @param instancesPath
	 *            the new instances path
	 */
	public final void setInstancesPath(String instancesPath) {
		this.instancesPath = instancesPath;
	}
	
	/**
	 * Saves the world to the given directory. The progress can be monitored.
	 * 
	 * @param path
	 *            the absolute path
	 * @param progressMonitor
	 *            the progress monitor. null is accepted.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void save(String path, ProgressMonitor progressMonitor) throws IOException {
		final AI ai = Model.getInstance().getAI();
		final World world = ai.getWorld();
		EntitiesFactory entitiesFactory = ai.getEntitiesFactory();
		
		// Progress monitor initialization
		if (progressMonitor != null) {
			final int size = world.getWorldObjects().size();
			progressMonitor.updateBounds(0, size);
		}
		int currentElement = 0;
		
		// Creating sub directories
		// objects
		final String objectsDirName = path + File.separatorChar + Constants.WORLD_OBJECTS_FOLDER_NAME;
		this.createDirectory(objectsDirName);
		
		// objects/misc
		final String objectsMiscDirName = objectsDirName + File.separatorChar + Constants.MISC_SUB_FOLDER_NAME;
		this.createDirectory(objectsMiscDirName);
		
		// agents
		final String agentsDirName = path + File.separatorChar + Constants.AGENTS_FOLDER_NAME;
		this.createDirectory(agentsDirName);
		
		// Saving the objects
		WorldObjectIO woio = new WorldObjectIO(entitiesFactory);
		for (WorldObject object : world.getReactiveObjects()) {
			String directory = objectsDirName;
			try {
				if (object.getPropertiesContainer().getBoolean(Internal.Object.IS_MISC)) {
					directory = objectsMiscDirName;
				}
			} catch (UnknownPropertyException e) {
				LOGGER.warn(e);
				e.printStackTrace();
			}
			final String filepath = this.generateFilepath(object, directory, Constants.WORLD_OBJECTS_EXTENSION);
			woio.exportObject(filepath, object);
			if (progressMonitor != null) {
				progressMonitor.updateContent(++currentElement, filepath);
			}
		}
		
		// Saving the agents
		AgentIO aio = new AgentIO(entitiesFactory);
		for (Agent agent : world.getAgents()) {
			final String filepath = this.generateFilepath(agent, agentsDirName, Constants.AGENTS_EXTENSION);
			aio.exportObject(filepath, agent);
			if (progressMonitor != null) {
				progressMonitor.updateContent(++currentElement, filepath);
			}
		}
		
		// File cleaning
		progressMonitor.updateContent(currentElement, "Cleaning unliked files...");
		this.clean(objectsDirName, objectsMiscDirName, agentsDirName);
		
		this.setInstancesPath(path);
	}
	
	/**
	 * Deletes files that do not correspond to an existing object.
	 * 
	 * @param objectsDirName
	 *            the objects dir name
	 * @param objectsMiscDirName
	 *            the objects misc dir name
	 * @param agentsDirName
	 *            the agents dir name
	 */
	public void clean(String objectsDirName, String objectsMiscDirName, String agentsDirName) {
		File directory;
		Map<Long, WorldObject> worldObjects = Model.getInstance().getAI().getWorld().getWorldObjects();
		
		directory = new File(objectsDirName);
		for (File file : directory.listFiles()) {
			this.deleteIfUnlinked(file, worldObjects);
		}
		
		directory = new File(objectsMiscDirName);
		for (File file : directory.listFiles()) {
			this.deleteIfUnlinked(file, worldObjects);
		}
		
		directory = new File(agentsDirName);
		for (File file : directory.listFiles()) {
			this.deleteIfUnlinked(file, worldObjects);
		}
	}
	
	/**
	 * Deletes a file if it doesn't correspond to an existing world object
	 * 
	 * @param file
	 *            the file
	 * @param worldObjects
	 *            the world objects
	 */
	private void deleteIfUnlinked(File file, Map<Long, WorldObject> worldObjects) {
		String[] fileModelNameAndID = file.getName().split("\\.")[0].split("_");
		if (fileModelNameAndID.length < 2) {
			return;
		}
		
		String fileModelName = fileModelNameAndID[0];
		long fileID = Long.parseLong(fileModelNameAndID[1]);
		
		WorldObject wo = worldObjects.get(fileID);
		
		if (wo == null || !wo.getModelName().toLowerCase().equals(fileModelName.toLowerCase())) {
			file.delete();
		}
	}
	
	/**
	 * Generates the file path.
	 * 
	 * @param object
	 *            the object
	 * @param directory
	 *            the directory
	 * @param extension
	 *            the extension
	 * @return the string
	 */
	private String generateFilepath(WorldObject object, String directory, String extension) {
		String modelName = object.getModelName();
		final char firstChar = modelName.charAt(0);
		if (Character.isUpperCase(firstChar)) {
			modelName = Character.toLowerCase(firstChar) + modelName.substring(1);
		}
		String filename = modelName + "_" + object.getId() + extension;
		final String filepath = directory + File.separatorChar + filename;
		return filepath;
	}
	
	/**
	 * Creates a directory.
	 * 
	 * @param objectsDirName
	 *            the objects dir name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void createDirectory(final String objectsDirName) throws IOException {
		boolean success;
		File objectsDir = new File(objectsDirName);
		if (!objectsDir.exists()) {
			success = objectsDir.mkdir();
			if (!success) {
				throw new IOException("The directory \"" + objectsDirName + "\" cannot be created.");
			}
		}
	}
	
}