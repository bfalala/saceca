package fr.n7.saceca.u3du.controller.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalAgent;

import javax.swing.JOptionPane;

/**
 * This controller is used to make the weather change .  
 * 
 * @author Eric Blachère
 */
public class WeatherController implements ActionListener{
	
	public WeatherController(){		
	}
	
	public void actionPerformed(ActionEvent e) {
		Model.getInstance().getGraphics().getEngine3D().getWeather().change();
		Model.getInstance().getGraphics().getEngine3D().getRootNode().updateGeometricState();
	}
}
