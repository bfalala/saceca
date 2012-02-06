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
package fr.n7.saceca.u3du.model.ai.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.CommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.emotion.EmotionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.ReasoningModule;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyFilter;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.graphics.animation.DieAnimation;
import fr.n7.saceca.u3du.model.graphics.animation.ReviveAnimation;

/**
 * The Class Agent. It represents the classic AI agent.<br/>
 * Its main attributes (on top of the WorldObject attributes) are a memory and intelligence modules.
 * 
 * @author Jérôme Dalbert
 */
@XStreamAlias("agent")
public class Agent extends WorldObject {
	
	/** The Constant DEFAULT_MEMORY_SIZE. */
	public static final int DEFAULT_MEMORY_SIZE = 100;
	
	/** The memory. */
	private Memory memory;
	
	/** The perception module. */
	private PerceptionModule perceptionModule;
	
	/** The communication module. */
	private CommunicationModule communicationModule;
	
	/** The reasoning module. */
	private ReasoningModule reasoningModule;
	
	private ReasoningModule mmReasoningModule;
	
	/** The planning module. */
	private PlanningModule planningModule;
	
	/** The emotion module. */
	private EmotionModule emotionModule;
	
	/**
	 * Instantiates a new agent.
	 * 
	 * @param modelName
	 *            the model name
	 * @param id
	 *            the id
	 */
	public Agent(String modelName, long id) {
		this(modelName, id, true);
	}
	
	/**
	 * A private constructor aimed at not necessarily creating the complete memory of the agent.
	 * This is especially needed on memory agents.
	 * 
	 * @param modelName
	 *            the model name
	 * @param id
	 *            the id
	 * @param initMemory
	 *            Whether the initial memory should be created and initialized.
	 */
	private Agent(String modelName, long id, boolean initMemory) {
		super(modelName, id);
		
		if (initMemory) {
			this.memory = new Memory(this, DEFAULT_MEMORY_SIZE);
		} else {
			this.memory = new Memory(null, 0);
		}
	}
	
	/**
	 * Gets the communication module.
	 * 
	 * @return the communication module
	 */
	public CommunicationModule getCommunicationModule() {
		return this.communicationModule;
	}
	
	/**
	 * Gets the emotion module.
	 * 
	 * @return the emotion module
	 */
	public EmotionModule getEmotionModule() {
		return this.emotionModule;
	}
	
	/**
	 * Gets the memory.
	 * 
	 * @return the memory
	 */
	public Memory getMemory() {
		return this.memory;
	}
	
	/**
	 * Gets the perception module.
	 * 
	 * @return the perception module
	 */
	public PerceptionModule getPerceptionModule() {
		return this.perceptionModule;
	}
	
	/**
	 * Gets the planning module.
	 * 
	 * @return the planning module
	 */
	public PlanningModule getPlanningModule() {
		return this.planningModule;
	}
	
	/**
	 * Gets the reasoning module.
	 * 
	 * @return the reasoning module
	 */
	public ReasoningModule getReasoningModule() {
		return this.reasoningModule;
	}
	
	/**
	 * Sets the communication module.
	 * 
	 * @param communicationModule
	 *            the new communication module
	 */
	public void setCommunicationModule(CommunicationModule communicationModule) {
		this.communicationModule = communicationModule;
	}
	
	/**
	 * Sets the emotion module.
	 * 
	 * @param emotionModule
	 *            the new emotion module
	 */
	public void setEmotionModule(EmotionModule emotionModule) {
		this.emotionModule = emotionModule;
	}
	
	/**
	 * Sets the memory.
	 * 
	 * @param memory
	 *            the new memory
	 */
	public void setMemory(Memory memory) {
		this.memory = memory;
	}
	
	/**
	 * Sets the perception module.
	 * 
	 * @param perceptionModule
	 *            the new perception module
	 */
	public void setPerceptionModule(PerceptionModule perceptionModule) {
		this.perceptionModule = perceptionModule;
	}
	
	/**
	 * Sets the planning module.
	 * 
	 * @param planningModule
	 *            the new planning module
	 */
	public void setPlanningModule(PlanningModule planningModule) {
		this.planningModule = planningModule;
	}
	
