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
package fr.n7.saceca.u3du.model.io.ai;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.EntitiesFactoryMaterials;
import fr.n7.saceca.u3du.model.io.ai.category.CategoryModelRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.clazz.ClassFileDirectoryIO;
import fr.n7.saceca.u3du.model.io.ai.model.agent.AgentModelRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.agent.module.CommunicationModuleRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.agent.module.EmotionModuleRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.agent.module.PerceptionModuleRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.agent.module.PlanningModuleRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.agent.module.ReasoningModuleRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.object.WorldObjectModelRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.model.object.behavior.BehaviorRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.service.ActionRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.service.ServiceRepositoryLoader;
import fr.n7.saceca.u3du.model.util.io.clazz.management.ClassFileDirectory;

/**
 * A class to read various files in a directory. The list currently contains:
 * <ul>
 * <li>Category Model;</li>
 * <li>Service;</li>
 * <li>World Object Model;</li>
 * <li>Classes implementing Behavior.</li>
 * </ul>
 * 
 * 
 * @author Sylvain Cambon
 */
public class EntitiesFactoryMaterialsLoader {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(EntitiesFactoryMaterialsLoader.class);
	
	/**
	 * Read all the compatible files in the given directory. The organization has to respect the
	 * official requirements.
	 * 
	 * //TODO: write these requirements
	 * 
	 * @param path
	 *            the path
	 * @return the world object factory materials
	 */
	public EntitiesFactoryMaterials readAll(String path) {
		final long before = new Date().getTime();
		
		final EntitiesFactoryMaterials materials = new EntitiesFactoryMaterials();
		
		final char separator = File.separatorChar;
		
		WorldObjectModelRepositoryLoader woLoader = new WorldObjectModelRepositoryLoader();
		materials.setWorldObjectModelRepository(woLoader.loadFilesToRepository(path + separator
				+ Constants.WORLD_OBJECTS_MODELS_FOLDER_NAME));
		
		AgentModelRepositoryLoader agLoader = new AgentModelRepositoryLoader();
		materials.setAgentModelRepository(agLoader.loadFilesToRepository(path + separator
				+ Constants.AGENTS_MODELS_FOLDER_NAME));
		
		CategoryModelRepositoryLoader catLoader = new CategoryModelRepositoryLoader();
		materials.setCategoryModelRepository(catLoader.loadFilesToRepository(path + separator
				+ Constants.CATEGORIES_FOLDER_NAME));
		
		// Class files loading
		ClassFileDirectoryIO classIO = new ClassFileDirectoryIO();
		ClassFileDirectory directory;
		String filename = null;
		try {
			filename = path + separator + Constants.CLASSES_CONFIGURATION_FILE_NAME;
			directory = classIO.importObject(filename);
			
			ActionRepositoryLoader actLoader = new ActionRepositoryLoader();
			materials.setActionRepository(actLoader.loadFilesToRepository(directory));
			
			BehaviorRepositoryLoader behLoader = new BehaviorRepositoryLoader();
			materials.setBehaviorRepository(behLoader.loadFilesToRepository(directory));
			
			PerceptionModuleRepositoryLoader perLoader = new PerceptionModuleRepositoryLoader();
			materials.setPerceptionRepository(perLoader.loadFilesToRepository(directory));
			
			ReasoningModuleRepositoryLoader reaLoader = new ReasoningModuleRepositoryLoader();
			materials.setReasoningRepository(reaLoader.loadFilesToRepository(directory));
			
			PlanningModuleRepositoryLoader plaLoader = new PlanningModuleRepositoryLoader();
			materials.setPlanningRepository(plaLoader.loadFilesToRepository(directory));
			
			CommunicationModuleRepositoryLoader comLoader = new CommunicationModuleRepositoryLoader();
			materials.setCommunicationRepository(comLoader.loadFilesToRepository(directory));
			
			EmotionModuleRepositoryLoader emoLoader = new EmotionModuleRepositoryLoader();
			materials.setEmotionRepository(emoLoader.loadFilesToRepository(directory));
		} catch (IOException e) {
			logger.error("Could not load " + filename + ", so class files loading was skipped.", e);
		}
		
		// EmotionWordRepositoryLoader emowLoader = new EmotionWordRepositoryLoader();
		// materials.setEmotionWordRepository(emowLoader.loadFilesToRepository(path + separator
		// + Constants.EMOTION_WORDS_FOLDER_NAME));
		//
		// MarkovEmotionRepositoryLoader emotionsLoader = new MarkovEmotionRepositoryLoader();
		// materials.setMarkovEmotionRepository(emotionsLoader.loadFilesToRepository(path +
		// separator
		// + Constants.MARKOV_EMOTIONS_FOLDER_NAME));
		
		ServiceRepositoryLoader servLoader = new ServiceRepositoryLoader(materials.getActionRepository());
		materials.setServiceRepository(servLoader.loadFilesToRepository(path + separator
				+ Constants.SERVICES_FOLDER_NAME));
		
		final long after = new Date().getTime();
		final long diff = after - before;
		logger.info("Loading finished in " + diff + "ms.");
		return materials;
	}
}