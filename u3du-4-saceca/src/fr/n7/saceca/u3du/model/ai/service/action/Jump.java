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
package fr.n7.saceca.u3du.model.ai.service.action;

import java.util.Map;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.graphics.animation.Jump_Animation;

/**
 * This class gather the common code for making an agent disappear in a building and reappear a
 * while after.
 * 
 * @author Sylvain Cambon
 */
public class Jump implements Action {
	
	/** The initialized. */
	protected boolean initialized;
	/** The expected duration. */
	private int expectedEnd;
	
	/**
	 * Instantiates a new do something in a building action.
	 */
	public Jump() {
		super();
		
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.getClass().getCanonicalName();
	}
	
	/**
	 * Executes a step.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @return the execution status
	 */
	@Override
	public ExecutionStatus executeStep(WorldObject provider, WorldObject consumer, Map<String, Object> parameters) {
		// This is the first call
		/*
		 * try { this.expectedEnd = this.getDuration(provider, consumer, parameters) +
		 * Model.getInstance().getAI().getSimulation().getTime(); } catch (UnknownPropertyException
		 * e) { Log.debug("A duration property related to the Action  \"" + this.getStorageLabel() +
		 * "\" is missing."); return ExecutionStatus.FAILURE; }
		 */
		// Send a jump animation
		Model.getInstance().getGraphics().sendAnimation(new Jump_Animation(consumer.getId()));
		
		// this.initialized = true;
		
		return ExecutionStatus.CONTINUE_NEXT_TIME;
	}
	
	/**
	 * Gets the duration of the action.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @return the duration
	 * @throws UnknownPropertyException
	 *             If the duration property was not found.
	 */
	
}