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
package integrationTest.ai.planning;

import integrationTest.ai.MockGraphics;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

public class PlanningTest {
	
	private static final String INSTANCES_PATH = "data/ai/instances/integration tests/planning module test";
	private static final String MODELS_PATH = "data/ai";
	
	private static final long PLANNING_TESTER_ID = 5;
	
	private Agent planningTester;
	
	@Before
	public void setUp() throws Exception {
		
		// Loading the data
		AI ai = Model.getInstance().getAI();
		ai.getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		Model.getInstance().setGraphics(new MockGraphics());
		
		Map<Long, WorldObject> worldObjects = ai.getWorld().getWorldObjects();
		this.planningTester = (Agent) worldObjects.get(PLANNING_TESTER_ID);
		
		int dist = 15;
		
		for (WorldObject pavement : Model.getInstance().getAI().getWorld().getWalkableGraph().getVertices()) {
			if (this.planningTester.getPosition().distance(pavement.getPosition()) < dist) {
				this.planningTester.getMemory().remember(pavement);
			}
		}
	}
	
	@Test
	public void PlanAndExecuteTest() throws UnknownPropertyException {
		this.planningTester.getMemory().getGoals().add(new Goal("id5_i_gauge_thirst>=50"));
		PlanningModule planningModule = this.planningTester.getPlanningModule();
		
		do {
			planningModule.planAndExecute();
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------");
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------");
			System.out.println(this.planningTester.getPlanningModule().toString());
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------");
			
			System.out.println(this.planningTester);
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------");
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------\n\n\n");
			
		} while (planningModule.getPlan() != null);
		
		System.out.println("Memory.pastPlans=" + this.planningTester.getMemory().getPastPlans());
		Assert.assertTrue(this.planningTester.getPropertiesContainer().getDouble(Internal.Agent.GAUGE_THIRST) >= 50);
	}
}
