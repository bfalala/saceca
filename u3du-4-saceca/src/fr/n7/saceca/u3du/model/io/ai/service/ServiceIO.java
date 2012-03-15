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
package fr.n7.saceca.u3du.model.io.ai.service;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.ai.statement.CompoundOperatorAssignmentEffect;
import fr.n7.saceca.u3du.model.ai.statement.Condition;
import fr.n7.saceca.u3du.model.ai.statement.DefaultCondition;
import fr.n7.saceca.u3du.model.ai.statement.DefaultEffect;
import fr.n7.saceca.u3du.model.ai.statement.Effect;
import fr.n7.saceca.u3du.model.ai.statement.Statement;
import fr.n7.saceca.u3du.model.ai.statement.StatementsGroup;
import fr.n7.saceca.u3du.model.ai.statement.U3duExpression;
import fr.n7.saceca.u3du.model.io.ai.category.CategoryConverter;
import fr.n7.saceca.u3du.model.io.ai.clazz.ClassConverter;
import fr.n7.saceca.u3du.model.io.ai.statement.StatementsGroupConverter;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * A class to handle service import and export to XML.
 * 
 * @author Sylvain Cambon
 */
public class ServiceIO extends XStreamIO<Service> {
	
	/** Whether XStream has been configured to handle this class. */
	private static boolean configured = false;
	
	/** The action repository. */
	private Repository<StorableClassWrapper<Action>> actionRepository;
	
	/**
	 * Instantiates a new service io.
	 * 
	 * @param actionRepository
	 *            the action repository
	 */
	public ServiceIO(Repository<StorableClassWrapper<Action>> actionRepository) {
		super();
		this.actionRepository = actionRepository;
	}
	
	/**
	 * Configure if necessary.
	 * 
	 * @param xStream
	 *            the x stream
	 */
	@Override
	protected synchronized void configureIfNecessary(XStream xStream) {
		if (!configured) {
			xStream.alias("concept", String.class);
			xStream.processAnnotations(Service.class);
			xStream.processAnnotations(Effect.class);
			xStream.processAnnotations(DefaultCondition.class);
			xStream.processAnnotations(CompoundOperatorAssignmentEffect.class);
			xStream.processAnnotations(DefaultEffect.class);
			xStream.processAnnotations(U3duExpression.class);
			xStream.processAnnotations(Statement.class);
			xStream.processAnnotations(StatementsGroup.class);
			xStream.processAnnotations(Condition.class);
			xStream.registerConverter(new StatementsGroupConverter());
			xStream.registerConverter(new CategoryConverter());
			xStream.registerConverter(new ClassConverter<Action>(this.actionRepository));
			configured = true;
		}
	}
	
	/**
	 * Checks if is configured.
	 * 
	 * @return true, if is configured
	 */
	@Override
	protected synchronized boolean isConfigured() {
		return configured;
	}
	
}