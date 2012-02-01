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
package integrationTest.engine3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2f;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.behavior.DefaultBehavior;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.animation.MoveVehicleAnimation;
import fr.n7.saceca.u3du.util.Log;

/**
 * The Class CarBehavior.
 * 
 * The car circularly drives from place to place.
 * 
 * @author Jérôme Dalbert
 */
public class BasicDriveBehavior extends DefaultBehavior {
	
	/** The places. */
	public List<Point2f> places;
	
	/** The nb places. */
	private int nbPlaces;
	
	/** The index of the next place to go */
	private int i;
	
	/**
	 * Instantiates a new car behavior with predefined places.
	 */
	public BasicDriveBehavior() {
		this.places = new ArrayList<Point2f>();
		
		this.places.add(new Point2f(4, 0));
		this.places.add(new Point2f(8, 0));
		this.places.add(new Point2f(12, 0));
		this.places.add(new Point2f(8, 0));
		this.places.add(new Point2f(4, 0));
		this.places.add(new Point2f(0, 0));
		
		this.nbPlaces = this.places.size();
		this.i = 0;
	}
	
	@Override
	public void behave() {
		if (this.object.getAnimation() != null) {
			Point2f place = this.places.get(this.i);
			Log.debug("[AI] Next desination : " + place);
			
			Animation a = new MoveVehicleAnimation(this.object.getId(), place);
			this.object.setAnimation(a);
			Model.getInstance().getGraphics().sendAnimation(a);
			
			this.i = (this.i + 1) % this.nbPlaces;
		}
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return BasicDriveBehavior.class.getCanonicalName();
	}
	
}
