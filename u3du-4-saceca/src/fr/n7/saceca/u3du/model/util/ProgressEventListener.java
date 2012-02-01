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
package fr.n7.saceca.u3du.model.util;

import java.util.EventListener;

/**
 * The listener interface for receiving progressEvent events. The class that is interested in
 * processing a progressEvent event implements this interface, and the object created with that
 * class is registered with a component using the component's <code>registerListener</code> method.
 * When the progressEvent event occurs, that object's appropriate method is invoked.
 * 
 * @see ProgressEventEvent
 */
public interface ProgressEventListener extends EventListener {
	
	/**
	 * Fires a bound update.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public void fireBoundUpdate(int min, int max);
	
	/**
	 * Fires a content change.
	 * 
	 * @param value
	 *            the value
	 * @param text
	 *            the text
	 */
	public void fireContentChange(int value, String text);
	
}