	/**
	 * Sets the reasoning module.
	 * 
	 * @param reasoningModule
	 *            the new reasoning module
	 */
	public void setReasoningModule(ReasoningModule reasoningModule) {
		this.reasoningModule = reasoningModule;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.toShortString());
		builder.append(" | ");
		builder.append(this.getPosition());
		builder.append("\nBehavior:\n\t");
		if (this.behavior != null) {
			builder.append(this.behavior.getClass().getCanonicalName());
			builder.append("\nModules:\n\t");
		}
		if (this.perceptionModule != null) {
			builder.append(this.perceptionModule.getClass().getCanonicalName());
			builder.append("\n\t");
		}
		if (this.reasoningModule != null) {
			builder.append(this.reasoningModule.getClass().getCanonicalName());
			builder.append("\n\t");
		}
		if (this.planningModule != null) {
			builder.append(this.planningModule.getClass().getCanonicalName());
			builder.append("\n\t");
		}
		if (this.communicationModule != null) {
			builder.append(this.communicationModule.getClass().getCanonicalName());
			builder.append("\n\t");
		}
		if (this.emotionModule != null) {
			builder.append(this.emotionModule.getClass().getCanonicalName());
		}
		builder.append("\nCategories:");
		for (Category category : this.getCategories()) {
			builder.append("\n\t");
			builder.append(category.getName());
		}
		builder.append("\nServices:");
		for (Service service : this.getServices()) {
			builder.append("\n\t");
			builder.append(service.getName());
		}
		builder.append('\n');
		builder.append(this.getPropertiesContainer());
		builder.append("\nBelongings:");
		for (WorldObject wo : this.getBelongings()) {
			builder.append("\n\t");
			builder.append(wo.toShortString());
		}
		builder.append("\n");
		builder.append(this.memory.toString());
		return builder.toString();
	}
	
	/**
	 * Sends a message to the agent.
	 * 
	 * @param m
	 *            the m
	 */
	public void sendMessage(Message m) {
		this.getCommunicationModule().addMessage(m);
	}
	
	/**
	 * Gets the gauges.
	 * 
	 * @return the gauges
	 */
	public List<Gauge> getGauges() {
		List<Gauge> gauges = new ArrayList<Gauge>();
		
		Collection<Property<?>> gaugeProps = this.getPropertiesContainer().getProperties(new PropertyFilter() {
			@Override
			public boolean accept(Property<?> property) {
				return property.getModel().getName().startsWith(Gauge.PREFIX);
			}
		});
		
		for (Property<?> gaugeProp : gaugeProps) {
			gauges.add((Gauge) gaugeProp);
		}
		
		return gauges;
	}
	
	public List<Emotion> getEmotions() {
		List<Emotion> emotions = new ArrayList<Emotion>();
		
		Collection<Property<?>> emotionProps = this.getPropertiesContainer().getProperties(new PropertyFilter() {
			@Override
			public boolean accept(Property<?> property) {
				return property.getModel().getName().startsWith(Emotion.PREFIX);
			}
		});
		
		for (Property<?> emotionProp : emotionProps) {
			Emotion emotion = (Emotion) emotionProp;
			if (emotion.isPrimary()) {
				emotions.add(emotion);
			}
		}
		
		return emotions;
	}
	
	public List<Emotion> getSecondaryEmotions() {
		List<Emotion> emotions = new ArrayList<Emotion>();
		
		Collection<Property<?>> emotionProps = this.getPropertiesContainer().getProperties(new PropertyFilter() {
			@Override
			public boolean accept(Property<?> property) {
				return property.getModel().getName().startsWith(Emotion.PREFIX);
			}
		});
		
		for (Property<?> emotionProp : emotionProps) {
			Emotion emotion = (Emotion) emotionProp;
			if (emotion.isSecondary()) {
				emotions.add(emotion);
			}
		}
		
		return emotions;
	}
	
	@Override
	public Agent getEmptyClone() {
		return new Agent(this.getModelName(), this.getId(), false);
	}
	
	/**
	 * Creates a deep data clone. This clone has the same categories and services as well as clones
	 * of its properties, belongings and memory.
	 * 
	 * @return the agent
	 */
	@Override
	public Agent deepDataClone() {
		WorldObject materials = super.deepDataClone();
		final long id = this.getId();
		Agent clone = new Agent(this.getModelName(), id);
		clone.setBehavior(null);
		clone.setBelongings(materials.getBelongings());
		clone.setCategories(materials.getCategories());
		clone.setCommunicationModule(null);
		clone.setEmotionModule(null);
		clone.setMemory(this.memory.deepDataClone());
		clone.getMemory().setAgent(clone);
		clone.setPause(true);
		clone.setPerceptionModule(null);
		clone.setPlanningModule(null);
		clone.setPosition(materials.getPosition());
		clone.setPropertiesContainer(materials.getPropertiesContainer());
		clone.setReasoningModule(null);
		clone.setServices(materials.getServices());
		return clone;
	}
	
	/**
	 * A short string representation of the object, of the form
	 * "Agent <ModelName> (id=<id>) \"<name>\"".
	 * 
	 * @return the string
	 */
	@Override
	public String toShortString() {
		String name;
		try {
			name = this.getPropertiesContainer().getString(Internal.Agent.NAME);
		} catch (UnknownPropertyException e) {
			return "Agent " + this.getModelName() + " (id=" + this.getId() + ") \"%Unknow name%\"";
		}
		return "Agent " + this.getModelName() + " (id=" + this.getId() + ") \"" + name + "\"";
	}
	
	@Override
	public void kill() {
		super.kill();
		if (this.animation != null) {
			this.animation.pause();
		}
		Model.getInstance().getGraphics().sendAnimation(new DieAnimation(this.id));
	}
	
	@Override
	public void revive() {
		super.revive();
		Model.getInstance().getGraphics().sendAnimation(new ReviveAnimation(this.id));
		if (this.animation != null) {
			this.animation.resume();
		}
	}
	
	public ArrayList<Service> getPossibleServices() {
		ArrayList<Service> serviceList = new ArrayList<Service>();
		
		boolean serviceAdded = false;
		
		for (WorldObject object : this.getMemory().getKnowledges()) {
			for (Service service : object.getServices()) {
				service.setProviderId(object.getId());
				if (service.getName().equals("walkTo")) {
					if (!serviceAdded) {
						serviceList.add(service.deepDataClone());
						serviceAdded = true;
					}
				} else {
					serviceList.add(service.deepDataClone());
				}
			}
		}
		return serviceList;
	}
	
	public ArrayList<ServiceProperty> getPossibleEffectsPlus(ArrayList<WorldObject> objects) {
		ArrayList<ServiceProperty> effectPlusList = new ArrayList<ServiceProperty>();
		
		boolean serviceAdded = false;
		
		for (WorldObject object : objects) {
			for (Service service : object.getServices()) {
				for (ServiceProperty property : service.getServiceEffectsPlus()) {
					if (service.getName().equals("walkTo")) {
						if (!serviceAdded) {
							effectPlusList.add(property.deepDataClone());
							serviceAdded = true;
						}
					} else {
						effectPlusList.add(property.deepDataClone());
					}
				}
			}
		}
		return effectPlusList;
	}
	
	public ArrayList<ServiceProperty> getPossibleEffects(List<WorldObject> objects) {
		ArrayList<ServiceProperty> effectList = new ArrayList<ServiceProperty>();
		
		boolean serviceAdded = false;
		
		for (WorldObject object : objects) {
			for (Service service : object.getServices()) {
				
				for (ServiceProperty property : service.getServiceEffectsPlus()) {
					if (service.getName().equals("walkTo")) {
						if (!serviceAdded) {
							effectList.add(property.deepDataClone());
							serviceAdded = true;
						}
					} else {
						effectList.add(property.deepDataClone());
					}
				}
				for (ServiceProperty property2 : service.getServiceEffectsMinus()) {
					if (service.getName().equals("walkTo")) {
						if (!serviceAdded) {
							effectList.add(property2.deepDataClone());
							serviceAdded = true;
						}
					} else {
						effectList.add(property2.deepDataClone());
					}
				}
			}
		}
		
		return effectList;
	}
	
	public void setMmReasoningModule(ReasoningModule mmReasoningModule) {
		this.mmReasoningModule = mmReasoningModule;
	}
	
	public ReasoningModule getMmReasoningModule() {
		return this.mmReasoningModule;
	}
}