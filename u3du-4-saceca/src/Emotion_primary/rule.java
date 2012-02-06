package Emotion_primary;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("rule")
public class rule {
	
	private property_rule premise;
	
	private property_rule conclusion;
	
	public rule() {
		this.premise = new property_rule();
		this.conclusion = new property_rule();
	}
	
	public property_rule getPremise() {
		return this.premise;
	}
	
	public void setPremise(property_rule premise) {
		this.premise = premise;
	}
	
	public property_rule getConclusion() {
		return this.conclusion;
	}
	
	public void setConclusion(property_rule conclusion) {
		this.conclusion = conclusion;
	}
}