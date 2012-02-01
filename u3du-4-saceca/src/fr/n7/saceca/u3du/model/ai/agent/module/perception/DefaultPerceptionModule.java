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
package fr.n7.saceca.u3du.model.ai.agent.module.perception;

import java.util.Collection;
import java.util.List;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.World;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;

/**
 * The Class DefaultPerceptionModule.
 * 
 * @author Jérôme Dalbert
 */
public class DefaultPerceptionModule implements PerceptionModule {
	
	/** The agent. */
	private Agent agent;
	
	/**
	 * Instantiates a new default perception module.
	 * 
	 * @param agent
	 *            the agent
	 */
	public DefaultPerceptionModule(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Perceive.
	 */
	@Override
	public void perceive() {
		this.doInternalPerception();
		this.doEyesightPerception();
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return DefaultPerceptionModule.class.getCanonicalName();
	}
	
	/**
	 * Do internal perception.
	 */
	private void doInternalPerception() {
		Agent knowledgeAboutOwner = this.agent.getMemory().getKnowledgeAboutOwner();
		PropertiesContainer agentKnownProperties = knowledgeAboutOwner.getPropertiesContainer();
		Collection<WorldObject> knownBelongings = knowledgeAboutOwner.getBelongings();
		
		// The agent perceives his position
		knowledgeAboutOwner.setPosition(this.agent.getPosition().clone());
		
		// The agent perceives his gauges values
		for (Gauge gauge : this.agent.getGauges()) {
			agentKnownProperties.addProperty(gauge.clone());
		}
		
		// The agent perceives his added belongings
		for (WorldObject belonging : this.agent.getBelongings()) {
			if (!knownBelongings.contains(belonging)) {
				knownBelongings.add(belonging);
			}
		}
		
		// The agent perceives his removed belongings
		for (WorldObject knownBelonging : knownBelongings) {
			if (!this.agent.getBelongings().contains(knownBelonging)) {
				knownBelongings.remove(knownBelonging);
			}
		}
	}
	
	/**
	 * Do eyesight perception.
	 */
	private void doEyesightPerception() {
		Model m = Model.getInstance();
		World world = m.getAI().getWorld();
		
		// We get the objects in the vision field
		List<Long> objectsInVisionField = m.getGraphics().getObjectsInVisionField(this.agent.getId());
		// We also get the objects nearby, even if the agent doesn't see them
		Collection<WorldObject> nearbyObjects = world.getWorldObjectsAround(this.agent,
				PerceptionModule.NEARBY_OBJECTS_RANGE);
		
		// We perceive the objects information
		for (Long object : objectsInVisionField) {
			this.perceiveInformation(world.getWorldObjects().get(object));
		}
		for (WorldObject worldObject : nearbyObjects) {
			this.perceiveInformation(worldObject);
		}
	}
	
	/**
	 * Perceives the visible information about a world object
	 * 
	 * @param object
	 *            the object
	 */
	private void perceiveInformation(WorldObject object) {
		// Get the memory about "object", and initialize it if it doesn't exist in memory
		WorldObject knowledgeAboutObject = this.agent.getMemory().getKnowledgeAbout(object);
		if (knowledgeAboutObject == null) {
			knowledgeAboutObject = this.agent.getMemory().remember(object.getEmptyClone());
		}
		
		// Perceive position
		knowledgeAboutObject.setPosition(object.getPosition().clone());
		
		// Perceive public properties
		PropertiesContainer knownProperties = knowledgeAboutObject.getPropertiesContainer();
		
		for (Property<?> prop : object.getPropertiesContainer().getProperties()) {
			PropertyModel<?> model = prop.getModel();
			Visibility visibility = model.getVisibility();
			if (visibility.equals(Visibility.PUBLIC)) {
				knownProperties.addProperty(prop.clone());
			}
		}
		
		// Perceive services
		knowledgeAboutObject.setServices(object.getServices());
	}
	
}
