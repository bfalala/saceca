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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import fr.n7.saceca.u3du.model.util.ProgressEventListener;

/**
 * A class to represent a progress.
 * 
 * @author Sylvain Cambon
 */
public class ProgressWindow extends JFrame implements ProgressEventListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant SAVING. */
	private static final String SAVING = "Saving ";
	
	/** The progress bar. */
	private JProgressBar progressBar;
	
	/** The saving label. */
	private JLabel savingLabel;
	
	/** The saved file. */
	private JLabel savedFile;
	
	/**
	 * Instantiates a new progress window.
	 */
	public ProgressWindow() {
		super();
		this.setUndecorated(true);
		this.setTitle("Exporting...");
		
		// Adding a panel to contain the elements
		JPanel panel = new JPanel();
		this.getContentPane().add(panel);
		
		SpringLayout springLayout = new SpringLayout();
		panel.setLayout(springLayout);
		
		// Progress bar
		this.progressBar = new JProgressBar();
		this.progressBar.setValue(50);
		springLayout.putConstraint(SpringLayout.NORTH, this.progressBar, 10, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, this.progressBar, 10, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, this.progressBar, -10, SpringLayout.EAST, panel);
		this.progressBar.setStringPainted(true);
		panel.add(this.progressBar);
		
		// Saving label
		this.savingLabel = new JLabel(SAVING);
		springLayout.putConstraint(SpringLayout.NORTH, this.savingLabel, 10, SpringLayout.SOUTH, this.progressBar);
		springLayout.putConstraint(SpringLayout.WEST, this.savingLabel, 10, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, this.savingLabel, -10, SpringLayout.SOUTH, panel);
		panel.add(this.savingLabel);
		
		// Saved file
		this.savedFile = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, this.savedFile, 0, SpringLayout.NORTH, this.savingLabel);
		springLayout.putConstraint(SpringLayout.WEST, this.savedFile, 10, SpringLayout.EAST, this.savingLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, this.savedFile, 0, SpringLayout.SOUTH, this.savingLabel);
		springLayout.putConstraint(SpringLayout.EAST, this.savedFile, -10, SpringLayout.EAST, panel);
		panel.add(this.savedFile);
		
		// Panel size
		// Screen size
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension preferredSize = new Dimension((int) (screenDim.getWidth() * 0.8), 75);
		panel.setPreferredSize(preferredSize);
		
		// Set uncloseable
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Centering
		int xCorner = (int) ((screenDim.getWidth() - preferredSize.getWidth()) / 2);
		int yCorner = (int) ((screenDim.getHeight() - preferredSize.getHeight()) / 2);
		this.setLocation(xCorner, yCorner);
		
		// Packing
		this.pack();
	}
	
	/**
	 * Fire bound update.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	@Override
	public void fireBoundUpdate(int min, int max) {
		this.progressBar.setMinimum(min);
		this.progressBar.setMaximum(max);
	}
	
	/**
	 * Fire content change.
	 * 
	 * @param value
	 *            the value
	 * @param text
	 *            the text
	 */
	@Override
	public void fireContentChange(int value, String text) {
		this.progressBar.setValue(value);
		this.savedFile.setText(text);
		this.update(this.getGraphics());
	}
}
