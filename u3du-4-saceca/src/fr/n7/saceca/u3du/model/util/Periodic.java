/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.util;

/**
 * This class provides utility methods and classes relative to periods.
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public class Periodic {
	
	/**
	 * Increments an update time.
	 * 
	 * @param updateTime
	 *            the update time
	 * @param updatePeriod
	 *            the update period
	 * @return the int
	 */
	public static int incrementPeriodTime(int updateTime, int updatePeriod) {
		return (updateTime + 1) % updatePeriod;
	}
	
	/**
	 * The Class Clock.
	 * 
	 * It can be used to check that a time period is over. Notably useful inside behavior classes.
	 */
	public static class Clock {
		
		/** The last tick time. */
		private long lastTickTime;
		
		/** The period. */
		private long period;
		
		/**
		 * Instantiates a new clock.
		 * 
		 * @param period
		 *            the period
		 */
		public Clock(long period) {
			this.lastTickTime = -1;
			this.period = period;
		}
		
		/**
		 * Checks if the clock has ticked.
		 * 
		 * <b>Warning !!!</b> The first time <tt>tic</tt> is used, it immediatly returns true.
		 * 
		 * @return true, if successful
		 */
		public boolean tic() {
			if (System.currentTimeMillis() - this.lastTickTime < this.period && this.lastTickTime != -1) {
				return false;
			} else {
				this.lastTickTime = System.currentTimeMillis();
				return true;
			}
		}
		
		/**
		 * Resets the clock.
		 */
		public void reset() {
			this.lastTickTime = System.currentTimeMillis();
		}
		
	}
	
}