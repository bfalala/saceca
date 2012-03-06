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
package fr.n7.saceca.u3du.model.io.ai.model.object.properties;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.object.properties.BooleanPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamHelper;

/**
 * A class to gather the XML processing of Property Models.
 * 
 * @author Sylvain Cambon
 */
public class PropertyModelsConvertionHelper {
	
	/**
	 * Marshal the properties.
	 * 
	 * @param properties
	 *            the properties
	 * @param writer
	 *            the writer
	 * @param context
	 *            the context
	 */
	public static void marshal(Collection<PropertyModel<?>> properties, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		for (PropertyModel<?> prop : properties) {
			String nodeName = XStreamHelper.getClassAlias(prop.getClass());
			writer.startNode(nodeName);
			context.convertAnother(prop);
			writer.endNode();
		}
	}
	
	/**
	 * Unmarshal the properties.
	 * 
	 * @param current
	 *            the current
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @return the sets of read properties models.
	 */
	public static Set<PropertyModel<?>> unmarshal(Object current, HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Set<PropertyModel<?>> properties = new HashSet<PropertyModel<?>>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String type = reader.getNodeName();
			Class<?> clazz = null;
			// Java 7 String switch would be welcome
			if ("string".equals(type)) {
				clazz = StringPropertyModel.class;
			} else if ("int".equals(type)) {
				clazz = IntegerPropertyModel.class;
			} else if ("double".equals(type)) {
				clazz = DoublePropertyModel.class;
			} else if ("enum".equals(type)) {
				clazz = EnumPropertyModel.class;
			} else if ("bool".equals(type)) {
				clazz = BooleanPropertyModel.class;
			} else {
				throw new ConversionException("An unexpected property model was found: \"" + type + "\".");
			}
			final PropertyModel<?> propertyModel = (PropertyModel<?>) context.convertAnother(current, clazz);
			if (propertyModel == null) {
				throw new ConversionException("The property model of type \"" + type
						+ "\" cannot be properly unmarshalled.");
			}
			properties.add(propertyModel);
			reader.moveUp();
		}
		return properties;
	}
}