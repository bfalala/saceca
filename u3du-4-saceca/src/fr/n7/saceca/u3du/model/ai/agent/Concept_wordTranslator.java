package fr.n7.saceca.u3du.model.ai.agent;

import java.util.HashMap;
import java.util.Map;

import fr.n7.saceca.u3du.model.util.io.storage.Repository;

public class Concept_wordTranslator {
	
	public static final String EMOTIONS_CONCEPTS = "data\\ai\\emotions_concepts.xml";
	
	private Map<String, EmotionWord> concepts_emotionWords;
	private Map<String, String> concepts_emotions;
	
	public Concept_wordTranslator(Repository<MarkovEmotion> emoRepository, Repository<EmotionWord> emowRepository) {
		this.concepts_emotionWords = new HashMap<String, EmotionWord>();
		this.concepts_emotions = new HashMap<String, String>();
		
		this.setEmotionWords(emowRepository);
		this.setEmotions(emoRepository);
		
	}
	
	private void setEmotionWords(Repository<EmotionWord> emowFolder) {
		
		for (EmotionWord emow : emowFolder) {
			for (String concept : emow.getConcepts()) {
				this.concepts_emotionWords.put(concept, emow);
			}
		}
	}
	
	private void setEmotions(Repository<MarkovEmotion> emoFolder) {
		for (MarkovEmotion emo : emoFolder) {
			for (String synonym : emo.getSynonyms()) {
				this.concepts_emotions.put(synonym, emo.getStorageLabel());
			}
		}
	}
	
}
