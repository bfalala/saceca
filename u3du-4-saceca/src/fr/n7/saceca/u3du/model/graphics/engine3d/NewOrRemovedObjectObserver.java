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
package fr.n7.saceca.u3du.model.graphics.engine3d;

/**
 * Used by the 3D engine to notify the 2D view that a new object has been built in the 3DEngine and
 * AI
 * 
 * @author Sylvain Cambon & JÃ©rome Dalbert & Anthony Foulfoin & Johann Legaye & AurÃ©lien Chabot
 * 
 */
public interface NewOrRemovedObjectObserver {
	
	/**
	 * Notify all the new object observers of an object creation or deletion
	 * 
	 * @param id
	 *            the created object
	 * @param removed
	 *            true if the object is removed, false if it is created
	 */
	public void newOrRemovedObject(long id, boolean removed);
	
}