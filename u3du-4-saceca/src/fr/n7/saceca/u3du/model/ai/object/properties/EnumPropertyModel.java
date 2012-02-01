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
package fr.n7.saceca.u3du.model.ai.object.properties;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.ai.Visibility;

/**
 * A class modeling an enumeration property.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("enum")
public final class EnumPropertyModel extends AbstractPropertyModel<EnumElement> {
	
	/** The different possible values. */
	private Set<EnumElement> values = null;
	
	/**
	 * Instantiates a new enumeration property model.
	 * 
	 * @param name
	 *            The name.
	 * @param defaultValue
	 *            The default value, it has to be part of values.
	 * @param values
	 *            The possible values.
	 * @param visibility
	 *            the visibility
	 */
	public EnumPropertyModel(final String name, final EnumElement defaultValue, final Collection<EnumElement> values,
			Visibility visibility) {
		super(name, defaultValue, visibility);
		this.values = new HashSet<EnumElement>(values);
		this.setDefaultValue(defaultValue);
	}
	
	/**
	 * Instantiates a new enumeration property model.
	 * 
	 * @param name
	 *            The name.
	 * @param defaultValue
	 *            The default value, it has to be part of values.
	 * @param values
	 *            The possible values.
	 * @param visibility
	 *            the visibility
	 */
	public EnumPropertyModel(final String name, final EnumElement defaultValue, final EnumElement[] values,
			Visibility visibility) {
		super(name, null, visibility);
		this.values = new HashSet<EnumElement>(values.length);
		for (final EnumElement value : values) {
			this.values.add(value);
		}
		this.setDefaultValue(defaultValue);
	}
	
	/**
	 * Gets the possible values.
	 * 
	 * @return the values
	 */
	public final Set<EnumElement> getValues() {
		return this.values;
	}
	
	/**
	 * Checks the new value containment in the possible values.
	 * 
	 * @param newDefaultValue
	 *            The value to check.
	 * @return true, if is acceptable
	 */
	@Override
	public boolean isAcceptable(EnumElement newDefaultValue) {
		// If this.values is still null, this means that the initialization is
		// not yet over.
		return this.values == null || this.values.contains(newDefaultValue);
	}
	
	@Override
	public String toString() {
		return "EnumPropertyModel[" + this.getName() + ": defaultValue=" + this.getDefaultValue() + ", values="
				+ this.values + "]";
	}
	
	/**
	 * Helps to rebuild the object.
	 * 
	 * @return the corrected enum property model
	 */
	public EnumPropertyModel readResolve() {
		if (this.values == null) {
			this.values = new HashSet<EnumElement>();
		}
		return this;
	}
	
}
