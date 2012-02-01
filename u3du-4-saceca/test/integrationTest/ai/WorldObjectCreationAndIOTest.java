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

import org.junit.Assert;
import org.junit.Test;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.EntitiesFactoryMaterials;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.io.ai.EntitiesFactoryMaterialsLoader;
import fr.n7.saceca.u3du.model.io.ai.instance.object.WorldObjectIO;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

public class WorldObjectCreationAndIOTest {
	
	private static final String INSTANCES_PATH = "data/ai/instances/integration tests/EntitiesFactory test";
	private static final String MODELS_PATH = "data/ai";
	
	private static final String MODEL_NAME = "Ball";
	
	@Test
	public void test() throws UnknownPropertyException, IOException {
		EntitiesFactoryMaterialsLoader reader = new EntitiesFactoryMaterialsLoader();
		EntitiesFactoryMaterials materials = reader.readAll(MODELS_PATH);
		EntitiesFactory factory = new EntitiesFactory(materials);
		WorldObject object = factory.createWorldObject(MODEL_NAME);
		
		object.setId(2l);
		
		object.getPropertiesContainer().setBoolean("c_Rolling_rolling", true);
		object.setPosition(new Oriented2DPosition(21, 31, 0.3f));
		
		System.out.println("Object =");
		System.out.println(object);
		
		WorldObjectIO io = new WorldObjectIO(factory);
		String filename = INSTANCES_PATH + "/" + Constants.WORLD_OBJECTS_FOLDER_NAME + "/"
				+ object.getModelName().toLowerCase() + "_" + object.getId() + Constants.WORLD_OBJECTS_EXTENSION;
		
		String xml = io.exportObject(filename, object);
		System.out.println("\nXML =");
		System.out.println(xml);
		
		WorldObject readObject = io.importObject(filename);
		System.out.println("\nRead =");
		System.out.println(readObject);
		
		System.out.println("\nBehavior test:");
		readObject.getBehavior().init(readObject);
		readObject.getBehavior().behave();
		
		Assert.assertEquals(object, readObject);
	}
}
