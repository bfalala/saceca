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
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * The Action Interface is designed for inserting Java code into services, between conditions
 * checking and execution of the results. This allows to send animations for instance.
 * 
 * @author JÃ©rÃ´me Dalbert, Bertrand Deguelle
 */
public interface Action extends Storable {
	
	/**
	 * Executes a step of the action.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @return the status after the execution.
	 */
	public ExecutionStatus executeStep(WorldObject provider, WorldObject consumer, Map<String, Object> parameters);
	
	/**
	 * Gets the duration of the action.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @param anticipation
	 *            In the case of anticipation, agents don't take in count their level gauge
	 * @return the duration
	 * @throws UnknownPropertyException
	 *             If the duration property was not found.
	 */
	public int getDuration(WorldObject provider, WorldObject consumer, Map<String, Object> parameters,
			Boolean anticipation) throws UnknownPropertyException;
	
}