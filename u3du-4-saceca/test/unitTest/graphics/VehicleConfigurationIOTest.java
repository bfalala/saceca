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
package unitTest.graphics;

import java.io.IOException;

import org.junit.Test;

import fr.n7.saceca.u3du.model.graphics.configuration.VehicleConfiguration;
import fr.n7.saceca.u3du.model.io.graphics.VehicleConfigurationIO;

import junit.framework.Assert;

public class VehicleConfigurationIOTest {
	private static final String FILENAME = "data/3d/conf/Car.veh3d.xml";
	
	@Test
	public void test() throws IOException {
		String name = "Car";
		String modelPath = "Models/Ferrari/Car.scene";
		float orientation = 0;
		float scale = 1;
		float elevation = 1;
		float collisionShapeRadius = 120.0f;// 200=f1 car
		float collisionShapeHeight = 0.2f; // (lower than damp!)
		float stepHeight = 0.3f;
		VehicleConfiguration conf = new VehicleConfiguration();
		conf.setCollisionShapeHeight(collisionShapeHeight);
		conf.setCorrectiveAngle(orientation);
		conf.setStepHeight(stepHeight);
		conf.setElevation(elevation);
		conf.setName(name);
		conf.setPathModel(modelPath);
		conf.setScaleX(scale);
		conf.setScaleY(scale);
		conf.setScaleZ(scale);
		conf.setCollisionShapeRadius(collisionShapeRadius);
		conf.setVisible(true);
		
		VehicleConfigurationIO vcio = new VehicleConfigurationIO();
		String filename = FILENAME;
		String xml = vcio.exportObject(filename, conf);
		System.out.println("XML = ");
		System.out.println(xml);
		
		System.out.print("Read     = ");
		VehicleConfiguration readConf = vcio.importObject(filename);
		System.out.println(readConf);
		System.out.println("Original = " + conf);
		
		Assert.assertEquals(conf, readConf);
	}
}
