/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * A <b>stateless</b> class to handle text I/O.
 * 
 * @author Sylvain Cambon
 */
public class TextFileIO {
	
	/**
	 * Reads a text file.
	 * 
	 * @param filename
	 *            The filename.
	 * @return The content of the file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred. The opened streams will have been
	 *             properly closed.
	 */
	public static String readFile(String filename) throws IOException {
		Reader reader = null;
		BufferedReader buffReader = null;
		StringBuilder sb = null;
		final String fileEnd = null;
		
		try {
			reader = new FileReader(filename);
			buffReader = new BufferedReader(reader);
			
			sb = new StringBuilder();
			boolean stop = false;
			do {
				String line = buffReader.readLine();
				if (line != fileEnd) {
					sb.append(line);
					sb.append('\n');
				} else {
					stop = true;
				}
			} while (!stop);
		} finally {
			if (buffReader != null) {
				buffReader.close();
			} else if (reader != null) {
				reader.close();
			}
		}
		return sb.toString();
	}
	
	/**
	 * Writes a text file.
	 * 
	 * @param filename
	 *            The filename.
	 * @param string
	 *            The string to be written.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred. The opened streams will have been
	 *             properly closed.
	 */
	public static void writeFile(String filename, String string) throws IOException {
		Writer writer = null;
		BufferedWriter buffWriter = null;
		
		try {
			writer = new FileWriter(filename);
			buffWriter = new BufferedWriter(writer);
			buffWriter.write(string);
		} finally {
			if (buffWriter != null) {
				buffWriter.close();
			} else if (writer != null) {
				writer.close();
			}
		}
	}
}