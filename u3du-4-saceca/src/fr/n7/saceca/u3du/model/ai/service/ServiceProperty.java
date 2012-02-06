/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * Aurélien Chabot, Anthony Foulfoin, Jérôme Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.service;

import java.util.ArrayList;
import java.util.Iterator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The service property class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
@XStreamAlias("property")
public class ServiceProperty {
	/** The property's name */
	@XStreamAlias("propertyName")
	private String propertyName;
	
	/** The property's parameters */
	@XStreamAlias("propertyParameters")
	private ArrayList<Param> propertyParameters;
	
	/** The order to satisfy the precondition */
	@XStreamAlias("order")
	private char order;
	
	/** The attractivity of the property */
	@XStreamAlias("attractivity")
	private int attractivity;
	
	/** The posible emotion implied */
	@XStreamAlias("possible_emotion")
	private String possible_emotion;
	
	/** The treatment of the precondition */
	@XStreamAlias("treatment_precond")
	private String treatment_precond;
	
	/** The treatment of the effect */
	@XStreamAlias("treatment_effect")
	private String treatment_effect;
	
	/** The constant MAX_ATTRACTIVITY */
	public static final int MAX_ATTRACTIVITY = 20;
	
	/**
	 * The constructor
	 * 
	 * @param name
	 *            the property's name
	 * @param param
	 *            the property's list of parameters
	 * @param order
	 *            the order to satisfy the precondition
	 * @param treatment_precond
	 *            the treatment of the precondition
	 * @param treatment_effect
	 *            the treatment of the effect
	 * @param attractivity
	 *            the attractivity
	 * @param possible_emotion
	 *            the posible implied emotion
	 */
	public ServiceProperty(String name, ArrayList<Param> param, char order, String treatment_precond,
			String treatment_effect, int attractivity, String possible_emotion) {
		this.propertyName = name;
		this.propertyParameters = param;
		this.order = order;
		this.attractivity = attractivity;
		this.treatment_effect = treatment_effect;
		this.treatment_precond = treatment_precond;
		this.possible_emotion = possible_emotion;
	}
	
	/**
	 * The constructor
	 * 
	 * @param name
	 *            the property's name
	 * @param param
	 *            the property's list of parameters
	 */
	public ServiceProperty(String name, ArrayList<Param> param) {
		this(name, param, '0', "", "", 0, "");
	}
	
	/**
	 * The default constructor
	 */
	public ServiceProperty() {
		this.propertyName = "";
		this.propertyParameters = new ArrayList<Param>();
		this.order = 0;
		this.treatment_effect = "";
		this.treatment_precond = "";
	}
	
