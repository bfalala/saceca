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
package fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization;

/**
 * The TableClass class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class TableClass {
	/** The name */
	private String name;
	
	/** The value */
	private String value;
	
	/**
	 * Default class constructor
	 */
	public TableClass() {
		
	}
	
	/**
	 * Creates a new TableClass object
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TableClass(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Gets the name
	 * 
	 * @return
	 */
	public String getname() {
		return this.name;
	}
	
	/**
	 * Sets the name
	 * 
	 * @param name
	 *            the name
	 */
	public void setname(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the value
	 * 
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Sets the value
	 * 
	 * @param value
	 *            the value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}