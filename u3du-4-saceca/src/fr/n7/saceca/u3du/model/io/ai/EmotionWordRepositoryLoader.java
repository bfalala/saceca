package fr.n7.saceca.u3du.model.io.ai;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.agent.EmotionWord;
import fr.n7.saceca.u3du.model.io.ai.emotionWord.EmotionWordIO;
import fr.n7.saceca.u3du.model.io.common.HighLevelImporter;
import fr.n7.saceca.u3du.model.io.common.XMLRepositoryLoader;

public class EmotionWordRepositoryLoader extends XMLRepositoryLoader<EmotionWord>{
	
	@Override
	protected String getExtension() {
		return Constants.EMOTION_WORD_EXTENSION;
	}
	
	@Override
	protected HighLevelImporter<EmotionWord> getImporter() {
		return new EmotionWordIO();
	}
	
	@Override
	protected String getRepositoryName() {
		return EmotionWord.class.getSimpleName() + " Repository";
	}
	
}
