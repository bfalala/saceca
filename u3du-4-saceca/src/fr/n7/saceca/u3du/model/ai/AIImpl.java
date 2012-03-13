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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import Emotion_primary.generate_propertyrule_tab;
import Emotion_primary.matrix_rule;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fr.n7.saceca.u3du.model.ai.service.Param;
import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.model.console.Console;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The implantation of the AI interface.
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public class AIImpl implements AI {
	
	/** The world. */
	private World world;
	
	/** The simulation. */
	private Simulation simulation;
	
	/** The console. */
	private Console console;
	
	/** The entities factory. */
	private EntitiesFactory entitiesFactory;
	
	/** The entities factory materials. */
	private EntitiesFactoryMaterials entitiesFactoryMaterials;
	
	/** The IO manager. */
	private IOManager ioManager;
	
	/**
	 * Instantiates a new AI implantation.
	 */
	public AIImpl() {
		this.world = new World();
		this.simulation = new Simulation(this.world);
		this.console = new Console();
		this.ioManager = new IOManager();
		
		this.createlinklist();
		
	}
	
	/**
	 * Tells the object that its animation is finished.
	 * 
	 * @param finishedAnimation
	 *            the finished animation
	 */
	@Override
	public void animationFinished(Animation finishedAnimation) {
		this.world.getWorldObjects().get(finishedAnimation.getObjectId()).setAnimation(null);
		finishedAnimation.getResult().applyResult();
	}
	
	@Override
	public void initAI() {
		this.create_matrix_effect_emotion();
		this.createMatrix_primary_secondary();
		this.createStimulusReactionTable();
		this.createruleTable();
	}
	
	/**
	 * Gets the world.
	 * 
	 * @return the world
	 */
	@Override
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Send commands.
	 * 
	 * @param commands
	 *            the commands
	 * @return the string
	 * @throws CommandException
	 *             the command exception
	 */
	@Override
	public String sendCommands(String commands) throws CommandException {
		return this.console.executeCommands(commands);
	}
	
	@Override
	public Simulation getSimulation() {
		return this.simulation;
	}
	
	@Override
	public IOManager getIOManager() {
		return this.ioManager;
	}
	
	/**
	 * Gets the entities factory.
	 * 
	 * @return the entities factory
	 */
	@Override
	public final EntitiesFactory getEntitiesFactory() {
		return this.entitiesFactory;
	}
	
	@Override
	public final void setEntitiesFactory(EntitiesFactory entitiesFactory) {
		this.entitiesFactory = entitiesFactory;
	}
	
	@Override
	public final EntitiesFactoryMaterials getEntitiesFactoryMaterials() {
		return this.entitiesFactoryMaterials;
	}
	
	@Override
	public final void setEntitiesFactoryMaterials(EntitiesFactoryMaterials entitiesFactoryMaterials) {
		this.entitiesFactoryMaterials = entitiesFactoryMaterials;
	}
	
	@SuppressWarnings("unchecked")
	private void createlinklist() {
		XStream xstr = new XStream(new DomDriver());
		
		try {
			FileInputStream fileStr = new FileInputStream("data/ai/links.xml");
			xstr.aliasType("links", ArrayList.class);
			xstr.aliasType("links", ArrayList.class);
			xstr.aliasType("link", PropertyLink.class);
			xstr.aliasType("param", Param.class);
			xstr.alias("param", Param.class);
			xstr.alias("property", ServiceProperty.class);
			xstr.alias("premise", ServiceProperty.class);
			xstr.alias("conclusion", ServiceProperty.class);
			
			this.world.setLinkList((ArrayList<PropertyLink>) xstr.fromXML(fileStr));
		} catch (Exception e) {
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void createStimulusReactionTable() {
		XStream xstr = new XStream(new DomDriver());
		try {
			FileInputStream fileStr = new FileInputStream("data/ai/stimulus_reaction.xml");
			xstr.aliasType("list", Hashtable.class);
			xstr.alias("list", Hashtable.class);
			
			this.world.setStimulusReactionTable((Hashtable<String, Class<? extends Action>>) xstr.fromXML(fileStr));
		} catch (Exception e) {
			
		}
	}
	
	private void create_matrix_effect_emotion() {
		int i = 0, j = 0;
		int[][] matrice_effect_emotion;
		ArrayList<Couple<String, ServiceProperty>> serviceEffectList = new ArrayList<Couple<String, ServiceProperty>>();
		
		ArrayList<String> emotionssecondaires = new ArrayList<String>();
		for (Service service : this.entitiesFactoryMaterials.getServiceRepository()) {
			for (ServiceProperty property : service.getServiceEffectsPlus()) {
				serviceEffectList.add(new Couple<String, ServiceProperty>(service.getName(), property.deepDataClone()));
			}
			for (ServiceProperty property : service.getServiceEffectsMinus()) {
				serviceEffectList.add(new Couple<String, ServiceProperty>(service.getName(), property.deepDataClone()));
			}
		}
		
		this.world.setServiceEffectList(serviceEffectList);
		
		try {
			FileReader allWords = new FileReader("data/ai/secondary_emotions.txt");
			BufferedReader input = new BufferedReader(allWords);
			String line = null;
			while ((line = input.readLine()) != null) {
				emotionssecondaires.add(line);
			}
		}

		catch (Exception e) {
			
		}
		matrice_effect_emotion = new int[serviceEffectList.size()][emotionssecondaires.size()];
		for (Couple<String, ServiceProperty> couple : serviceEffectList) {
			
			j = emotionssecondaires.indexOf(couple.getSecondElement().getPossible_emotion());
			if (j != -1) {
				matrice_effect_emotion[i][j] = 1;
			}
			i++;
		}
		this.world.setMatrix_effect_emotions(matrice_effect_emotion);
	}
	
	private void createMatrix_primary_secondary() {
		ArrayList<String> secondaryEmotions = new ArrayList<String>();
		ArrayList<String> primaryEmotions = new ArrayList<String>();
		try {
			FileReader allWords = new FileReader("data/ai/secondary_emotions.txt");
			BufferedReader input = new BufferedReader(allWords);
			String line = null;
			while ((line = input.readLine()) != null) {
				secondaryEmotions.add(line);
			}
		} catch (Exception e) {
			
		}
		try {
			FileReader allWords = new FileReader("data/ai/primary_emotions.txt");
			BufferedReader input = new BufferedReader(allWords);
			String line = null;
			while ((line = input.readLine()) != null) {
				primaryEmotions.add(line);
			}
		} catch (Exception e) {
			
		}
		
		double[][] matrix_primary_secondary = new double[primaryEmotions.size()][secondaryEmotions.size()];
		
		try {
			FileReader allWords = new FileReader("data/ai/primary_secondary.txt");
			BufferedReader input = new BufferedReader(allWords);
			String lineFile = null;
			String currentSecondaryEm = "";
			String[] currentPrimaryEm;
			int number_em = 0, column = 0, line = 0;
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			while ((lineFile = input.readLine()) != null) {
				// currentPrimaryEm = null;
				currentSecondaryEm = lineFile.split("=")[0];
				
				currentPrimaryEm = lineFile.split("=")[1].split("_");
				number_em = currentPrimaryEm.length;
				column = secondaryEmotions.indexOf(currentSecondaryEm);
				for (String primary : currentPrimaryEm) {
					line = primaryEmotions.indexOf(primary);
					
					matrix_primary_secondary[line][column] = (double) 1 / number_em;
				}
			}
		} catch (Exception e) {
			System.out.print("sdasd");
		}
		this.world.setMatrix_primary_secondary(matrix_primary_secondary);
		
	}
	
	private void createruleTable() {
		
		// Récupération table des implications
		generate_propertyrule_tab tabs = new generate_propertyrule_tab("data/ai/knowlage.xml");
		// Création de la matrice associée
		this.world.setMatrule(new matrix_rule(tabs.getProperty_rule_Tab(), tabs.getRuleTab()));
		this.world.setProperty_rule_tab(tabs.getProperty_rule_Tab());
		
		Hashtable<String, Integer> emotion_index_tab = new Hashtable<String, Integer>();
		
		emotion_index_tab.put("i_emotion_fear", tabs.getIndex_Fear());
		emotion_index_tab.put("i_emotion_joy", tabs.getIndex_Joy());
		emotion_index_tab.put("i_emotion_angry", tabs.getIndex_Angry());
		emotion_index_tab.put("i_emotion_disgust", tabs.getIndex_Disgust());
		emotion_index_tab.put("i_emotion_sadness", tabs.getIndex_Sadness());
		emotion_index_tab.put("i_emotion_surprise", tabs.getIndex_Surprise());
		
		this.world.setEmotion_index_tab(emotion_index_tab);
	}
	
}