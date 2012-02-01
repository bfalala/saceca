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
package fr.n7.saceca.u3du.model.io.graphics;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.graphics.configuration.ShapeConfiguration;

/**
 * A class to convert configurations of texture-using shapes.
 * 
 * @author Sylvain Cambon
 */
public class ShapeConfigurationConverter extends ConfigurationConverter {
	
	/** The Constant TEXTURE_NODE. */
	private static final String TEXTURE_NODE = "texture-path";
	
	/**
	 * Marshals.
	 * 
	 * @param conf
	 *            the conf
	 * @param writer
	 *            the writer
	 * @param context
	 *            the context
	 */
	protected void marshal(ShapeConfiguration conf, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshal(conf, writer, context);
		
		writer.startNode(TEXTURE_NODE);
		writer.setValue(conf.getPathTexture());
		writer.endNode();
	}
	
	/**
	 * Unmarshals.
	 * 
	 * @param conf
	 *            the conf
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 */
	protected void unmarshal(ShapeConfiguration conf, HierarchicalStreamReader reader, UnmarshallingContext context) {
		super.unmarshal(conf, reader, context);
		
		reader.moveDown();
		String texture = reader.getValue();
		conf.setPathTexture(texture);
		reader.moveUp();
	}
}
