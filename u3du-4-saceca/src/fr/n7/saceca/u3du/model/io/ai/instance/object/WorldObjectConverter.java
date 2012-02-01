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
package fr.n7.saceca.u3du.model.io.ai.instance.object;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamHelper;
import fr.n7.saceca.u3du.model.util.IDProvider;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * A world object instance converter.
 * 
 * @author Sylvain Cambon
 */
public class WorldObjectConverter implements Converter {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(WorldObjectConverter.class);
	
	/** The Constant ID_ATTRIBUTE. */
	private static final String ID_ATTRIBUTE = "id";
	
	/** The Constant MODEL_ATTRIBUTE. */
	private static final String MODEL_ATTRIBUTE = "model";
	
	/** The Constant POSITION_NODE. */
	private static final String POSITION_NODE = "position";
	
	/** The Constant PROPERTIES_NODE. */
	private static final String PROPERTIES_NODE = "properties";
	
	/** The Constant NAME_ATTRIBUTE. */
	private static final String NAME_ATTRIBUTE = "name";
	
	/** The Constant VALUE_ATTRIBUTE. */
	private static final String VALUE_ATTRIBUTE = "value";
	
	/** The Constant BELONGINGS_NODE. */
	private static final String BELONGINGS_NODE = "belongings";
	
	/** The Constant BELONGING_NODE. */
	private static final String BELONGING_NODE = "belonging";
	
	/** The factory. */
	private EntitiesFactory factory;
	
	/**
	 * Instantiates a new world object converter.
	 * 
	 * @param factory
	 *            the factory
	 */
	public WorldObjectConverter(EntitiesFactory factory) {
		super();
		this.factory = factory;
	}
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return WorldObject.class.equals(type);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		WorldObject object = (WorldObject) source;
		
		writer.addAttribute(MODEL_ATTRIBUTE, object.getModelName());
		writer.addAttribute(ID_ATTRIBUTE, "" + object.getId());
		
		writer.startNode(POSITION_NODE);
		context.convertAnother(object.getPosition());
		writer.endNode();
		
		writer.startNode(PROPERTIES_NODE);
		for (String propertyName : object.getPropertiesContainer().getPropertiesNames()) {
			try {
				Property<?> property = object.getPropertiesContainer().getProperty(null, propertyName);
				String nodeName = XStreamHelper.getClassAlias(property.getModel().getClass());
				writer.startNode(nodeName);
				writer.addAttribute(NAME_ATTRIBUTE, property.getModel().getName());
				writer.addAttribute(VALUE_ATTRIBUTE, property.getValue().toString());
				writer.endNode();
			} catch (UnknownPropertyException e) {
				logger.error("The property " + propertyName + " could not be found.", e);
			}
		}
		writer.endNode();
		
