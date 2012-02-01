import fr.n7.saceca.u3du.model.ai.object.behavior.DefaultBehavior;

/**
 * The Class GeekBehavior.
 * 
 * @author Sylvain Cambon
 */
public class GeekBehavior extends DefaultBehavior {
	
	@Override
	public void behave() {
		System.out.println("Oh no, a seg. fault!");
	}
	
	@Override
	public String getStorageLabel() {
		return GeekBehavior.class.getCanonicalName();
	}
}
