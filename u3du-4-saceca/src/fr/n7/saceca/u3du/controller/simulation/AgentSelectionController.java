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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.n7.saceca.u3du.view.SimulationWindow;

/**
 * The Class AgentSelectionController.
 */
public class AgentSelectionController implements ListSelectionListener {
	
	/** The sw. */
	private SimulationWindow sw;
	
	/**
	 * Instantiates a new agent selection controller.
	 * 
	 * @param sw
	 *            the sw
	 */
	public AgentSelectionController(SimulationWindow sw) {
		this.sw = sw;
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
		Object selectedValue = this.sw.agentsList.getSelectedValue();
		if (selectedValue != null) {
			String value = selectedValue.toString();
			value = value.split("#")[1].split("\\)")[0];
			final long agentId = Long.parseLong(value);
			
			AgentSelectionController.this.sw.setSelection(agentId, true);
			
		}
	}
}
