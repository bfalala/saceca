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
import java.util.concurrent.Callable;

import javax.swing.JButton;

import fr.n7.saceca.u3du.model.Model;

/**
 * The Class GraphDisplayController.
 */
public class GraphDisplayController implements ActionListener {
	
	/**
	 * Instantiates a new graph display controller.
	 */
	public GraphDisplayController() {
	}
	
	/**
	 * Action performed.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		final JButton button = (JButton) arg0.getSource();
		
		// In the 3D Engine
		// We must wrap the call to JME so that it will be executed from the OpenGL thread
		Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				
				if (!Model.getInstance().getGraphics().getEngine3D().isWalkableGraphDisplayed()) {
					button.setText("Mask");
					Model.getInstance().getGraphics().getEngine3D().drawGraph();
				} else {
					button.setText("Draw");
					Model.getInstance().getGraphics().getEngine3D().removeGraph();
				}
				return null;
			}
		});
	}
}
