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

/**
 * A class to generate IDs. The getNewID() method is synchronized to ensure ID uniqueness in the VM.
 * This class is not adapted to network usage.
 * 
 * @author Sylvain Cambon
 */
public class DefaultIDProvider implements IDProvider {
	
	/** The next id. */
	private static long nextID = 0;
	
	@Override
	public synchronized long getNewID() {
		return nextID++;
	}
	
	@Override
	public synchronized void tellAboutUsage(long id) {
		nextID = Math.max(nextID, id + 1);
	}
	
}
