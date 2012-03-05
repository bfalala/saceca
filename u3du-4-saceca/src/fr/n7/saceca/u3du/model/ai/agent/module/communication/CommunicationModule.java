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
package fr.n7.saceca.u3du.model.ai.agent.module.communication;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * The Interface CommunicationModule.
 * 
 * @author Sylvain Cambon & Jérôme Dalbert
 */
public interface CommunicationModule extends Storable {
	
	/**
	 * Communicate.
	 */
	public void communicate();
	
	/**
	 * Adds a message to the message inbox.
	 * 
	 * @param msg
	 *            the msg
	 */
	public void addMessage(Message msg);
	
	/**
	 * Gets the agent.
	 * 
	 * @return the agent
	 */
	public Agent getAgent();
	
	/**
	 * Checks if the communication thread is alive
	 * 
	 * @return thread's state
	 */
	boolean isAlive();
	
	/**
	 * Starts the communication module's thread
	 */
	void start();
}
