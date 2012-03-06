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
package fr.n7.saceca.u3du.model.ai.service;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The PropertyLink class - represents a link between two service properties. Example: premise =
 * biggerThan conclussion = become_plus - helps in the planning process
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
@XStreamAlias("link")
public class PropertyLink {
	/** The premise of the link */
	@XStreamAlias("premise")
	private ServiceProperty premise;
	
	/** The consclusion of the link */
	@XStreamAlias("conclusion")
	private ServiceProperty conclusion;
	
	/**
	 * Default constructor
	 */
	public PropertyLink() {
		this.premise = new ServiceProperty();
		this.conclusion = new ServiceProperty();
	}
	
	/**
	 * Constructor
	 * 
	 * @param premise
	 *            the premise
	 * @param conclussion
	 *            the conclussion
	 */
	public PropertyLink(ServiceProperty premise, ServiceProperty conclussion) {
		this.premise = premise.deepDataClone();
		this.conclusion = conclussion.deepDataClone();
	}
	
	/**
	 * Gets the premise
	 * 
	 * @return
	 */
	public ServiceProperty getPremise() {
		return this.premise;
	}
	
	/**
	 * Sets the premise
	 * 
	 * @param premise
	 *            the premise
	 */
	public void setPremise(ServiceProperty premise) {
		this.premise = premise;
	}
	
	/**
	 * Gets the conclusion
	 * 
	 * @return
	 */
	public ServiceProperty getConclusion() {
		return this.conclusion;
	}
	
	/**
	 * Sets the conclusion
	 * 
	 * @param conclusion
	 *            the conclusion
	 */
	public void setConclusion(ServiceProperty conclusion) {
		this.conclusion = conclusion;
	}
	
	/**
	 * Clones the link
	 * 
	 * @return the cloned link
	 */
	public PropertyLink deepDataClone() {
		return new PropertyLink(this.premise.deepDataClone(), this.conclusion.deepDataClone());
	}
}