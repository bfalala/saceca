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
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;

/**
 * This class gather the common code for making an agent disappear in a building and reappear a
 * while after.
 * 
 * @author Sylvain Cambon
 */
public class Wait implements Action {
	
	/** The initialized. */
	protected boolean initialized;
	/** The expected duration. */
	private int expectedEnd;
	
	/**
	 * Instantiates a new do something in a building action.
	 */
	public Wait() {
		super();
		this.initialized = false;
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
		if (!this.initialized) {
			// This is the first call
			
			this.expectedEnd = 5 + Model.getInstance().getAI().getSimulation().getTime();
			
			while (Model.getInstance().getAI().getSimulation().getTime() < this.expectedEnd) {
			}
			// Send a disappearance animation to simulate doing something in a building
			// Model.getInstance().getGraphics().sendAnimation(new
			// Jump_Animation(consumer.getId()));
			// Model.getInstance().getGraphics().sendAnimation(new
			// DisappearAnimation(consumer.getId()));
			
			this.initialized = true;
		}
		
		if (Model.getInstance().getAI().getSimulation().getTime() >= this.expectedEnd) {
			// The agent comes back
			
			// Model.getInstance().getGraphics().sendAnimation(new
			// AppearAnimation(consumer.getId()));
			return ExecutionStatus.SUCCESSFUL_TERMINATION;
		}
		
		return ExecutionStatus.CONTINUE_NEXT_TIME;
	}
	
	@Override
	public int getDuration(WorldObject provider, WorldObject consumer, Map<String, Object> parameters)
			throws UnknownPropertyException {
		// TODO Auto-generated method stub
		return 0;
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