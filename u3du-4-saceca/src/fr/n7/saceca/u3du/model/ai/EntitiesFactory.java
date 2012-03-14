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
package fr.n7.saceca.u3du.model.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.category.CategoryModel;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.ai.object.behavior.Behavior;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.io.ai.model.agent.AgentModelIO;
import fr.n7.saceca.u3du.model.io.ai.model.object.WorldObjectModelIO;
import fr.n7.saceca.u3du.model.util.IDProvider;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;
import fr.n7.saceca.u3du.model.util.io.storage.HashMapRepository;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * A factory to create "entities" (i.e. reactive objects and agents).
 * 
 * @author Sylvain Cambon
 */
public class EntitiesFactory {
	
	/** The Constant DEFAULT_POSITION. */
	public static final Oriented2DPosition DEFAULT_POSITION = new Oriented2DPosition();
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(EntitiesFactory.class);
	
	/** The materials. */
	private EntitiesFactoryMaterials materials;
	
	/** The categories repository. */
	private Repository<Category> categoriesRepository;
	
	/** The world object common properties. */
	private static Set<PropertyModel<?>> worldObjectCommonProperties = new HashSet<PropertyModel<?>>();
	
	/** The agent common properties. */
	private static Set<PropertyModel<?>> agentCommonProperties = new HashSet<PropertyModel<?>>();
	
	// Static code to initialize common properties that would otherwise be programmatically added.
	static {
		WorldObjectModelIO womio = new WorldObjectModelIO();
		AgentModelIO amio = new AgentModelIO();
		try {
			worldObjectCommonProperties = womio.importObject(
					"bin/fr/n7/saceca/u3du/model/ai/object/_CommonWorldObjectModel.objmod.xml").getProperties();
			agentCommonProperties = amio.importObject(
					"bin/fr/n7/saceca/u3du/model/ai/agent/_CommonAgentModel.agmod.xml").getProperties();
		} catch (IOException e) {
			logger.error("The common descriptions for models cannot be retrieved.", e);
		}
	}
	
	/**
	 * Instantiates a new world object factory.
	 * 
	 * @param materials
	 *            the materials
	 */
	public EntitiesFactory(EntitiesFactoryMaterials materials) {
		super();
		this.materials = materials;
		this.categoriesRepository = new HashMapRepository<Category>("Category");
	}
	
	/**
	 * Get all the names of the emotions felt by an agent
	 * 
	 * @return the emotion list
	 */
	public static ArrayList<String> getEmotions() {
		ArrayList<String> emotionList = new ArrayList<String>();
		
		for (PropertyModel pm : EntitiesFactory.agentCommonProperties) {
			if (pm.getName().startsWith(Emotion.PREFIX)) { // if it is an emotion
				emotionList.add(pm.getName().split("_")[2]); // we add its name
			}
		}
		
		return emotionList;
	}
	
	/**
	 * Repair the belonging relation among given world objects.
	 * 
	 * @param objects
	 *            the objects
	 */
	public static void repairBelongings(Collection<WorldObject> objects) {
		Map<Long, WorldObject> map = new HashMap<Long, WorldObject>();
		
		// Filling the map
		for (WorldObject object : objects) {
			map.put(object.getId(), object);
		}
		
		// Repairing
		for (WorldObject object : objects) {
			Set<WorldObject> toBeRemoved = new HashSet<WorldObject>();
			Set<WorldObject> toBeAdded = new HashSet<WorldObject>();
			for (WorldObject ghostBelonging : object.getBelongings()) {
				toBeRemoved.add(ghostBelonging);
				WorldObject belonging = map.get(ghostBelonging.getId());
				if (belonging == null) {
					logger.warn(object.toShortString() + " has an item " + ghostBelonging.toShortString()
							+ " but no such item could be found. This link was skipped.");
				} else {
					toBeAdded.add(belonging);
				}
			}
			object.getBelongings().removeAll(toBeRemoved);
			object.getBelongings().addAll(toBeAdded);
		}
	}
	
