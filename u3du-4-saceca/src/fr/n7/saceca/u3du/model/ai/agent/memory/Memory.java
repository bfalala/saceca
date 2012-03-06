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
package fr.n7.saceca.u3du.model.ai.agent.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.Plan;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization.TableClass;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.DefaultMMGoalStack;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoalStack;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.util.Couple;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * The memory of an agent.<br/>
 * 
 * It remembers memory elements (a memory element is basically a kind of clone of a world object)
 * 
 * The Memory also contains the message inbox, the goals, and the past plans of the agent.
 * 
 * @author JÃ©rÃ´me Dalbert, Ciprian Munteanu
 */
@XStreamAlias("memory")
public class Memory {
	
	/**
	 * The memories about world objects. It is a map so as to have immediate access to a memory if
	 * we know the id of the remembered worldObject.
	 */
	@XStreamAlias("memory-elements")
	private Map<Long, MemoryElement> memoryElements;
	
	/** UNUSED !! The memories sorted by insertion order, from the older to the newer. */
	private List<MemoryElement> insertionOrderedElements;
	
	/** The message inbox. */
	private Queue<Message> messageInbox;
	
	/**
	 * The list of goals - useless but kept here because of the old modules that were not removed
	 * and they use this variable
	 */
	private List<Goal> goals;
	
	/** The stack of goals. */
	private MMGoalStack goalStack;
	
	/**
	 * The past plans that have succeeded.
	 */
	private Map<MMGoal, Plan> pastPlans;
	
	/** The max size for memory elements. */
	@XStreamAlias("maxSize")
	@XStreamAsAttribute
	private int maxSize;
	
	/** The agent who owns this memory. */
	@XStreamOmitField
	private Agent ownerAgent;
	
	/** The list of perceived objects */
	@XStreamOmitField
	private ArrayList<Couple<WorldObject, Boolean>> perceivedObjects;
	
	/** UNUSED :-) The surrounding objects. */
	public Collection<WorldObject> surroundingObjects;
	
	/** The Constant INITIAL_KNOWN_PROPERTIES. */
	public static final String[] INITIAL_KNOWN_PROPERTIES = { Internal.Agent.NAME };
	
	/**
	 * The Constant INITIAL_ENTRY_NB_REFERENCES - number of references when a new object is added in
	 * the memory.
	 */
	public static final int INITIAL_ENTRY_NB_REFERENCES = 10;
	
	/**
	 * The Constant LIMIT_NB_REFERENCES - the required number of references for an object to be
	 * added in the long-term memory.
	 */
	public static final int LIMIT_NB_REFERENCES = 30;
	
	/**
	 * The Constant NB_REFERENCES_FROM_PERCEPTION - the increase value of number of references from
	 * perception.
	 */
	public static final int NB_REFERENCES_FROM_PERCEPTION = 1;
	
	/**
	 * The Constant NB_REFERENCES_FROM_USAGE - the increase value of number of references when the
	 * agent uses an object's service.
	 */
	public static final int NB_REFERENCES_FROM_USAGE = 10;
	
	/**
	 * The Constant DIVIDE_PERCENT - the division percent = how much represents the short-term
	 * memory from the entire memory.
	 */
	public static final double DIVIDE_PERCENT = 0.3;
	
	/**
	 * The Constant MISC_MODELS - models of objects that are not very important for the agent - they
	 * cannot be added in the long-term memory.
	 */
	public static final String[] MISC_MODELS = { "Pavement", "TrafficLight", "Road", "PedestrianCrossing", "Car", "Bus" };
	
	/**
	 * Instantiates a new memory.
	 * 
	 * @param ownerAgent
	 *            the agent
	 * @param maxSize
	 *            the max size
	 */
	public Memory(Agent ownerAgent, int maxSize) {
		this.ownerAgent = ownerAgent;
		this.memoryElements = new ConcurrentHashMap<Long, MemoryElement>();
		this.maxSize = maxSize;
		this.messageInbox = new ConcurrentLinkedQueue<Message>();
		this.pastPlans = new ConcurrentHashMap<MMGoal, Plan>();
		this.surroundingObjects = Collections.synchronizedList(new ArrayList<WorldObject>());
		this.insertionOrderedElements = Collections.synchronizedList(new ArrayList<MemoryElement>());
		this.goalStack = new DefaultMMGoalStack();
		this.perceivedObjects = new ArrayList<Couple<WorldObject, Boolean>>();
	}
	
