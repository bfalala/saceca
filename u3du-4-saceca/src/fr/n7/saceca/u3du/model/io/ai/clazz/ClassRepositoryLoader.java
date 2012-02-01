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
package fr.n7.saceca.u3du.model.io.ai.clazz;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.util.io.clazz.management.ClassFileDirectory;
import fr.n7.saceca.u3du.model.util.io.clazz.management.ClassUtils;
import fr.n7.saceca.u3du.model.util.io.clazz.management.U3duClassLoader;
import fr.n7.saceca.u3du.model.util.io.storage.HashMapRepository;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * A class to gather common code from Class Loaders. It aims at loading all the files matching
 * criteria in the given folder.
 * 
 * @param <T>
 *            The type of element to be read.
 * @author Sylvain Cambon
 */
public abstract class ClassRepositoryLoader<T extends Storable> {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ClassRepositoryLoader.class);
	
	/**
	 * Gets the repository name.
	 * 
	 * @return the repository name
	 */
	protected abstract String getRepositoryName();
	
	/**
	 * Gets the target class type.
	 * 
	 * @return the target class type
	 */
	protected abstract Class<T> getTargetClassType();
	
	/**
	 * Loads all the files on the given path into a repository.
	 * 
	 * @param directory
	 *            The directory to look at.
	 * @return the repository
	 */
	public Repository<StorableClassWrapper<T>> loadFilesToRepository(ClassFileDirectory directory) {
		Repository<StorableClassWrapper<T>> repository = new HashMapRepository<StorableClassWrapper<T>>(
				this.getRepositoryName());
		U3duClassLoader loader = U3duClassLoader.getInstance();
		loader.setDirectory(directory);
		for (String qName : directory.getQualifiedNames()) {
			try {
				Class<?> clazz = loader.loadClass(qName);
				if (ClassUtils.getAllImplementedInterfaces(clazz).contains(this.getTargetClassType())) {
					@SuppressWarnings("unchecked")
					StorableClassWrapper<T> wrappedClass = new StorableClassWrapper<T>((Class<T>) clazz);
					repository.put(wrappedClass);
					logger.info(this.getRepositoryName() + ": loaded " + wrappedClass.getStorageLabel());
				}
			} catch (ClassNotFoundException e) {
				logger.error("Could not load " + qName, e);
			}
		}
		
		return repository;
	}
}
