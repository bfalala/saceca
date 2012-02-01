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
package integrationTest.ai.perception;

import integrationTest.ai.MockGraphics;

import fr.n7.saceca.u3du.exception.SacecaStrictException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

public class InternalPerceptionTest {
	
	private static final String INSTANCES_PATH = "data/ai/instances/integration tests/perception module test";
	private static final String MODELS_PATH = "data/ai";
	
	public static void main(String[] args) throws InterruptedException, SacecaStrictException, UnknownPropertyException {
		/* INITIALISATION */
		Model m = Model.getInstance();
		
		AI ai = m.getAI();
		ai.getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		MockGraphics graphics = new MockGraphics();
		m.setGraphics(graphics);
		
		// Objects config.
		WorldObject ball = null;
		for (WorldObject object : ai.getWorld().getReactiveObjects()) {
			object.kill();
			if (object.getModelName().equals("Ball")) {
				ball = object;
			}
		}
		
		// Agent config.
		Agent agent = null;
		boolean isFirstAgent = true;
		for (Agent a : ai.getWorld().getAgents()) {
			if (isFirstAgent) {
				agent = a;
				final PerceptionOnlyBehavior behavior = new PerceptionOnlyBehavior();
				a.setBehavior(behavior);
				behavior.init(a);
				isFirstAgent = false;
			} else {
				a.kill();
			}
		}
		
		/* DEBUT TEST */
		System.out.println("\n\n\n\n\n");
		
		System.out.println("* Agent initial\n");
		System.out.println(agent);
		
		System.out.println("* Connaissance initiale que l'agent a de lui-même\n");
		System.out.println(agent.getMemory().getKnowledgeAboutOwner());
		
		System.out.println("---------------------------------------------------\n\n");
		
		ai.getSimulation().start();
		Thread.sleep(2000);
		
		System.out.println("* Connaissance que l'agent a de lui-même après perception\n");
		System.out.println(agent.getMemory().getKnowledgeAboutOwner());
		
		System.out.println("\nModification: \n" + "- i_gauge_tiredness = 15\n" + "- position.x = 30\n"
				+ "- ajout d'une balle en possession\n");
		agent.getPropertiesContainer().setDouble("i_gauge_tiredness", 15.0);
		agent.getPosition().x = 30;
		agent.getBelongings().add(ball);
		
		System.out.println("* Connaissance que l'agent a de lui-même\n");
		System.out.println(agent.getMemory().getKnowledgeAboutOwner());
		
		Thread.sleep(2000);
		
		System.out.println("* Connaissance que l'agent a de lui-même après perception\n");
		System.out.println(agent.getMemory().getKnowledgeAboutOwner());
		
		System.exit(0);
	}
	
}
