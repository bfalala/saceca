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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class AgentSelectionController.
 */
public class ModelsSelectionEditionController implements ListSelectionListener {
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new agent selection controller.
	 * 
	 * @param ew
	 *            the ew
	 */
	public ModelsSelectionEditionController(EditionWindow ew) {
		this.ew = ew;
	}
	
	/**
	 * Value changed.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// We get the selected item
		Object selectedValue = this.ew.modelsList.getSelectedValue();
		if (selectedValue != null) {
			final String value = selectedValue.toString();
			
			ModelsSelectionEditionController.this.ew.setModelSelection(value);
			
		}
	}
}
