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
package fr.n7.saceca.u3du.model.io.ai.model.object.properties;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.object.properties.AbstractNumericPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.NumericPropertyModel;
import fr.n7.saceca.u3du.model.util.io.clazz.ComparableNumbers2NamesAndBack;

/**
 * A converter class for XStream to properly handle NumericPropertyModel.
 * 
 * @see AbstractNumericPropertyModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * 
 * @author Sylvain Cambon
 */
public abstract class NumericPropertyModelConverter implements Converter {
	
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		NumericPropertyModel<?> property = (AbstractNumericPropertyModel<?>) value;
		writer.addAttribute("name", property.getName());
		writer.addAttribute("min", property.getMinValue().toString());
		writer.addAttribute("max", property.getMaxValue().toString());
		writer.addAttribute("default", property.getDefaultValue().toString());
		writer.addAttribute("visibility", property.getVisibility().toString());
	}
	
	@Override
	public NumericPropertyModel<?> unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		String typeShortName = reader.getNodeName();
		Class<?> type = ComparableNumbers2NamesAndBack.getClassFor(typeShortName);
		String name = reader.getAttribute("name");
		String minString = reader.getAttribute("min");
		String maxString = reader.getAttribute("max");
		String defaultString = reader.getAttribute("default");
		Visibility visibility = Visibility.fromString(reader.getAttribute("visibility"));
		// Boilerplate code because of the poor generics design...
		if (type.equals(Double.class)) {
			Double min = new Double(minString);
			Double max = new Double(maxString);
			Double defaultValue = new Double(defaultString);
			return new DoublePropertyModel(name, min, max, defaultValue, visibility);
		} else if (type.equals(Integer.class)) {
			Integer min = new Integer(minString);
			Integer max = new Integer(maxString);
			Integer defaultValue = new Integer(defaultString);
			return new IntegerPropertyModel(name, min, max, defaultValue, visibility);
		} else {
			return null;
		}
	}
}