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

import javax.swing.SwingUtilities;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.SimulationListener;
import fr.n7.saceca.u3du.view.SimulationWindow;

/**
 * The Class AIChangedController.
 */
public class AIChangedController implements SimulationListener {
	
	/** The simulation window. */
	private SimulationWindow sw;
	
	/**
	 * Instantiates a new aI changed controller.
	 * 
	 * @param sw
	 *            the sw
	 */
	public AIChangedController(SimulationWindow sw) {
		this.sw = sw;
	}
	
	/**
	 * World changed.
	 */
	@Override
	public void worldChanged() {
		int time = Model.getInstance().getAI().getSimulation().getTime();
		
		int hour = time / 60;
		int minute = time - (hour * 60);
		
		this.sw.time.setText(hour + "h " + minute + "m");
		if (this.sw.currentSelection != null) {
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					AIChangedController.this.sw.setSelection(AIChangedController.this.sw.currentSelection, false);
				}
			});
			
		}
	}
	
}
