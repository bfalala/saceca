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

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.view.EditionWindow;
import fr.n7.saceca.u3du.view.PropertyEditionDialog;

/**
 * The Class ObjectSelectionController.
 */
public class PropertiesTableSelectionController implements ListSelectionListener {
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new object selection controller.
	 * 
	 * @param ew
	 *            the ew
	 */
	public PropertiesTableSelectionController(EditionWindow ew) {
		this.ew = ew;
	}
	
	/**
	 * Value changed.
	 * 
	 * @param listSelectionEvent
	 *            the list selection event
	 */
	@Override
	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		if (listSelectionEvent.getValueIsAdjusting()) {
			return;
		}
		if (this.ew.currentSelection != null) {
			ListSelectionModel lsm = (ListSelectionModel) listSelectionEvent.getSource();
			if (!lsm.isSelectionEmpty()) {
				int selectedRow = lsm.getMinSelectionIndex();
				String propertyName = (String) this.ew.propertiesTableModel.getValueAt(selectedRow, 0);
				WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(
						this.ew.currentSelection);
				try {
					Property<?> property = object.getPropertiesContainer().getProperty(propertyName);
					PropertyEditionDialog editionDialog = new PropertyEditionDialog(property, this.ew);
					editionDialog.setVisible(true);
				} catch (UnknownPropertyException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
