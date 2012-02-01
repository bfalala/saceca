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
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * A class to tell where an object is.
 */
public class WhereIsItAnswerMessage extends DefaultMessage {
	
	/** The id. */
	private long id;
	
	/** The position. */
	private Oriented2DPosition position;
	
	/**
	 * Instantiates a new where is it answer message.
	 * 
	 * @param agent
	 *            the agent
	 * @param id
	 *            the id
	 * @param position
	 *            the position
	 */
	public WhereIsItAnswerMessage(Agent agent, long id, Oriented2DPosition position) {
		super(agent);
		this.id = id;
		this.position = position;
	}
	
	/**
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public Oriented2DPosition getPosition() {
		return this.position;
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
		return super.toString() + "Yes, " + object.toShortString() + " is at " + this.position.toString() + ".";
	}
}
