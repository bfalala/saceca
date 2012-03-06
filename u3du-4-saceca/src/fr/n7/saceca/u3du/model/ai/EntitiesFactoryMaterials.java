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

import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.CommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.emotion.EmotionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.ReasoningModule;
import fr.n7.saceca.u3du.model.ai.category.CategoryModel;
import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.ai.object.behavior.Behavior;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

/**
 * A class to gather elements required by the entities factory to run.
 * 
 * @author Sylvain Cambon
 */
public class EntitiesFactoryMaterials {
	
	/** The category model repository. */
	private Repository<CategoryModel> categoryModelRepository;
	
	/** The service repository. */
	private Repository<Service> serviceRepository;
	
	/** The world object repository. */
	private Repository<WorldObjectModel> worldObjectModelRepository;
	
	/** The agent repository. */
	private Repository<AgentModel> agentModelRepository;
	
	/** The behavior classes repository. */
	private Repository<StorableClassWrapper<Behavior>> behaviorRepository;
	
	/** The perception classes repository. */
	private Repository<StorableClassWrapper<PerceptionModule>> perceptionRepository;
	
	/** The reasoning classes repository. */
	private Repository<StorableClassWrapper<ReasoningModule>> reasoningRepository;
	
	/** The planning classes repository. */
	private Repository<StorableClassWrapper<PlanningModule>> planningRepository;
	
	/** The communication classes repository. */
	private Repository<StorableClassWrapper<CommunicationModule>> communicationRepository;
	
	/** The emotion classes repository. */
	private Repository<StorableClassWrapper<EmotionModule>> emotionRepository;
	
	/** The action classes repository. */
	private Repository<StorableClassWrapper<Action>> actionRepository;
	
	/**
	 * Instantiates a new entities factory materials.
	 */
	public EntitiesFactoryMaterials() {
		super();
	}
	
	/**
	 * Gets the behavior classes repository.
	 * 
	 * @return the behavior classes repository
	 */
	public final Repository<StorableClassWrapper<Behavior>> getBehaviorRepository() {
		return this.behaviorRepository;
	}
	
	/**
	 * Gets the category model repository.
	 * 
	 * @return the category model repository
	 */
	public final Repository<CategoryModel> getCategoryModelRepository() {
		return this.categoryModelRepository;
	}
	
	/**
	 * Gets the service repository.
	 * 
	 * @return the service repository
	 */
	public final Repository<Service> getServiceRepository() {
		return this.serviceRepository;
	}
	
	/**
	 * Gets the world object model repository.
	 * 
	 * @return the world object model repository
	 */
	public final Repository<WorldObjectModel> getWorldObjectModelRepository() {
		return this.worldObjectModelRepository;
	}
	
	/**
	 * Sets the behavior classes repository.
	 * 
	 * @param behaviorRepository
	 *            the new behavior classes repository
	 */
	public final void setBehaviorRepository(Repository<StorableClassWrapper<Behavior>> behaviorRepository) {
		this.behaviorRepository = behaviorRepository;
	}
	
	/**
	 * Sets the category model repository.
	 * 
	 * @param categoryModelRepository
	 *            the new category model repository
	 */
	public final void setCategoryModelRepository(Repository<CategoryModel> categoryModelRepository) {
		this.categoryModelRepository = categoryModelRepository;
	}
	
	/**
	 * Sets the service repository.
	 * 
	 * @param serviceRepository
	 *            the new service repository
	 */
	public final void setServiceRepository(Repository<Service> serviceRepository) {
		this.serviceRepository = serviceRepository;
	}
	
	/**
	 * Sets the world object model repository.
	 * 
	 * @param worldObjectModelRepository
	 *            the new world object repository
	 */
	public final void setWorldObjectModelRepository(Repository<WorldObjectModel> worldObjectModelRepository) {
		this.worldObjectModelRepository = worldObjectModelRepository;
	}
	
	/**
	 * Gets the agent repository.
	 * 
	 * @return the agent repository
	 */
	public final Repository<AgentModel> getAgentModelRepository() {
		return this.agentModelRepository;
	}
	
	/**
	 * Sets the agent repository.
	 * 
	 * @param agentModelRepository
	 *            the new agent repository
	 */
	public final void setAgentModelRepository(Repository<AgentModel> agentModelRepository) {
		this.agentModelRepository = agentModelRepository;
	}
	
	/**
	 * Gets the perception classes repository.
	 * 
	 * @return the perception classes repository
	 */
	public final Repository<StorableClassWrapper<PerceptionModule>> getPerceptionRepository() {
		return this.perceptionRepository;
	}
	
	/**
	 * Sets the perception classes repository.
	 * 
	 * @param perceptionRepository
	 *            the new perception classes repository
	 */
	public final void setPerceptionRepository(Repository<StorableClassWrapper<PerceptionModule>> perceptionRepository) {
		this.perceptionRepository = perceptionRepository;
	}
	
	/**
	 * Gets the reasoning classes repository.
	 * 
	 * @return the reasoning classes repository
	 */
	public final Repository<StorableClassWrapper<ReasoningModule>> getReasoningRepository() {
		return this.reasoningRepository;
	}
	
	/**
	 * Sets the reasoning classes repository.
	 * 
	 * @param reasoningRepository
	 *            the new reasoning classes repository
	 */
	public final void setReasoningRepository(Repository<StorableClassWrapper<ReasoningModule>> reasoningRepository) {
		this.reasoningRepository = reasoningRepository;
	}
	
	/**
	 * Gets the planning classes repository.
	 * 
	 * @return the planning classes repository
	 */
	public final Repository<StorableClassWrapper<PlanningModule>> getPlanningRepository() {
		return this.planningRepository;
	}
	
	/**
	 * Sets the planning classes repository.
	 * 
	 * @param planningRepository
	 *            the new planning classes repository
	 */
	public final void setPlanningRepository(Repository<StorableClassWrapper<PlanningModule>> planningRepository) {
		this.planningRepository = planningRepository;
	}
	
	/**
	 * Gets the communication classes repository.
	 * 
	 * @return the communication classes repository
	 */
	public final Repository<StorableClassWrapper<CommunicationModule>> getCommunicationRepository() {
		return this.communicationRepository;
	}
	
	/**
	 * Sets the communication classes repository.
	 * 
	 * @param communicationRepository
	 *            the new communication classes repository
	 */
	public final void setCommunicationRepository(
			Repository<StorableClassWrapper<CommunicationModule>> communicationRepository) {
		this.communicationRepository = communicationRepository;
	}
	
	/**
	 * Gets the emotion classes repository.
	 * 
	 * @return the emotion classes repository
	 */
	public final Repository<StorableClassWrapper<EmotionModule>> getEmotionRepository() {
		return this.emotionRepository;
	}
	
	/**
	 * Sets the emotion classes repository.
	 * 
	 * @param emotionRepository
	 *            the new emotion classes repository
	 */
	public final void setEmotionRepository(Repository<StorableClassWrapper<EmotionModule>> emotionRepository) {
		this.emotionRepository = emotionRepository;
	}
	
	/**
	 * Gets the action classes repository.
	 * 
	 * @return the action classes repository
	 */
	public final Repository<StorableClassWrapper<Action>> getActionRepository() {
		return this.actionRepository;
	}
	
	/**
	 * Sets the action classes repository.
	 * 
	 * @param actionRepository
	 *            the new action classes repository
	 */
	public final void setActionRepository(Repository<StorableClassWrapper<Action>> actionRepository) {
		this.actionRepository = actionRepository;
	}
	
}