	/**
	 * Creates a new agent according to its model. The agent has to be manually added to the world.
	 * 
	 * @param modelName
	 *            the model name
	 * @return the world object instance, null if a problem arose.
	 * @throws MalformedObjectException
	 *             If the object was not well-formed.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Agent createAgent(String modelName) throws MalformedObjectException {
		boolean endedWell;
		
		// 1. Gather elements
		// 1.0. Model
		if (null == modelName) {
			logger.error("The given model name is null so creation is cancelled");
			return null;
		}
		AgentModel model = this.materials.getAgentModelRepository().get(modelName);
		if (model == null) {
			logger.error("No model named " + modelName + " can be retrieved so creation is cancelled");
			return null;
		}
		
		// A. Building the agent
		Agent agent = new Agent(modelName, IDProvider.INSTANCE.getNewID());
		
		// 1.1. Position
		agent.setPosition(DEFAULT_POSITION.clone());
		
		// 1.2. Behavior and modules
		Behavior behavior = this.handleBehavior(model.getBehaviorName());
		if (behavior == null) {
			return null;
		}
		agent.setBehavior(behavior);
		behavior.init(agent);
		
		// 1.3. Categories & properties models part I
		Set<PropertyModel<?>> propertiesModels = new HashSet<PropertyModel<?>>();
		this.addAllAndReplaceExisting(propertiesModels, EntitiesFactory.worldObjectCommonProperties);
		this.addAllAndReplaceExisting(propertiesModels, EntitiesFactory.agentCommonProperties);
		Collection<Category> categories = agent.getCategories();
		endedWell = this.handleCategoriesAndRelatedProperties(model, propertiesModels, categories);
		if (!endedWell) {
			return null;
		}
		
		// 1.4. Properties models part II
		if (model.getProperties() == null) {
			logger.warn("The collection of properties for model " + modelName + " is null. Creation continues.");
		} else {
			this.addAllAndReplaceExisting(propertiesModels, model.getProperties());
		}
		
		// 1.5. Services
		endedWell = this.handleServices(model, agent, categories);
		if (!endedWell) {
			return null;
		}
		
		// 2. Building the properties
		PropertiesContainer properties = agent.getPropertiesContainer();
		for (PropertyModel<?> propertyModel : propertiesModels) {
			Property<?> property = new Property(propertyModel);
			if (Double.class.equals(propertyModel.getType()) && propertyModel.getName().startsWith(Gauge.PREFIX)) {
				property = new Gauge((Property<Double>) property);
			} else if (Double.class.equals(propertyModel.getType())
					&& propertyModel.getName().startsWith(Emotion.PREFIX)) {
				property = new Emotion((Property<Double>) property);
			}
			this.addProperty(property, properties);
		}
		
		agent.getMemory().initializeKnowledge();
		
		return agent;
	}
	
	/**
	 * Creates a new WorldObject object according to its model. The object has to be manually added
	 * to the world.
	 * 
	 * @param modelName
	 *            the model name
	 * @return the world object instance, null if a problem arose.
	 */
	@SuppressWarnings("rawtypes")
	public WorldObject createWorldObject(String modelName) {
		// 1. Gather elements
		// 1.0. Model
		if (null == modelName) {
			logger.error("The given model name is null so creation is cancelled");
			return null;
		}
		WorldObjectModel wom = this.materials.getWorldObjectModelRepository().get(modelName);
		if (wom == null) {
			logger.error("No model named " + modelName + " can be retrieved so creation is cancelled");
			return null;
		}
		
		// A. Building the object
		WorldObject worldObject = new WorldObject(modelName, IDProvider.INSTANCE.getNewID());
		
		// 1.1. Position
		worldObject.setPosition(DEFAULT_POSITION.clone());
		
		// 1.2. Behavior
		String behaviorName = wom.getBehaviorName();
		if (behaviorName == null) {
			logger.error("The behavior model" + modelName + " has a null name so creation is cancelled");
			return null;
		}
		Behavior behavior = this.handleBehavior(behaviorName);
		if (behavior == null) {
			// A problem occurred
			return null;
		}
		
		// B. Building the object
		worldObject.setBehavior(behavior);
		behavior.init(worldObject);
		
		// 1.3. Categories & properties models part I
		Set<PropertyModel<?>> propertiesModels = new HashSet<PropertyModel<?>>();
		this.addAllAndReplaceExisting(propertiesModels, EntitiesFactory.worldObjectCommonProperties);
		Collection<Category> categories = worldObject.getCategories();
		boolean endedWell = this.handleCategoriesAndRelatedProperties(wom, propertiesModels, categories);
		if (!endedWell) {
			return null;
		}
		
		// 1.4. Properties models part II
		if (wom.getProperties() == null) {
			logger.warn("The collection of properties for model " + modelName + " is null. Creation continues.");
		} else {
			this.addAllAndReplaceExisting(propertiesModels, wom.getProperties());
		}
		
		// 1.5. Services
		endedWell = this.handleServices(wom, worldObject, categories);
		if (!endedWell) {
			return null;
		}
		
		// 1.6 Concepts (for the markov method)
		worldObject.setConcepts(wom.getConcepts());
		
		// 2. Building the properties
		PropertiesContainer properties = worldObject.getPropertiesContainer();
		for (PropertyModel<?> propertyModel : propertiesModels) {
			@SuppressWarnings("unchecked")
			Property<?> property = new Property(propertyModel);
			this.addProperty(property, properties);
		}
		
		return worldObject;
	}
	
	/**
	 * Adds all the given elements and replace existing ones if an element is in both.
	 * 
	 * @param initialSet
	 *            the initial set
	 * @param changeSet
	 *            the change set
	 */
	private void addAllAndReplaceExisting(Set<PropertyModel<?>> initialSet, Set<PropertyModel<?>> changeSet) {
		initialSet.removeAll(changeSet);
		initialSet.addAll(changeSet);
	}
	