	/**
	 * Helps to rebuild the object.
	 * 
	 * @return the memory
	 */
	public Memory readResolve() {
		if (this.memoryElements == null) {
			this.memoryElements = new ConcurrentHashMap<Long, MemoryElement>();
		}
		
		if (this.goalStack == null) {
			this.goalStack = new DefaultMMGoalStack();
		}
		if (this.messageInbox == null) {
			this.messageInbox = new ConcurrentLinkedQueue<Message>();
		}
		if (this.pastPlans == null) {
			this.pastPlans = new ConcurrentHashMap<MMGoal, Plan>();
		}
		if (this.surroundingObjects == null) {
			this.surroundingObjects = Collections.synchronizedList(new ArrayList<WorldObject>());
		}
		if (this.insertionOrderedElements == null) {
			this.insertionOrderedElements = Collections.synchronizedList(new ArrayList<MemoryElement>());
		}
		if (this.perceivedObjects == null) {
			this.perceivedObjects = new ArrayList<Couple<WorldObject, Boolean>>();
		}
		
		return this;
	}
	
	/**
	 * Initialize knowledge. Used by the agent factory
	 * 
	 * @throws MalformedObjectException
	 *             If a required property is missing.
	 */
	public void initializeKnowledge() throws MalformedObjectException {
		if (this.ownerAgent != null) {
			// We initialize an empty knowledge that the owner has about himself
			WorldObject knowledgeAboutOwner = this.remember(this.ownerAgent.getEmptyClone(), false);
			
			// The owner knows where he is
			knowledgeAboutOwner.setPosition(this.ownerAgent.getPosition().clone());
			
			// The owner knows the properties defined in INITIAL_KNOWN_PROPERTIES,
			// if they exist
			for (String propertyName : INITIAL_KNOWN_PROPERTIES) {
				try {
					Property<?> prop = this.ownerAgent.getPropertiesContainer().getProperty(propertyName);
					knowledgeAboutOwner.getPropertiesContainer().addProperty(prop.clone());
				} catch (UnknownPropertyException e) {
					// Earlier exceptions in the agent creation should prevent this.
					throw new MalformedObjectException(this.ownerAgent, propertyName, e);
				}
			}
		}
	}
	
	/**
	 * Forget.
	 * 
	 * @param memoryElement
	 *            the object
	 */
	public void forget(MemoryElement memoryElement) {
		this.memoryElements.remove(memoryElement.getWorldObject().getId());
		this.insertionOrderedElements.remove(memoryElement);
	}
	
	/**
	 * Forget
	 * 
	 * @param id
	 *            the id of object to forget
	 */
	public void forget(Long id) {
		this.memoryElements.remove(id);
	}
	
	/**
	 * foergets an element from the short-term memory
	 * 
	 * @return true if succesful
	 */
	public boolean forgetElementFromShortTermMemory() {
		this.forget(this.getIdElementToForget());
		
		return true;
	}
	
	/**
	 * gets the object with the lowest number of references
	 * 
	 * @return the id of the object to forget
	 */
	public Long getIdElementToForget() {
		int min = Integer.MAX_VALUE;
		long id = 0L;
		for (MemoryElement element : this.getShortTermMemory().values()) {
			if (min > element.getNbReferences()) {
				min = element.getNbReferences();
				id = element.getWorldObject().getId();
			}
		}
		return id;
	}
	
	/**
	 * Remembers a world object.
	 * 
	 * @param worldObject
	 *            the world object
	 * @param forgettable
	 *            the forgettable
	 * @return the world object
	 */
	public WorldObject remember(WorldObject worldObject, boolean forgettable) {
		MemoryElement memoryElement = null;
		if (!forgettable) {
			memoryElement = new MemoryElement(worldObject, forgettable, "long", Memory.INITIAL_ENTRY_NB_REFERENCES);
			this.memoryElements.put(worldObject.getId(), memoryElement);
			return memoryElement.getWorldObject();
		}
		
		if (this.getShortTermMemory().size() == this.maxSize * Memory.DIVIDE_PERCENT) {
			boolean forgotten = this.forgetElementFromShortTermMemory();
			if (!forgotten) {
				throw new FullMemoryException("Memory is full.");
			}
		}
		
		memoryElement = new MemoryElement(worldObject, forgettable, "short", Memory.INITIAL_ENTRY_NB_REFERENCES);
		
		this.memoryElements.put(worldObject.getId(), memoryElement);
		
		return memoryElement.getWorldObject();
	}
	
