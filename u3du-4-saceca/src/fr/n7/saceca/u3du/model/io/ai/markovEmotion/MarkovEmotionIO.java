package fr.n7.saceca.u3du.model.io.ai.markovEmotion;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.agent.MarkovEmotion;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;

public class MarkovEmotionIO extends XStreamIO<MarkovEmotion> {
	
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
			xStream.processAnnotations(MarkovEmotion.class);
			xStream.aliasType("concept", String.class);
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
