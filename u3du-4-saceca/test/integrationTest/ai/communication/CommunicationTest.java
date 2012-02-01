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
package integrationTest.ai.communication;

import integrationTest.ai.MockGraphics;

import java.util.Map;

import org.junit.Test;

import fr.n7.saceca.u3du.exception.SacecaStrictException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.memory.MemoryElement;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.WhereIsItQuestionMessage;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

import junit.framework.Assert;

public class CommunicationTest {
	
	private static final String INSTANCES_PATH = "data/ai/instances/integration tests/communication module test";
	private static final String MODELS_PATH = "data/ai";
	
	private static final long ATM_ID = 2l;
	
	private static final String ATM_NAME = "Atm";
	
	private static final long JACK_ID = 0l;
	
	@Test
	public void test() throws InterruptedException, UnknownPropertyException, SacecaStrictException {
		loadExamples();
		
		final AI ai = Model.getInstance().getAI();
		for (Agent agent : ai.getWorld().getAgents()) {
			final CommunicationOnlyBehavior behavior = new CommunicationOnlyBehavior();
			agent.setBehavior(behavior);
			behavior.init(agent);
		}
		Model.getInstance().setGraphics(new MockGraphics());
		ai.getSimulation().start();
		final Map<Long, WorldObject> objects = ai.getWorld().getWorldObjects();
		Agent jack = (Agent) objects.get(JACK_ID);
		jack.getPropertiesContainer().setInt(Internal.Agent.PERCEPTION_MAX_HEARING_DISTANCE, 1000);
		ai.getWorld().locallyBroadcastMessage(jack, new WhereIsItQuestionMessage(jack, ATM_ID));
		
		System.out.println("Sleep 2s");
		
		Thread.currentThread();
		Thread.sleep(2000);
		
		Memory memory = jack.getMemory();
		Map<Long, MemoryElement> memoryElements = memory.getMemoryElements();
		MemoryElement memoryElement = memoryElements.get(ATM_ID);
		WorldObject worldObject = memoryElement.getWorldObject();
		System.out.println(">>>> Jack thinks that the Computersaurus is at: " + worldObject.getPosition());
		
		Assert.assertEquals(objects.get(ATM_ID).getPosition(), worldObject.getPosition());
		ai.getSimulation().stop();
	}
	
	/**
	 * Loads examples from XML files.
	 * 
	 * @throws SacecaStrictException
	 *             If a problem occurs during the initialization phase.
	 */
	public static void loadExamples() throws SacecaStrictException {
		Model.getInstance().getAI().getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		((Agent) Model.getInstance().getAI().getWorld().getWorldObjects().get(CommunicationTest.JACK_ID)).getMemory()
				.remember(new WorldObject(CommunicationTest.ATM_NAME, CommunicationTest.ATM_ID));
		
	}
	
}