	/**
	 * Checks if two properties are the same
	 * 
	 * @param propr
	 *            the second property
	 * @return true, if they are the same
	 */
	public boolean egal(ServiceProperty propr) {
		if (!this.propertyName.equals(propr.getPropertyName())) {
			return false;
		}
		if (this.propertyParameters.size() != propr.getPropertyParameters().size()) {
			return false;
		}
		Iterator<Param> it_j = propr.getPropertyParameters().iterator();
		for (Param param : this.propertyParameters) {
			if (!param.egal(it_j.next())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Clones the service property
	 * 
	 * @return the cloned property
	 */
	public ServiceProperty deepDataClone() {
		ServiceProperty clone = new ServiceProperty(this.propertyName, null, this.order, this.treatment_precond,
				this.treatment_effect, this.attractivity, this.possible_emotion);
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		for (Param parameter : this.propertyParameters) {
			paramList.add(parameter.deepDataClone());
		}
		
		clone.setPropertyParameters(paramList);
		
		return clone;
	}
	
	/**
	 * Gets the consclussion from the list of links using the property as a premise
	 * 
	 * @param linksTab
	 *            the list of links
	 * @return
	 */
	public ServiceProperty getConclussion(ArrayList<PropertyLink> linksTab) {
		ServiceProperty conclusion = new ServiceProperty();
		boolean found = false;
		for (int i = 0; i < linksTab.size(); i++) {
			if (linksTab.get(i).getPremise().egal(this)) {
				found = true;
				conclusion = linksTab.get(i).getConclusion().deepDataClone();
				break;
			}
		}
		if (found) {
			for (int i = 0; i < conclusion.getPropertyParameters().size(); i++) {
				Param param = conclusion.getPropertyParameters().get(i);
				
				if (param.getFixed().equals("false")) {
					for (int j = 0; j < this.getPropertyParameters().size(); j++) {
						if (param.getParamName().equals(this.getPropertyParameters().get(j).getParamName())) {
							if (param.getFindValue().equals("")) {
								param.setParamValue(this.getPropertyParameters().get(j).getParamValue());
							} else {
								param.setParamValue(this.findTheValue(this.getPropertyParameters(),
										param.getFindValue()));
							}
						}
					}
				}
			}
			return conclusion;
		}
		return null;
	}
	
	/**
	 * Gets the premise from the list of links using the property as a conclusion
	 * 
	 * @param linkList
	 *            the list of links
	 * @return
	 */
	public ServiceProperty getPremise(ArrayList<PropertyLink> linkList) {
		for (PropertyLink link : linkList) {
			if (link.getConclusion().egal(this)) {
				return link.getPremise().deepDataClone();
			}
		}
		return null;
	}
	
	/**
	 * Finds the parameter's value
	 * 
	 * @param paramList
	 *            the list of parameters
	 * @param findValue
	 *            how to find the parameter's value
	 * @return
	 */
	private String findTheValue(ArrayList<Param> paramList, String findValue) {
		String[] args = findValue.split("__");
		String var1 = args[0], type = "";
		String operator = args[1];
		String var2 = args[2];
		String val1 = "", val2 = "";
		
		for (int i = 0; i < paramList.size(); i++) {
			if (paramList.get(i).getParamName().equals(var1)) {
				val1 = paramList.get(i).getParamValue();
				type = paramList.get(i).getParamType();
			}
			if (paramList.get(i).getParamName().equals(var2)) {
				val2 = paramList.get(i).getParamValue();
				type = paramList.get(i).getParamType();
			}
		}
		
		if (!(var1.equals("") || var2.equals("") || operator.equals(""))) {
			if (operator.equals("+")) {
				if (type.equals("double")) {
					return String.valueOf(Double.parseDouble(val1) + Double.parseDouble(val2));
				} else if (type.equals("int")) {
					return String.valueOf(Integer.parseInt(val1) + Integer.parseInt(val2));
				}
				
			} else if (operator.equals("-")) {
				if (type.equals("double")) {
					return String.valueOf(Double.parseDouble(val1) - Double.parseDouble(val2));
				} else if (type.equals("int")) {
					return String.valueOf(Integer.parseInt(val1) - Integer.parseInt(val2));
				}
			}
		}
		
		return "";
	}
	
	/**
	 * Gets the property's name
	 * 
	 * @return
	 */
	public String getPropertyName() {
		return this.propertyName;
	}
	
	/**
	 * Sets the property's name
	 * 
	 * @param propertyName
	 *            the name
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	/**
	 * Gets the list of parameters
	 * 
	 * @return
	 */
	public ArrayList<Param> getPropertyParameters() {
		return this.propertyParameters;
	}
	
	/**
	 * Sets the list of parameters
	 * 
	 * @param propertyParameters
	 *            the list of parameters
	 */
	public void setPropertyParameters(ArrayList<Param> propertyParameters) {
		this.propertyParameters = propertyParameters;
	}
	
	/**
	 * Gets the order of the property
	 * 
	 * @return
	 */
	public char getOrder() {
		return this.order;
	}
	
	/**
	 * Sets the order of the property
	 * 
	 * @param order
	 *            the order
	 */
	public void setOrder(char order) {
		this.order = order;
	}
	
	/**
	 * Gets the treament precondition
	 * 
	 * @return
	 */
	public String getTreatment_precond() {
		return this.treatment_precond;
	}
	
	/**
	 * Sets the treatment precondtion
	 * 
	 * @param treatment_precond
	 *            the treatment precondtion
	 */
	public void setTreatment_precond(String treatment_precond) {
		this.treatment_precond = treatment_precond;
	}
	
	/**
	 * Gets the treatment effect
	 * 
	 * @return
	 */
	public String getTreatment_effect() {
		return this.treatment_effect;
	}
	
	/**
	 * Sets the treatment effect
	 * 
	 * @param treatment_effect
	 *            the treatment effect
	 */
	public void setTreatment_effect(String treatment_effect) {
		this.treatment_effect = treatment_effect;
	}
	
	/**
	 * Gets a parameter
	 * 
	 * @param index
	 *            the index
	 * @return
	 */
	public Param getParameter(int index) {
		return this.propertyParameters.get(index);
	}
	
	/**
	 * Sets a parameter
	 * 
	 * @param index
	 *            the index
	 * @param parameter
	 *            the new parameter
	 */
	public void setParameter(int index, Param parameter) {
		this.propertyParameters.set(index, parameter);
	}
	
	/**
	 * Sets the last parameter
	 * 
	 * @param parameter
	 *            the new parameter
	 */
	public void setLastParameter(Param parameter) {
		this.setParameter(this.propertyParameters.size() - 1, parameter);
	}
	
	/**
	 * Gets the last parameter
	 * 
	 * @return
	 */
	public Param getLastParameter() {
		return this.getParameter(this.propertyParameters.size() - 1);
	}
	
	/**
	 * Gets a parameter
	 * 
	 * @param name
	 *            the parameter's name
	 * @return
	 */
	public Param getParameter(String name) {
		for (Param parameter : this.propertyParameters) {
			if (parameter.getParamName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}
	
	/**
	 * Sets the attractivity
	 * 
	 * @param attractivity
	 *            the attractivity
	 */
	public void setAttractivity(int attractivity) {
		this.attractivity = attractivity;
	}
	
	/**
	 * Gets the attractivity
	 * 
	 * @return
	 */
	public int getAttractivity() {
		return this.attractivity;
	}
	
	/**
	 * Gets the possible implied emotion
	 * 
	 * @return
	 */
	public String getPossible_emotion() {
		return this.possible_emotion;
	}
	
	/**
	 * Sets the possible implied emotion
	 * 
	 * @param possible_emotion
	 *            the possible implied emotion
	 */
	public void setPossible_emotion(String possible_emotion) {
		this.possible_emotion = possible_emotion;
	}
}
