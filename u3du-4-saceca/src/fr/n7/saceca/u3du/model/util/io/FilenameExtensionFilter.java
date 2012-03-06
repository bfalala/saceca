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
package fr.n7.saceca.u3du.model.util.io;

import java.io.File;
import java.io.FilenameFilter;



/**
 * A class to filter files by their extension. Folders which name ends with the
 * provided extension are returned as well.
 * 
 * @author Sylvain Cambon
 */
public class FilenameExtensionFilter implements FilenameFilter {

    /** The accepted extension. */
    private final String acceptedExtension;


    /**
     * Instantiates a new filename extension filter.
     * 
     * @param acceptedExtension
     *            the accepted extension
     */
    public FilenameExtensionFilter(String acceptedExtension) {
        super();
        this.acceptedExtension = acceptedExtension;
    }


    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(this.acceptedExtension);
    }

}