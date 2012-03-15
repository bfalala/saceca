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

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.graphics.animation.AppearAnimation;
import fr.n7.saceca.u3du.model.graphics.animation.DisappearAnimation;

/**
 * This class gather the common code for making an agent disappear in a building and reappear a
 * while after.
 * 
 * @author Sylvain Cambon, Bertrand Deguelle
 */
public abstract class DoSomethingInABuildingAction implements Action {
	
	/** The initialized. */
	protected boolean initialized;
	/** The expected duration. */
	private int expectedEnd;
	/** The Constant DEFAULT_TICK_PERIOD, in ms. */
	public static final long DEFAULT_TICK_PERIOD = 10;
	
	/** The Constant DAY_MINUTES. */
	private static final int DAY_MINUTES = 24 * 60;
	
	/**
	 * Instantiates a new do something in a building action.
	 */
	public DoSomethingInABuildingAction() {
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
		int currentTime = Model.getInstance().getAI().getSimulation().getTime();
		if (!this.initialized) {
			try {
				this.expectedEnd = this.getDuration(provider, consumer, parameters, false) + currentTime;
			} catch (UnknownPropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Send a disappearance animation to simulate doing something in a building
			// Model.getInstance().getGraphics().sendAnimation(new
			// Jump_Animation(consumer.getId()));
			Model.getInstance().getGraphics().sendAnimation(new DisappearAnimation(consumer.getId()));
			
			this.initialized = true;
		}
		
		// Gestion du modulo des 24h (1440 minutes)
		
		if ((this.expectedEnd < DAY_MINUTES && currentTime >= this.expectedEnd)
				|| (this.expectedEnd >= DAY_MINUTES && currentTime >= this.expectedEnd % DAY_MINUTES && currentTime <= DAY_MINUTES / 2)) {
			// The agent comes back
			
			Model.getInstance().getGraphics().sendAnimation(new AppearAnimation(consumer.getId()));
			return ExecutionStatus.SUCCESSFUL_TERMINATION;
		}
		
		return ExecutionStatus.CONTINUE_NEXT_TIME;
	}
	
	@Override
	public int getDuration(WorldObject provider, WorldObject consumer, Map<String, Object> parameters,
			Boolean anticipation) throws UnknownPropertyException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}