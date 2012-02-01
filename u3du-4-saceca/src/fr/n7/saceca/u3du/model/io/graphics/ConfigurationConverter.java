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

import fr.n7.saceca.u3du.model.graphics.configuration.Configuration;

/**
 * A class to convert the common part of the configuration.
 * 
 * @author Sylvain Cambon
 */
public class ConfigurationConverter {
	
	/** The Constant NAME_ATTRIBUTE. */
	private static final String NAME_ATTRIBUTE = "name";
	
	/** The Constant VISIBLE_ATTRIBUTE. */
	private static final String VISIBLE_ATTRIBUTE = "visible";
	
	/** The Constant PERCEPTIBLE_ATTRIBUTE. */
	private static final String PERCEPTIBLE_ATTRIBUTE = "perceptible";
	
	/** The Constant NORMALIZATION_NODE. */
	private static final String NORMALIZATION_NODE = "normalization";
	
	/** The Constant ELEVATION_ATTRIBUTE. */
	private static final String ELEVATION_ATTRIBUTE = "elevation";
	
	/** The Constant ANGLE_ATTRIBUTE. */
	private static final String ANGLE_ATTRIBUTE = "angle";
	
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
	protected void marshal(Configuration conf, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.addAttribute(NAME_ATTRIBUTE, conf.getName());
		writer.addAttribute(VISIBLE_ATTRIBUTE, "" + conf.isVisible());
		writer.addAttribute(PERCEPTIBLE_ATTRIBUTE, "" + conf.isPerceptible());
		
		writer.startNode(NORMALIZATION_NODE);
		writer.addAttribute(ELEVATION_ATTRIBUTE, "" + conf.getElevation());
		writer.addAttribute(ANGLE_ATTRIBUTE, "" + conf.getCorrectiveAngle());
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
	protected void unmarshal(Configuration conf, HierarchicalStreamReader reader, UnmarshallingContext context) {
		String name = reader.getAttribute(NAME_ATTRIBUTE);
		conf.setName(name);
		boolean visible = Boolean.parseBoolean(reader.getAttribute(VISIBLE_ATTRIBUTE));
		conf.setVisible(visible);
		boolean perceptible = Boolean.parseBoolean(reader.getAttribute(PERCEPTIBLE_ATTRIBUTE));
		conf.setPerceptible(perceptible);
		
		// NORMALIZATION_NODE
		reader.moveDown();
		float zOffset = Float.parseFloat(reader.getAttribute(ELEVATION_ATTRIBUTE));
		conf.setElevation(zOffset);
		float angle = Float.parseFloat(reader.getAttribute(ANGLE_ATTRIBUTE));
		conf.setCorrectiveAngle(angle);
		reader.moveUp();
	}
}