	/**
	 * Remembers a world object that can be forgettable.
	 * 
	 * @param worldObject
	 *            the world object
	 * @return the world object
	 */
	public WorldObject remember(WorldObject worldObject) {
		return this.remember(worldObject, true);
	}
	
	/**
	 * Gets the memory element about the owner of this memory.
	 * 
	 * @return the memory element about the owner
	 */
	public Agent getKnowledgeAboutOwner() {
		if (this.ownerAgent == null) {
			return null;
		}
		
		return (Agent) this.getKnowledgeAbout(this.ownerAgent);
	}
	
	/**
	 * Gets the knowledge about an object.
	 * 
	 * @param object
	 *            the object
	 * @return the knowledge about this object
	 */
	public WorldObject getKnowledgeAbout(WorldObject object) {
		MemoryElement e = this.memoryElements.get(object.getId());
		if (e != null) {
			return e.getWorldObject();
		}
		
		return null;
	}
	
	/**
	 * Gets the knowledge about an object.
	 * 
	 * @param objectId
	 *            the object's ID
	 * @return the knowledge about this object
	 */
	public WorldObject getKnowledgeAbout(Long objectId) {
		MemoryElement e = this.memoryElements.get(objectId);
		if (e != null) {
			return e.getWorldObject();
		}
		
		return null;
	}
	
	/**
	 * Gets the all the knowledges about objects.
	 * 
	 * @return all the knowledges
	 */
	public List<WorldObject> getKnowledges() {
		List<WorldObject> knowledges = new ArrayList<WorldObject>();
		
		ArrayList<MemoryElement> ame = new ArrayList<MemoryElement>(this.memoryElements.values());
		for (MemoryElement e : ame) {
			knowledges.add(e.getWorldObject());
		}
		
		return knowledges;
	}
	
	/**
	 * Gets the memories about world objects.
	 * 
	 * @return the memories about world objects
	 */
	public final Map<Long, MemoryElement> getMemoryElements() {
		return this.memoryElements;
	}
	
	/**
	 * Sets the memories about world objects.
	 * 
	 * @param memoryElements
	 *            the new memories about world objects
	 */
	public final void setMemoryElements(Map<Long, MemoryElement> memoryElements) {
		this.memoryElements = memoryElements;
	}
	
	/**
	 * gets all the elements from the short-term memory
	 * 
	 * @return
	 */
	public Map<Long, MemoryElement> getShortTermMemory() {
		Map<Long, MemoryElement> shortTermMemory = new ConcurrentHashMap<Long, MemoryElement>();
		ArrayList<MemoryElement> ame = new ArrayList<MemoryElement>(this.memoryElements.values());
		for (MemoryElement element : ame) {
			if (element.getPlace().equals("short")) {
				shortTermMemory.put(element.getWorldObject().getId(), element);
			}
		}
		return shortTermMemory;
	}
	
	/**
	 * gets all the elements from the long-term memory
	 * 
	 * @return
	 */
	public Map<Long, MemoryElement> getLongTermMemory() {
		Map<Long, MemoryElement> longTermMemory = new ConcurrentHashMap<Long, MemoryElement>();
		ArrayList<MemoryElement> ame = new ArrayList<MemoryElement>(this.memoryElements.values());
		for (MemoryElement element : ame) {
			if (element.getPlace().equals("long")) {
				longTermMemory.put(element.getWorldObject().getId(), element);
			}
		}
		return longTermMemory;
	}
	
