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
package fr.n7.saceca.u3du.model.util.io.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
 * A HashMap-based repository.
 * 
 * @param <T>
 *            The class (at most extended) by the contained elements.
 * @author Sylvain Cambon
 */
public final class HashMapRepository<T extends Storable> implements Repository<T> {

    /** The backing map. */
    private Map<String, T> map;

    /** The repository name. */
    private String repositoryName;


    /**
     * Instantiates a new hash map repository.
     * 
     * @param repositoryName
     *            The repository name.
     */
    public HashMapRepository(String repositoryName) {
        super();
        this.map = new HashMap<String, T>();
        this.repositoryName = repositoryName;
    }


    @Override
    public void clear() {
        this.map.clear();
    }


    @Override
    public boolean contains(String label) {
        return this.map.containsKey(label);
    }


    @Override
    public boolean contains(T element) {
        return this.map.containsValue(element);
    }


    @Override
    public T get(String label) {
        return this.map.get(label);
    }


    @Override
    public Iterator<T> iterator() {
        return this.map.values().iterator();
    }


    @Override
    public void put(T element) {
        this.map.put(element.getStorageLabel(), element);
    }


    @Override
    public void remove(String label) {
        this.map.remove(label);
    }


    @Override
    public void remove(T element) {
        this.remove(element.getStorageLabel());
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.repositoryName + "\n");
        for (T element : this) {
            builder.append('\t');
            builder.append(element.getStorageLabel());
            builder.append('\n');
        }
        return builder.toString();
    }

}
