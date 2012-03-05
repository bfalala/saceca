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
package fr.n7.saceca.u3du.model.ai.agent.module.emotion;

import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * The Interface EmotionModule.
 * 
 * @author Jérôme Dalbert
 */
public interface EmotionModule extends Storable {
	
	/**
	 * Detect emotions.
	 */
	public void detectEmotions();
	
	/**
	 * Checks if the emotion thread is alive
	 * 
	 * @return thread's state
	 */
	boolean isAlive();
	
	/**
	 * Starts the emotion module's thread
	 */
	void start();
	
}
