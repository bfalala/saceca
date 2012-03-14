package fr.n7.saceca.u3du.model.io.ai;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.agent.MarkovEmotion;
import fr.n7.saceca.u3du.model.io.ai.markovEmotion.MarkovEmotionIO;
import fr.n7.saceca.u3du.model.io.common.HighLevelImporter;
import fr.n7.saceca.u3du.model.io.common.XMLRepositoryLoader;

public class MarkovEmotionRepositoryLoader extends XMLRepositoryLoader<MarkovEmotion> {
	
	@Override
	protected String getExtension() {
		return Constants.MARKOV_EMOTION_EXTENSION;
	}
	
	@Override
	protected HighLevelImporter<MarkovEmotion> getImporter() {
		return new MarkovEmotionIO();
	}
	
	@Override
	protected String getRepositoryName() {
		return MarkovEmotion.class.getSimpleName() + " Repository";
	}
	
}
