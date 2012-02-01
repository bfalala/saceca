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
package fr.n7.saceca.u3du.model.util.io.clazz.management;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.model.io.ai.clazz.ClassFileDirectoryIO;

/**
 * A class linking qualified class names to class files.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("classes")
public class ClassFileDirectory {
	
	/** The Constant COMMON_CLASSES_FILE. */
	@XStreamOmitField
	private static final String COMMON_CLASSES_FILE = "bin/fr/n7/saceca/u3du/model/ai/_CommonClasses.xml";
	
	/** The directory. */
	@XStreamImplicit(itemFieldName = "class")
	Set<ClassFileDirectoryEntry> directory = new HashSet<ClassFileDirectoryEntry>();
	
	/** The common classes. */
	@XStreamOmitField
	private static ClassFileDirectory commonClasses = null;
	
	/** The logger. */
	@XStreamOmitField
	private static Logger logger = Logger.getLogger(ClassFileDirectory.class);
	
	/**
	 * Gets the path corresponding to a qualified name.
	 * 
	 * @param qName
	 *            the qualified name
	 * @return the corresping path, or null if nothing was found.
	 */
	public String getPath(String qName) {
		for (ClassFileDirectoryEntry ref : this.directory) {
			if (ref.getQualifiedName().equals(qName)) {
				return ref.getFilePath();
			}
		}
		for (ClassFileDirectoryEntry ref : this.getCommonClassFileDirectory().directory) {
			if (ref.getQualifiedName().equals(qName)) {
				return ref.getFilePath();
			}
		}
		return null;
	}
	
	/**
	 * Gets the all the available qualified names.
	 * 
	 * @return the qualified names
	 */
	public Collection<String> getQualifiedNames() {
		Collection<String> keys = new LinkedList<String>();
		for (ClassFileDirectoryEntry ref : this.directory) {
			keys.add(ref.getQualifiedName());
		}
		for (ClassFileDirectoryEntry ref : this.getCommonClassFileDirectory().directory) {
			keys.add(ref.getQualifiedName());
		}
		return keys;
	}
	
	/**
	 * Put a (qualified name, path) couple.
	 * 
	 * @param qName
	 *            the qualified name
	 * @param path
	 *            the path
	 */
	public void put(String qName, String path) {
		this.directory.add(new ClassFileDirectoryEntry(qName, path));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("ClassFileDirectory:\n");
		for (ClassFileDirectoryEntry entry : this.directory) {
			builder.append('\t');
			builder.append(entry.toString());
			builder.append('\n');
		}
		for (ClassFileDirectoryEntry entry : this.getCommonClassFileDirectory().directory) {
			builder.append('\t');
			builder.append(entry.toString());
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	/**
	 * Gets the common class file directory.
	 * 
	 * @return the common class file directory
	 */
	private ClassFileDirectory getCommonClassFileDirectory() {
		if (commonClasses == null) {
			try {
				commonClasses = new ClassFileDirectoryIO().importObject(COMMON_CLASSES_FILE);
			} catch (IOException e) {
				logger.warn("Cannot load the common classes.", e);
			}
		}
		return commonClasses;
	}
	
	/**
	 * Checks the serialized data.
	 * 
	 * @return the object
	 */
	private Object readResolve() {
		if (this.directory == null) {
			this.directory = new HashSet<ClassFileDirectoryEntry>();
		}
		return this;
	}
	
}
