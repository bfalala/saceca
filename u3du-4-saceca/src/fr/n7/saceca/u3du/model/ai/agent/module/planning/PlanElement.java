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

import java.util.Map;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.service.ExecutionStatus;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;

/**
 * A class to represent an element of a plan.
 * 
 * @author Sylvain Cambon
 */
public class PlanElement {
	
	/** The service. */
	private Service service;
	
	/** The provider. */
	private WorldObject provider;
	
	/** The parameters. */
	private Map<String, Object> parameters;
	
	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	public final Service getService() {
		return this.service;
	}
	
	/**
	 * Sets the service.
	 * 
	 * @param service
	 *            the new service
	 */
	public final void setService(Service service) {
		this.service = service;
	}
	
	/**
	 * Gets the provider.
	 * 
	 * @return the provider
	 */
	public final WorldObject getProvider() {
		return this.provider;
	}
	
	/**
	 * Sets the provider.
	 * 
	 * @param provider
	 *            the new provider
	 */
	public final void setProvider(WorldObject provider) {
		this.provider = provider;
	}
	
	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public final Map<String, Object> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Sets the parameters.
	 * 
	 * @param parameters
	 *            the new parameters
	 */
	public final void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Instantiates a new plan element.
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
		this.parameters = parameters;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		String providerString = this.provider == null ? null : this.provider.toShortString();
		return "PlanElement [service=" + this.service.getName() + ", provider=" + providerString + ", parameters="
				+ (this.parameters == null ? "{}" : "{...}") + "]";
	}
	
	/**
	 * Executes the elements, i.e. its service.
	 * 
	 * @param consumer
	 *            the consumer
	 * @param mode
	 *            the mode
	 * @return the execution status of this element.
	 */
	public ExecutionStatus execute(WorldObject consumer, ExecutionMode mode) {
		return this.service.execute(this.provider, consumer, this.parameters, mode);
	}
	
}
