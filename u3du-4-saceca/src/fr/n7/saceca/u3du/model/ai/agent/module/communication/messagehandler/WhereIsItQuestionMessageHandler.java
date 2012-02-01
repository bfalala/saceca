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
package fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler;

import fr.n7.saceca.u3du.model.ai.agent.memory.MemoryElement;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.CommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.WhereIsItAnswerMessage;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.WhereIsItDunnoAnswerMessage;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.WhereIsItQuestionMessage;

/**
 * A class to handle a question about an object location.
 */
public class WhereIsItQuestionMessageHandler implements MessageHandler {
	
	/** The comm mod. */
	private CommunicationModule commMod;
	
	/**
	 * Instantiates a new object location question message handler.
	 * 
	 * @param commModule
	 *            the comm module
	 */
	public WhereIsItQuestionMessageHandler(CommunicationModule commModule) {
		super();
		this.commMod = commModule;
	}
	
	/**
	 * Gets the handled message type.
	 * 
	 * @return the handled message type
	 */
	@Override
	public Class<WhereIsItQuestionMessage> getHandledMessageType() {
		return WhereIsItQuestionMessage.class;
	}
	
	/**
	 * Handle.
	 * 
	 * @param msg
	 *            the msg
	 * @return the message
	 */
	@Override
	public Message handle(Message msg) {
		WhereIsItQuestionMessage olqMsg = (WhereIsItQuestionMessage) msg;
		MemoryElement element = this.commMod.getAgent().getMemory().getMemoryElements().get(olqMsg.getId());
		if (element == null || element.getWorldObject().getPosition() == null) {
			return new WhereIsItDunnoAnswerMessage(this.commMod.getAgent(), olqMsg.getId());
		} else {
			return new WhereIsItAnswerMessage(this.commMod.getAgent(), olqMsg.getId(), element.getWorldObject()
					.getPosition());
		}
	}
}
