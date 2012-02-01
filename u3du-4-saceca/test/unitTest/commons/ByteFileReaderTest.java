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

import java.io.IOException;

import org.junit.Test;

import fr.n7.saceca.u3du.model.util.io.ByteFileReader;

import junit.framework.Assert;

public class ByteFileReaderTest {
	
	@Test
	public void main() throws IOException {
		byte[] bytes = ByteFileReader.getBytesFromFile("./test/unitTest/commons/test.txt");
		String readString = new String(bytes);
		String expected = "Lorem ipsum.";
		System.out.println("Result   = " + readString);
		System.out.println("Expected = " + expected);
		System.out.println("Match    = " + expected.equals(readString));
		Assert.assertEquals(expected, readString);
	}
}