	/**
	 * arranges the memory in short-term and long-term memory
	 */
	public void arrangeMemory() {
		int nbElementsLongTermMemory = 0, nbElementsShortTermMemory = 0;
		int minNbReferencesLongTerm = 0;
		long idMin = -1;
		
		while (this.memoryElements.size() > this.maxSize) {
			this.forgetElementFromShortTermMemory();
		}
		ArrayList<MemoryElement> ame = new ArrayList<MemoryElement>(this.memoryElements.values());
		for (MemoryElement element : ame) {
			if (element.getNbReferences() <= 0) {
				this.forget(element);
				continue;
			}
			// if the number of references is bigger than the LIMIT_NB_REFERENCES and if it is an
			// important object we put it in the long-term memory
			if (element.getNbReferences() >= Memory.LIMIT_NB_REFERENCES
					&& !this.isUnimportantObject(element.getWorldObject().getModelName())) {
				// if the long-term memory is not full we put the object in the long-term memory
				if (nbElementsLongTermMemory <= (1 - Memory.DIVIDE_PERCENT) * this.maxSize) {
					element.setPlace("long");
					
					if ((minNbReferencesLongTerm > element.getNbReferences())) {
						minNbReferencesLongTerm = element.getNbReferences();
						idMin = element.getWorldObject().getId();
					}
					
					nbElementsLongTermMemory++;
					// if the lowest number of references from the long-term memory is smaller than
					// the object's number of references, we put the element from the long-term in
					// the short-term memory
				} else if (minNbReferencesLongTerm < element.getNbReferences()) {
					this.memoryElements.get(idMin).setPlace("short");
					element.setPlace("long");
				}
				// if there is enough space in the short-term memory, we put the object there
			} else if (nbElementsShortTermMemory <= Memory.DIVIDE_PERCENT * this.maxSize) {
				element.setPlace("short");
				nbElementsShortTermMemory++;
			}
		}
	}
	
