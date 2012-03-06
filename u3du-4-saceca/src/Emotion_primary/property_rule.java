package Emotion_primary;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class property_rule {
	@XStreamAlias("Name")
	private String Name;
	
	public String getName() {
		return this.Name;
	}
	
	public void setName(String name) {
		this.Name = name;
	}
	
	public property_rule() {
		this.Name = "";
	}
	
	public property_rule(String name) {
		this.Name = name;
	}
}