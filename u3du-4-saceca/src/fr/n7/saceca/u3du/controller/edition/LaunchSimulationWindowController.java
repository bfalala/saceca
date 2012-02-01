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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.engine3d.Config3D;
import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class LaunchSimulationWindowController.
 */
public class LaunchSimulationWindowController implements ActionListener {
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new launch simulation window controller.
	 * 
	 * @param ew
	 *            the ew
	 */
	public LaunchSimulationWindowController(EditionWindow ew) {
		this.ew = ew;
	}
	
	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// reinitialise the pavements graph
		// The pavements position may have changed in the edition window
		Model.getInstance().getAI().getWorld().buildWalkableGraph();
		
		this.ew.frame.setVisible(false);
		this.ew.simulation.display();
		// We place the engine on the simulation mode
		Model.getInstance().getGraphics().getEngine3D().visualisationMode = Config3D.VisualisationMode.SIMULATION;
	}
	
}
