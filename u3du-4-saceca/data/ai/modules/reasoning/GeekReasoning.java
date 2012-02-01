import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.Goal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.ReasoningModule;

/**
 * The Class GeekReasoning.
 * 
 * @author Sylvain Cambon
 */
public class GeekReasoning implements ReasoningModule {
	
	@Override
	public Goal getGoalToReach() {
		return null;
	}
	
	@Override
	public void reason() {
		System.out.println("I am reasoning about how to improve the kernel.");
	}
	
	@Override
	public String getStorageLabel() {
		return GeekReasoning.class.getCanonicalName();
	}
	
	@Override
	public void init(Agent agent) {
		// TODO Auto-generated method stub
		
	}
	
}
