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

import fr.n7.saceca.u3du.model.graphics.configuration.AgentConfiguration;

/**
 * A class to convert configurations of agents.
 * 
 * @author Sylvain Cambon
 */
public class AgentConfigurationConverter extends SpatialConfigurationConverter {
	
	/** The Constant EYES_HEIGHT_NODE. */
	private static final String EYES_HEIGHT_NODE = "eyes-height";
	
	/** The Constant Y_ATTRIBUTE. */
	private static final String Y_ATTRIBUTE = "y";
	
	/**
	 * Can convert.
	 * 
	 * @param otherClass
	 *            the other class
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class otherClass) {
		return AgentConfiguration.class.equals(otherClass);
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
		AgentConfiguration conf = (AgentConfiguration) object;
		super.marshal(conf, writer, context);
		
		writer.startNode(EYES_HEIGHT_NODE);
		writer.addAttribute(Y_ATTRIBUTE, "" + conf.getEyesHeight());
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
		AgentConfiguration conf = new AgentConfiguration();
		super.unmarshal(conf, reader, context);
		
		reader.moveDown();
		float z = Float.parseFloat(reader.getAttribute(Y_ATTRIBUTE));
		conf.setEyesHeight(z);
		reader.moveUp();
		
		return conf;
	}
}
