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
package fr.n7.saceca.u3du.model.io.graphics;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.graphics.configuration.VehicleConfiguration;

/**
 * A class to convert configurations of vehicles.
 * 
 * @author Sylvain Cambon
 */
public class VehicleConfigurationConverter extends SpatialConfigurationConverter {
	
	/** The Constant STIFFNESS_NODE. */
	private static final String COLLISION_SHAPE_RADIUS_NODE = "collision-shape-radius";
	
	/** The Constant COMPRESSION_VALUE_NODE. */
	private static final String COLLISION_SHAPE_HEIGHT_NODE = "collision-shape-height";
	
	/** The Constant DAMPING_NODE. */
	private static final String STEP_HEIGHT_NODE = "step-height";
	
	/** The Constant VALUE_ATTRIBUTE. */
	private static final String VALUE_ATTRIBUTE = "value";
	
	/**
	 * Can convert.
	 * 
	 * @param otherClass
	 *            the other class
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class otherClass) {
		return VehicleConfiguration.class.equals(otherClass);
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
		VehicleConfiguration conf = (VehicleConfiguration) object;
		super.marshal(conf, writer, context);
		
		writer.startNode(COLLISION_SHAPE_RADIUS_NODE);
		writer.addAttribute(VALUE_ATTRIBUTE, "" + conf.getCollisionShapeRadius());
		writer.endNode();
		
		writer.startNode(COLLISION_SHAPE_HEIGHT_NODE);
		writer.addAttribute(VALUE_ATTRIBUTE, "" + conf.getCollisionShapeHeight());
		writer.endNode();
		
		writer.startNode(STEP_HEIGHT_NODE);
		writer.addAttribute(VALUE_ATTRIBUTE, "" + conf.getStepHeight());
		writer.endNode();
	}
	
	/**
	 * Unmarshal.
	 * 
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @return the object
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		VehicleConfiguration conf = new VehicleConfiguration();
		super.unmarshal(conf, reader, context);
		
		reader.moveDown();
		float collisionShapeRadius = Float.parseFloat(reader.getAttribute(VALUE_ATTRIBUTE));
		conf.setCollisionShapeRadius(collisionShapeRadius);
		reader.moveUp();
		
		reader.moveDown();
		float collisionShapeHeight = Float.parseFloat(reader.getAttribute(VALUE_ATTRIBUTE));
		conf.setCollisionShapeHeight(collisionShapeHeight);
		reader.moveUp();
		
		reader.moveDown();
		float stepHeight = Float.parseFloat(reader.getAttribute(VALUE_ATTRIBUTE));
		conf.setStepHeight(stepHeight);
		reader.moveUp();
		
		return conf;
	}
}