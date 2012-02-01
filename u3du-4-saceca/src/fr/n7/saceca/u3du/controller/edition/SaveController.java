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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.configuration.WorldConfiguration;
import fr.n7.saceca.u3du.model.io.graphics.WorldConfigurationIO;
import fr.n7.saceca.u3du.model.util.ProgressMonitor;
import fr.n7.saceca.u3du.view.EditionWindow;
import fr.n7.saceca.u3du.view.ProgressWindow;

/**
 * The controller for save.
 * 
 * @author Sylvain Cambon
 */
public class SaveController implements ActionListener {
	
	/** The edition window. */
	private EditionWindow editionWindow;
	
	/** Determines if we directly save, without asking the user a folder. */
	private boolean directSave;
	
	/**
	 * Instantiates a new save controller.
	 * 
	 * @param ew
	 *            the ew
	 * @param directSave
	 *            the direct save
	 */
	public SaveController(EditionWindow ew, boolean directSave) {
		this.editionWindow = ew;
		this.directSave = directSave;
	}
	
	/**
	 * Opens a save dialog and handle the results. The world can be saved, or an error can occur, or
	 * the dialog can be escaped.
	 * 
	 * @param e
	 *            the event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// The window disappears
		// this.editionWindow.frame.setEnabled(false);
		
		String instancesPath = Model.getInstance().getAI().getIOManager().getInstancesPath();
		
		if (this.directSave) {
			this.save(new File(instancesPath));
		} else {
			this.saveAs(instancesPath);
		}
		
		// this.editionWindow.frame.setEnabled(true);
		this.editionWindow.frame.requestFocus();
	}
	
	/**
	 * Save the simulation in the specified folder
	 * 
	 * @param instancesPath
	 *            Where to save the simulation
	 */
	private void saveAs(String instancesPath) {
		// Opening a JFileChooser only for directories.
		// Only one directory can be chosen
		JFileChooser jFileChooser = new JFileChooser(instancesPath);
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setMultiSelectionEnabled(false);
		
		int result = jFileChooser.showSaveDialog(this.editionWindow.frame);
		
		switch (result) {
			case JFileChooser.APPROVE_OPTION:
				File file = jFileChooser.getSelectedFile();
				jFileChooser.setVisible(false);
				this.save(file);
				break;
			case JFileChooser.ERROR_OPTION:
				jFileChooser.setVisible(false);
				JOptionPane.showMessageDialog(this.editionWindow.frame, "An error occurred", "Error!",
						JOptionPane.ERROR_MESSAGE);
				break;
			case JFileChooser.CANCEL_OPTION:
			default:
				jFileChooser.setVisible(false);
				break;
		}
	}
	
	/**
	 * Saves.
	 * 
	 * @param file
	 *            the file
	 */
	private void save(File file) {
		try {
			// Saving the world characteristics
			final int terrainSize = Model.getInstance().getGraphics().getEngine3D().getTerrainSize();
			WorldConfiguration worldConf = new WorldConfiguration(terrainSize);
			WorldConfigurationIO wcio = new WorldConfigurationIO();
			wcio.exportObject(file.getAbsolutePath() + File.separatorChar + Constants.WORLD_CONFIGURATION_FILE_NAME,
					worldConf);
			// Saving the instances
			ProgressMonitor progressMonitor = new ProgressMonitor();
			ProgressWindow progressWindow = new ProgressWindow();
			progressWindow.setVisible(true);
			progressMonitor.registerListener(progressWindow);
			Model.getInstance().getAI().getIOManager().save(file.getAbsolutePath(), progressMonitor);
			progressWindow.setVisible(false);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this.editionWindow.frame, Arrays.toString(e1.getStackTrace()),
					"IOException!", JOptionPane.ERROR_MESSAGE);
		}
	}
}
