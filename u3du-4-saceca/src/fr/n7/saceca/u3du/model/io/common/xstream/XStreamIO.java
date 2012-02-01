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
package fr.n7.saceca.u3du.model.io.common.xstream;

import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.io.common.HighLevelExporter;
import fr.n7.saceca.u3du.model.io.common.HighLevelImporter;
import fr.n7.saceca.u3du.model.util.io.TextFileIO;

/**
 * A class to deal with object I/O through XStream.
 * 
 * @param <T>
 *            The type of model.
 * @author Sylvain Cambon
 */
public abstract class XStreamIO<T> implements HighLevelImporter<T>, HighLevelExporter<T> {
	
	/**
	 * Configures the XStream instance if necessary.
	 * 
	 * @param xStream
	 *            The XStream instance.
	 */
	protected abstract void configureIfNecessary(final XStream xStream);
	
	/**
	 * Exports an object.
	 * 
	 * @param filename
	 *            The filename.
	 * @param object
	 *            The object.
	 * @return the xml written text.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public final String exportObject(String filename, T object) throws IOException {
		XStream xStream = XStreamProvider.getXStreamInstance();
		this.configureIfNecessary(xStream);
		String xml = xStream.toXML(object);
		TextFileIO.writeFile(filename, xml);
		return xml;
	}
	
	/**
	 * Imports an object from a file.
	 * 
	 * @param filename
	 *            The filename.
	 * @return The read object.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public final T importObject(final String filename) throws IOException {
		XStream xStream = XStreamProvider.getXStreamInstance();
		this.configureIfNecessary(xStream);
		String xml = TextFileIO.readFile(filename);
		
		@SuppressWarnings("unchecked")
		T model = (T) xStream.fromXML(xml);
		
		return model;
	}
	
	/**
	 * Checks if the configuration has been done.
	 * 
	 * @return true, if is configured
	 */
	protected abstract boolean isConfigured();
}
