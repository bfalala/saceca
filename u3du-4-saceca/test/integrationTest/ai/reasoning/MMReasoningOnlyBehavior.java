package integrationTest.ai.reasoning;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMReasoningModule;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.ReasoningModule;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.behavior.DefaultBehavior;

public class MMReasoningOnlyBehavior extends DefaultBehavior {
	
	@Override
	public void behave() {
		Agent agent = (Agent) this.object;
		// System.out.print("\nreason\n");
		agent.getReasoningModule().reason();
	}
	
	@Override
	public String getStorageLabel() {
		return ReasoningOnlyBehavior.class.getCanonicalName();
	}
	
	/**
	 * Initializes the reasoning module.
	 */
	@Override
	public void init(WorldObject object) {
		super.init(object);
		Agent agent = (Agent) object;
		final ReasoningModule reasoningModule = new MMReasoningModule(agent);
		agent.setReasoningModule(reasoningModule);
	}
	
}