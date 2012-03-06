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
package fr.n7.saceca.u3du.model.ai.object;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * A class for modeling a world object.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("object-model")
public class WorldObjectModel implements Storable {
	
	/** The name of the world object. */
	@XStreamAsAttribute
	private String name;
	
	/** The behavior of the world object. */
	@XStreamAlias("behavior")
	private String behaviorName;
	
	/** The names of the world object categoriesNames. */
	@XStreamAlias("cats")
	private Set<String> categoriesNames = new HashSet<String>();
	
	/** The names of the services provided by the world object. */
	@XStreamImplicit(itemFieldName = "serv")
	private Set<String> servicesNames = new HashSet<String>();
	
	/** The properties of the world object. */
	@XStreamAlias("props")
	private Set<PropertyModel<?>> properties = new HashSet<PropertyModel<?>>();
	
	/**
	 * Instantiates a new world object model.
	 * 
	 * @param name
	 *            The name.
	 */
	public WorldObjectModel(final String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Gets the behavior of the world object.
	 * 
	 * @return the behavior of the world object
	 */
	public final String getBehaviorName() {
		return this.behaviorName;
	}
	
	/**
	 * Gets the categoriesNames of the world object.
	 * 
	 * @return The world object categoriesNames
	 */
	public final Set<String> getCategoriesNames() {
		return this.categoriesNames;
	}
	
	/**
	 * Gets the name of the world object.
	 * 
	 * @return the name of the world object
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Gets the properties of the world object.
	 * 
	 * @return the properties of the world object
	 */
	public final Set<PropertyModel<?>> getProperties() {
		return this.properties;
	}
	
	/**
	 * Gets the names of the services provided by the world object.
	 * 
	 * @return the names of the services provided by the world object
	 */
	public final Set<String> getServicesNames() {
		return this.servicesNames;
	}
	
	/**
	 * Sets the name of the world object.
	 * 
	 * @param name
	 *            the new name of the world object
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the behavior of the world object.
	 * 
	 * @param behaviorName
	 *            the new behavior of the world object
	 */
	public final void setBehaviorName(String behaviorName) {
		this.behaviorName = behaviorName;
	}
	
	/**
	 * Sets the names of the world object categoriesNames.
	 * 
	 * @param categoriesNames
	 *            the new names of the world object categoriesNames
	 */
	public final void setCategoriesNames(Set<String> categoriesNames) {
		this.categoriesNames = categoriesNames;
	}
	
	/**
	 * Sets the names of the services provided by the world object.
	 * 
	 * @param servicesNames
	 *            the new names of the services provided by the world object
	 */
	public final void setServicesNames(Set<String> servicesNames) {
		this.servicesNames = servicesNames;
	}
	
	/**
	 * Sets the properties of the world object.
	 * 
	 * @param properties
	 *            the new properties of the world object
	 */
	public final void setProperties(Set<PropertyModel<?>> properties) {
		this.properties = properties;
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.getName();
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.name + "\nbehaviorName=" + this.behaviorName + "\ncategoriesNames=" + this.categoriesNames
				+ "\nservicesNames=" + this.servicesNames + "\nproperties=" + this.properties;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.behaviorName == null) ? 0 : this.behaviorName.hashCode());
		result = prime * result + ((this.categoriesNames == null) ? 0 : this.categoriesNames.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.properties == null) ? 0 : this.properties.hashCode());
		result = prime * result + ((this.servicesNames == null) ? 0 : this.servicesNames.hashCode());
		return result;
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
		WorldObjectModel other = (WorldObjectModel) obj;
		
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return this.elementsEquality(other);
	}
	
	/**
	 * Elements equality to reduce the complexity of equals.
	 * 
	 * @param other
	 *            the other
	 * @return true, if successful
	 */
	private boolean elementsEquality(WorldObjectModel other) {
		if (this.properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!this.properties.equals(other.properties)) {
			return false;
		}
		if (this.behaviorName == null) {
			if (other.behaviorName != null) {
				return false;
			}
		} else if (!this.behaviorName.equals(other.behaviorName)) {
			return false;
		}
		if (this.categoriesNames == null) {
			if (other.categoriesNames != null) {
				return false;
			}
		} else if (!this.categoriesNames.equals(other.categoriesNames)) {
			return false;
		}
		if (this.servicesNames == null) {
			if (other.servicesNames != null) {
				return false;
			}
		} else if (!this.servicesNames.equals(other.servicesNames)) {
			return false;
		}
		return true;
	}
}