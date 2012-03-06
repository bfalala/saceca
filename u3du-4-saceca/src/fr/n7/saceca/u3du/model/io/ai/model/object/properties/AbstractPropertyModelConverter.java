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

import fr.n7.saceca.u3du.model.ai.object.properties.AbstractPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;

/**
 * A common XStream converter for simple classes using only the fields provided by
 * AbstractPropertyModel.
 * 
 * @author Sylvain Cambon
 * 
 * @see AbstractPropertyModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 */
public abstract class AbstractPropertyModelConverter implements Converter {
	
	@Override
	public abstract boolean canConvert(@SuppressWarnings("rawtypes") Class clazz);
	
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		PropertyModel<?> property = (AbstractPropertyModel<?>) value;
		writer.addAttribute("name", property.getName());
		writer.addAttribute("default", property.getDefaultValue().toString());
		writer.addAttribute("visibility", property.getDefaultValue().toString());
	}
	
	@Override
	public abstract PropertyModel<?> unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context);
}