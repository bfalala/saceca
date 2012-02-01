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

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class to monitor a task progress. It dispatches messages to registered listeners.
 * 
 * @author Sylvain Cambon
 */
public class ProgressMonitor {
	
	/** The listeners. */
	Collection<ProgressEventListener> listeners;
	
	/**
	 * Instantiates a new progress monitor.
	 */
	public ProgressMonitor() {
		this.listeners = new ArrayList<ProgressEventListener>();
	}
	
	/**
	 * Registers a listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerListener(ProgressEventListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Removes the listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeListener(ProgressEventListener listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Update the progress bounds.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public void updateBounds(int min, int max) {
		for (ProgressEventListener listener : this.listeners) {
			listener.fireBoundUpdate(min, max);
		}
	}
	
	/**
	 * Updates the content.
	 * 
	 * @param value
	 *            the value of the progress in the bounds
	 * @param message
	 *            the corresponding message
	 */
	public void updateContent(int value, String message) {
		for (ProgressEventListener listener : this.listeners) {
			listener.fireContentChange(value, message);
		}
	}
}