	/**
	 * check if an object is important or not for the agent
	 * 
	 * @param modelName
	 *            the name of the object's model
	 * @return true if the object is unimportant
	 */
	public boolean isUnimportantObject(String modelName) {
		for (String miscModel : Memory.MISC_MODELS) {
			if (miscModel.equals(modelName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Memory (maxSize=");
		builder.append(this.maxSize);
		builder.append("):");
		for (Long id : this.memoryElements.keySet()) {
			builder.append("\n\t");
			builder.append(this.memoryElements.get(id).toString());
		}
		return builder.toString();
	}
	
	/**
	 * Gets the message queue.
	 * 
	 * @return the message queue
	 */
	public Queue<Message> getMessageInbox() {
		// synchronized (this.messageInbox) {
		return this.messageInbox;
		// }
	}
	
	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            the new agent
	 */
	public void setAgent(Agent agent) {
		this.ownerAgent = agent;
	}
	
	/**
	 * Creates a clone of the memory. Only the Memory Elements are copied.
	 * 
	 * @return the memory
	 */
	public Memory deepDataClone() {
		Memory memory = new Memory(null, this.maxSize);
		memory.setMemoryElements(new ConcurrentHashMap<Long, MemoryElement>());
		ArrayList<MemoryElement> ame = new ArrayList<MemoryElement>(this.getMemoryElements().values());
		for (MemoryElement element : ame) {
			MemoryElement memoryElement = element.deepDataClone();
			memory.getMemoryElements().put(memoryElement.getWorldObject().getId(), memoryElement);
		}
		return memory;
	}
	
	/**
	 * Gets the past plans that have succeeded.
	 * 
	 * @return the past plans that have succeeded
	 */
	public Map<MMGoal, Plan> getPastPlans() {
		return this.pastPlans;
	}
	
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
	
	public List<Goal> getGoals() {
		return this.goals;
	}
	
	/**
	 * Sets the stack of goals
	 * 
	 * @param goalStack
	 */
	public void setGoalStack(MMGoalStack goalStack) {
		this.goalStack = goalStack;
	}
	
	/**
	 * Gets the stack of goals
	 * 
	 * @return
	 */
	public MMGoalStack getGoalStack() {
		return this.goalStack;
	}
	
	/**
	 * Sets the list of perceived objects
	 * 
	 * @param perceived_objects
	 */
	public void setPerceivedObjects(ArrayList<Couple<WorldObject, Boolean>> perceived_objects) {
		this.perceivedObjects = perceived_objects;
	}
	
	/**
	 * Gets the list of perceived objects
	 * 
	 * @return
	 */
	public ArrayList<Couple<WorldObject, Boolean>> getPerceivedObjects() {
		return this.perceivedObjects;
	}
	
	/**
	 * Adds a new object in the list of perceived objects
	 * 
	 * @param object
	 *            the object to add in the list
	 * @param new_object
	 *            if it's a new perceived object
	 */
	public void addPerceivedObject(WorldObject object, boolean new_object) {
		if (this.perceivedObjects == null) {
			this.perceivedObjects = new ArrayList<Couple<WorldObject, Boolean>>();
		}
		this.perceivedObjects.add(new Couple<WorldObject, Boolean>(object, new_object));
	}
	
	/**
	 * Checks the virtual memory to see if a service property is true using the
	 * treatment_precondition of the service property
	 * 
	 * @param property
	 *            the service property to check
	 * @return true if the service property is true according to the virtual memory
	 */
	public boolean checkVirtualMemory(ServiceProperty property) {
		String treatment = property.getTreatment_precond();
		
		if (treatment.equals("")) {
			return false;
		}
		
		String[] vars = new String[3];
		vars = treatment.split("__");
		
		String paramValue = property.getParameter(vars[2]).getParamValue();
		String paramType = property.getParameter(vars[2]).getParamType();
		
		try {
			if (vars[1].equals("=")) {
				if (!paramType.equals("3DPoint")) {
					return paramValue == String.valueOf(this.getKnowledgeAboutOwner().getPropertiesContainer()
							.getProperty(vars[0]).getValue());
				} else {
					String[] coordonates = paramValue.split("_");
					Oriented2DPosition point = new Oriented2DPosition(Float.parseFloat(coordonates[0]),
							Float.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2]));
					return this.getKnowledgeAboutOwner().getPosition().egal(point);
				}
			} else if (vars[1].equals(">")) {
				if (paramType.equals("int")) {
					return this.getKnowledgeAboutOwner().getPropertiesContainer().getInt(vars[0]) >= Integer
							.parseInt(paramValue);
				} else if (paramType.equals("double")) {
					return this.getKnowledgeAboutOwner().getPropertiesContainer().getDouble(vars[0]) >= Double
							.parseDouble(paramValue);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * Updates the virtual memory by applying the effects of a certain service. It takes the
	 * positive and negative effects and applies them to the virtual memory
	 * 
	 * @param service
	 *            the service
	 * @return a list of TableClass with the changes is the virtual memory
	 */
	public Set<TableClass> updateVirtualMemory(Service service) {
		
		ArrayList<ServiceProperty> servicePropertyList = new ArrayList<ServiceProperty>();
		
		for (ServiceProperty serviceProperty : service.getServiceEffectsPlus()) {
			servicePropertyList.add(serviceProperty.deepDataClone());
		}
		
		for (ServiceProperty serviceProperty : service.getServiceEffectsMinus()) {
			servicePropertyList.add(serviceProperty.deepDataClone());
		}
		
		String treatment;
		String type, paramValue;
		
		Set<TableClass> updateList = new HashSet<TableClass>();
		
		PropertiesContainer ownerProperties = this.getKnowledgeAboutOwner().getPropertiesContainer();
		
		for (ServiceProperty serviceProperty : servicePropertyList) {
			treatment = serviceProperty.getTreatment_effect();
			String[] vars = new String[3];
			vars = treatment.split("__");
			type = serviceProperty.getParameter(vars[2]).getParamType();
			paramValue = serviceProperty.getParameter(vars[2]).getParamValue();
			
			try {
				if (!vars[0].equals("position")) {
					updateList.add(new TableClass(vars[0], String.valueOf(ownerProperties.getProperty(vars[0])
							.getValue())));
				} else {
					updateList
							.add(new TableClass(vars[0], this.getKnowledgeAboutOwner().getPosition().toStringForXML()));
				}
				
				if (vars[1].equals("+")) {
					if (type.equals("int")) {
						Integer value = Integer.parseInt(paramValue);
						ownerProperties.setInt(vars[0], ownerProperties.getInt(vars[0]) + value);
					} else if (type.equals("double")) {
						Double value = Double.parseDouble(paramValue);
						ownerProperties.setDouble(vars[0], ownerProperties.getDouble(vars[0]) + value);
					}
				} else if (vars[1].equals("-")) {
					if (type.equals("int")) {
						Integer value = Integer.parseInt(paramValue);
						ownerProperties.setInt(vars[0], ownerProperties.getInt(vars[0]) - value);
					} else if (type.equals("double")) {
						Double value = Double.parseDouble(paramValue);
						ownerProperties.setDouble(vars[0], ownerProperties.getDouble(vars[0]) - value);
					}
				} else if (vars[1].equals("=")) {
					if (type.equals("string")) {
						ownerProperties.setString(vars[0], paramValue);
					} else if (type.equals("3DPoint")) {
						String[] coordonates = paramValue.split("_");
						this.getKnowledgeAboutOwner().setPosition(
								new Oriented2DPosition(Float.parseFloat(coordonates[0]), Float
										.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2])));
					}
					
				}
			} catch (Exception e) {
				
			}
		}
		return updateList;
	}
}