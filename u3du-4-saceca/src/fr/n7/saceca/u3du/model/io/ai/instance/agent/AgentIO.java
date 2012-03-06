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
package fr.n7.saceca.u3du.model.io.ai.instance.agent;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.io.ai.instance.object.Oriented2DPositionConverter;
import fr.n7.saceca.u3du.model.io.ai.instance.object.WorldObjectConverter;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;

/**
 * A class to handle agents IO.
 * 
 * @author Sylvain Cambon
 */
public class AgentIO extends XStreamIO<Agent> {
	
	/** Whether XStream was configured to handle agents. */
	private static boolean configured = false;
	
	/** The factory. */
	private EntitiesFactory factory;
	
	/**
	 * Instantiates a new agent io.
	 * 
	 * @param factory
	 *            the factory
	 */
	public AgentIO(EntitiesFactory factory) {
		super();
		this.factory = factory;
	}
	
	@Override
	protected void configureIfNecessary(XStream xStream) {
		if (!configured) {
			xStream.processAnnotations(WorldObject.class);
			xStream.registerConverter(new WorldObjectConverter(this.factory));
			xStream.processAnnotations(Agent.class);
			xStream.registerConverter(new AgentConverter(this.factory));
			xStream.registerConverter(new Oriented2DPositionConverter());
			AgentIO.configured = true;
		}
	}
	
	@Override
	protected boolean isConfigured() {
		return configured;
	}
}