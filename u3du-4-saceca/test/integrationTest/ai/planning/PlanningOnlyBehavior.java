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
package integrationTest.ai.planning;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.DefaultPlanningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.behavior.DefaultBehavior;

public class PlanningOnlyBehavior extends DefaultBehavior {
	
	@Override
	public String getStorageLabel() {
		return PlanningOnlyBehavior.class.getCanonicalName();
	}
	
	@Override
	public void behave() {
		Agent agent = (Agent) this.object;
		agent.getCommunicationModule().communicate();
	}
	
	/**
	 * Initializes the planning module.
	 */
	@Override
	public void init(WorldObject object) {
		super.init(object);
		Agent agent = (Agent) object;
		final PlanningModule planningModule = new DefaultPlanningModule(agent);
		agent.setPlanningModule(planningModule);
	}
	
}