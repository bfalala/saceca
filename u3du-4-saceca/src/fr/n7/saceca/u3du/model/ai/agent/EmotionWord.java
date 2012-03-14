package fr.n7.saceca.u3du.model.ai.agent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.model.util.io.storage.Storable;

@XStreamAlias("emotion-word")
public class EmotionWord implements Storable {
	/** The logger. */
	@XStreamOmitField
	private static Logger logger = Logger.getLogger(EmotionWord.class);
	
	/** The name. */
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;
	
	/** The service's parameters */
	@XStreamAlias("effects")
	private ArrayList<EmotionWordEffect> effects;
	
	/** The concepts equivalent to this word */
	@XStreamAlias("concepts")
	private ArrayList<String> concepts;
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.name;
	}
	
	public ArrayList<EmotionWordEffect> getEffects() {
		return this.effects;
	}
	
	public void setEffects(ArrayList<EmotionWordEffect> effects) {
		this.effects = effects;
	}
	
	public ArrayList<String> getConcepts() {
		return this.concepts;
	}
	
	public void setConcepts(ArrayList<String> concepts) {
		this.concepts = concepts;
	}
	
	public EmotionWord(String name, ArrayList<EmotionWordEffect> effects, ArrayList<String> concepts) {
		this.name = name;
		this.effects = effects;
		this.concepts = concepts;
	}
	
}
