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
package unitTest.commons;

import org.junit.Test;

import fr.n7.saceca.u3du.model.util.io.clazz.management.ClassFileDirectory;
import fr.n7.saceca.u3du.model.util.io.clazz.management.U3duClassLoader;

import junit.framework.Assert;

public class U3duClassLoaderTest {
	
	// The file "FooSource" has to be renamed to "Foo" as well as the contained
	// class. The corresponding .class file has to be put in the
	// test/unitTest/commons folder. Then Foo has to be returned to its initial
	// state.
	@Test
	public void test() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ClassFileDirectory directory = new ClassFileDirectory();
		directory.put("unitTest.commons.Foo", "test/unitTest/commons/Foo.class");
		U3duClassLoader loader = U3duClassLoader.getInstance();
		loader.setDirectory(directory);
		FooItf foo = (FooItf) (loader.loadClass("unitTest.commons.Foo").newInstance());
		String result = foo.bar();
		System.out.println("Result   = " + result);
		System.out.println("Expected = quux");
		System.out.println("Match    = " + "quux".equals(result));
		Assert.assertEquals("quux", result);
	}
	
}
