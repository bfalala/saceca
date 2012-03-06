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
package fr.n7.saceca.u3du.model.util.io.storage;

/**
 * A wrapper around the Class class to make it Storable-friendly.
 * 
 * @param <T>
 *            The type of the class.
 * @author Sylvain Cambon
 */
public class StorableClassWrapper<T> implements Storable {
	
	/** The class. */
	private final Class<? extends T> clazz;
	
	/**
	 * Instantiates a new storable class wrapper.
	 * 
	 * @param clazz
	 *            the clazz
	 */
	public StorableClassWrapper(Class<? extends T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.clazz.getCanonicalName();
	}
	
	/**
	 * Gets the wrapped class.
	 * 
	 * @return the class
	 */
	public final Class<? extends T> getWrappedClass() {
		return this.clazz;
	}
	
	/**
	 * Creates a new instance. This is a short hand for <code>getWrappedClass().newInstance()</code>
	 * .
	 * 
	 * @return A new instance of the class.
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 */
	public T newInstance() throws InstantiationException, IllegalAccessException {
		return this.clazz.newInstance();
	}
}