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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;

/**
 * A converter class for XStream to properly handle EnumPropertyModel.
 * 
 * @see EnumPropertyModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * 
 * @author Sylvain Cambon
 */
public class EnumPropertyModelConverter implements Converter {
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return EnumPropertyModel.class.equals(clazz);
	}
	
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		EnumPropertyModel property = (EnumPropertyModel) value;
		writer.addAttribute("name", property.getName());
		String valuesString = Arrays.toString(property.getValues().toArray());
		writer.addAttribute("values", valuesString.substring(1, valuesString.length() - 1));
		writer.addAttribute("default", property.getDefaultValue().toString());
		writer.addAttribute("visibility", property.getVisibility().toString());
	}
	
	@Override
	public EnumPropertyModel unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String name = reader.getAttribute("name");
		String valuesString = reader.getAttribute("values");
		EnumElement defaultValue = null;
		
		String defaultValueString = reader.getAttribute("default");
		
		Collection<EnumElement> values = new ArrayList<EnumElement>();
		for (String valueName : valuesString.split(", ")) {
			EnumElement value = new EnumElement(valueName);
			values.add(value);
			if (valueName.equals(defaultValueString)) {
				defaultValue = value;
			}
		}
		
		Visibility visibility = Visibility.fromString(reader.getAttribute("visibility"));
		EnumPropertyModel spm = new EnumPropertyModel(name, defaultValue, values, visibility);
		return spm;
	}
}