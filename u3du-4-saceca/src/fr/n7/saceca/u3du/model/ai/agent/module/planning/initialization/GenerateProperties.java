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
package fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization;

import java.util.ArrayList;

import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;

/**
 * The GenerateProperties class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class GenerateProperties {
	/** The list of services */
	private ArrayList<Service> serviceList;
	
	/** The list of service properties */
	private ArrayList<ServiceProperty> propertyList;
	
	/** The list of links */
	private ArrayList<PropertyLink> linkList;
	
	/**
	 * Generates the service property list from the service list
	 * 
	 * @param serviceList
	 *            the list of services
	 * @param linkList
	 *            the list of links
	 */
	public GenerateProperties(ArrayList<Service> serviceList, ArrayList<PropertyLink> linkList) {
		this.serviceList = new ArrayList<Service>();
		for (Service service : serviceList) {
			this.serviceList.add(service.deepDataClone());
		}
		
		this.getPropertiesFromServiceList();
		
		this.linkList = new ArrayList<PropertyLink>();
		for (PropertyLink link : linkList) {
			this.linkList.add(link.deepDataClone());
		}
	}
	
	/**
	 * Gets the properties from the service list
	 * 
	 * @return
	 */
	public ArrayList<ServiceProperty> getPropertiesFromServiceList() {
		this.propertyList = new ArrayList<ServiceProperty>();
		
		for (Service service : this.serviceList) {
			for (ServiceProperty property : service.getServicePreconditions()) {
				if (!this.existProperty(property, this.propertyList)) {
					this.propertyList.add(property.deepDataClone());
				}
			}
			
			for (ServiceProperty property : service.getServiceEffectsPlus()) {
				if (!this.existProperty(property, this.propertyList)) {
					this.propertyList.add(property.deepDataClone());
				}
			}
			
			for (ServiceProperty property : service.getServiceEffectsMinus()) {
				if (!this.existProperty(property, this.propertyList)) {
					this.propertyList.add(property);
				}
			}
		}
		return this.propertyList;
	}
	
	/**
	 * Checks if a property has been already added in the list of service properties
	 * 
	 * @param property
	 *            the property to check
	 * @param propertyList
	 *            the list of properties
	 * @return
	 */
	private boolean existProperty(ServiceProperty property, ArrayList<ServiceProperty> propertyList) {
		for (ServiceProperty existing_property : propertyList) {
			if (property.egal(existing_property)) {
				if (existing_property.getTreatment_precond().equals("")) {
					existing_property.setTreatment_precond(property.getTreatment_precond());
				}
				if (existing_property.getTreatment_effect().equals("")) {
					existing_property.setTreatment_effect(property.getTreatment_effect());
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the list of services
	 * 
	 * @return
	 */
	public ArrayList<Service> getServiceList() {
		return this.serviceList;
	}
	
	/**
	 * Sets the list of services
	 * 
	 * @param serviceList
	 *            the service list
	 */
	public void setServiceList(ArrayList<Service> serviceList) {
		this.serviceList = serviceList;
	}
	
	/**
	 * Gets the list of properties
	 * 
	 * @return
	 */
	public ArrayList<ServiceProperty> getPropertyList() {
		return this.propertyList;
	}
	
	/**
	 * Sets the list of properties
	 * 
	 * @param propertyList
	 *            the list of service properties
	 */
	public void setPropertyList(ArrayList<ServiceProperty> propertyList) {
		this.propertyList = propertyList;
	}
	
	/**
	 * Gets the list of links
	 * 
	 * @return
	 */
	public ArrayList<PropertyLink> getLinkList() {
		return this.linkList;
	}
	
	/**
	 * Sets the list of links
	 * 
	 * @param linkList
	 *            the list of links
	 */
	public void setLinkList(ArrayList<PropertyLink> linkList) {
		this.linkList = linkList;
	}
	
}
