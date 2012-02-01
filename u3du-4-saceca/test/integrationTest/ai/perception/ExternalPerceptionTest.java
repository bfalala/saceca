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
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

public class ExternalPerceptionTest {
	
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
				graphics.getVisionField().add(object.getId());
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
		
		System.out.println("* Balle réelle\n");
		System.out.println(ball);
		
		System.out.println("---------------------------------------------------\n\n");
		
		ai.getSimulation().start();
		Thread.sleep(2000);
		
		System.out.println("* Balle percue/mémorisée\n");
		System.out.println(agent.getMemory().getKnowledgeAbout(ball));
		
		System.out.println("\n>>>Deplacement de la balle à x=50 et modif du logo ADIDAS en NIKE<<<\n");
		ball.getPosition().x = 50;
		ball.getPropertiesContainer().setEnumElement("p_logo", new EnumElement("NIKE"));
		
		System.out.println("* Balle percue/mémorisée\n");
		System.out.println(agent.getMemory().getKnowledgeAbout(ball));
		
		Thread.sleep(2000);
		
		System.out.println("* Balle percue/mémorisée\n");
		System.out.println(agent.getMemory().getKnowledgeAbout(ball));
		
		System.exit(0);
	}
	
}
