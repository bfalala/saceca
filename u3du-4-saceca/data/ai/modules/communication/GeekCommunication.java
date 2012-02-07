import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.CommunicationModule;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;

/**
 * The Class GeekCommunication.
 * 
 * @author Sylvain Cambon
 */
public class GeekCommunication implements CommunicationModule {
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return GeekCommunication.class.getCanonicalName();
	}
	
	/**
	 * Communicate.
	 */
	@Override
	public void communicate() {
		System.out.println("Communication failure: I have no communication interface.");
	}
	
	@Override
	public void addMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void init(Agent agent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Agent getAgent() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
