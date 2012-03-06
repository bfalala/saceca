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
package fr.n7.saceca.u3du.model.io.ai.model.agent;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.PropertyModelsConvertionHelper;

/**
 * A converter for an Agent Model.
 * 
 * @see AgentModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * @author Sylvain Cambon
 */
public class AgentModelConverter implements Converter {
	
	/** The Constant NAME_ATTRIBUTE. */
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
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return AgentModel.class.equals(type);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// TODO upgrade when the behavior instantiates the modules
		AgentModel model = (AgentModel) source;
		writer.addAttribute(NAME_ATTRIBUTE, model.getName());
		
		// Behavior and modules
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
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String name = reader.getAttribute(NAME_ATTRIBUTE);
		
		AgentModel model = new AgentModel(name);
		
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String nodeName = reader.getNodeName();
			
			if (BEHAVIOR_NODE.equals(nodeName)) {
				// Behavior
				String behaviorName = reader.getValue();
				model.setBehaviorName(behaviorName);
				
			} else if (CATEGORIES_NODE.equals(nodeName)) {
				// Categories
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					model.getCategoriesNames().add(reader.getValue());
					reader.moveUp();
				}
				
			} else if (SERVICES_NODE.equals(nodeName)) {
				// Services
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					model.getServicesNames().add(reader.getValue());
					reader.moveUp();
				}
				
			} else if (PROPERTIES_NODE.equals(nodeName)) {
				// Properties
				model.getProperties().addAll(PropertyModelsConvertionHelper.unmarshal(model, reader, context));
			}
			reader.moveUp();
		}
		
		return model;
	}
}