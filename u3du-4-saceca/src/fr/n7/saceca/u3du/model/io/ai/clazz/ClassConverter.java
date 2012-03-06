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
package fr.n7.saceca.u3du.model.io.ai.clazz;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.util.io.clazz.management.U3duClassLoader;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * A converter to handle qualified name <---> class correspondence in XStream.
 * 
 * @param <T>
 *            the type of classes
 * @author Sylvain Cambon
 */
public class ClassConverter<T> implements Converter {
	
	/** The class repository. */
	Repository<StorableClassWrapper<T>> classRepository;
	
	/**
	 * Instantiates a new class converter.
	 * 
	 * @param classRepository
	 *            the class repository
	 */
	public ClassConverter(Repository<StorableClassWrapper<T>> classRepository) {
		super();
		this.classRepository = classRepository;
	}
	
	/**
	 * Can convert.
	 * 
	 * @param other
	 *            the other
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class other) {
		return Class.class.equals(other);
	}
	
	/**
	 * Marshal.
	 * 
	 * @param object
	 *            the object
	 * @param writer
	 *            the writer
	 * @param context
	 *            the context
	 */
	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Class<?> clazz = (Class<?>) object;
		writer.setValue(clazz.getCanonicalName());
	}
	
	/**
	 * Unmarshal.
	 * 
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @return the class
	 */
	@Override
	public Class<? extends T> unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String qName = reader.getValue();
		if (qName == null) {
			throw new ConversionException("The qualified name of a class was \"null\".");
		}
		if (qName.isEmpty()) {
			throw new ConversionException("The qualified name of a class was empty.");
		}
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) U3duClassLoader.getInstance().loadClass(qName);
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new ConversionException("The class \"" + qName + "\" cannot be resolved", e);
		}
	}
}