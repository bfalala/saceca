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
import java.awt.FlowLayout;

import javax.swing.JPanel;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;

/**
 * The Class JMECanvas.
 */
public class JMECanvas extends JPanel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The simulation viewer. */
	Engine3D simulationViewer;
	
	/**
	 * Create the panel.
	 */
	public JMECanvas() {
		this.setLayout(new FlowLayout());
		
		AppSettings settings = new AppSettings(true);
		settings.setWidth(680);
		settings.setHeight(470);
		
		if (this.simulationViewer == null) {
			this.simulationViewer = (Engine3D) Model.getInstance().getGraphics();
			this.simulationViewer.setSettings(settings);
			this.simulationViewer.createCanvas(); // create canvas!
		}
		
		JmeCanvasContext ctx = (JmeCanvasContext) this.simulationViewer.getContext();
		ctx.setSystemListener(this.simulationViewer);
		Dimension dim = new Dimension(680, 475);
		ctx.getCanvas().setPreferredSize(dim);
		
		this.add(ctx.getCanvas());
		this.simulationViewer.startCanvas();
	}
	
}
