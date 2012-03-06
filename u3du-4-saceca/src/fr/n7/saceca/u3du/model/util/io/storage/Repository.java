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
 * A generic repository interface using the Storable.getLabelName() method to generate keys.
 * 
 * @see Storable#getStorageLabel()
 * @param <T>
 *            The type of the repository content.
 * @author Sylvain Cambon
 */
public interface Repository<T extends Storable> extends Iterable<T> {
	
	// TODO : Sylvain Ã  Jerome :
	// "en V2, je ferai une sous-classe si tu veux. Il suffit que le repository ait une mÃ©thode capable de lire ce nom"
	
	/**
	 * Clears the repository.
	 */
	public void clear();
	
	/**
	 * Checks if the label is mapped in the repository.
	 * 
	 * @param label
	 *            The label.
	 * @return true, if the label is mapped.
	 */
	public boolean contains(String label);
	
	/**
	 * Checks if the element is stored in the repository.
	 * 
	 * @param element
	 *            The element.
	 * @return true, if the element is stored in the repository
	 */
	public boolean contains(T element);
	
	/**
	 * Gets the elements mapped to the label.
	 * 
	 * @param label
	 *            The label.
	 * @return The element mapped to the label.
	 */
	public T get(String label);
	
	/**
	 * Puts an element into the repository.
	 * 
	 * @param element
	 *            The element to be put.
	 */
	public void put(T element);
	
	/**
	 * Removes the element corresponding to the label.
	 * 
	 * @param label
	 *            The label to be removed.
	 */
	public void remove(String label);
	
	/**
	 * Removes the element.
	 * 
	 * @param element
	 *            The element to be removed.
	 */
	void remove(T element);
	
}