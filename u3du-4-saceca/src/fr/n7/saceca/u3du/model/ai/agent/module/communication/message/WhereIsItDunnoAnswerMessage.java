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

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * A class to show ignorance about where an object is.
 */
public class WhereIsItDunnoAnswerMessage extends DefaultMessage {
	
	/** The id. */
	private long id;
	
	/**
	 * Instantiates a new where is it dunno answer message.
	 * 
	 * @param agent
	 *            the agent
	 * @param id
	 *            the id
	 */
	public WhereIsItDunnoAnswerMessage(Agent agent, long id) {
		super(agent);
		this.id = id;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(this.id);
		return super.toString() + "Sorry, I do not know where " + object.toShortString() + " may be.";
	}
}
