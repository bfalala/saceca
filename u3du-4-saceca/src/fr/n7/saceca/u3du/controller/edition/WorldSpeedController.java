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

import javax.swing.JOptionPane;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class WorldSpeedController.
 */
public class WorldSpeedController implements ActionListener {
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new world speed controller.
	 * 
	 * @param ew
	 *            the ew
	 */
	public WorldSpeedController(EditionWindow ew) {
		this.ew = ew;
	}
	
	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		
		String speedTxt = this.ew.speedField.getText();
		boolean speedOK = true;
		try {
			long speed = Long.parseLong(speedTxt);
			if (speed > 0) {
				Model.getInstance().getAI().getSimulation().setTickPeriod(speed);
			} else {
				speedOK = false;
			}
		} catch (NumberFormatException ex) {
			speedOK = false;
		}
		
		if (!speedOK) {
			JOptionPane.showMessageDialog(this.ew.frame, "The world speed value must be a long value > 0", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
}
