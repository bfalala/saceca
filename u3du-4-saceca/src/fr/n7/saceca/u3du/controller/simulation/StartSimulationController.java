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

import javax.swing.JButton;

import fr.n7.saceca.u3du.model.Model;

/**
 * The Class StartSimulationController.
 */
public class StartSimulationController implements ActionListener {
	
	/**
	 * Instantiates a new start simulation controller.
	 */
	public StartSimulationController() {
	}
	
	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (((JButton) e.getSource()).getText().equals("Start simulation")) {
			Model.getInstance().getAI().getSimulation().setPause(false);
			Model.getInstance().getAI().getSimulation().start();
			Model.getInstance().getGraphics().getEngine3D().setPaused(false);
			((JButton) e.getSource()).setText("Pause simulation");
		} else if (((JButton) e.getSource()).getText().equals("Pause simulation")) {
			Model.getInstance().getGraphics().getEngine3D().setPaused(true);
			Model.getInstance().getAI().getSimulation().setPause(true);
			((JButton) e.getSource()).setText("Resume simulation");
		} else if (((JButton) e.getSource()).getText().equals("Resume simulation")) {
			Model.getInstance().getGraphics().getEngine3D().setPaused(false);
			Model.getInstance().getAI().getSimulation().setPause(false);
			((JButton) e.getSource()).setText("Pause simulation");
		}
	}
}
