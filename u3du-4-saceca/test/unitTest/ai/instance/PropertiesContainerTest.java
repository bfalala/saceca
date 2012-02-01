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
package unitTest.ai.instance;

import org.junit.Before;
import org.junit.Test;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

import junit.framework.Assert;

/**
 * Test class for the properties container.
 * 
 * @author Sylvain Cambon
 */
public class PropertiesContainerTest {
	
	private PropertiesContainer propertiesContainer;
	
	private int noseLength = 3;
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	@Before
	public void setup() {
		PropertyModel<Integer> pm1 = new IntegerPropertyModel("noseLength", 0, 15, this.noseLength, Visibility.PUBLIC);
		Property<Integer> p1 = new Property<Integer>(pm1);
		
		PropertyModel<String> pm2 = new StringPropertyModel("language", "English", Visibility.PUBLIC);
		Property<String> p2 = new Property<String>(pm2);
		
		this.propertiesContainer = new PropertiesContainer();
		this.propertiesContainer.addProperty(p1);
		this.propertiesContainer.addProperty(p2);
	}
	
	@Test
	public void test1() throws UnknownPropertyException {
		int extractedNoseLength = this.propertiesContainer.getInt("noseLength");
		Assert.assertEquals(this.noseLength, extractedNoseLength);
	}
	
	@Test(expected = UnknownPropertyException.class)
	public void test2() throws UnknownPropertyException {
		int extractedNoseLength = this.propertiesContainer.getInt("not here");
		Assert.assertEquals(this.noseLength, extractedNoseLength);
	}
}
