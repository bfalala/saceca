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
package fr.n7.saceca.u3du.model.ai.agent.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.Plan;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;

/**
 * The memory of an agent.<br/>
 * 
 * It remembers memory elements (a memory element is basically a kind of clone of a world object)
 * that are forgettable. When the memory is full, the oldest added memory elements are forgotten.<br/>
 * 
 * The Memory also contains the message inbox, the goals, and the past plans of the agent.
 * 
 * @author Jérôme Dalbert
 */
@XStreamAlias("memory")
public class Memory {
	
	/**
	 * The memories about world objects. It is a map so as to have immediate access to a memory if
	 * we know the id of the remembered worldObject.
	 */
	@XStreamAlias("memory-elements")
	private Map<Long, MemoryElement> memoryElements;
	
	/** The memories sorted by insertion order, from the older to the newer. */
	private List<MemoryElement> insertionOrderedElements;
	
	/** The message inbox. */
	private Queue<Message> messageInbox;
	
	/**
	 * The goals, sorted by decreasing order of priority.
	 */
	private List<Goal> goals;
	
	/**
	 * The past plans that have succeeded.
	 */
	private Map<Goal, Plan> pastPlans;
	
	/** The max size for memory elements. */
	@XStreamAlias("maxSize")
	@XStreamAsAttribute
	private int maxSize;
	
	/** The agent who owns this memory. */
	@XStreamOmitField
	private Agent ownerAgent;
	
	/** The surrounding objects. */
	// TODO : a enlever, inutile
	public Collection<WorldObject> surroundingObjects;
	
	/** The Constant INITIAL_KNOWN_PROPERTIES. */
	public static final String[] INITIAL_KNOWN_PROPERTIES = { Internal.Agent.NAME };
	
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
		this.goals = new CopyOnWriteArrayList<Goal>();
		this.messageInbox = new ConcurrentLinkedQueue<Message>();
		this.pastPlans = new HashMap<Goal, Plan>();
		this.surroundingObjects = new ArrayList<WorldObject>();
		this.insertionOrderedElements = new ArrayList<MemoryElement>();
	}
	
	/**
	 * Helps to rebuild the object.
	 * 
	 * @return the memory
	 */
	public Memory readResolve() {
		if (this.memoryElements == null) {
			this.memoryElements = new HashMap<Long, MemoryElement>();
		}
		if (this.goals == null) {
			this.goals = new ArrayList<Goal>();
		}
		if (this.messageInbox == null) {
			this.messageInbox = new ConcurrentLinkedQueue<Message>();
		}
		if (this.pastPlans == null) {
			this.pastPlans = new HashMap<Goal, Plan>();
		}
		if (this.surroundingObjects == null) {
			this.surroundingObjects = new ArrayList<WorldObject>();
		}
		if (this.insertionOrderedElements == null) {
			this.insertionOrderedElements = new ArrayList<MemoryElement>();
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
	 * Forget first possible element.
	 * 
	 * @return true, if successful
	 */
	public boolean forgetFirstPossibleElement() {
		for (MemoryElement memElem : this.insertionOrderedElements) {
			if (memElem.isForgettable()) {
				this.forget(memElem);
				return true;
			}
		}
		
		return false;
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
		if (this.memoryElements.size() == this.maxSize) {
			boolean forgotten = this.forgetFirstPossibleElement();
			if (!forgotten) {
				throw new FullMemoryException("Memory is full.");
			}
		}
		
		MemoryElement memoryElement = new MemoryElement(worldObject, forgettable);
		this.memoryElements.put(worldObject.getId(), memoryElement);
		this.insertionOrderedElements.add(memoryElement);
		
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
	 * Gets the all the knowledges about objects.
	 * 
	 * @return all the knowledges
	 */
	public List<WorldObject> getKnowledges() {
		List<WorldObject> knowledges = new ArrayList<WorldObject>();
		
		for (MemoryElement e : this.memoryElements.values()) {
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
		return this.messageInbox;
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
	 * Gets the goals.
	 * 
	 * @return the goals
	 */
	public List<Goal> getGoals() {
		return this.goals;
	}
	
	/**
	 * Creates a clone of the memory. Only the Memory Elements are copied.
	 * 
	 * @return the memory
	 */
	public Memory deepDataClone() {
		Memory memory = new Memory(null, this.maxSize);
		for (MemoryElement element : this.getMemoryElements().values()) {
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
	public Map<Goal, Plan> getPastPlans() {
		return this.pastPlans;
	}
	
}
