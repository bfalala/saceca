/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.ai.service;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Param class - represent a parameter of a service or of a service property
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
@XStreamAlias("param")
public class Param {
	/** The parameter's name */
	@XStreamAlias("paramName")
	private String paramName;
	
	/** The parameter's value */
	@XStreamAlias("paramValue")
	private String paramValue;
	
	/** The parameter's type */
	@XStreamAlias("paramType")
	private String paramType;
	
	/** The paramter has a fixed value or not */
	@XStreamAlias("fixed")
	private String fixed;
	
	/** How to find the parameter's value */
	@XStreamAlias("findValue")
	private String findValue;
	
	/**
	 * Constructor
	 * 
	 * @param name
	 *            the parameter's anme
	 * @param value
	 *            the parameter's value
	 * @param type
	 *            the parameter's type
	 * @param findValue
	 *            how to find the parameter's value
	 * @param fixed
	 *            if the parameter has a fixed value
	 */
	public Param(String name, String value, String type, String findValue, String fixed) {
		this.paramName = name;
		this.paramValue = value;
		this.paramType = type;
		this.fixed = fixed;
		this.findValue = findValue;
	}
	
	/**
	 * Default constructor
	 */
	public Param() {
		this.paramName = "";
		this.paramType = "string";
		this.paramValue = "";
		this.fixed = "false";
		this.findValue = "";
	}
	
	/**
	 * If two parameters are the same
	 * 
	 * @param param
	 *            the parameter to compare with
	 * @return true, if equal
	 */
	public boolean egal(Param param) {
		if (!this.paramName.equals(param.paramName)) {
			return false;
		}
		if (!this.paramType.equals(param.paramType)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the parameter's name
	 * 
	 * @return
	 */
	public String getParamName() {
		return this.paramName;
	}
	
	/**
	 * Sets the parameter's name
	 * 
	 * @param paramName
	 *            the parameter's name
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	/**
	 * Gets the parameter's value
	 * 
	 * @return
	 */
	public String getParamValue() {
		return this.paramValue;
	}
	
	/**
	 * Sets the parameter's value
	 * 
	 * @param paramValue
	 *            the parameter's value
	 */
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	/**
	 * Gets the parameter's type
	 * 
	 * @return
	 */
	public String getParamType() {
		return this.paramType;
	}
	
	/**
	 * Sets the parameter's type
	 * 
	 * @param paramType
	 *            the parameter's type
	 */
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	/**
	 * Sets how to find the parameter's value
	 * 
	 * @param findValue
	 *            how to find the parameter's value
	 */
	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}
	
	/**
	 * Gets how to find the parameter's value
	 * 
	 * @return
	 */
	public String getFindValue() {
		return this.findValue;
	}
	
	/**
	 * Sets if the parameter has a fixed value
	 * 
	 * @param fixed
	 *            the parameter has a fixed value
	 */
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
	
	/**
	 * Gets if the parameter has a fixed value
	 * 
	 * @return
	 */
	public String getFixed() {
		return this.fixed;
	}
	
	/**
	 * Clone the parameter
	 * 
	 * @return the cloned parameter
	 */
	public Param deepDataClone() {
		return new Param(this.paramName, this.paramValue, this.paramType, this.findValue, this.fixed);
	}
}