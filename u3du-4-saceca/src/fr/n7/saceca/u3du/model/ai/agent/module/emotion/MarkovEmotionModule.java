package fr.n7.saceca.u3du.model.ai.agent.module.emotion;

import java.util.ArrayList;
import java.util.Map;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Concept_wordTranslator;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.MarkovMatrix;
import fr.n7.saceca.u3du.model.ai.agent.behavior.DefaultAgentBehavior;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.util.Couple;

public class MarkovEmotionModule implements EmotionModule {
	
	public static final String MODELS_PATH = "data/ai";
	private static final MarkovMaterials materials = new MarkovMaterials();
	
	private MarkovMatrix matrix;
	private Concept_wordTranslator translator;
	
	public MarkovEmotionModule(Agent agent) {
		super();
		this.agent = agent;
		this.matrix = materials.getMatrix();
		this.translator = materials.getTranslator();
	}
	
	/**
	 * The Class EmotionThread.
	 */
	private class EmotionThread extends Thread {
		
		/**
		 * Instantiates a new emotion thread and names it according to the owning object.
		 */
		public EmotionThread() {
			super();
			this.setName(MarkovEmotionModule.this.agent.toShortString() + "Emotion thread");
		}
		
		/**
		 * This method represents the "emotion loop"
		 */
		@Override
		public void run() {
			while (MarkovEmotionModule.this.agent.isAlive() && !MarkovEmotionModule.this.agent.isPause()) {
				MarkovEmotionModule.this.detectEmotions();
				try {
					Thread.sleep(DefaultAgentBehavior.BEHAVE_PERIOD / 2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/** The emotion thread */
	private EmotionThread emotionThread;
	
	/** The owning agent */
	private Agent agent;
	
	/**
	 * Checks if the emotion thread is alive
	 */
	@Override
	public boolean isAlive() {
		return this.emotionThread != null && this.emotionThread.isAlive();
	}
	
	private void detectOldObjectsEmotions() {
		// for the old objects, we just maintain or lightly decrement the emotion
		
		ArrayList<String> vision = new ArrayList<String>();
		for (Couple<WorldObject, Boolean> newCouple : (ArrayList<Couple<WorldObject, Boolean>>) this.agent.getMemory()
				.getOldPerceivedObjects().clone()) {
			
			if (newCouple != null) {
				vision.addAll(newCouple.getFirstElement().getConcepts());
			}
			
		}
		// vision.add("car_1");
		// if none, nothing to do
		if (vision == null || vision.size() == 0) {
			return;
		}
		
		Map<String, Float> emotionsScores = this.matrix.generateEmotions(vision);
		Map<String, Float> emowScores = this.matrix.generateEmotionWords(vision);
		
		for (Emotion e : this.agent.getEmotions()) {
			String emotionName = e.getModel().getName().split("_")[2];
			
			if ((emotionsScores.get(emotionName) != null) && (emotionsScores.get(emotionName) != 0f)) {
				if (emotionName.equals("disgust")) {
					System.out.println("deg");
				}
				e.setValue(e.getValue() + 2);
			}
			
		}
	}
	
	private void detectNewObjectsEmotions() {
		// we get the name of perceived objects
		ArrayList<String> vision = new ArrayList<String>();
		for (Couple<WorldObject, Boolean> newCouple : (ArrayList<Couple<WorldObject, Boolean>>) this.agent.getMemory()
				.getNewlyPerceivedObjects().clone()) {
			
			if (newCouple != null) {
				vision.addAll(newCouple.getFirstElement().getConcepts());
			}
			
		}
		// vision.add("car_1");
		// if none, nothing to do
		if (vision == null || vision.size() == 0) {
			return;
		}
		
		Map<String, Float> emotionsScores = this.matrix.generateEmotions(vision);
		Map<String, Float> emowScores = this.matrix.generateEmotionWords(vision);
		
		for (Emotion e : this.agent.getEmotions()) {
			String emotionName = e.getModel().getName().split("_")[2];
			if (emotionsScores.get(emotionName) != null) {
				float score = emotionsScores.get(emotionName);
				if (score != 0f) {
					float effect;
					if (3 * score > 25) {
						effect = 25;
					} else {
						effect = 3 * score;
					}
					e.setValue(e.getValue() + effect);
				}
			}
			
		}
	}
	
	/**
	 * Detect emotions.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void detectEmotions() {
		this.detectOldObjectsEmotions();
		this.detectNewObjectsEmotions();
		
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
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return EmotionModule.class.getCanonicalName();
	}
	
}
