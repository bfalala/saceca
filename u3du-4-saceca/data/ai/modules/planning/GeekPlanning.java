import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanningModule;

/**
 * The Class GeekPlanning.
 * 
 * @author Sylvain Cambon
 */
public class GeekPlanning implements PlanningModule {
	
	@Override
	public void planAndExecute() {
		System.out.println("I plan to play WoW after diner.");
	}
	
	@Override
	public String getStorageLabel() {
		return GeekPlanning.class.getCanonicalName();
	}
	
	@Override
	public void init(Agent agent) {
		// TODO Auto-generated method stub
		
	}
	
}
