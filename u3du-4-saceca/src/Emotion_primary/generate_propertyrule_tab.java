package Emotion_primary;

import java.io.FileInputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class generate_propertyrule_tab {
	
	private ArrayList<rule> ruleTab;
	private ArrayList<property_rule> property_rule_Tab;
	private int index_Fear = 0, index_Angry = 0, index_Sadness = 0, index_Joy = 0, index_Surprise = 0,
			index_Disgust = 0;
	
	public int getIndex_Fear() {
		return this.index_Fear;
	}
	
	public void setIndex_Fear(int index_Fear) {
		this.index_Fear = index_Fear;
	}
	
	public int getIndex_Angry() {
		return this.index_Angry;
	}
	
	public void setIndex_Angry(int index_Angry) {
		this.index_Angry = index_Angry;
	}
	
	public int getIndex_Sadness() {
		return this.index_Sadness;
	}
	
	public void setIndex_Sadness(int index_Sadness) {
		this.index_Sadness = index_Sadness;
	}
	
	public int getIndex_Joy() {
		return this.index_Joy;
	}
	
	public void setIndex_Joy(int index_Joy) {
		this.index_Joy = index_Joy;
	}
	
	public int getIndex_Surprise() {
		return this.index_Surprise;
	}
	
	public void setIndex_Surprise(int index_Surprise) {
		this.index_Surprise = index_Surprise;
	}
	
	public int getIndex_Disgust() {
		return this.index_Disgust;
	}
	
	public void setIndex_Disgust(int index_Disgust) {
		this.index_Disgust = index_Disgust;
	}
	
	@SuppressWarnings("unchecked")
	public generate_propertyrule_tab(String knowlagefile) {
		this.ruleTab = new ArrayList<rule>();
		this.property_rule_Tab = new ArrayList<property_rule>();
		XStream xstr = new XStream(new DomDriver());
		
		try {
			
			/*
			 * rule r = new rule(); r.setPremise(new property_rule()); r.setConclusion(new
			 * property_rule());
			 * 
			 * propertyruleTab.add(r);
			 * 
			 * FileOutputStream fileOutStr = new FileOutputStream("k.xml"); //xstr.alias("actions",
			 * propertyruleTab.getClass()); xstr.toXML(propertyruleTab, fileOutStr);
			 */

			FileInputStream fileStr = new FileInputStream(knowlagefile);
			// xstr.alias("rules",ArrayList.class);
			// xstr.alias("premise",property_rule.class);
			
			// xstr.alias("conclusion",property_rule.class);
			
			xstr.aliasType("rule", rule.class);
			xstr.aliasType("Name", String.class);
			xstr.aliasType("premise", property_rule.class);
			xstr.aliasType("conclusion", property_rule.class);
			
			xstr.alias("rule", rule.class);
			xstr.alias("Name", String.class);
			xstr.alias("premise", property_rule.class);
			xstr.alias("conclusion", property_rule.class);
			
			this.ruleTab = (ArrayList<rule>) xstr.fromXML(fileStr);
			
			// FileOutputStream fileStr = new FileOutputStream(knowlagefile);
			// // xstr.alias("rules",ArrayList.class);
			// // xstr.alias("premise",property_rule.class);
			//
			// // xstr.alias("conclusion",property_rule.class);
			//
			// xstr.aliasType("list", ArrayList.class);
			// xstr.aliasType("rule", rule.class);
			// xstr.alias("list", ArrayList.class);
			// xstr.alias("rule", rule.class);
			//
			// ArrayList<rule> list = new ArrayList<rule>();
			// list.add(new rule(new property_rule("psada"), new property_rule("dsfds")));
			// list.add(new rule(new property_rule("psada"), new property_rule("dsfds")));
			//
			// xstr.toXML(list, fileStr);
			
			this.property_rule_Tab = this.getPropertiesFromRuleTab(this.ruleTab);
			// System.out.print("num actions: " + actionsTab.size() + " num properties: " +
			// propertiesTab.size());
			System.out.println("\n\n tabs of rules is : \n");
			for (rule rule1 : this.ruleTab) {
				
				System.out.print(rule1.getPremise().getName() + "  ---->  ");
				System.out.print(rule1.getConclusion().getName() + " ");
				System.out.println();
			}
			System.out.println("\n\n tabs of property_rules is : \n");
			
			for (property_rule property_rule1 : this.property_rule_Tab) {
				
				System.out.print(property_rule1.getName() + "  ");
				
			}
			
		} catch (Exception e) {
			// System.out.println("\n\n ici4 : \n");
		}
	}
	
	public ArrayList<property_rule> getPropertiesFromRuleTab(ArrayList<rule> tabrule) {
		int i = 0;
		ArrayList<property_rule> rulepropertyList = new ArrayList<property_rule>();
		
		for (rule rule1 : tabrule) {
			if (!this.existPropertyrule(rule1.getPremise(), rulepropertyList)) {
				
				rulepropertyList.add(rule1.getPremise());
				if (rule1.getPremise().getName().equals("Fear")) {
					this.index_Fear = i;
				}
				if (rule1.getPremise().getName().equals("Angry")) {
					this.index_Angry = i;
				}
				if (rule1.getPremise().getName().equals("Sadness")) {
					this.index_Sadness = i;
				}
				if (rule1.getPremise().getName().equals("Joy")) {
					this.index_Joy = i;
				}
				if (rule1.getPremise().getName().equals("Surprise")) {
					this.index_Surprise = i;
				}
				if (rule1.getPremise().getName().equals("Disgust")) {
					this.index_Disgust = i;
				}
				i++;
			}
			if (!this.existPropertyrule(rule1.getConclusion(), rulepropertyList)) {
				
				rulepropertyList.add(rule1.getConclusion());
				if (rule1.getConclusion().getName().equals("Fear")) {
					this.index_Fear = i;
				}
				if (rule1.getConclusion().getName().equals("Angry")) {
					this.index_Angry = i;
				}
				if (rule1.getConclusion().getName().equals("Sadness")) {
					this.index_Sadness = i;
				}
				if (rule1.getConclusion().getName().equals("Joy")) {
					this.index_Joy = i;
				}
				if (rule1.getConclusion().getName().equals("Surprise")) {
					this.index_Surprise = i;
				}
				if (rule1.getConclusion().getName().equals("Disgust")) {
					this.index_Disgust = i;
				}
				i++;
			}
			
		}
		return rulepropertyList;
		
	}
	
	private boolean existPropertyrule(property_rule propr, ArrayList<property_rule> ruleproprList) {
		for (property_rule propr_old : ruleproprList) {
			if (propr.getName().equals(propr_old.getName())) {
				
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<rule> getRuleTab() {
		return this.ruleTab;
	}
	
	public void setRuleTab(ArrayList<rule> ruleTab) {
		this.ruleTab = ruleTab;
	}
	
	public ArrayList<property_rule> getProperty_rule_Tab() {
		return this.property_rule_Tab;
	}
	
	public void setProperty_rule_Tab(ArrayList<property_rule> property_rule_Tab) {
		this.property_rule_Tab = property_rule_Tab;
	}
}