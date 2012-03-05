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
package fr.n7.saceca.u3du.model.ai.agent.behavior;

import java.util.ArrayList;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.agent.memory.MemoryElement;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.CommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.DefaultCommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.emotion.DefaultEmotionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.DefaultPerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.MatrixMethodPlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMReasoningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.ReasoningModule;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.behavior.Behavior;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.util.Periodic.Clock;

/**
 * The Class DefaultAgentBehavior.
 * 
 * It is the class that our long project group (2011) provide as a basic implementation of
 * artificial intelligence.<br/>
 * <br/>
 * 
 * The behavior doesn't use or create any thread. So it is mono-thread in that it is only controlled
 * and executed by the behavioral thread of the agent.
 * 
 * Basically, what it does is :
 * <ul>
 * <li>executing the behavior every BEHAVE_PERIOD ms
 * <li>updating gauges and automatically using passive services
 * <li>checking the agent's death
 * <li>launch the intelligence modules' threads (perception, emotion, communication, reasonning,
 * planning) when necessary
 * </ul>
 * 
 * @author Jérôme Dalbert, Ciprian Munteanu
 */
public class DefaultAgentBehavior implements Behavior {
	
	/** The agent. */
	private Agent agent;
	
	/** The agent will behave only every BEHAVE_PERIOD ms. */
	public static final long BEHAVE_PERIOD = 500;
	
	/** The behave clock. */
	private Clock behaveClock;
	
	/** The Constant STORAGE_LABEL. */
	public static final String STORAGE_LABEL = DefaultAgentBehavior.class.getSimpleName();
	
	/**
	 * Creates the default modules for the agent.
	 * 
	 * @param object
	 *            the object
	 */
	@Override
	public void init(WorldObject object) {
		this.agent = (Agent) object;
		this.behaveClock = new Clock(BEHAVE_PERIOD);
		
		final CommunicationModule communicationModule = new DefaultCommunicationModule(this.agent);
		this.agent.setCommunicationModule(communicationModule);
		
		final DefaultEmotionModule emotionModule = new DefaultEmotionModule(this.agent);
		this.agent.setEmotionModule(emotionModule);
		
		final PerceptionModule perceptionModule = new DefaultPerceptionModule(this.agent);
		this.agent.setPerceptionModule(perceptionModule);
		
		final PlanningModule planningModule = new MatrixMethodPlanningModule(this.agent);
		this.agent.setPlanningModule(planningModule);
		
		final ReasoningModule mmReasoningModule = new MMReasoningModule(this.agent);
		this.agent.setMmReasoningModule(mmReasoningModule);
	}
	
	/**
	 * Behaves.
	 */
	@Override
	public void behave() {
		/* If the agent is not ready to react, we exit the behave method */
		if (!this.behaveClock.tic() || this.agent.isPause()) {
			return;
		}
		
		/* Initial updates */
		this.periodicGaugesDecrement(this.agent);
		
		this.periodicEmotionsDecrement(this.agent);
		
		this.usePassiveServices(this.agent);
		
		this.periodicMemoryReferencesDecrement(this.agent);
		this.agent.getMemory().arrangeMemory();
		
		/* Death check */
		if (this.isDead(this.agent)) {
			this.agent.kill();
			return;
		}
		
		/* Intelligence modules execution */
		if (!this.agent.getPerceptionModule().isAlive()) {
			this.agent.getPerceptionModule().start();
		}
		
		if (!this.agent.getEmotionModule().isAlive()) {
			this.agent.getEmotionModule().start();
		}
		
		if (!this.agent.getCommunicationModule().isAlive()) {
			this.agent.getCommunicationModule().start();
		}
		
		if (!this.agent.getMmReasoningModule().isAlive()) {
			this.agent.getMmReasoningModule().start();
		}
		
		this.sleepIfPause(this.agent);
		
		if (!this.agent.getPlanningModule().isAlive()) {
			this.agent.getPlanningModule().start();
		}
	}
	
	/**
	 * Uses the available passive services.
	 * 
	 * @param agent
	 *            the agent
	 */
	private void usePassiveServices(Agent agent) {
		for (WorldObject object : Model.getInstance().getAI().getWorld().getWorldObjects().values()) {
			for (Service service : object.getServices()) {
				if (!service.isActive()
						&& service.getMaxDistanceForUsage() >= object.getPosition().distance(agent.getPosition())
						&& service.isUsable(object, agent, null)) {
					service.useService(object, agent, null, ExecutionMode.REAL);
				}
			}
		}
	}
	
	/**
	 * Updates gauges periodically.
	 * 
	 * @param agent
	 *            the agent
	 */
	private void periodicGaugesDecrement(Agent agent) {
		for (Gauge gauge : agent.getGauges()) {
			if (gauge.isDecreased()) {
				gauge.periodicDecrement(agent);
			}
		}
	}
	
	/**
	 * Updates the primary and secondary emotions periodically
	 * 
	 * @param agent
	 *            the agent
	 */
	private void periodicEmotionsDecrement(Agent agent) {
		for (Emotion emotion : agent.getEmotions()) {
			emotion.periodicDecrement(agent);
		}
		for (Emotion emotion : agent.getSecondaryEmotions()) {
			emotion.periodicDecrement(agent);
		}
	}
	
	/**
	 * Updates periodically the number of references for every element in the memory
	 * 
	 * @param agent
	 *            the agent
	 */
	private void periodicMemoryReferencesDecrement(Agent agent) {
		ArrayList<MemoryElement> ame = new ArrayList<MemoryElement>(agent.getMemory().getMemoryElements().values());
		for (MemoryElement element : ame) {
			if (element.isForgettable()) {
				element.periodicDecrement(agent);
			}
		}
	}
	
	/**
	 * Checks if the agent is dead.
	 * 
	 * @param agent
	 *            the agent
	 * @return true, if it is dead
	 */
	private boolean isDead(Agent agent) {
		for (Gauge gauge : agent.getGauges()) {
			if (gauge.getValue() == 0 && gauge.isSurvival()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return STORAGE_LABEL;
	}
	
	/**
	 * Sleep if pause.
	 * 
	 * @param a
	 *            the a
	 */
	public void sleepIfPause(Agent a) {
		if (a.isPause()) {
			a.sleep();
		}
	}
	
}
