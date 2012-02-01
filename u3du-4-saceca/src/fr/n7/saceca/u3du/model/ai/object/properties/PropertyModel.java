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

import fr.n7.saceca.u3du.model.ai.Visibility;

/**
 * The Interface for models of properties.
 * 
 * @param <T>
 *            The type of the parameter.
 * @author Sylvain Cambon
 */
public interface PropertyModel<T> {
	
	/**
	 * Gets the default value.
	 * 
	 * @return the default value
	 */
	public T getDefaultValue();
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the type, even at runtime.
	 * 
	 * @return the type.
	 */
	public Class<T> getType();
	
	/**
	 * Tells whether the value is correct or not.
	 * 
	 * @param newValue
	 *            The value to check.
	 * @return true, if the value is correct.
	 */
	public boolean isAcceptable(T newValue);
	
	/**
	 * Gets the visibility.
	 * 
	 * @return the visibility
	 */
	public Visibility getVisibility();
	
}
