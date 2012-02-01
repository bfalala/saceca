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
package fr.n7.saceca.u3du.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Class AboutController.
 * 
 * @author Jérôme Dalbert
 */
public class AboutController implements ActionListener {
	
	/** The msg about. */
	private String msgAbout;
	
	/** The frame. */
	private JFrame frame;
	
	/**
	 * Instantiates a new about controller.
	 * 
	 * @param frame
	 *            the frame
	 */
	public AboutController(JFrame frame) {
		this.frame = frame;
		
		this.msgAbout = "<html><b>U3DU-4-SACECA</b><br/>" + "\"Univers 3D Urbain pour une Société d'Agents<br/>"
				+ "Cognitifs Émotionnels Communicants Autonomes\"<br/><br/>" + "<b>Team</b><br/>"
				+ "- Sylvain Cambon<br/>" + "- Aurélien Chabot<br/>" + "- Jérôme Dalbert<br/>"
				+ "- Anthony Foulfoin<br/>" + "- Johann Legaye<br/><br/>" + "2011";
	}
	
	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(this.frame, this.msgAbout, "About", JOptionPane.INFORMATION_MESSAGE);
	}
}
