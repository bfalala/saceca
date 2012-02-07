import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.perception.PerceptionModule;

/**
 * The Class GeekPerception.
 * 
 * @author Sylvain Cambon
 */
public class GeekPerception implements PerceptionModule {
	
	@Override
	public void init(Agent agent) {
		
	}
	
	@Override
	public String getStorageLabel() {
		return GeekPerception.class.getCanonicalName();
	}
	
	@Override
	public void perceive() {
		System.out.println("I perceive the matrix is all around...");
	}
	
}
