package integrationTest.ai.planning;

import integrationTest.ai.MockGraphics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.Param;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;

public class MMPlanningTest {
	private static final String INSTANCES_PATH = "data/ai/instances/planning test 1";
	private static final String MODELS_PATH = "data/ai";
	
	private static final long PLANNING_TESTER_ID = 4255;
	
	private ArrayList<Hashtable<String, Double>> testsResults;
	
	private Agent planningTester;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		
		// Loading the data
		AI ai = Model.getInstance().getAI();
		ai.getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		Model.getInstance().setGraphics(new MockGraphics());
		
		Model.getInstance().getAI().initAI();
		
		Map<Long, WorldObject> worldObjects = ai.getWorld().getWorldObjects();
		this.planningTester = (Agent) worldObjects.get(PLANNING_TESTER_ID);
		
		for (WorldObject pavement : Model.getInstance().getAI().getWorld().getWalkableGraph().getVertices()) {
			this.planningTester.getMemory().remember(pavement);
			break;
		}
		
		this.testsResults = new ArrayList<Hashtable<String, Double>>();
		XStream xstr = new XStream(new DomDriver());
		
		try {
			FileInputStream fileStr = new FileInputStream("data/matrixMethod_"
					+ String.valueOf(this.planningTester.getMemory().getKnowledges().size()) + ".xml");
			
			this.testsResults.addAll((Collection<? extends Hashtable<String, Double>>) xstr.fromXML(fileStr));
		} catch (Exception e) {
		}
	}
	
	@Test
	public void PlanAndExecuteTest() throws UnknownPropertyException {
		// this.planningTester.getMemory().getGoals().add(new
		// Goal("id5_i_gauge_primordial_thirst>=50"));
		
		// List<Emotion> emotions = this.planningTester.getEmotions();
		
		PlanningModule planningModule = this.planningTester.getPlanningModule();
		
		// ArrayList<ServiceProperty> prList =
		// this.planningTester.getPossibleEffects(this.planningTester.getMemory()
		// .getKnowledges());
		
		// this.planningTester.getMmReasoningModule().reason();
		
		ArrayList<Param> goalParameterList = new ArrayList<Param>();
		goalParameterList.add(new Param("i_gauge_primordial_thirst", "", "double", "", "false"));
		goalParameterList.add(new Param("amount", "50", "double", "", "false"));
		ServiceProperty successCondition = new ServiceProperty("biggerThan", goalParameterList);
		successCondition.setOrder('1');
		successCondition.setTreatment_effect("");
		successCondition.setTreatment_precond("i_gauge_primordial_thirst__>__amount");
		
		MMGoal goal = new MMGoal(successCondition, 80);
		
		this.planningTester.getMemory().getGoalStack().addBegining(goal);
		
		long start = System.nanoTime();
		
		planningModule.planAndExecute();
		
		double elapsedTimeInMilSec = (System.nanoTime() - start) * 1.0e-6;
		
		Hashtable<String, Double> result = new Hashtable<String, Double>();
		result.put("nbElements", (double) this.planningTester.getPlanningModule().getPlan().size());
		result.put("executionTime", elapsedTimeInMilSec);
		
		this.testsResults.add(result);
		
		XStream xstr = new XStream(new DomDriver());
		
		try {
			FileOutputStream fileStr = new FileOutputStream("data/matrixMethod_"
					+ String.valueOf(this.planningTester.getMemory().getKnowledges().size()) + ".xml");
			
			xstr.toXML(this.testsResults, fileStr);
		} catch (Exception e) {
		}
		
		// do {
		// long start = System.nanoTime();
		// planningModule.planAndExecute();
		// double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		// System.out.print("\n\n\nBUILD THE PLAN TIME: " + elapsedTimeInSec + " sec\n\n");
		//
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// // System.out.println(this.planningTester.getMmPlanningModule().toString());
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// //
		// // System.out.println(this.planningTester);
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------\n\n\n");
		// //
		// } while (planningModule.getPlan() != null);
		
		// this.planningTester.getMmReasoningModule().reason();
		//
		// do {
		// long start = System.nanoTime();
		// planningModule.planAndExecute();
		// double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		// System.out.print("\n\n\nBUILD THE PLAN TIME: " + elapsedTimeInSec + " sec\n\n");
		//
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// // System.out.println(this.planningTester.getMmPlanningModule().toString());
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// //
		// // System.out.println(this.planningTester);
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------");
		// // System.out
		// //
		// .println("--------------------------------------------------------------------------------------------------------------------------\n\n\n");
		// //
		// } while (planningModule.getPlan() != null);
		//
		// System.out.println("Memory.pastPlans=" + this.planningTester.getMemory().getPastPlans());
		Assert.assertTrue(this.planningTester.getPropertiesContainer()
				.getDouble(Internal.Agent.GAUGE_PRIMORDIAL_THIRST) >= 50);
	}
}
