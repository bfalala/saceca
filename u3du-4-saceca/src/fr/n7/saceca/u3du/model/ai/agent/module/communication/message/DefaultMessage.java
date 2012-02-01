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
package fr.n7.saceca.u3du.model.ai.agent.module.communication.message;

import fr.n7.saceca.u3du.model.ai.agent.Agent;

/**
 * A class handling the common Sender presence constraint that should be extended by real messages.
 */
public abstract class DefaultMessage implements Message {
	
	/** The sender. */
	private Agent sender;
	
	/**
	 * Instantiates a new default message.
	 * 
	 * @param sender
	 *            the sender
	 */
	public DefaultMessage(Agent sender) {
		super();
		this.sender = sender;
	}
	
	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	@Override
	public final Agent getSender() {
		return this.sender;
	}
	
	/**
	 * Text to be added as a prefix to messages, which has the form:<br/>
	 * <code>JohnModel (id=42) John: </code>
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.getSender().toShortString() + ": ";
	}
}
