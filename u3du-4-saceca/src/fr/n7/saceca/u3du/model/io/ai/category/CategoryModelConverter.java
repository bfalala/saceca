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
package fr.n7.saceca.u3du.model.io.ai.category;

import java.util.HashSet;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.category.CategoryModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.PropertyModelsConvertionHelper;

/**
 * A converter for a Category Model.
 * 
 * @see CategoryModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * @author Sylvain Cambon
 */
public class CategoryModelConverter implements Converter {
	
	/** The Constant NAME_ATTRIBUTE. */
	private static final String NAME_ATTRIBUTE = "name";
	
	/** The Constant PROPERTIES_NODE. */
	private static final String PROPERTIES_NODE = "properties";
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return CategoryModel.class.equals(type);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		CategoryModel model = (CategoryModel) source;
		writer.addAttribute(NAME_ATTRIBUTE, model.getName());
		
		// Properties
		writer.startNode(PROPERTIES_NODE);
		PropertyModelsConvertionHelper.marshal(model.getProperties(), writer, context);
		writer.endNode();
	}
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String name = reader.getAttribute(NAME_ATTRIBUTE);
		if (name == null) {
			throw new ConversionException("The category model has no \"" + NAME_ATTRIBUTE + "\" attribute.");
		}
		CategoryModel model = new CategoryModel(name, new HashSet<PropertyModel<?>>());
		
		// Properties
		reader.moveDown();
		model.getProperties().addAll(PropertyModelsConvertionHelper.unmarshal(model, reader, context));
		reader.moveUp();
		
		return model;
	}
	
}
