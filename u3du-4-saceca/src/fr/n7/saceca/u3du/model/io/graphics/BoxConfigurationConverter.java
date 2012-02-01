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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.graphics.configuration.BoxConfiguration;

/**
 * A class to convert configurations of boxes.
 * 
 * @author Sylvain Cambon
 */
public class BoxConfigurationConverter extends ShapeConfigurationConverter implements Converter {
	
	/** The Constant DIMENSIONS_NODE. */
	private static final String DIMENSIONS_NODE = "dimensions";
	
	/** The Constant WIDTH_ATTRIBUTE. */
	private static final String WIDTH_ATTRIBUTE = "width";
	
	/** The Constant LENGTH_ATTRIBUTE. */
	private static final String LENGTH_ATTRIBUTE = "length";
	
	/** The Constant HEIGHT_ATTRIBUTE. */
	private static final String HEIGHT_ATTRIBUTE = "height";
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class otherClass) {
		return BoxConfiguration.class.equals(otherClass);
	}
	
	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		BoxConfiguration conf = (BoxConfiguration) object;
		
		super.marshal(conf, writer, context);
		
		writer.startNode(DIMENSIONS_NODE);
		writer.addAttribute(WIDTH_ATTRIBUTE, "" + conf.getWidth());
		writer.addAttribute(LENGTH_ATTRIBUTE, "" + conf.getLength());
		writer.addAttribute(HEIGHT_ATTRIBUTE, "" + conf.getHeight());
		writer.endNode();
	}
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		BoxConfiguration conf = new BoxConfiguration();
		super.unmarshal(conf, reader, context);
		
		// DIMENSIONS_NODE
		reader.moveDown();
		float width = Float.parseFloat(reader.getAttribute(WIDTH_ATTRIBUTE));
		conf.setWidth(width);
		float length = Float.parseFloat(reader.getAttribute(LENGTH_ATTRIBUTE));
		conf.setLength(length);
		float height = Float.parseFloat(reader.getAttribute(HEIGHT_ATTRIBUTE));
		conf.setHeight(height);
		reader.moveUp();
		
		return conf;
	}
	
}
