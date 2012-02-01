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
package fr.n7.saceca.u3du.model.io.ai.service;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.io.common.HighLevelImporter;
import fr.n7.saceca.u3du.model.io.common.XMLRepositoryLoader;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * A class to load all the services described in XML from a directory.
 * 
 * @author Sylvain Cambon
 */
public class ServiceRepositoryLoader extends XMLRepositoryLoader<Service> {
	
	/** The action repository. */
	private Repository<StorableClassWrapper<Action>> actionRepository;
	
	/**
	 * Instantiates a new service repository loader.
	 * 
	 * @param actionRepository
	 *            the action repository
	 */
	public ServiceRepositoryLoader(Repository<StorableClassWrapper<Action>> actionRepository) {
		super();
		this.actionRepository = actionRepository;
	}
	
	@Override
	protected String getExtension() {
		return Constants.SERVICES_EXTENSION;
	}
	
	@Override
	protected HighLevelImporter<Service> getImporter() {
		return new ServiceIO(this.actionRepository);
	}
	
	@Override
	protected String getRepositoryName() {
		return Service.class.getSimpleName() + " Repository";
	}
	
}
