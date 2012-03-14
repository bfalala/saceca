package fr.n7.saceca.u3du.model.ai.agent.module.emotion;

import java.io.File;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.agent.Concept_wordTranslator;
import fr.n7.saceca.u3du.model.ai.agent.EmotionWord;
import fr.n7.saceca.u3du.model.ai.agent.MarkovEmotion;
import fr.n7.saceca.u3du.model.ai.agent.MarkovMatrix;
import fr.n7.saceca.u3du.model.io.ai.EmotionWordRepositoryLoader;
import fr.n7.saceca.u3du.model.io.ai.MarkovEmotionRepositoryLoader;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;

/**
 * The class which contains the necessary material to run the markov method
 * 
 * */
public class MarkovMaterials {
	
	private MarkovMatrix matrix;
	private Concept_wordTranslator translator;
	
	/** The emotion words repository. */
	private Repository<MarkovEmotion> markovEmotionRepository;
	
	/** The emotion words repository. */
	private Repository<EmotionWord> emotionWordRepository;
	
	public MarkovMaterials() {
		final char separator = File.separatorChar;
		final String path = MarkovEmotionModule.MODELS_PATH;
		
		MarkovEmotionRepositoryLoader emoLoader = new MarkovEmotionRepositoryLoader();
		this.markovEmotionRepository = emoLoader.loadFilesToRepository(path + separator
				+ Constants.MARKOV_EMOTIONS_FOLDER_NAME);
		EmotionWordRepositoryLoader emowLoader = new EmotionWordRepositoryLoader();
		this.emotionWordRepository = emowLoader.loadFilesToRepository(path + separator
				+ Constants.EMOTION_WORDS_FOLDER_NAME);
		
		this.translator = new Concept_wordTranslator(this.markovEmotionRepository, this.emotionWordRepository);
		this.matrix = new MarkovMatrix(this.markovEmotionRepository, this.emotionWordRepository);
	}
	
	public MarkovMatrix getMatrix() {
		return this.matrix;
	}
	
	public void setMatrix(MarkovMatrix matrix) {
		this.matrix = matrix;
	}
	
	public Concept_wordTranslator getTranslator() {
		return this.translator;
	}
	
	public void setTranslator(Concept_wordTranslator translator) {
		this.translator = translator;
	}
	
	public Repository<MarkovEmotion> getMarkovEmotionRepository() {
		return this.markovEmotionRepository;
	}
	
	public void setMarkovEmotionRepository(Repository<MarkovEmotion> markovEmotionRepository) {
		this.markovEmotionRepository = markovEmotionRepository;
	}
	
	public Repository<EmotionWord> getEmotionWordRepository() {
		return this.emotionWordRepository;
	}
	
	public void setEmotionWordRepository(Repository<EmotionWord> emotionWordRepository) {
		this.emotionWordRepository = emotionWordRepository;
	}
	
}
