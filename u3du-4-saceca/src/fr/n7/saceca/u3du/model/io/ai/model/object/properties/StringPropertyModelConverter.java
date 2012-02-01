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
package fr.n7.saceca.u3du.model.io.ai.model.object.properties;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;

/**
 * A converter class for XStream to properly handle StringPropertyModel.
 * 
 * @see StringPropertyModel
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * @author Sylvain Cambon
 */
public class StringPropertyModelConverter extends AbstractPropertyModelConverter {
	
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return StringPropertyModel.class.equals(clazz);
	}
	
	@Override
	public StringPropertyModel unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String name = reader.getAttribute("name");
		String defaultValue = reader.getAttribute("default");
		Visibility visibility = Visibility.fromString(reader.getAttribute("visibility"));
		StringPropertyModel spm = new StringPropertyModel(name, defaultValue, visibility);
		return spm;
	}
}
