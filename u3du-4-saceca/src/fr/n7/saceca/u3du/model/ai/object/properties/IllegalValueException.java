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
package fr.n7.saceca.u3du.model.ai.object.properties;

import fr.n7.saceca.u3du.exception.SacecaException;



/**
 * An exception for illegal values assigned to certain fields.
 * 
 * @author Sylvain Cambon
 */
public class IllegalValueException extends SacecaException {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Instantiates a new illegal value exception.
     * 
     * @param text
     *            the text
     */
    public IllegalValueException(String text) {
        super(text);
    }


    /**
     * Instantiates a new illegal value exception.
     * 
     * @param text
     *            The text.
     * @param cause
     *            The cause.
     */
    public IllegalValueException(String text, Throwable cause) {
        super(text, cause);
    }


    /**
     * Instantiates a new illegal value exception.
     * 
     * @param cause
     *            The cause.
     */
    public IllegalValueException(Throwable cause) {
        super(cause);
    }

}