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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.category.CategoryModel;
import fr.n7.saceca.u3du.model.io.ai.category.CategoryModelRepositoryLoader;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;

import junit.framework.Assert;

public class CategoryModelRepositoryLoaderTest {
	
	private static final String DIRECTORY = "data/ai/categories";
	
	/**
	 * @param args
	 */
	@Test
	public void test() {
		CategoryModelRepositoryLoader loader = new CategoryModelRepositoryLoader();
		Repository<CategoryModel> repository = loader.loadFilesToRepository(DIRECTORY);
		System.out.println(repository);
		
		Collection<String> filenames = new ArrayList<String>();
		for (String filename : new File(DIRECTORY).list()) {
			if (filename.endsWith(Constants.CATEGORY_MODEL_EXTENSION)) {
				filenames.add(filename.substring(0, filename.indexOf(Constants.CATEGORY_MODEL_EXTENSION)));
			}
		}
		
		for (CategoryModel catMod : repository) {
			if (filenames.contains(catMod.getName())) {
				filenames.remove(catMod.getName());
			} else {
				Assert.fail("The category " + catMod.getName()
						+ " is not expected. Check name/filename correspondance.");
			}
		}
		
		Assert.assertEquals(0, filenames.size());
	}
}
