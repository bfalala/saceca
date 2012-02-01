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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The Class View.
 * 
 * <p>
 * It is the main class of the <tt>view</tt> package. It represents the view in the MVC pattern.
 * 
 * @author Jérôme Dalbert
 */
public class View {
	
	/** The simulation window. */
	private SimulationWindow simulationWindow;
	
	/** The edition window. */
	private EditionWindow editionWindow;
	
	/**
	 * Instantiates the view.
	 */
	public View() {
		this.setSystemLook();
		
		this.simulationWindow = new SimulationWindow();
		this.editionWindow = new EditionWindow(this.simulationWindow);
		
		this.simulationWindow.setEditionWindow(this.editionWindow);
	}
	
	/**
	 * Sets the look of the UI components to the system look.
	 */
	private void setSystemLook() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the simulation window.
	 * 
	 * @return the simulation window
	 */
	public SimulationWindow getSimulationWindow() {
		return this.simulationWindow;
	}
	
	/**
	 * Sets the simulation window.
	 * 
	 * @param simulationWindow
	 *            the new simulation window
	 */
	public void setSimulationWindow(SimulationWindow simulationWindow) {
		this.simulationWindow = simulationWindow;
	}
	
}
