package integrationTest.ai.reasoning;

import integrationTest.ai.MockGraphics;

import java.util.Collection;

import fr.n7.saceca.u3du.exception.SacecaStrictException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

public class MMReasoningTest {
	private static final String INSTANCES_PATH = "data/ai/instances/integration tests/reasoning module test";
	private static final String MODELS_PATH = "data/ai";
	
	public static void main(String[] args) throws InterruptedException, SacecaStrictException, UnknownPropertyException {
		/* INITIALISATION */
		Model m = Model.getInstance();
		
		AI ai = m.getAI();
		ai.getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		MockGraphics graphics = new MockGraphics();
		m.setGraphics(graphics);
		
		// Objects config.
		for (WorldObject object : ai.getWorld().getReactiveObjects()) {
			object.kill();
			ai.getWorld().getWorldObjects().remove(object.getId());
		}
		
		// Agent config.
		Agent agent = null;
		final Collection<Agent> agents = ai.getWorld().getAgents();
		for (Agent a : agents) {
			if (a.getPropertiesContainer().getString("i_name").equals("reasoningtester_5")) {
				agent = a;
			} else {
				a.kill();
			}
		}
		
		final MMReasoningOnlyBehavior behavior = new MMReasoningOnlyBehavior();
		agent.setBehavior(behavior);
		behavior.init(agent);
		
		// Gauge initial perception
		for (Gauge g : agent.getGauges()) {
			agent.getMemory().getKnowledgeAboutOwner().getPropertiesContainer().addProperty(g);
		}
		
		/* DEBUT TEST */
		System.out.println("\n\n\n\n\n");
		
		System.out.println("* Etat de l'agent\n");
		System.out.println(agent);
		
		System.out.println("---------------------------------------------------\n\n");
		
		ai.getSimulation().start();
		
		Thread.sleep(2000);
		
		System.out
				.println("\n* Buts (test de la règle CreateGaugeGoal et ChooseGoalToReach.\n Le premier but de la liste devrait avoir une priorité de 100 pour signifier que c'est le but à atteindre pendant un bon moment.\n");
		for (MMGoal goal : agent.getMemory().getGoalStack()) {
			System.out.println(goal);
		}
		
		Thread.sleep(2000);
		
		/*
		 * Remarque : le code suivant fait parfois bugger, mais c'est normal car on n'est pas censé
		 * avoir le droit d'ajouter des buts à un agent, si on n'est pas cet agent
		 */

		// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		// System.out
		// .println("\nOn ajoute en plus du goal \"id7_i_gauge_tiredness>=80\" de priorité 30, un même goal mais avec une priorité de 35\n");
		//
		// Goal g = new Goal("id7_i_gauge_tiredness>=100", 35);
		// agent.getMemory().getGoals().add(g);
		//
		// Thread.sleep(2000);
		//
		// System.out.println("\n* Buts (test de la règle DeleteGoalSameGauge)\n");
		// for (Goal goal : agent.getMemory().getGoals()) {
		// System.out.println(goal);
		// }
		// // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//
		// System.out.println("\nOn définit le but \"id7_i_gauge_tiredness>=100\" comme inatteignable\n");
		// for (Goal goal : agent.getMemory().getGoals()) {
		// if (goal.getSuccessCondition().equals("id7_i_gauge_tiredness>=100")) {
		// goal.setReachable(false);
		// }
		// }
		//
		// Thread.sleep(2000);
		//
		// System.out.println("\n* Buts (test de la règle PenalizeUnreachableGoal)\n");
		// for (Goal goal : agent.getMemory().getGoals()) {
		// System.out.println(goal);
		// }
		//
		// System.out.println("\nOn définit le but \"id7_i_gauge_happiness>=80\" comme atteint\n");
		// agent.getPropertiesContainer().setDouble("i_gauge_happiness", 100.0);
		// for (Goal goal : agent.getMemory().getGoals()) {
		// if (goal.getSuccessCondition().equals("id7_i_gauge_happiness>=80")) {
		// goal.setReached(true);
		// }
		// }
		//
		// Thread.sleep(2000);
		//
		// System.out.println("\n* Buts (test de la règle DeleteReachedGoal)\n");
		// for (Goal goal : agent.getMemory().getGoals()) {
		// System.out.println(goal);
		// }
		//
		System.exit(0);
	}
}
