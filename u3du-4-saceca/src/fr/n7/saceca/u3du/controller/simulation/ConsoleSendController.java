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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.view.Conf2D.PrintLevel;
import fr.n7.saceca.u3du.view.SimulationWindow;

/**
 * The Class ConsoleSendController.
 */
public class ConsoleSendController extends KeyAdapter implements ActionListener {
	
	/** The sw. */
	private SimulationWindow sw;
	
	/**
	 * Instantiates a new console send controller.
	 * 
	 * @param sw
	 *            the sw
	 */
	public ConsoleSendController(SimulationWindow sw) {
		this.sw = sw;
	}
	
	/**
	 * Action performed.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Getting the command
		String command = this.sw.consoleTextField.getText();
		
		// If the command is null, we ignore it
		if (command.equals("")) {
			return;
		}
		
		// We print the command in the textPane
		this.sw.console.println(command, PrintLevel.MSG_LOW);
		
		// We clear the text field
		this.sw.consoleTextField.setText("");
		
		// Executing the command
		try {
			String consoleOutput = Model.getInstance().getAI().sendCommands(command);
			
			if (consoleOutput.isEmpty()) {
				this.sw.console.println("Command sent.", PrintLevel.MSG_HIGH);
			} else {
				this.sw.console.println(consoleOutput, PrintLevel.MSG_HIGH);
			}
		} catch (CommandException e) {
			// If there is an error, we print it
			this.sw.console.println(e.getMessage(), PrintLevel.ERR);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println(e.getKeyCode());
	}
}
