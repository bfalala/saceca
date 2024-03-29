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
package fr.n7.saceca.u3du.model.io.ai.category;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import fr.n7.saceca.u3du.model.ai.category.Category;

/**
 * A class to convert Categories.
 * 
 * @see Category
 * @see <a href=
 *      "http://xstream.codehaus.org/javadoc/com/thoughtworks/xstream/converters/Converter.html"
 *      >com.thoughtworks.xstream.converters.Converter</a>
 * @author Sylvain Cambon
 */
public class CategoryConverter extends AbstractSingleValueConverter {
	
	/**
	 * Can convert.
	 * 
	 * @param type
	 *            the type
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return Category.class.equals(type);
	}
	
	/**
	 * From string.
	 * 
	 * @param str
	 *            the str
	 * @return the object
	 */
	@Override
	public Object fromString(String str) {
		return new Category(str);
	}
	
}