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
package fr.n7.saceca.u3du.controller.simulation;

import fr.n7.saceca.u3du.view.SimulationWindow;

/**
 * The Class NewOrRemovedObjectController.
 */
public class NewOrRemovedObjectController implements
		fr.n7.saceca.u3du.model.graphics.engine3d.NewOrRemovedObjectObserver
{
	
	/** The simulation window. */
	private SimulationWindow sw;
	
	/**
	 * Instantiates a new new or removed object controller.
	 * 
	 * @param sw
	 *            the sw
	 */
	public NewOrRemovedObjectController(SimulationWindow sw) {
		this.sw = sw;
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
		this.sw.initObjectsTree();
		// Update the agent list
		this.sw.initAgentsList();
	}
}
