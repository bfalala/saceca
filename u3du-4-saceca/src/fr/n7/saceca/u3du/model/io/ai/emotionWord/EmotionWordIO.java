package fr.n7.saceca.u3du.model.io.ai.emotionWord;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.agent.EmotionWord;
import fr.n7.saceca.u3du.model.ai.agent.EmotionWordEffect;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;

public class EmotionWordIO extends XStreamIO<EmotionWord> {
	
	/** Whether XStream has been configured to handle this class. */
	private static boolean configured = false;
	
	/**
	 * Configure if necessary.
	 * 
	 * @param xStream
	 *            the x stream
	 */
	@Override
	protected synchronized void configureIfNecessary(XStream xStream) {
		if (!configured) {
			xStream.processAnnotations(EmotionWord.class);
			xStream.processAnnotations(EmotionWordEffect.class);
			xStream.aliasType("concept", String.class);
			/*
			 * xStream.processAnnotations(DefaultCondition.class);
			 * xStream.processAnnotations(CompoundOperatorAssignmentEffect.class);
			 * xStream.processAnnotations(DefaultEffect.class);
			 * xStream.processAnnotations(U3duExpression.class);
			 * xStream.processAnnotations(Statement.class);
			 * xStream.processAnnotations(StatementsGroup.class);
			 * xStream.processAnnotations(Condition.class); xStream.registerConverter(new
			 * StatementsGroupConverter()); xStream.registerConverter(new CategoryConverter());
			 * xStream.registerConverter(new ClassConverter<Action>(this.actionRepository));
			 */
			configured = true;
		}
	}
	
	/**
	 * Checks if is configured.
	 * 
	 * @return true, if is configured
	 */
	@Override
	protected synchronized boolean isConfigured() {
		return configured;
	}
}
