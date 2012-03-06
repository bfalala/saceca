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
package fr.n7.saceca.u3du.model.ai.object.properties;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.ai.Visibility;

/**
 * A class representing an integer property model.
 */
@XStreamAlias("int")
public class IntegerPropertyModel extends AbstractNumericPropertyModel<Integer> {
	
	/**
	 * Instantiates a new integer property model.
	 * 
	 * @param name
	 *            the name
	 * @param minValue
	 *            the minimum value
	 * @param maxValue
	 *            the maximum value
	 * @param defaultValue
	 *            the default value
	 * @param visibility
	 *            the visibility
	 */
	public IntegerPropertyModel(String name, Integer minValue, Integer maxValue, Integer defaultValue,
			Visibility visibility) {
		super(name, minValue, maxValue, defaultValue, visibility);
	}
	
}