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
package fr.n7.saceca.u3du.controller.edition;

import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class NewOrRemovedObjectEditionController.
 */
public class NewOrRemovedObjectEditionController implements
		fr.n7.saceca.u3du.model.graphics.engine3d.NewOrRemovedObjectObserver
{
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new new or removed object edition controller.
	 * 
	 * @param ew
	 *            the ew
	 */
	public NewOrRemovedObjectEditionController(EditionWindow ew) {
		this.ew = ew;
	}
	
	/**
	 * New or removed object.
	 * 
	 * @param id
	 *            the id
	 * @param removed
	 *            the removed
	 */
	@Override
	public void newOrRemovedObject(long id, boolean removed) {
		// Update the object tree
		this.ew.initObjectsTree();
		// Update the agent list
		this.ew.initAgentsList();
	}
}
