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
package fr.n7.saceca.u3du.model.graphics;

import java.util.List;

import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;

/**
 * The Interface Graphics.
 * 
 * It represents the Graphics module.<br>
 * Any external class that wants to use services from the <tt>graphics</tt> package, shall call this
 * interface.
 * 
 * @author Jérôme Dalbert
 */
public interface Graphics {
	
	/**
	 * Gets the objects in vision field.
	 * 
	 * @param agent
	 *            the agent
	 * @return the objects in vision field
	 */
	public List<Long> getObjectsInVisionField(Long agent);
	
	/**
	 * Update orientation.
	 * 
	 * @param object
	 *            the object
	 * @param theta
	 *            the theta
	 */
	public void updateOrientation(Long object, float theta);
	
	/**
	 * Send animation.
	 * 
	 * @param animation
	 *            the animation
	 */
	public void sendAnimation(Animation animation);
	
	/**
	 * Show message.
	 * 
	 * @param emitter
	 *            the emitter
	 * @param message
	 *            the message
	 */
	public void showMessage(Long emitter, String message);
	
	/**
	 * Gets the 3D Engine. <br/>
	 * Should not be called by the AI, because all the methods the AI needs are in the Graphics
	 * interface.
	 * 
	 * @return the engine3 d
	 */
	public Engine3D getEngine3D();
}
