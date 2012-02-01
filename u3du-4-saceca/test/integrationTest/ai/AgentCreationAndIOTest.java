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
package integrationTest.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.EntitiesFactoryMaterials;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.memory.MemoryElement;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.io.ai.EntitiesFactoryMaterialsLoader;
import fr.n7.saceca.u3du.model.io.ai.instance.agent.AgentIO;
import fr.n7.saceca.u3du.model.io.ai.instance.object.WorldObjectIO;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

import junit.framework.Assert;

public class AgentCreationAndIOTest {
	
	private static final String INSTANCES_PATH = "data/ai/instances/integration tests/EntitiesFactory test";
	private static final String MODELS_PATH = "data/ai";
	
	private static final String AGENT_FILENAME = INSTANCES_PATH + "/" + Constants.AGENTS_FOLDER_NAME + "/neo_0"
			+ Constants.AGENTS_EXTENSION;
	
	@Test
	public void test() throws UnknownPropertyException, IOException, MalformedObjectException {
		EntitiesFactoryMaterialsLoader reader = new EntitiesFactoryMaterialsLoader();
		EntitiesFactoryMaterials materials = reader.readAll(MODELS_PATH);
		EntitiesFactory factory = new EntitiesFactory(materials);
		
		// Instance creation and customization
		Agent neo = factory.createAgent("Geek");
		neo.getPropertiesContainer().setBoolean("c_Committer_ready2commit", true);
		neo.getPropertiesContainer().setString("i_name", "Neo");
		neo.setPosition(new Oriented2DPosition(1, 2, 3));
		
		WorldObject pc = factory.createWorldObject("Computerosaurus");
		pc.setPosition(new Oriented2DPosition(42, 51, 3.14159f));
		new WorldObjectIO(factory).exportObject(INSTANCES_PATH + "/objects/computerosaurus_1.obj.xml", pc);
		neo.getBelongings().add(pc);
		neo.getMemory().initializeKnowledge();
		MemoryElement memory = new MemoryElement(pc, false);
		neo.getMemory().getMemoryElements().put(pc.getId(), memory);
		
		// IO
		AgentIO aio = new AgentIO(factory);
		String agentXML = aio.exportObject(AgentCreationAndIOTest.AGENT_FILENAME, neo);
		System.out.println("\nAgent  XML =");
		System.out.println(agentXML);
		
		Agent readNeo = aio.importObject(AGENT_FILENAME);
		Collection<WorldObject> objects = new ArrayList<WorldObject>();
		objects.add(readNeo);
		objects.add(pc);
		EntitiesFactory.repairBelongings(objects);
		System.out.println("\nRead agent = ");
		System.out.println(readNeo);
		
		Assert.assertEquals(neo, readNeo);
	}
}
