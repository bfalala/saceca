package fr.n7.saceca.u3du.model.ai.agent;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("effect")
public class EmotionWordEffect {
	
	@XStreamAlias("precond")
	private String precondition;
	
	@XStreamAlias("emotion")
	private String emotion;
	
	@XStreamAlias("value")
	private String value;
	
	public EmotionWordEffect(String precondition, String emotion, String value) {
		this.precondition = precondition;
		this.emotion = emotion;
		this.value = value;
	}
	
	public String getPrecondition() {
		return this.precondition;
	}
	
	public void setPrecondition(String precondition) {
		this.precondition = precondition;
	}
	
	public String getEmotion() {
		return this.emotion;
	}
	
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
