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
package integrationTest;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.exception.SacecaStrictException;
import fr.n7.saceca.u3du.model.Model;

/**
 * This is the main class of the application. It must be called to run it.
 * 
 * @author Jérôme Dalbert
 */
public class CleanTest {
	
	/** The Constant INSTANCES_PATH. */
	private static final String INSTANCES_PATH = "data/ai/instances/normal";
	
	/** The Constant MODELS_PATH. */
	private static final String MODELS_PATH = "data/ai";
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws SacecaStrictException
	 *             If a problem occurs.
	 */
	public static void main(String[] args) throws SacecaStrictException {
		Model m = Model.getInstance();
		m.getAI().getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		String objectsDirName = INSTANCES_PATH + "/" + Constants.WORLD_OBJECTS_FOLDER_NAME;
		String objectsMiscDirName = objectsDirName + "/" + Constants.MISC_SUB_FOLDER_NAME;
		String agentsDirName = INSTANCES_PATH + "/" + Constants.AGENTS_FOLDER_NAME;
		
		m.getAI().getIOManager().clean(objectsDirName, objectsMiscDirName, agentsDirName);
	}
	
}
