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
package fr.n7.saceca.u3du.model.ai.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.ai.statement.StatementsGroup;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * A class to represent a service. A service is shared among all its providers.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("service")
public class Service implements Storable {
	
	/** The logger. */
	@XStreamOmitField
	private static Logger logger = Logger.getLogger(Service.class);
	
	/** The name. */
	@XStreamAlias("name")
	@XStreamAsAttribute
	private String name;
	
	/** The kind of service. */
	@XStreamAlias("active")
	@XStreamAsAttribute
	private boolean active;
	
	/** The max distance for usage. */
	@XStreamAlias("maxDist")
	@XStreamAsAttribute
	private int maxDistanceForUsage;
	
	/** The required consumer's categories. */
	@XStreamAlias("consumer-categories")
	private Set<Category> consumerCategories;
	
	/** The required provider's categories. */
	@XStreamAlias("provider-categories")
	private Set<Category> providerCategories;
	
	/** The statements. */
	@XStreamAlias("statements")
	private StatementsGroup statements;
	
	/** The action class. */
	@XStreamAlias("java-action")
	private Class<? extends Action> actionClass;
	
	/** The associated action. */
	@XStreamOmitField
	private Map<Couple<WorldObject, WorldObject>, Action> runningActions;
	
	/**
	 * Instantiates a new service.
	 * 
	 * @param name
	 *            The name.
	 * @param active
	 *            Whether the service is active
	 * @param maxDistanceForUsage
	 *            the maximum distance for usage
	 * @param providerCategories
	 *            the provider categories
	 * @param consumerCategories
	 *            All the consumerCategories the consumer has to be part of at the same time to use
	 *            this service.
	 * @param statements
	 *            The statements, i.e. the conditions and results of this sevice.
	 * @param actionClass
	 *            the action class
	 */
	public Service(String name, boolean active, int maxDistanceForUsage, Set<Category> providerCategories,
			Set<Category> consumerCategories, StatementsGroup statements, Class<? extends Action> actionClass) {
		super();
		this.active = active;
		this.maxDistanceForUsage = maxDistanceForUsage;
		this.providerCategories = providerCategories;
		this.consumerCategories = consumerCategories;
		this.name = name;
		this.statements = statements;
		this.actionClass = actionClass;
	}
	
	/**
	 * Read resolve to add non serialized data.
	 * 
	 * @return the object
	 */
	private Object readResolve() {
		if (this.consumerCategories == null) {
			this.consumerCategories = new HashSet<Category>();
		}
		if (this.providerCategories == null) {
			this.providerCategories = new HashSet<Category>();
		}
		if (this.runningActions == null) {
			this.runningActions = new HashMap<Couple<WorldObject, WorldObject>, Action>();
		}
		return this;
	}
	
	/**
	 * Executes the service. All the effects are edge-effects. The conditions are checked before
	 * usage.
	 * 
	 * @param provider
	 *            The provider.
	 * @param consumer
	 *            The consumer.
	 * @param parameters
	 *            the parameters
	 * @param mode
	 *            the mode
	 * @return the execution status
	 */
	private ExecutionStatus applyEffects(WorldObject provider, WorldObject consumer, Map<String, Object> parameters,
			ExecutionMode mode) {
		boolean useable = this.isUsable(provider, consumer, parameters);
		if (useable) {
			this.statements.executeEffects(provider, consumer, parameters, mode);
		}
		
		return useable ? ExecutionStatus.SUCCESSFUL_TERMINATION : ExecutionStatus.FAILURE;
	}
	
	/**
	 * Execute the service.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @param mode
	 *            the mode
	 * @return The execution status after the execution of a part of the service.
	 */
	public ExecutionStatus execute(WorldObject provider, WorldObject consumer, Map<String, Object> parameters,
			ExecutionMode mode) {
		ExecutionStatus state = ExecutionStatus.SUCCESSFUL_TERMINATION;
		Couple<WorldObject, WorldObject> couple = new Couple<WorldObject, WorldObject>(provider, consumer);
		
		// Useless in virtual mode
		if (mode != ExecutionMode.VIRTUAL) {
			Action action = this.runningActions.get(couple);
			if (action == null && this.actionClass != null) {
				try {
					action = this.actionClass.newInstance();
					this.runningActions.put(couple, action);
				} catch (InstantiationException e) {
					logger.warn("Could not instantiate " + this.actionClass.getCanonicalName(), e);
					state = ExecutionStatus.SUCCESSFUL_TERMINATION;
				} catch (IllegalAccessException e) {
					logger.warn("Could not access the default constructor of " + this.actionClass.getCanonicalName(), e);
					state = ExecutionStatus.SUCCESSFUL_TERMINATION;
				}
				
			}
			if (action != null) {
				state = action.executeStep(provider, consumer, parameters);
			}
		}
		
		if (state == ExecutionStatus.SUCCESSFUL_TERMINATION) {
			this.runningActions.remove(couple);
			state = this.applyEffects(provider, consumer, parameters, mode);
		}
		
		return state;
	}
	
