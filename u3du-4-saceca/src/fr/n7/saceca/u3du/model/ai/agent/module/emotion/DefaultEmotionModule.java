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
package fr.n7.saceca.u3du.model.ai.agent.module.emotion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Emotion_primary.forward_chaining;
import Emotion_primary.property_rule;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.EmotionalState;
import fr.n7.saceca.u3du.model.ai.agent.behavior.DefaultAgentBehavior;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.util.Couple;

/**
 * The Class DefaultEmotionModule.
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 */
public class DefaultEmotionModule implements EmotionModule {
	
	/**
	 * The Class EmotionThread.
	 */
	private class EmotionThread extends Thread {
		
		/**
		 * Instantiates a new emotion thread and names it according to the owning object.
		 */
		public EmotionThread() {
			super();
			this.setName(DefaultEmotionModule.this.agent.toShortString() + "Emotion thread");
		}
		
		/**
		 * This method represents the "emotion loop"
		 */
		@Override
		public void run() {
			while (DefaultEmotionModule.this.agent.isAlive() && !DefaultEmotionModule.this.agent.isPause()) {
				DefaultEmotionModule.this.detectEmotions();
				try {
					Thread.sleep(DefaultAgentBehavior.BEHAVE_PERIOD / 2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private Agent agent;
	
	/** The emotion thread */
	private EmotionThread emotionThread;
	
	/**
	 * Checks if the emotion thread is alive
	 */
	@Override
	public boolean isAlive() {
		return this.emotionThread != null && this.emotionThread.isAlive();
	}
	
	/**
	 * Detect emotions.
	 */
	@Override
	public void detectEmotions() {
		this.dovirtualPerception();
		
	}
	
	/**
	 * Starts the emotion module's thread
	 */
	@Override
	public void start() {
		this.emotionThread = new EmotionThread();
		this.emotionThread.start();
	}
	
	/**
	 * Constructor
	 * 
	 * @param agent
	 *            the agent
	 */
	public DefaultEmotionModule(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Takes the objects from the vision field, calculates using forward chaining the implied
	 * primary emotion and then updates the emotions' gauges
	 */
	public void dovirtualPerception() {
		ArrayList<String> vision = new ArrayList<String>();
		for (Couple<WorldObject, Boolean> newCouple : (ArrayList<Couple<WorldObject, Boolean>>) this.agent.getMemory()
				.getPerceivedObjects().clone()) {
			
			if (newCouple != null) {
				vision.add(newCouple.getFirstElement().getModelName());
			}
			
		}
		if (vision == null || vision.size() == 0) {
			return;
		}
		
		ArrayList<property_rule> tabpropertyrule = Model.getInstance().getAI().getWorld().getProperty_rule_tab();
		
		int[][] vector_perception = new int[1][tabpropertyrule.size()];
		int j = 0;
		for (int i = 0; i < tabpropertyrule.size(); i++) {
			
			vector_perception[0][i] = 0;
			
		}
		for (String vision_1 : vision) {
			j = 0;
			for (property_rule current_prop : tabpropertyrule) {
				if (vision_1.equals(current_prop.getName())) {
					vector_perception[0][j] = 1;
				}
				j++;
			}
			
		}
		
		forward_chaining forw1 = new forward_chaining(Model.getInstance().getAI().getWorld().getMatrule().getMatrice(),
				vector_perception);
		
		this.update_emotion_jauge(forw1.getVector_forwarded_result());
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return EmotionModule.class.getCanonicalName();
	}
	
	/**
	 * Updates the primary emotions' gauges
	 * 
	 * @param vector_forwarded_result
	 */
	public void update_emotion_jauge(int[][] vector_forwarded_result) {
		Hashtable<String, Integer> emotion_index_tab = Model.getInstance().getAI().getWorld().getEmotion_index_tab();
		for (Emotion emotion : this.agent.getEmotions()) {
			int value = vector_forwarded_result[0][emotion_index_tab.get(emotion.getName())];
			emotion.setValue(emotion.getValue() + 5 * value);
		}
		// agent.getMemory().emotionsAffectSizeMemory();
	}
	
	// TODO : write this method
	@Override
	public void applyEmotions(List<String> concepts) {
		
	}
	
	// TODO : write this method
	@Override
	public EmotionalState generateEmotions(ArrayList<String> concepts) {
		return null;
	}
}