		writer.startNode(BELONGINGS_NODE);
		for (WorldObject belonging : object.getBelongings()) {
			writer.startNode(BELONGING_NODE);
			writer.addAttribute(ID_ATTRIBUTE, "" + belonging.getId());
			writer.endNode();
		}
		writer.endNode();
		
	}
	
	/**
	 * <p>
	 * Reads a world object instance from XML. The belongings cannot be completely initialized, thus
	 * a later binding mechanism is required. World objects which model is null have to be replaced
	 * by the instances having the same ID. This mechanism is implemented in the
	 * <code>EntitiesFactory.repairBelongings</code>.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @see EntitiesFactory#repairBelongings(java.util.Collection)
	 * @param reader
	 *            the XStream reader
	 * @param context
	 *            the XStream context
	 * @return the read object
	 * @throws
	 */
	@Override
	public WorldObject unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		// Model
		String modelName = this.readModelAttribute(reader);
		
		// Building the initial object
		WorldObject object = this.factory.createWorldObject(modelName);
		if (object == null) {
			throw new ConversionException("The factory produced a null object.");
		}
		
		// The following read methods read and set the value in the object.
		
		// ID
		long id = this.readIdAttribute(reader, modelName, object);
		IDProvider.INSTANCE.tellAboutUsage(id);
		
		// The short string representation can be built to help debugging the correct xml file.
		String shortString = modelName + " (id=" + id + ")";
		
		// Position
		this.readPositionNode(reader, context, object, shortString);
		
		// Properties
		this.readPropertiesNode(reader, object, shortString);
		
		// Belongings
		this.readBelongingsNode(reader, object, shortString);
		
		return object;
	}
	
	/**
	 * <p>
	 * Reads the model attribute.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @return the string
	 */
	protected String readModelAttribute(HierarchicalStreamReader reader) {
		String modelName = reader.getAttribute(MODEL_ATTRIBUTE);
		if (modelName == null) {
			throw new ConversionException("The entity \"" + MODEL_ATTRIBUTE + "\" attribute was null.");
		}
		return modelName;
	}
	
	/**
	 * <p>
	 * Reads the id attribute.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param modelName
	 *            the model name
	 * @param object
	 *            the object
	 * @return the long
	 */
	protected long readIdAttribute(HierarchicalStreamReader reader, String modelName, WorldObject object) {
		String idString = reader.getAttribute(ID_ATTRIBUTE);
		if (idString == null) {
			throw new ConversionException("The entity \"" + ID_ATTRIBUTE + "\" attribute was null.");
		}
		long id;
		try {
			id = Long.parseLong(idString);
			object.setId(id);
		} catch (NumberFormatException nfe) {
			throw new ConversionException("The entity \"" + ID_ATTRIBUTE
					+ "\" attribute could not be parsed to a long in an instance of \"" + modelName + "\": \""
					+ idString + "\".", nfe);
		}
		return id;
	}
	
	/**
	 * <p>
	 * Reads the belongings node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param object
	 *            the object
	 * @param shortString
	 *            the short string
	 */
	protected void readBelongingsNode(HierarchicalStreamReader reader, WorldObject object, String shortString) {
		String nodeName;
		reader.moveDown();
		nodeName = reader.getNodeName();
		if (!BELONGINGS_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + BELONGINGS_NODE + "\" but was instead: \""
					+ nodeName + "\" in " + shortString + ".");
		}
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			nodeName = reader.getNodeName();
			if (!BELONGING_NODE.equals(nodeName)) {
				throw new ConversionException("The expected node was \"" + BELONGING_NODE + "\" but was instead: \""
						+ nodeName + "\" in " + shortString + ".");
			}
			String belongingIdString = reader.getAttribute(ID_ATTRIBUTE);
			if (belongingIdString == null) {
				throw new ConversionException("The \"" + ID_ATTRIBUTE + "\" attribute in the \"" + BELONGING_NODE
						+ "\" node of " + shortString + " is null.");
			}
			long belongingId;
			try {
				belongingId = new Long(belongingIdString);
			} catch (NumberFormatException nfe) {
				throw new ConversionException("The value of the \"" + ID_ATTRIBUTE + "\" attribute in the \""
						+ BELONGING_NODE + "\" node of " + shortString + " could not be parsed to a long: "
						+ belongingIdString + ".", nfe);
			}
			WorldObject objectToBeResolvedLater = new WorldObject(null, belongingId);
			object.getBelongings().add(objectToBeResolvedLater);
			reader.moveUp();
		}
		reader.moveUp();
	}
	
	/**
	 * <p>
	 * Reads the properties node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param object
	 *            the object
	 * @param shortString
	 *            the short string
	 */
	protected void readPropertiesNode(HierarchicalStreamReader reader, WorldObject object, String shortString) {
		String nodeName;
		reader.moveDown();
		nodeName = reader.getNodeName();
		if (!PROPERTIES_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + PROPERTIES_NODE + "\" but was instead: \""
					+ nodeName + "\" in " + shortString + ".");
		}
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String type = reader.getNodeName();
			String name = reader.getAttribute(NAME_ATTRIBUTE);
			if (name == null) {
				throw new ConversionException("The name of a property in " + shortString + " was null.");
			}
			String valueString = reader.getAttribute(VALUE_ATTRIBUTE);
			if (valueString == null) {
				throw new ConversionException("The value of a property in " + shortString + " was null");
			}
			try {
				// "String switch" on the type to build the correct property.
				// Java 7 String switch would be welcome
				if ("string".equals(type)) {
					object.getPropertiesContainer().setString(name, valueString);
				} else if ("int".equals(type)) {
					Integer value = new Integer(valueString);
					object.getPropertiesContainer().setInt(name, value);
				} else if ("double".equals(type)) {
					Double value = new Double(valueString);
					object.getPropertiesContainer().setDouble(name, value);
				} else if ("enum".equals(type)) {
					EnumElement value = new EnumElement(valueString);
					object.getPropertiesContainer().setEnumElement(name, value);
				} else if ("bool".equals(type)) {
					Boolean value = Boolean.valueOf(valueString);
					object.getPropertiesContainer().setBoolean(name, value);
				} else {
					throw new ConversionException("No known attribute type could be retrieved: found \"" + type + "\".");
				}
			} catch (UnknownPropertyException e) {
				throw new ConversionException("The property \"" + name + "\" could not be found in " + shortString
						+ ".", e);
			}
			reader.moveUp();
		}
		reader.moveUp();
	}
	
	/**
	 * <p>
	 * Reads the position node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param context
	 *            the context
	 * @param object
	 *            the object
	 * @param shortString
	 *            the short string
	 */
	protected void readPositionNode(HierarchicalStreamReader reader, UnmarshallingContext context, WorldObject object,
			String shortString) {
		reader.moveDown();
		String nodeName = reader.getNodeName();
		if (!POSITION_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + POSITION_NODE + "\" but was instead: \""
					+ nodeName + "\" in " + shortString + ".");
		}
		Oriented2DPosition position = (Oriented2DPosition) context.convertAnother(object, Oriented2DPosition.class);
		if (position == null) {
			throw new ConversionException("The position could not be parsed properly in " + shortString + ".");
		}
		object.setPosition(position);
		reader.moveUp();
	}
	
	/**
	 * Gets the factory.
	 * 
	 * @return the factory
	 */
	public final EntitiesFactory getFactory() {
		return this.factory;
	}
	
}
