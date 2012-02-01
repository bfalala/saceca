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
package fr.n7.saceca.u3du.view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import fr.n7.saceca.u3du.view.Conf2D.PrintLevel;

/**
 * 
 * Allows to print informations on a console represented thanks to a JTextPane.
 * 
 * @author Sylvain Cambon & Jérome Dalbert & Anthony Foulfoin & Johann Legaye & Aurélien Chabot
 * 
 */
public class Console {
	
	/** The console */
	private JTextPane console;
	
	/**
	 * COnstructor.
	 * 
	 * @param console
	 *            The console on which we want to print messages
	 */
	public Console(JTextPane console) {
		this.console = console;
		console.setEditable(false);
		Style msgLow = console.getStyledDocument().addStyle(PrintLevel.MSG_LOW + "", null);
		StyleConstants.setForeground(msgLow, new Color(0, 0, 0));
		Style msgHigh = console.getStyledDocument().addStyle(PrintLevel.MSG_HIGH + "", null);
		StyleConstants.setForeground(msgHigh, new Color(0, 102, 0));
		Style wng = console.getStyledDocument().addStyle(PrintLevel.WRN + "", null);
		StyleConstants.setForeground(wng, new Color(220, 102, 0));
		Style err = console.getStyledDocument().addStyle(PrintLevel.ERR + "", null);
		StyleConstants.setForeground(err, Color.red);
	}
	
	/**
	 * Print the given message in the console, with the color associated to the print level
	 * 
	 * @param s
	 *            the message
	 * @param kind
	 *            the print level
	 */
	public void println(String s, PrintLevel kind) {
		try {
			this.console.getStyledDocument().insertString(this.console.getStyledDocument().getLength(), s + "\n",
					this.console.getStyledDocument().getStyle(kind + ""));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		this.console.setCaretPosition(this.console.getDocument().getLength());
	}
	
	/**
	 * Remove all the messages in the console
	 */
	public void clean() {
		this.console.setText(null);
	}
	
	/**
	 * Return the console
	 * 
	 * @return the console
	 */
	public JTextPane getConsole() {
		return this.console;
	}
	
	/**
	 * Set the console
	 * 
	 * @param console
	 *            The console
	 */
	public void setConsole(JTextPane console) {
		this.console = console;
	}
}