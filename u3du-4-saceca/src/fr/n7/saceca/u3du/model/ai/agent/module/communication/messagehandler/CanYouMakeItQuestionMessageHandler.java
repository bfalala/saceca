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

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.memory.MemoryElement;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.CommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.CanYouMakeItNoAnswerMessage;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.CanYouMakeItQuestionMessage;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.CanYouMakeItYesAnswerMessage;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.statement.MemoryAwareU3duJexlContext;

/**
 * A class to handle service usage questions.
 */
public class CanYouMakeItQuestionMessageHandler implements MessageHandler {
	
	/** The comm mod. */
	private CommunicationModule commMod;
	
	/**
	 * Instantiates a new can you do it question message handler.
	 * 
	 * @param commMod
	 *            the comm mod
	 */
	public CanYouMakeItQuestionMessageHandler(CommunicationModule commMod) {
		super();
		this.commMod = commMod;
	}
	
	/**
	 * Gets the handled message type.
	 * 
	 * @return the handled message type
	 */
	@Override
	public Class<CanYouMakeItQuestionMessage> getHandledMessageType() {
		return CanYouMakeItQuestionMessage.class;
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
		CanYouMakeItQuestionMessage suqMsg = (CanYouMakeItQuestionMessage) msg;
		
		// Has the agent any knowledge of the object and of the service on this object? May it be
		// used?
		long objectId = suqMsg.getObjectId();
		MemoryElement element = this.commMod.getAgent().getMemory().getMemoryElements().get(objectId);
		WorldObject otherMemory = this.commMod.getAgent().getMemory().getKnowledgeAbout(suqMsg.getSender());
		if (element != null) {
			boolean seemsFeasible = suqMsg.getCondition()
					.check(new MemoryAwareU3duJexlContext(this.commMod.getAgent()));
			if (seemsFeasible) {
				((Agent) this.commMod.getAgent().getMemory().getKnowledgeAbout(otherMemory)).getMemory().getGoals()
						.add(new Goal(suqMsg.getCondition().toString()));
				return new CanYouMakeItYesAnswerMessage(this.commMod.getAgent(), suqMsg.getCondition(), objectId);
			}
		}
		return new CanYouMakeItNoAnswerMessage(this.commMod.getAgent(), suqMsg.getCondition(), objectId);
	}
}
