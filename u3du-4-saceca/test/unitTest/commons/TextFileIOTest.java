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

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import fr.n7.saceca.u3du.model.util.io.Path;
import fr.n7.saceca.u3du.model.util.io.TextFileIO;

import junit.framework.Assert;

/**
 * A test for TextFileIO.
 * 
 * @author Sylvain Cambon
 */
public class TextFileIOTest {
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {
		
		String filename = "./test/unitTest/commons/test.txt";
		String text = "Lorem ipsum.";
		
		TextFileIO.writeFile(filename, text);
		String readString = TextFileIO.readFile(filename);
		System.out.print(readString);
		
		if (text.equals(readString)) {
			System.out.println(" [OK]");
		} else {
			System.out.println(" [KO]");
		}
		
		System.out.println("Created file: " + Path.retrieve() + File.separatorChar + filename);
		Assert.assertEquals(text, readString);
	}
}
