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
package fr.n7.saceca.u3du.model.ai.category;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * The model for the categories.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("category-model")
public class CategoryModel implements Storable {
	
	/** The name. */
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;
	
	/** The properties. */
	private Set<PropertyModel<?>> properties;
	
	/**
	 * Instantiates a new category model.
	 * 
	 * @param name
	 *            the name
	 * @param properties
	 *            the properties
	 */
	public CategoryModel(String name, Set<PropertyModel<?>> properties) {
		super();
		this.name = name;
		this.properties = properties;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		CategoryModel other = (CategoryModel) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!this.properties.equals(other.properties)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public final Set<PropertyModel<?>> getProperties() {
		return this.properties;
	}
	
	@Override
	public String getStorageLabel() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.properties == null) ? 0 : this.properties.hashCode());
		return result;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the properties.
	 * 
	 * @param properties
	 *            the new properties
	 */
	public final void setProperties(Set<PropertyModel<?>> properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("CategoryModel ");
		sb.append(this.name);
		sb.append(" =");
		for (PropertyModel<?> prop : this.properties) {
			sb.append("\n\t" + prop.toString());
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * Helps to rebuild correctly from serialized versions.
	 * 
	 * @return the corrected object.
	 */
	public CategoryModel readResolve() {
		if (this.properties == null) {
			this.properties = new HashSet<PropertyModel<?>>();
		}
		return this;
	}
	
}
