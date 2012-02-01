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
package integrationTest.ai.perception;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.DefaultPerceptionModule;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.behavior.DefaultBehavior;

public class PerceptionOnlyBehavior extends DefaultBehavior {
	
	@Override
	public void behave() {
		Agent agent = (Agent) this.object;
		agent.getPerceptionModule().perceive();
	}
	
	@Override
	public String getStorageLabel() {
		return PerceptionOnlyBehavior.class.getCanonicalName();
	}
	
	/**
	 * Initializes the perception module.
	 */
	@Override
	public void init(WorldObject object) {
		super.init(object);
		Agent agent = (Agent) object;
		final PerceptionModule perceptionModule = new DefaultPerceptionModule(agent);
		agent.setPerceptionModule(perceptionModule);
	}
	
}
