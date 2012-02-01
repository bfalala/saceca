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
package fr.n7.saceca.u3du.model.util.io.clazz.management;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



/**
 * An entry of the class file directory.
 * 
 * @author Sylvain Cambon
 */
public class ClassFileDirectoryEntry {

    /** The qualified name. */
    @XStreamAlias("qName")
    @XStreamAsAttribute
    private String qualifiedName;

    /** The file path. */
    @XStreamAlias("file")
    @XStreamAsAttribute
    private String filePath;


    /**
     * Instantiates a new ClassFileDirectoryEntry.
     * 
     * @param qualifiedName
     *            the qualified name
     * @param filePath
     *            the file
     */
    public ClassFileDirectoryEntry(String qualifiedName, String filePath) {
        super();
        this.qualifiedName = qualifiedName;
        this.filePath = filePath;
    }


    /**
     * Gets the filePath.
     * 
     * @return the filePath
     */
    public final String getFilePath() {
        return this.filePath;
    }


    /**
     * Gets the qualified name.
     * 
     * @return the qualified name
     */
    public final String getQualifiedName() {
        return this.qualifiedName;
    }


    /**
     * Sets the filePath.
     * 
     * @param filePath
     *            the new filePath
     */
    public final void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    /**
     * Sets the qualified name.
     * 
     * @param qualifiedName
     *            the new qualified name
     */
    public final void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }


    @Override
    public String toString() {
        return this.qualifiedName + " <=> " + this.filePath;
    }

}
