package fr.n7.saceca.u3du.model.ai.agent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.model.util.io.storage.Storable;

@XStreamAlias("emotion")
public class MarkovEmotion implements Storable {
	
	/** The logger. */
	@XStreamOmitField
	private static Logger logger = Logger.getLogger(EmotionWord.class);
	
	/** The name. */
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;
	
	/** The service's parameters */
	@XStreamAlias("synonyms")
	private ArrayList<String> synonyms;
	
	/** The concepts equivalent to this word */
	@XStreamAlias("opposites")
	private ArrayList<String> opposites;
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.name;
	}
	
	public ArrayList<String> getSynonyms() {
		return this.synonyms;
	}
	
	public ArrayList<String> getOpposites() {
		return this.opposites;
	}
	
	public MarkovEmotion(String name, ArrayList<String> synonyms, ArrayList<String> opposites) {
		this.name = name;
		this.synonyms = synonyms;
		this.opposites = opposites;
	}
	
}
