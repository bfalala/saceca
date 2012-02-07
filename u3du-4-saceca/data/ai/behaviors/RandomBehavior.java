import fr.n7.saceca.u3du.model.ai.object.behavior.DefaultBehavior;

/**
 * The Class RandomBehaviorSource.
 * 
 * @author Sylvain Cambon
 */
public class RandomBehavior extends DefaultBehavior {
	
	/**
	 * Think about removing Source before compiling, to have it out of the class path.
	 */
	public static final String QUALIFIED_NAME = "RandomBehavior";
	
	@Override
	public void behave() {
		System.out.println("I randomly behave.");
	}
}
