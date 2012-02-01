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
 * A class to ask about an object location.
 */
public class WhereIsItQuestionMessage extends DefaultMessage {
	
	/** The object. */
	private long objectId;
	
	/**
	 * Instantiates a new object location question message.
	 * 
	 * @param sender
	 *            the sender
	 * @param objectId
	 *            the object id
	 */
	public WhereIsItQuestionMessage(Agent sender, long objectId) {
		super(sender);
		this.objectId = objectId;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return this.objectId;
	}
	
	@Override
	public String toString() {
		WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(this.objectId);
		return super.toString() + "Do you know where " + object.toShortString() + " is?";
	}
	
}
