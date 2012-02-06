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
package fr.n7.saceca.u3du.model.ai.agent.module.planning;

import java.util.HashMap;
import java.util.Map;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * The PlanElement class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class PlanElement {
	/** The service. */
	private Service service;
	
	/** The provider. */
	private WorldObject provider;
	
	/** The provider's id */
	private long providerId;
	
	/** The parameters. */
	private Map<String, Object> parameters;
	
	/**
	 * Creates a new plan element
	 * 
	 * @param service
	 *            the service
	 * @param provider
	 *            the provider
	 * @param parameters
	 *            the parameters
	 */
	public PlanElement(Service service, WorldObject provider, Map<String, Object> parameters) {
		super();
		this.service = service;
		this.provider = provider;
		this.providerId = provider.getId();
		
		this.parameters = parameters;
	}
	
	/**
	 * Creates a new plan element
	 * 
	 * @param service
	 *            the service
	 * @param providerId
	 *            the provider's id
	 */
	public PlanElement(Service service, long providerId) {
		super();
		this.service = service;
		this.providerId = providerId;
	}
	
	/**
	 * Gets the service
	 * 
	 * @return
	 */
	public Service getService() {
		return this.service;
	}
	
	/**
	 * Sets the service
	 * 
	 * @param service
	 *            the service
	 */
	public void setService(Service service) {
		this.service = service;
	}
	
	/**
	 * Gets the provider
	 * 
	 * @return
	 */
	public WorldObject getProvider() {
		return this.provider;
	}
	
	/**
	 * Sets the provider
	 * 
	 * @param provider
	 *            the provider
	 */
	public void setProvider(WorldObject provider) {
		this.provider = provider;
	}
	
	/**
	 * Gets the provider's id
	 * 
	 * @return
	 */
	public long getProviderId() {
		return this.providerId;
	}
	
	/**
	 * Sets the provider's id
	 * 
	 * @param providerId
	 *            the provider's id
	 */
	public void setProviderId(long providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * Gets the parameters of the plan element
	 * 
	 * @return
	 */
	public Map<String, Object> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Sets the parameters
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		String providerString = this.provider == null ? null : this.provider.toShortString();
		return "PlanElement [service=" + this.service.getName() + ", provider=" + providerString + ", parameters="
				+ (this.parameters == null ? "{}" : "{...}") + "]";
	}
	
	/**
	 * Executes the plan element
	 * 
	 * @param consumer
	 *            the consumer
	 * @param mode
	 *            the execution mode
	 * @return the execution status
	 */
	public ExecutionStatus execute(WorldObject consumer, ExecutionMode mode) {
		Map<String, Object> parameters = null;
		if (this.service.getName().equals("walkTo")) {
			parameters = new HashMap<String, Object>();
			
			String[] coordonates = this.service.getParamValue("destination").split("_");
			Oriented2DPosition destinationPosition = new Oriented2DPosition(Float.parseFloat(coordonates[0]),
					Float.parseFloat(coordonates[1]), Float.parseFloat(coordonates[2]));
			
			parameters.put("destination", Model.getInstance().getAI().getWorld()
					.getClosestWalkable(destinationPosition));
			
			this.parameters = parameters;
		}
		
		return this.service.execute(this.provider, consumer, this.parameters, mode);
	}
	
}
