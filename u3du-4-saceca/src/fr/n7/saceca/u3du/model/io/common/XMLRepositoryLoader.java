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
package fr.n7.saceca.u3du.model.io.common;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.util.io.FilenameExtensionFilter;
import fr.n7.saceca.u3du.model.util.io.storage.HashMapRepository;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;



/**
 * A class to gather common code from XML Loaders. It aims at loading all the
 * files matching criteria in the given folder.
 * 
 * @param <T>
 *            The type of element to be read.
 * @author Sylvain Cambon
 */
public abstract class XMLRepositoryLoader<T extends Storable> {

    /** The logger. */
    private static Logger logger = Logger.getLogger(XMLRepositoryLoader.class);


    /**
     * Gets the extension.
     * 
     * @return the extension
     */
    protected abstract String getExtension();


    /**
     * Gets the importer.
     * 
     * @return the importer
     */
    protected abstract HighLevelImporter<T> getImporter();


    /**
     * Gets the repository name.
     * 
     * @return the repository name
     */
    protected abstract String getRepositoryName();


    /**
     * Loads all the files on the given path into a repository.
     * 
     * @param path
     *            the path
     * @return the repository
     */
    public Repository<T> loadFilesToRepository(String path) {
        Repository<T> repository = new HashMapRepository<T>(this.getRepositoryName());

        File dir = new File(path);
        FilenameFilter filter = new FilenameExtensionFilter(this.getExtension());
        HighLevelImporter<T> importer = this.getImporter();

        for (File file : dir.listFiles(filter)) {
            if (!file.isDirectory()) {
                try {
                    T object = importer.importObject(file.getAbsolutePath());
                    if (object != null) {
                        repository.put(object);
                        logger.info(this.getRepositoryName() + ": loaded "
                                + object.getStorageLabel());
                    }
                } catch (IOException e) {
                    logger.error("Cannot load " + file.getAbsolutePath(), e);
                }
            }
        }

        return repository;
    }
}
