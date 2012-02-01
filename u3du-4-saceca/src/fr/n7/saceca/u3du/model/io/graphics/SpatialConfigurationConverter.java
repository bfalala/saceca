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

import fr.n7.saceca.u3du.model.graphics.configuration.SpatialConfiguration;

/**
 * A class to convert configurations of spatials.
 * 
 * @author Sylvain Cambon
 */
public class SpatialConfigurationConverter extends ConfigurationConverter implements Converter {
	
	/** The Constant MODEL_NODE. */
	private static final String MODEL_NODE = "model-path";
	
	/** The Constant SCALE_NODE. */
	private static final String SCALE_NODE = "scale";
	
	/** The Constant SCALE_X_ATTRIBUTE. */
	private static final String SCALE_X_ATTRIBUTE = "x";
	
	/** The Constant SCALE_Y_ATTRIBUTE. */
	private static final String SCALE_Y_ATTRIBUTE = "y";
	
	/** The Constant SCALE_Z_ATTRIBUTE. */
	private static final String SCALE_Z_ATTRIBUTE = "z";
	
	/** The Constant CHILDREN_NODE. */
	private static final String CHILDREN_NODE = "children";
	
	/**
	 * Can convert.
	 * 
	 * @param otherClass
	 *            the other class
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class otherClass) {
		return SpatialConfiguration.class.equals(otherClass);
	}
	
	/**
	 * Marshals. Children are accepted.
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
		SpatialConfiguration conf = (SpatialConfiguration) object;
		this.marshal(conf, writer, context);
		
		writer.startNode(CHILDREN_NODE);
		for (SpatialConfiguration child : conf.getChildren().values()) {
			context.convertAnother(child);
		}
		writer.endNode();
	}
	
	/**
	 * Marshals for extending classes. There should be no children!
	 * 
	 * @param conf
	 *            the conf
	 * @param writer
	 *            the writer
	 * @param context
	 *            the context
	 */
	protected void marshal(SpatialConfiguration conf, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshal(conf, writer, context);
		writer.startNode(MODEL_NODE);
		writer.setValue(conf.getPathModel());
		writer.endNode();
		
		writer.startNode(SCALE_NODE);
		writer.addAttribute(SCALE_X_ATTRIBUTE, "" + conf.getScaleX());
		writer.addAttribute(SCALE_Y_ATTRIBUTE, "" + conf.getScaleY());
		writer.addAttribute(SCALE_Z_ATTRIBUTE, "" + conf.getScaleZ());
		writer.endNode();
	}
	
	/**
	 * Unmarshals.
	 * 
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @return the object
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		SpatialConfiguration conf = new SpatialConfiguration();
		this.unmarshal(conf, reader, context);
		
		// CHILDREN_NODE
		reader.moveDown();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			SpatialConfiguration child = (SpatialConfiguration) context
					.convertAnother(conf, SpatialConfiguration.class);
			conf.getChildren().put(child.getName(), child);
			reader.moveUp();
		}
		reader.moveUp();
		
		return conf;
	}
	
	/**
	 * Unmarshals for extending classes.
	 * 
	 * @param conf
	 *            the conf
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @return the object
	 */
	protected Object unmarshal(SpatialConfiguration conf, HierarchicalStreamReader reader, UnmarshallingContext context) {
		super.unmarshal(conf, reader, context);
		
		reader.moveDown();
		String modelPath = reader.getValue();
		conf.setPathModel(modelPath);
		reader.moveUp();
		
		// SCALE_NODE
		reader.moveDown();
		float scaleX = Float.parseFloat(reader.getAttribute(SCALE_X_ATTRIBUTE));
		conf.setScaleX(scaleX);
		float scaleY = Float.parseFloat(reader.getAttribute(SCALE_Y_ATTRIBUTE));
		conf.setScaleY(scaleY);
		float scaleZ = Float.parseFloat(reader.getAttribute(SCALE_Z_ATTRIBUTE));
		conf.setScaleZ(scaleZ);
		reader.moveUp();
		
		return conf;
	}
	
}
