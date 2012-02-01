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
package fr.n7.saceca.u3du.model.io.common.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



/**
 * A class to use a singleton instance of the main XStream class.
 * 
 * @author Sylvain Cambon
 */
public class XStreamProvider {

    /** The instance. */
    private static XStream xStreamInstance = null;


    /**
     * Gets the XStream instance and create it if necessary.
     * 
     * @return the XStream instance
     */
    public static XStream getXStreamInstance() {
        if (xStreamInstance == null) {
            xStreamInstance = new XStream(new DomDriver());
        }
        return xStreamInstance;
    }
}
