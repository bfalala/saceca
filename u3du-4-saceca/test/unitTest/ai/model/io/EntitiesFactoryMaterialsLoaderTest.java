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

import fr.n7.saceca.u3du.model.ai.EntitiesFactoryMaterials;
import fr.n7.saceca.u3du.model.io.ai.EntitiesFactoryMaterialsLoader;

public class EntitiesFactoryMaterialsLoaderTest {
	
	private static final String FOLDER = "data/ai";
	
	public static void main(String[] args) {
		EntitiesFactoryMaterialsLoader loader = new EntitiesFactoryMaterialsLoader();
		EntitiesFactoryMaterials materials = loader.readAll(FOLDER);
		System.out.println(materials.getCategoryModelRepository());
		System.out.println(materials.getServiceRepository());
		System.out.println(materials.getWorldObjectModelRepository());
		System.out.println(materials.getBehaviorRepository());
	}
}
