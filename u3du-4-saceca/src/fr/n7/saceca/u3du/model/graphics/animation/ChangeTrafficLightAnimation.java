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
package fr.n7.saceca.u3du.model.graphics.animation;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.behavior.LightState;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalObject;

/**
 * The Class ChangeTrafficLightAnimation.
 * 
 * @author Sylvain Cambon & Jérôme Dalbert
 */
public class ChangeTrafficLightAnimation extends Animation {
	
	/** The new color. */
	protected LightState color;
	
	/**
	 * Instantiates a new change traffic light animation.
	 * 
	 * @param objId
	 *            the obj id
	 * @param state
	 *            the new color
	 */
	public ChangeTrafficLightAnimation(long objId, LightState state) {
		super(objId);
		this.color = state;
	}
	
	/**
	 * Execute.
	 */
	@Override
	public void execute() {
		Engine3D engine3d = Model.getInstance().getGraphics().getEngine3D();
		GraphicalObject trafficLight = engine3d.objects.get(this.objectId);
		
		// The basis: all black colors ON
		trafficLight.getChildren().get("red").setVisible(false);
		trafficLight.getChildren().get("orange").setVisible(false);
		trafficLight.getChildren().get("green").setVisible(false);
		trafficLight.getChildren().get("blackred").setVisible(true);
		trafficLight.getChildren().get("blackorange").setVisible(true);
		trafficLight.getChildren().get("blackgreen").setVisible(true);
		
		// The delta is changed according to the real color.
		switch (this.color) {
			case RED:
				trafficLight.getChildren().get("red").setVisible(true);
				trafficLight.getChildren().get("blackred").setVisible(false);
				break;
			
			case ORANGE:
				trafficLight.getChildren().get("orange").setVisible(true);
				trafficLight.getChildren().get("blackorange").setVisible(false);
				break;
			
			case GREEN:
				trafficLight.getChildren().get("green").setVisible(true);
				trafficLight.getChildren().get("blackgreen").setVisible(false);
				break;
			
			default: // NOP
		}
		// Log.debug("Traffic light (id=" + this.objectId + ") color change => " +
		// this.color.toString());
	}
}
