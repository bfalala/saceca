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

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.behavior.DefaultAgentBehavior;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.CanYouMakeItNoAnswerMessageHandler;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.CanYouMakeItQuestionMessageHandler;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.CanYouMakeItYesAnswerMessageHandler;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.MessageHandler;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.WhereIsItAnswerMessageHandler;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.WhereIsItDunnoAnswerMessageHandler;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.messagehandler.WhereIsItQuestionMessageHandler;

/**
 * A default communication module using an inbox in the memory.
 */
public class DefaultCommunicationModule implements CommunicationModule {
	
	/**
	 * The Class CommunicationThread.
	 */
	private class CommunicationThread extends Thread {
		
		/**
		 * Instantiates a new communication thread and names it according to the owning object.
		 */
		public CommunicationThread() {
			super();
			this.setName(DefaultCommunicationModule.this.getAgent().toShortString() + "Communication thread");
		}
		
		/**
		 * This method represents the "communication loop"
		 */
		@Override
		public void run() {
			while (DefaultCommunicationModule.this.agent.isAlive() && !DefaultCommunicationModule.this.agent.isPause()) {
				DefaultCommunicationModule.this.communicate();
				try {
					Thread.sleep(DefaultAgentBehavior.BEHAVE_PERIOD / 2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	// TODO: find another place for it
	/** The Constant DEFAULT_MAX_DIST. */
	public static final int DEFAULT_MAX_DIST = 10;
	
	/** The messages handlers. */
	private Map<Class<? extends Message>, MessageHandler> handlers;
	
	/** The agent. */
	private Agent agent;
	
	/** The communication thread */
	private CommunicationThread communicationThread;
	
	/**
	 * Checks if the communication thread is alive
	 */
	@Override
	public boolean isAlive() {
		return this.communicationThread != null && this.communicationThread.isAlive();
	}
	
	/**
	 * Instantiates a new default communication module.
	 * 
	 * @param agent
	 *            the agent
	 */
	public DefaultCommunicationModule(Agent agent) {
		this.agent = agent;
		this.handlers = new HashMap<Class<? extends Message>, MessageHandler>();
		this.addMessageHandler(new WhereIsItQuestionMessageHandler(this));
		this.addMessageHandler(new WhereIsItAnswerMessageHandler(this));
		this.addMessageHandler(new WhereIsItDunnoAnswerMessageHandler());
		this.addMessageHandler(new CanYouMakeItQuestionMessageHandler(this));
		this.addMessageHandler(new CanYouMakeItYesAnswerMessageHandler());
		this.addMessageHandler(new CanYouMakeItNoAnswerMessageHandler());
	}
	
	/**
	 * This implementation is not pro-active, i.e. it only answers messages.
	 */
	@Override
	public void communicate() {
		Message message = null;
		Message answer = null;
		Queue<Message> inbox = this.agent.getMemory().getMessageInbox();
		while (!inbox.isEmpty()) {
			// Because of the !isEmpty, when poll returns null, STOP_CONVERSAITION(=null) is
			// meant.
			message = inbox.poll();
			if (message != Message.STOP_CONVERSATION) {
				answer = this.handleMessage(message);
				if (answer != null) {
					Model.getInstance().getAI().getWorld().locallyBroadcastMessage(this.agent, answer);
				}
			}
		}
	}
	
	/**
	 * Starts the communication module's thread
	 */
	@Override
	public void start() {
		this.communicationThread = new CommunicationThread();
		this.communicationThread.start();
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return DefaultCommunicationModule.class.getCanonicalName();
	}
	
	/**
	 * Adds the message.
	 * 
	 * @param msg
	 *            the msg
	 */
	@Override
	public void addMessage(Message msg) {
		this.agent.getMemory().getMessageInbox().add(msg);
	}
	
	/**
	 * Adds the message handler.
	 * 
	 * @param handler
	 *            the handler
	 */
	public void addMessageHandler(MessageHandler handler) {
		this.handlers.put(handler.getHandledMessageType(), handler);
	}
	
	/**
	 * Handles a message. The registered handlers are used if possible.
	 * 
	 * @param message
	 *            the message
	 * @return the message
	 */
	private Message handleMessage(Message message) {
		if (message == null) {
			return Message.STOP_CONVERSATION;
		} else {
			MessageHandler handler = this.handlers.get(message.getClass());
			if (handler != null) {
				return handler.handle(message);
			} else {
				return Message.STOP_CONVERSATION;
			}
		}
	}
	
	/**
	 * Gets the agent.
	 * 
	 * @return the agent
	 */
	@Override
	public Agent getAgent() {
		return this.agent;
	}
	
}
