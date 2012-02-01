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
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * A class to handle receiving information about an object location.
 */
public class WhereIsItAnswerMessageHandler implements MessageHandler {
	
	/** The comm mod. */
	private CommunicationModule commMod;
	
	/**
	 * Instantiates a new object location answer message handler.
	 * 
	 * @param commMod
	 *            the comm mod
	 */
	public WhereIsItAnswerMessageHandler(CommunicationModule commMod) {
		super();
		this.commMod = commMod;
	}
	
	/**
	 * Gets the handled message type.
	 * 
	 * @return the handled message type
	 */
	@Override
	public Class<WhereIsItAnswerMessage> getHandledMessageType() {
		return WhereIsItAnswerMessage.class;
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
		WhereIsItAnswerMessage olaMsg = (WhereIsItAnswerMessage) msg;
		long id = olaMsg.getId();
		Oriented2DPosition position = olaMsg.getPosition();
		MemoryElement elt = this.commMod.getAgent().getMemory().getMemoryElements().get(id);
		if (elt != null && elt.getWorldObject() != null) {
			elt.getWorldObject().setPosition(position);
		}
		
		return Message.STOP_CONVERSATION;
	}
	
}
