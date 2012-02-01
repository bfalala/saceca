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
package fr.n7.saceca.u3du.model.io.ai.instance.object;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.io.ai.instance.agent.AgentConverter;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;

/**
 * A class to handle world object IO.
 * 
 * @author Sylvain Cambon
 */
public class WorldObjectIO extends XStreamIO<WorldObject> {
	
	/** Whether XStream was configured to handle world objects. */
	private static boolean configured = false;
	
	/** The factory. */
	private EntitiesFactory factory;
	
	/**
	 * Instantiates a new world object io.
	 * 
	 * @param factory
	 *            the factory
	 */
	public WorldObjectIO(EntitiesFactory factory) {
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
			WorldObjectIO.configured = true;
		}
	}
	
	@Override
	protected boolean isConfigured() {
		return configured;
	}
	
}
