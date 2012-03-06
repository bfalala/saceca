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
package fr.n7.saceca.u3du.model.util.io.clazz.management;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.model.util.io.ByteFileReader;
import fr.n7.saceca.u3du.model.util.io.storage.HashMapRepository;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * <p>
 * A class loader to dynamically load class files in a certain directory). A class directory has to
 * be provided in order to know where to find files that are not part of the class path.
 * </p>
 * <p>
 * The class offers singleton access to prevent from reading files multiple times.
 * </p>
 * <p>
 * Typical usage may be:<br/>
 * <code>
 * U3duClassLoader loader = U3duClassLoader.getInstance();<br/>
 * loader.setDirectory(directory);<br/>
 * MyInterface instance = (MyInterface) loader.loadClass(myClassQualifiedName).newInstance();<br/>
 * </code>
 * </p>
 * 
 * @author Sylvain Cambon
 */
public final class U3duClassLoader extends ClassLoader {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(U3duClassLoader.class);
	
	/** The instance. */
	private static U3duClassLoader instance = null;
	
	/** The directory. */
	private ClassFileDirectory directory;
	
	/** The class repository. */
	private Repository<StorableClassWrapper<?>> classRepository;
	
	/**
	 * Instantiates a new dynamic class loader.
	 * 
	 */
	private U3duClassLoader() {
		super();
		this.classRepository = new HashMapRepository<StorableClassWrapper<?>>("Cached class repository");
	}
	
	/**
	 * Gets the instance.
	 * 
	 * @return the instance
	 */
	public static synchronized U3duClassLoader getInstance() {
		if (instance == null) {
			instance = AccessController.doPrivileged(new PrivilegedAction<U3duClassLoader>() {
				@Override
				public U3duClassLoader run() {
					return new U3duClassLoader();
				}
			});
		}
		return instance;
	}
	
	/**
	 * Sets the directory.
	 * 
	 * @param directory
	 *            the new directory
	 */
	public synchronized void setDirectory(ClassFileDirectory directory) {
		this.directory = directory;
		this.forceLoad();
	}
	
	/**
	 * Finds a class. A caching system should prevent from repeated access to the same file.
	 * 
	 * @param qName
	 *            the qualified name
	 * @return the class
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class<?> findClass(String qName) {
		Class<?> clazz = null;
		StorableClassWrapper<?> classWrapper = this.classRepository.get(qName);
		if (classWrapper != null) {
			clazz = classWrapper.getWrappedClass();
		} else {
			byte[] bytes = this.loadClassBytes(qName);
			clazz = this.defineClass(qName, bytes, 0, bytes.length);
			this.classRepository.put(new StorableClassWrapper(clazz));
		}
		return clazz;
	}
	
	/**
	 * Loads data from a class file.
	 * 
	 * @param qName
	 *            the qualified name
	 * @return the content of the corresponding class file or null if no file was found.
	 */
	private byte[] loadClassBytes(String qName) {
		byte[] bytes = null;
		String path = null;
		try {
			path = this.directory.getPath(qName);
			logger.info("Attempt to load " + qName + " from " + path);
			bytes = ByteFileReader.getBytesFromFile(path);
			logger.info("Loading " + qName + " successful");
		} catch (IOException e) {
			logger.error("Could not read " + qName + " from " + path, e);
		}
		return bytes;
	}
	
	/**
	 * Forces loading the classes in the directory.
	 */
	public void forceLoad() {
		logger.info("FORCE LOAD!");
		for (String qName : this.directory.getQualifiedNames()) {
			try {
				this.loadClass(qName);
			} catch (ClassNotFoundException e) {
				logger.warn("Forceload of " + qName + " failed. This class is skipped.", e);
			}
		}
	}
}