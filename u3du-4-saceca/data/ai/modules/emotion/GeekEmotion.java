import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.emotion.EmotionModule;

/**
 * The Class GeekEmotion.
 * 
 * @author Sylvain Cambon
 */
public class GeekEmotion implements EmotionModule {
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return GeekEmotion.class.getCanonicalName();
	}
	
	/**
	 * Detect emotions.
	 */
	@Override
	public void detectEmotions() {
		System.out.println("I know a bot. Its name is Anna. As far as I am concerned, Anna will always remain a bot.");
	}
	
	@Override
	public void init(Agent agent) {
		// TODO Auto-generated method stub
		
	}
	
}
