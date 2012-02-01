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

import fr.n7.saceca.u3du.model.graphics.configuration.BoxConfiguration;
import fr.n7.saceca.u3du.model.io.graphics.BoxConfigurationIO;

import junit.framework.Assert;

public class BoxConfigurationIOTest {
	
	private static final String FILENAME = "data/3d/conf/Box.box3d.xml";
	
	@Test
	public void test() throws IOException {
		String name = "Box";
		float orientation = 0;
		float elevation = 0.5f;
		float dimension = 1;
		BoxConfiguration conf = new BoxConfiguration();
		conf.setCorrectiveAngle(orientation);
		conf.setElevation(elevation);
		conf.setName(name);
		conf.setVisible(true);
		conf.setHeight(dimension);
		conf.setLength(dimension);
		conf.setPathTexture("Interface/Boxes/MarioBox.jpg");
		conf.setWidth(dimension);
		
		BoxConfigurationIO bcio = new BoxConfigurationIO();
		String xml = bcio.exportObject(FILENAME, conf);
		System.out.println("XML = ");
		System.out.println(xml);
		
		System.out.print("Read     = ");
		BoxConfiguration readConf = bcio.importObject(FILENAME);
		System.out.println(readConf);
		System.out.println("Original = " + conf);
		Assert.assertEquals(conf, readConf);
	}
}