	/**
	 * Gets the required consumer's categories.
	 * 
	 * @return the required consumer's categories
	 */
	public final Set<Category> getConsumerCategories() {
		return this.consumerCategories;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Gets the required provider's categories.
	 * 
	 * @return the required provider's categories
	 */
	public final Set<Category> getProviderCategories() {
		return this.providerCategories;
	}
	
	/**
	 * Gets the statements.
	 * 
	 * @return the statements
	 */
	public final StatementsGroup getStatements() {
		return this.statements;
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return this.name;
	}
	
	/**
	 * Checks if is the kind of service.
	 * 
	 * @return the kind of service
	 */
	public final boolean isActive() {
		return this.active;
	}
	
	/**
	 * Checks if the service is usable considering the provider and the consumer.
	 * 
	 * @param provider
	 *            The provider.
	 * @param consumer
	 *            The consumer.
	 * @param parameters
	 *            the parameters
	 * @return true, if is usable
	 */
	public boolean isUsable(WorldObject provider, WorldObject consumer, Map<String, Object> parameters) {
		final boolean consumerCategoryCheck = consumer.getCategories().containsAll(this.consumerCategories);
		final boolean providerCategoryCheck = provider.getCategories().containsAll(this.providerCategories);
		if (consumerCategoryCheck && providerCategoryCheck) {
			// The consumer & the provider have the right categories, the
			// preconditions
			// have to be checked
			final boolean preconditionsCheck = this.statements.checkConditions(provider, consumer, parameters);
			return preconditionsCheck;
		}
		return false;
	}
	
	/**
	 * Sets the kind of service.
	 * 
	 * @param active
	 *            the new kind of service
	 */
	public final void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Sets the required consumer's categories.
	 * 
	 * @param consumerCategories
	 *            the new required consumer's categories
	 */
	public final void setConsumerCategories(Set<Category> consumerCategories) {
		this.consumerCategories = consumerCategories;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the required provider's categories.
	 * 
	 * @param providerCategories
	 *            the new required provider's categories
	 */
	public final void setProviderCategories(Set<Category> providerCategories) {
		this.providerCategories = providerCategories;
	}
	
	/**
	 * Sets the statements.
	 * 
	 * @param statements
	 *            the new statements
	 */
	public final void setStatements(StatementsGroup statements) {
		this.statements = statements;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Service ");
		builder.append(this.name);
		builder.append("(active=");
		builder.append(this.active);
		builder.append(") (maxDist=");
		builder.append(this.maxDistanceForUsage);
		builder.append(")\nConsumer Categories=\n");
		for (Category cat : this.consumerCategories) {
			builder.append("\t");
			builder.append(cat);
			builder.append("\n");
		}
		builder.append("\nProvider Categories=\n");
		for (Category cat : this.providerCategories) {
			builder.append("\t");
			builder.append(cat);
			builder.append("\n");
		}
		builder.append(this.statements);
		builder.append("Java-code=");
		builder.append(this.actionClass == null ? null : this.actionClass.getCanonicalName());
		builder.append("\n");
		
		return builder.toString();
	}
	
	/**
	 * Gets the max distance for usage.
	 * 
	 * @return the max distance for usage
	 */
	public final int getMaxDistanceForUsage() {
		return this.maxDistanceForUsage;
	}
	
	/**
	 * Sets the max distance for usage.
	 * 
	 * @param maxDistanceForUsage
	 *            the new max distance for usage
	 */
	public final void setMaxDistanceForUsage(int maxDistanceForUsage) {
		this.maxDistanceForUsage = maxDistanceForUsage;
	}
	
}
