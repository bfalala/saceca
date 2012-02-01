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
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.category.CategoryModel;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;
import fr.n7.saceca.u3du.model.io.ai.category.CategoryModelIO;

import junit.framework.Assert;

/**
 * A general test class for categories.
 * 
 * @author Sylvain Cambon
 */
public class CategoryModelIOTest {
	
	private static final String filename = "data/ai/categories/Supporter.catmod.xml";
	
	/**
	 * A test to check whether category model IO works well.
	 * 
	 * @param args
	 *            Not used.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCategoryuModelIO() throws IOException {
		CategoryModel model = new CategoryModel("Supporter", new HashSet<PropertyModel<?>>());
		final int beg = 0;
		final int end = 255;
		final int def = 128;
		
		Set<PropertyModel<?>> properties = model.getProperties();
		properties.add(new IntegerPropertyModel("p_color", beg, end, def, Visibility.PUBLIC));
		properties.add(new StringPropertyModel("p_team", "none", Visibility.PUBLIC));
		properties.add(new EnumPropertyModel("p_logo", new EnumElement("ADIDAS"), new EnumElement[] {
				new EnumElement("ADIDAS"), new EnumElement("NIKE") }, Visibility.PUBLIC));
		
		CategoryModelIO io = new CategoryModelIO();
		String xml = io.exportObject(filename, model);
		System.out.println("XML=");
		System.out.println(xml);
		
		System.out.println();
		System.out.println("Unmarshalled object=");
		final CategoryModel readModel = io.importObject(filename);
		System.out.println(readModel);
		
		Assert.assertEquals(model, readModel);
	}
}
