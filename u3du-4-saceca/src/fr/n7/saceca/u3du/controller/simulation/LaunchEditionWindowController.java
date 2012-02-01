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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.engine3d.Config3D;
import fr.n7.saceca.u3du.view.SimulationWindow;

/**
 * The Class LaunchEditionWindowController.
 */
public class LaunchEditionWindowController implements ActionListener {
	
	/** The sw. */
	private SimulationWindow sw;
	
	/**
	 * Instantiates a new launch edition window controller.
	 * 
	 * @param sw
	 *            the sw
	 */
	public LaunchEditionWindowController(SimulationWindow sw) {
		this.sw = sw;
	}
	
	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.sw.frame.setVisible(false);
		if (!Model.getInstance().getGraphics().getEngine3D().isPaused()) {
			this.sw.btnStart.doClick();
		}
		this.sw.editionWindow.display();
		// We place the engine on the edition mode
		Model.getInstance().getGraphics().getEngine3D().visualisationMode = Config3D.VisualisationMode.EDITION;
	}
}
