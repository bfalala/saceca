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
package unitTest.ai.model.io;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.io.ai.model.object.WorldObjectModelIO;

import junit.framework.Assert;

/**
 * A general test class for the world object model.
 * 
 * @author Sylvain Cambon
 */
public class WorldObjectModelIOTest {
	
	private static final String filename = "data/ai/" + Constants.WORLD_OBJECTS_MODELS_FOLDER_NAME + "/Ball.objmod.xml";
	
	/**
	 * A test for
	 * 
	 * @param args
	 *            Not used.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {
		
		final WorldObjectModel ball = new WorldObjectModel("Ball");
		ball.setBehaviorName("RandomBehavior");
		
		Set<String> categories = ball.getCategoriesNames();
		categories.add("Sport");
		categories.add("Rolling");
		
		Set<String> services = ball.getServicesNames();
		services.add("throw");
		services.add("shoot");
		
		final int beg = 0;
		final int end = 255;
		final int def = 128;
		final double begF = 0;
		final double endF = 2;
		final double defF = 1.5;
		
		Set<PropertyModel<?>> properties = ball.getProperties();
		properties.add(new IntegerPropertyModel("p_color", beg, end, def, Visibility.PUBLIC));
		properties.add(new DoublePropertyModel("p_formFactor", begF, endF, defF, Visibility.PUBLIC));
		properties.add(new EnumPropertyModel("p_logo", new EnumElement("ADIDAS"), new EnumElement[] {
				new EnumElement("ADIDAS"), new EnumElement("NIKE") }, Visibility.PUBLIC));
		
		WorldObjectModelIO io = new WorldObjectModelIO();
		
		final String xml = io.exportObject(WorldObjectModelIOTest.filename, ball);
		System.out.println("XML=");
		System.out.println(xml);
		
		System.out.println();
		System.out.println("Unmarshalled object=");
		final WorldObjectModel readBall = io.importObject(WorldObjectModelIOTest.filename);
		System.out.println(readBall);
		
		Assert.assertEquals(ball, readBall);
	}
}
