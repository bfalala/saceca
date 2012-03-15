package fr.n7.saceca.u3du.model.ai.agent;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** A snapshot of all the emotions at a precise moment */

@XStreamAlias("emotionalState")
public class EmotionalState {
	
	/** the map emotion name/value */
	@XStreamAlias("emotions")
	private Map<String, Float> emotions;
	
	public Map<String, Float> getEmotions() {
		return this.emotions;
	}
	
	public void setEmotions(Map<String, Float> emotions) {
		this.emotions = emotions;
	}
	
	public EmotionalState() {
		this.emotions = new HashMap<String, Float>();
	}
	
	public void put(String emotionName, float value) {
		this.emotions.put(emotionName, value);
	}
	
	/** compute the gain this - other */
	public float computeGain(EmotionalState other) {
		float gain = 0;
		for (String emotionName : this.emotions.keySet()) {
			gain += this.emotions.get(emotionName) - other.emotions.get(emotionName);
		}
		
		return gain;
	}
	
}