	/**
	 * Adds the property.
	 * 
	 * @param property
	 *            the property
	 * @param properties
	 *            the properties
	 */
	@SuppressWarnings("unchecked")
	private void addProperty(Property<?> property, PropertiesContainer properties) {
		if (property.getModel().getName().startsWith(Gauge.PREFIX)) {
			properties.addProperty(new Gauge((Property<Double>) property));
		} else if (property.getModel().getName().startsWith(Emotion.PREFIX)) {
			properties.addProperty(new Emotion((Property<Double>) property));
		} else {
			properties.addProperty(property);
		}
	}
	
	/**
	 * Handles behavior creation.
	 * 
	 * @param behaviorName
	 *            the behavior name
	 * @return the behavior or null if a problem arose.
	 */
	private Behavior handleBehavior(String behaviorName) {
		return (Behavior) this.handleSubElementsInstantiation(behaviorName, this.materials.getBehaviorRepository());
	}
	
	/**
	 * Handle categories and related properties.
	 * 
	 * @param model
	 *            the world object model
	 * @param propertiesModels
	 *            the properties models
	 * @param categories
	 *            the categories
	 * @return true, if no error was encountered (warnings may however be issued).
	 */
	private boolean handleCategoriesAndRelatedProperties(WorldObjectModel model,
			Set<PropertyModel<?>> propertiesModels, Collection<Category> categories) {
		Set<PropertyModel<?>> categoryProperties;
		Set<String> categoriesNames = model.getCategoriesNames();
		if (categoriesNames != null) {
			for (String categoryName : categoriesNames) {
				if (categoryName != null) {
					if (!this.categoriesRepository.contains(categoryName)) {
						this.categoriesRepository.put(new Category(categoryName));
					}
					Category category = this.categoriesRepository.get(categoryName);
					categories.add(category);
					Repository<CategoryModel> categoryModelRepository = this.materials.getCategoryModelRepository();
					if (categoryModelRepository != null) {
						CategoryModel categoryModel = categoryModelRepository.get(categoryName);
						if (categoryModel != null) {
							categoryProperties = categoryModel.getProperties();
							this.addAllAndReplaceExisting(propertiesModels, categoryProperties);
						} else {
							logger.warn("No category model " + categoryName + " can be retrieved. Creation continues.");
						}
					} else {
						logger.warn("The categories repository is null. Creation continues.");
					}
					
				} else {
					logger.error("The model " + model.getName() + " has a null category name. Creation continues.");
					return false;
				}
			}
		} else {
			logger.warn("The model " + model.getName() + " has null as categories names. Creation continues.");
		}
		return true;
	}
	
	/**
	 * Handles services.
	 * 
	 * @param model
	 *            the model
	 * @param object
	 *            the object
	 * @param categories
	 *            the categories
	 * @return true, if no error was encountered (warnings may however be issued).
	 */
	private boolean handleServices(WorldObjectModel model, WorldObject object, Collection<Category> categories) {
		Set<String> servicesNames = model.getServicesNames();
		Collection<Service> services = object.getServices();
		if (servicesNames == null) {
			logger.warn("The collection of services names for model " + model.getName()
					+ " is null. Creation continues.");
		} else {
			for (String serviceName : servicesNames) {
				Repository<Service> serviceRepository = this.materials.getServiceRepository();
				if (serviceRepository == null) {
					logger.warn("The service repository is null. Creation continues.");
				} else {
					Service service = serviceRepository.get(serviceName);
					if (service != null) {
						if (categories.containsAll(service.getProviderCategories())) {
							services.add(service);
						} else {
							logger.error("The service " + serviceName + " requires categories that " + model.getName()
									+ " has not so the creation is cancelled.");
							return false;
						}
					} else {
						logger.warn("The service " + serviceName + " cannot be found.");
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Handles behavior and modules instances creation. The repository has to contain
	 * StorableClassWrapper elements.
	 * 
	 * @param qName
	 *            the qualified name of the class to be instantiated
	 * @param classRepository
	 *            the class repository
	 * @return the instance or null if a problem arose.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object handleSubElementsInstantiation(String qName, Repository classRepository) {
		Object object = null;
		try {
			StorableClassWrapper<?> clazz = (StorableClassWrapper<?>) classRepository.get(qName);
			if (clazz == null) {
				Class<?> clClazz = EntitiesFactory.class.getClassLoader().loadClass(qName);
				clazz = new StorableClassWrapper(clClazz);
			}
			object = clazz.newInstance();
			
		} catch (InstantiationException e) {
			logger.error("The class " + qName + " has no default constructor so creation is cancelled");
			return null;
		} catch (IllegalAccessException e) {
			logger.error("The class " + qName + " has no visible default constructor so creation is cancelled");
			return null;
		} catch (ClassNotFoundException e) {
			logger.error("No class corresponding to " + qName + " can be retrieved so creation is cancelled", e);
			e.printStackTrace();
		}
		return object;
	}
}