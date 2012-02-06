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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import Emotion_secondary.update_secondary;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;
import fr.n7.saceca.u3du.model.util.io.storage.Storable;

/**
 * A class to represent a service. A service is shared among all its providers.
 * 
 * @author Sylvain Cambon, Ciprian Munteanu, Mehdi Boukhris
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
	
	/** The provider's id */
	@XStreamOmitField
	private Long providerId;
	
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
	
	/** The service's parameters */
	@XStreamAlias("service-parameters")
	private ArrayList<Param> serviceParameters;
	
	/** The service's preconditions */
	@XStreamAlias("service-preconditions")
	private ArrayList<ServiceProperty> servicePreconditions;
	
	/** The service's effects plus */
	@XStreamAlias("service-effectsPlus")
	private ArrayList<ServiceProperty> serviceEffectsPlus;
	
	/** The service negative effects */
	@XStreamAlias("service-effectsMinus")
	private ArrayList<ServiceProperty> serviceEffectsMinus;
	
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
			Set<Category> consumerCategories, ArrayList<Param> serviceParameters,
			ArrayList<ServiceProperty> servicePreconditions, ArrayList<ServiceProperty> serviceEffectsPlus,
			ArrayList<ServiceProperty> serviceEffectsMinus, Class<? extends Action> actionClass) {
		super();
		this.active = active;
		this.maxDistanceForUsage = maxDistanceForUsage;
		this.providerCategories = providerCategories;
		this.consumerCategories = consumerCategories;
		this.name = name;
		this.serviceParameters = serviceParameters;
		this.servicePreconditions = servicePreconditions;
		this.serviceEffectsPlus = serviceEffectsPlus;
		this.serviceEffectsMinus = serviceEffectsMinus;
		this.actionClass = actionClass;
		this.readResolve();
	}
	
	/**
	 * Default constructor
	 */
	public Service() {
		super();
		this.active = true;
		this.maxDistanceForUsage = 0;
		this.providerCategories = null;
		this.consumerCategories = null;
		this.name = "";
		this.serviceParameters = null;
		this.servicePreconditions = null;
		this.serviceEffectsPlus = null;
		this.serviceEffectsMinus = null;
		this.setProviderId(0L);
		this.readResolve();
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
	 * Executes the service by applying all the positive and negative effects of that service
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
			Agent agent = (Agent) consumer;
			
			ArrayList<ServiceProperty> servicePropertyList = new ArrayList<ServiceProperty>();
			
			for (ServiceProperty serviceProperty : this.serviceEffectsPlus) {
				servicePropertyList.add(serviceProperty.deepDataClone());
			}
			
			for (ServiceProperty serviceProperty : this.serviceEffectsMinus) {
				servicePropertyList.add(serviceProperty.deepDataClone());
			}
			
			String treatment;
			String type, paramValue;
			
			PropertiesContainer agentProperties = agent.getPropertiesContainer();
			PropertiesContainer agentPropertiesFromMemory = agent.getMemory().getKnowledgeAboutOwner()
					.getPropertiesContainer();
			
			for (ServiceProperty serviceProperty : servicePropertyList) {
				treatment = serviceProperty.getTreatment_effect();
				String[] vars = new String[3];
				vars = treatment.split("__");
				type = serviceProperty.getParameter(vars[2]).getParamType();
				paramValue = serviceProperty.getParameter(vars[2]).getParamValue();
				
				try {
					if (vars[1].equals("+")) {
						if (type.equals("int")) {
							Integer value = Integer.parseInt(paramValue);
							agentProperties.setInt(vars[0], agentProperties.getInt(vars[0]) + value);
							agentPropertiesFromMemory
									.setInt(vars[0], agentPropertiesFromMemory.getInt(vars[0]) + value);
						} else if (type.equals("double")) {
							Double value = Double.parseDouble(paramValue);
							agentProperties.setDouble(vars[0], agentProperties.getDouble(vars[0]) + value);
							agentPropertiesFromMemory.setDouble(vars[0], agentPropertiesFromMemory.getDouble(vars[0])
									+ value);
						}
					} else if (vars[1].equals("-")) {
						if (type.equals("int")) {
							Integer value = Integer.parseInt(paramValue);
							agentProperties.setInt(vars[0], agentProperties.getInt(vars[0]) - value);
							agentPropertiesFromMemory
									.setInt(vars[0], agentPropertiesFromMemory.getInt(vars[0]) - value);
						} else if (type.equals("double")) {
							Double value = Double.parseDouble(paramValue);
							agentProperties.setDouble(vars[0], agentProperties.getDouble(vars[0]) - value);
							agentPropertiesFromMemory.setDouble(vars[0], agentPropertiesFromMemory.getDouble(vars[0])
									- value);
						}
					} else if (vars[1].equals("=")) {
						if (type.equals("string")) {
							agentProperties.setString(vars[0], paramValue);
							agentPropertiesFromMemory.setString(vars[0], paramValue);
						} else if (type.equals("3DPoint")) {
							String[] coordonates = paramValue.split("_");
							agent.setPosition(new Oriented2DPosition(Float.parseFloat(coordonates[0]), Float
									.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2])));
							agent.getMemory()
									.getKnowledgeAboutOwner()
									.setPosition(
											new Oriented2DPosition(Float.parseFloat(coordonates[0]), Float
													.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2])));
						}
						
					}
				} catch (Exception e) {
					
				}
			}
			
			if (agent.getMemory().getKnowledgeAbout(this.providerId) != null) {
				agent.getMemory().getMemoryElements().get(this.providerId)
						.increaseNbReferences(Memory.NB_REFERENCES_FROM_USAGE);
			}
			
			update_secondary update = new update_secondary();
			
			int[][] result = update.resultingSecondaryEmotions(update.potentialsecondary(this),
					update.potentialvaluesecondary(agent.getEmotions()));
			int i = 0;
			for (Emotion secondary : agent.getSecondaryEmotions()) {
				if (result[0][i] != 0) {
					secondary.setValue(secondary.getValue() + result[0][i]);
					System.out.println(secondary.getValue());
				}
				i++;
			}
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
	 * Uses the service.
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
	public ExecutionStatus useService(WorldObject provider, WorldObject consumer, Map<String, Object> parameters,
			ExecutionMode mode) {
		ExecutionStatus state = ExecutionStatus.SUCCESSFUL_TERMINATION;
		Couple<WorldObject, WorldObject> couple = new Couple<WorldObject, WorldObject>(provider, consumer);
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
		if (provider == null || consumer == null) {
			return true;
		}
		final boolean consumerCategoryCheck = consumer.getCategories().containsAll(this.consumerCategories);
		final boolean providerCategoryCheck = provider.getCategories().containsAll(this.providerCategories);
		if (consumerCategoryCheck && providerCategoryCheck) {
			
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the service is usable considering the provider and the consumer. This methos is
	 * used by the classical method of planning
	 * 
	 * @param provider
	 *            The provider.
	 * @param consumer
	 *            The consumer.
	 * @param parameters
	 *            the parameters
	 * @return true, if is usable
	 */
	public boolean isClassicUsable(WorldObject provider, Agent consumer) {
		if (provider == null || consumer == null) {
			return true;
		}
		final boolean consumerCategoryCheck = consumer.getCategories().containsAll(this.consumerCategories);
		final boolean providerCategoryCheck = provider.getCategories().containsAll(this.providerCategories);
		
		if (consumerCategoryCheck && providerCategoryCheck) {
			for (ServiceProperty precondition : this.servicePreconditions) {
				if (!precondition.getPropertyName().equals("at")
						&& !consumer.getMemory().checkVirtualMemory(precondition)) {
					return false;
				}
			}
			return true;
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
		// builder.append(this.statements);
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
	
	/**
	 * Gets the service parameters
	 * 
	 * @return
	 */
	public ArrayList<Param> getServiceParameters() {
		return this.serviceParameters;
	}
	
	/**
	 * Sets the service parameters
	 * 
	 * @param serviceParameters
	 *            the list of parameters
	 */
	public void setServiceParameters(ArrayList<Param> serviceParameters) {
		this.serviceParameters = serviceParameters;
	}
	
	/**
	 * Gets the service preconditions
	 * 
	 * @return
	 */
	public ArrayList<ServiceProperty> getServicePreconditions() {
		return this.servicePreconditions;
	}
	
	/**
	 * Sets the service preconditions
	 * 
	 * @param servicePreconditions
	 *            the list of preconditions
	 */
	public void setServicePreconditions(ArrayList<ServiceProperty> servicePreconditions) {
		this.servicePreconditions = servicePreconditions;
	}
	
	/**
	 * Gets the service effects plus
	 * 
	 * @return
	 */
	public ArrayList<ServiceProperty> getServiceEffectsPlus() {
		return this.serviceEffectsPlus;
	}
	
	/**
	 * Sets the service effects plus
	 * 
	 * @param serviceEffectsPlus
	 *            the positive effects list
	 */
	public void setServiceEffectsPlus(ArrayList<ServiceProperty> serviceEffectsPlus) {
		this.serviceEffectsPlus = serviceEffectsPlus;
	}
	
	/**
	 * Gets the service effects minus
	 * 
	 * @return
	 */
	public ArrayList<ServiceProperty> getServiceEffectsMinus() {
		return this.serviceEffectsMinus;
	}
	
	/**
	 * Sets the service effects minus
	 * 
	 * @param serviceEffectsMinus
	 *            the negative effects list
	 */
	public void setServiceEffectsMinus(ArrayList<ServiceProperty> serviceEffectsMinus) {
		this.serviceEffectsMinus = serviceEffectsMinus;
	}
	
	/**
	 * Gets a service parameter
	 * 
	 * @param index
	 *            the index
	 * @return
	 */
	public Param getParameter(int index) {
		return this.serviceParameters.get(index);
	}
	
	/**
	 * Gets a service parameter
	 * 
	 * @param name
	 *            the parameter's name
	 * @return
	 */
	public Param getParameter(String name) {
		for (Param parameter : this.serviceParameters) {
			if (parameter.getParamName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}
	
	/**
	 * Gets the parameter's value
	 * 
	 * @param name
	 *            the parameter's name
	 * @return
	 */
	public String getParamValue(String name) {
		for (Param parameter : this.serviceParameters) {
			if (parameter.getParamName().equals(name)) {
				return parameter.getParamValue();
			}
		}
		return "";
	}
	
	/**
	 * Sets a paramter
	 * 
	 * @param index
	 *            the index of parameter to be set
	 * @param parameter
	 *            the new parameter
	 */
	public void setParameter(int index, Param parameter) {
		this.serviceParameters.set(index, parameter);
	}
	
	/**
	 * Sets the last parameter in the parameters list
	 * 
	 * @param parameter
	 *            the parameter
	 */
	public void setLastParameter(Param parameter) {
		this.setParameter(this.serviceParameters.size() - 1, parameter);
	}
	
	/**
	 * Gets the last parameter in the parameters list
	 * 
	 * @return
	 */
	public Param getLastParameter() {
		return this.getParameter(this.serviceParameters.size() - 1);
	}
	
	/**
	 * Gets a precondition
	 * 
	 * @param index
	 *            the index
	 * @return
	 */
	public ServiceProperty getPrecondition(int index) {
		return this.servicePreconditions.get(index);
	}
	
	/**
	 * Gets the last precondition
	 * 
	 * @return
	 */
	public ServiceProperty getLastPrecondition() {
		return this.getPrecondition(this.servicePreconditions.size() - 1);
	}
	
	/**
	 * Sets a precondition
	 * 
	 * @param index
	 *            the index
	 * @param precond
	 *            the new precondition
	 */
	public void setPrecondition(int index, ServiceProperty precond) {
		this.servicePreconditions.set(index, precond);
	}
	
	/**
	 * Sets the last precondition in the list
	 * 
	 * @param precond
	 *            the new precondition
	 */
	public void setLastPrecondition(ServiceProperty precond) {
		this.setPrecondition(this.servicePreconditions.size() - 1, precond);
	}
	
	/**
	 * Gets an effect plus
	 * 
	 * @param index
	 *            the index
	 * @return
	 */
	public ServiceProperty getEffectPlus(int index) {
		return this.serviceEffectsPlus.get(index);
	}
	
	/**
	 * Gets the last positive effect in the list
	 * 
	 * @return
	 */
	public ServiceProperty getLastEffectPlus() {
		return this.getEffectPlus(this.serviceEffectsPlus.size() - 1);
	}
	
	/**
	 * Sets a positive effect
	 * 
	 * @param index
	 *            the index
	 * @param precond
	 *            the positive effect
	 */
	public void setEffectPlus(int index, ServiceProperty precond) {
		this.serviceEffectsPlus.set(index, precond);
	}
	
	/**
	 * Sets the last positive effect in the list
	 * 
	 * @param precond
	 *            the new positive effect
	 */
	public void setLastEffectPlus(ServiceProperty precond) {
		this.setEffectPlus(this.serviceEffectsPlus.size() - 1, precond);
	}
	
	/**
	 * Gets a negative effect
	 * 
	 * @param index
	 *            the index
	 * @return
	 */
	public ServiceProperty getEffectMinus(int index) {
		return this.serviceEffectsMinus.get(index);
	}
	
	/**
	 * Gets the last negative effect
	 * 
	 * @return
	 */
	public ServiceProperty getLastEffectMinus() {
		return this.getEffectMinus(this.serviceEffectsMinus.size() - 1);
	}
	
	/**
	 * Sets a negative effect
	 * 
	 * @param index
	 *            the index in the list
	 * @param precond
	 *            the new negative effect
	 */
	public void setEffectMinus(int index, ServiceProperty precond) {
		this.serviceEffectsMinus.set(index, precond);
	}
	
	/**
	 * Sets the last negative effect
	 * 
	 * @param precond
	 *            the new negative effect
	 */
	public void setLastEffectMinus(ServiceProperty precond) {
		this.setEffectMinus(this.serviceEffectsMinus.size() - 1, precond);
	}
	
	/**
	 * Sets the provider's id
	 * 
	 * @param providerId
	 *            the provider's id
	 */
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * Gets the provider's id
	 * 
	 * @return
	 */
	public Long getProviderId() {
		return this.providerId;
	}
	
	/**
	 * Completly clones the service
	 * 
	 * @return the cloned service
	 */
	public Service deepDataClone() {
		Service clone = new Service(this.name, this.active, this.maxDistanceForUsage, null, null, null, null, null,
				null, this.actionClass);
		
		clone.setProviderId(this.providerId);
		
		Set<Category> catSet = new HashSet<Category>();
		for (Category cat : this.providerCategories) {
			catSet.add(cat);
		}
		clone.setProviderCategories(catSet);
		
		catSet = new HashSet<Category>();
		
		for (Category cat : this.consumerCategories) {
			catSet.add(cat);
		}
		clone.setConsumerCategories(catSet);
		
		ArrayList<Param> paramList = new ArrayList<Param>();
		for (Param parameter : this.serviceParameters) {
			paramList.add(parameter.deepDataClone());
		}
		clone.setServiceParameters(paramList);
		
		ArrayList<ServiceProperty> proprList = new ArrayList<ServiceProperty>();
		for (ServiceProperty propr : this.servicePreconditions) {
			proprList.add(propr.deepDataClone());
		}
		clone.setServicePreconditions(proprList);
		
		proprList = new ArrayList<ServiceProperty>();
		for (ServiceProperty propr : this.serviceEffectsPlus) {
			proprList.add(propr.deepDataClone());
		}
		clone.setServiceEffectsPlus(proprList);
		
		proprList = new ArrayList<ServiceProperty>();
		for (ServiceProperty propr : this.serviceEffectsMinus) {
			proprList.add(propr.deepDataClone());
		}
		clone.setServiceEffectsMinus(proprList);
		
		return clone;
	}
	
}
