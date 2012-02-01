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

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class ObjectSelectionController.
 */
public class ObjectSelectionEditionController implements TreeSelectionListener {
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new object selection controller.
	 * 
	 * @param ew
	 *            the ew
	 */
	public ObjectSelectionEditionController(EditionWindow ew) {
		this.ew = ew;
	}
	
	/**
	 * Value changed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.ew.objectsTree.getLastSelectedPathComponent();
		if (node != null && node.isLeaf() && node.getLevel() > 1) {
			final long objectId = (Long) node.getUserObject();
			
			ObjectSelectionEditionController.this.ew.setAgentObjectSelection(objectId, true);
			
		}
	}
	
}
