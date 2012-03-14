package fr.n7.saceca.u3du.model.ai.agent;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("effect")
public class EmotionWordEffect {
	
	@XStreamAlias("precond")
	private String precondition ;
	
	@XStreamAlias("emotion")
	private String emotion ;
		
	
	@XStreamAlias("value")
	private String value ;


	public EmotionWordEffect(String precondition, String emotion, String value) {		
		this.precondition = precondition;
		this.emotion = emotion;
		this.value = value;
	}

	

}
