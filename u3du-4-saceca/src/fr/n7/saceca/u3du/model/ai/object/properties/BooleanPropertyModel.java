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
package fr.n7.saceca.u3du.model.ai.object.properties;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.ai.Visibility;

/**
 * A class modeling a boolean property.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("bool")
public final class BooleanPropertyModel extends AbstractPropertyModel<Boolean> {
	
	/**
	 * Instantiates a new boolean property model.
	 * 
	 * @param name
	 *            The name.
	 * @param defaultValue
	 *            The default value.
	 * @param visibility
	 *            The visibility.
	 */
	public BooleanPropertyModel(String name, Boolean defaultValue, Visibility visibility) {
		super(name, defaultValue, visibility);
	}
	
	@Override
	public String toString() {
		return "BooleanPropertyModel[" + this.getName() + ": defaultValue=" + this.getDefaultValue() + "]";
	}
	
}
