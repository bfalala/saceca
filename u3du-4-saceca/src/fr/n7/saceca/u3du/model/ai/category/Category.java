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

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.util.io.storage.Storable;



/**
 * A class representing a category.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("category")
public class Category implements Storable {

    /** The name. */
    private String name;


    /**
     * Instantiates a new category.
     * 
     * @param name
     *            the name
     */
    public Category(String name) {
        super();
        this.name = name;
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
        Category other = (Category) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
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


    @Override
    public String getStorageLabel() {
        return this.name;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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


    @Override
    public String toString() {
        return this.name;
    }
}
