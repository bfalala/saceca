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
package fr.n7.saceca.u3du.model.io.ai.model.object;

import java.util.Set;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.PropertyModelsConvertionHelper;

/**
 * A converter for a World Object Model.
 * 
 * @see WorldObjectModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * @author Sylvain Cambon
 */
public class WorldObjectModelConverter implements Converter {
	
	/** The Constant NAME_NODE. */
	private static final String NAME_ATTRIBUTE = "name";
	
	/** The Constant BEHAVIOR_NODE. */
	private static final String BEHAVIOR_NODE = "behavior";
	
	/** The Constant CATEGORIES_NODE. */
	private static final String CATEGORIES_NODE = "categories";
	
	/** The Constant CATEGORY_NODE. */
	private static final String CATEGORY_NODE = "category";
	
	/** The Constant SERVICES_NODE. */
	private static final String SERVICES_NODE = "services";
	
	/** The Constant SERVICE_NODE. */
	private static final String SERVICE_NODE = "service";
	
	/** The Constant PROPERTIES_NODE. */
	private static final String PROPERTIES_NODE = "properties";
	
	/**
	 * Can convert.
	 * 
	 * @param type
	 *            the type
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return WorldObjectModel.class.equals(type);
	}
	
	/**
	 * Marshal.
	 * 
	 * @param source
	 *            the source
	 * @param writer
	 *            the writer
	 * @param context
	 *            the context
	 */
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		WorldObjectModel model = (WorldObjectModel) source;
		writer.addAttribute(NAME_ATTRIBUTE, model.getName());
		
		// Behavior
		writer.startNode(BEHAVIOR_NODE);
		writer.setValue(model.getBehaviorName());
		writer.endNode();
		
		// Categories
		writer.startNode(CATEGORIES_NODE);
		for (String categoryName : model.getCategoriesNames()) {
			writer.startNode(CATEGORY_NODE);
			writer.setValue(categoryName);
			writer.endNode();
		}
		writer.endNode();
		
		// Services
		writer.startNode(SERVICES_NODE);
		for (String serviceName : model.getServicesNames()) {
			writer.startNode(SERVICE_NODE);
			writer.setValue(serviceName);
			writer.endNode();
		}
		writer.endNode();
		
		// Properties
		writer.startNode(PROPERTIES_NODE);
		PropertyModelsConvertionHelper.marshal(model.getProperties(), writer, context);
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
	public WorldObjectModel unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		// Name
		String name = this.readNameAttribute(reader);
		
		// Model creation
		WorldObjectModel model = new WorldObjectModel(name);
		
		// Behavior
		this.readBehaviorNode(reader, name, model);
		
		// Category models
		this.readCategoryModelsNode(reader, name, model);
		
		// Services
		this.readServicesNode(reader, name, model);
		
		// Properties
		this.readPropertiesNode(reader, context, name, model);
		
		return model;
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
	 * @param context
	 *            the context
	 * @param name
	 *            the name
	 * @param model
	 *            the model
	 */
	protected void readPropertiesNode(HierarchicalStreamReader reader, UnmarshallingContext context, String name,
			WorldObjectModel model) {
		String nodeName;
		reader.moveDown();
		nodeName = reader.getNodeName();
		if (!PROPERTIES_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + PROPERTIES_NODE + "\" but was instead: \""
					+ nodeName + "\" in the model \"" + name + "\".");
		}
		Set<PropertyModel<?>> propertyModels = PropertyModelsConvertionHelper.unmarshal(model, reader, context);
		if (propertyModels == null) {
			throw new ConversionException("The property models in the model \"" + name + "\" were null.");
		}
		model.getProperties().addAll(propertyModels);
		reader.moveUp();
	}
	
	/**
	 * <p>
	 * Reads the services node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param name
	 *            the name
	 * @param model
	 *            the model
	 */
	protected void readServicesNode(HierarchicalStreamReader reader, String name, WorldObjectModel model) {
		String nodeName;
		reader.moveDown();
		nodeName = reader.getNodeName();
		if (!SERVICES_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + SERVICES_NODE + "\" but was instead: \""
					+ nodeName + "\" in the model \"" + name + "\".");
		}
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			nodeName = reader.getNodeName();
			if (!SERVICE_NODE.equals(nodeName)) {
				throw new ConversionException("The expected node was \"service\" but was instead: \"" + nodeName
						+ "\" in the model \"" + name + "\".");
			}
			String service = reader.getValue();
			if (service == null) {
				throw new ConversionException("A service name in the model \"" + name + "\" was null.");
			}
			model.getServicesNames().add(service);
			reader.moveUp();
		}
		reader.moveUp();
	}
	
	/**
	 * <p>
	 * Reads the category models node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param name
	 *            the name
	 * @param model
	 *            the model
	 */
	protected void readCategoryModelsNode(HierarchicalStreamReader reader, String name, WorldObjectModel model) {
		String nodeName;
		reader.moveDown();
		nodeName = reader.getNodeName();
		if (!CATEGORIES_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + CATEGORIES_NODE + "\" but was instead: \""
					+ nodeName + "\" in the model \"" + name + "\".");
		}
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			nodeName = reader.getNodeName();
			if (!"category".equals(nodeName)) {
				throw new ConversionException("The expected node was \"category\" but was instead: \"" + nodeName
						+ "\" in the model \"" + name + "\".");
			}
			String category = reader.getValue();
			if (category == null) {
				throw new ConversionException("A category name in the model \"" + name + "\" was null.");
			}
			model.getCategoriesNames().add(category);
			reader.moveUp();
		}
		reader.moveUp();
	}
	
	/**
	 * <p>
	 * Reads the behavior node.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @param name
	 *            the name
	 * @param model
	 *            the model
	 */
	protected void readBehaviorNode(HierarchicalStreamReader reader, String name, WorldObjectModel model) {
		reader.moveDown();
		String nodeName = reader.getNodeName();
		if (!BEHAVIOR_NODE.equals(nodeName)) {
			throw new ConversionException("The expected node was \"" + BEHAVIOR_NODE + "\" but was instead: \""
					+ nodeName + "\" in the model \"" + name + "\".");
		}
		String behaviorName = reader.getValue();
		if (behaviorName == null) {
			throw new ConversionException("The behavior name in the model \"" + name + "\" was null.");
		}
		model.setBehaviorName(behaviorName);
		reader.moveUp();
	}
	
	/**
	 * <p>
	 * Reads the name attribute.
	 * </p>
	 * <p>
	 * A ConversionException is thrown if a problem arose during the conversion.
	 * </p>
	 * 
	 * @param reader
	 *            the reader
	 * @return the string
	 */
	protected String readNameAttribute(HierarchicalStreamReader reader) {
		String name = reader.getAttribute(NAME_ATTRIBUTE);
		if (name == null) {
			throw new ConversionException("The attribute \"" + NAME_ATTRIBUTE + "\" was null in a model.");
		}
		return name;
	}
}
