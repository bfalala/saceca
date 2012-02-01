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
import fr.n7.saceca.u3du.model.ai.statement.Condition;

/**
 * A class to tell an agent that the required service is to be/has been used.
 */
public class CanYouMakeItYesAnswerMessage extends DefaultMessage {
	
	/** The condition. */
	private Condition condition;
	
	/** The object id. */
	private long objectId;
	
	/**
	 * Instantiates a new can you make it yes answer message.
	 * 
	 * @param agent
	 *            the agent
	 * @param condition
	 *            the condition
	 * @param objectId
	 *            the object id
	 */
	public CanYouMakeItYesAnswerMessage(Agent agent, Condition condition, long objectId) {
		super(agent);
		this.condition = condition;
		this.objectId = objectId;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(this.objectId);
		return super.toString() + "Yes, I can make " + this.condition.toString() + " true on " + object.toShortString()
				+ " for you.";
	}
}