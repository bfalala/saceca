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
package fr.n7.saceca.u3du.model.ai.service.action;

import java.util.Map;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

/**
 * A class to simulate eating in a restaurant. This class is the code linked to the service "eatAt".
 * 
 * @author Sylvain Cambon
 */
public class Eat_home extends DoSomethingInABuildingAction {
	
	/**
	 * Gets the duration of the meal.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param params
	 *            the params
	 * @return the duration
	 * @throws UnknownPropertyException
	 *             the unknown property exception
	 */
	@Override
	protected int getDuration(WorldObject provider, WorldObject consumer, Map<String, Object> params)
			throws UnknownPropertyException {
		return provider.getPropertiesContainer().getInt("c_Building_mealDuration");
	}
}