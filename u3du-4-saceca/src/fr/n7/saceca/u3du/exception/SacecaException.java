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
package fr.n7.saceca.u3du.exception;

import org.apache.log4j.Logger;

/**
 * This exception is "loose" : it doesn't need to be caught (because it extends RuntimeException).
 * 
 * <p>
 * It is automatically logged when constructed, so that there is always a trace even if an external
 * library absorbs it.
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public class SacecaException extends RuntimeException {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(SacecaException.class);
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new saceca exception.
	 * 
	 * @param msg
	 *            the msg
	 */
	public SacecaException(String msg) {
		super(msg);
		LOGGER.error(msg);
	}
	
	/**
	 * Instantiates a new saceca exception.
	 * 
	 * @param msg
	 *            the msg
	 * @param cause
	 *            the cause
	 */
	public SacecaException(String msg, Throwable cause) {
		super(msg, cause);
		LOGGER.error(msg);
	}
	
	/**
	 * Instantiates a new saceca exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public SacecaException(Throwable cause) {
		super(cause);
		LOGGER.error("Exception with cause : " + cause.getMessage());
	}
	
}