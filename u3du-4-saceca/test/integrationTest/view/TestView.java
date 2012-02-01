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
package integrationTest.view;

import fr.n7.saceca.u3du.exception.SacecaStrictException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.WhereIsItQuestionMessage;
import fr.n7.saceca.u3du.view.View;

public class TestView {
	
	private static final String INSTANCES_PATH = "data/ai/instances/normal";
	private static final String MODELS_PATH = "data/ai";
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws SacecaStrictException
	 *             If a problem occurs during the initialization.
	 */
	public static void main(String[] args) throws SacecaStrictException {
		AI ai = Model.getInstance().getAI();
		
		ai.getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		
		Agent jack = (Agent) ai.getWorld().getWorldObjects().get(0L);
		// il se possede lui meme, ca n'a pas de sens mais c'est pour tester la possession
		// Sylvain : tellement pas de sens que ça fait planter Jack, pour cause de récursion infinie
		// sur la plannification.
		// jack.getBelongings().add(jack);
		jack.sendMessage(new WhereIsItQuestionMessage(jack, jack.getId()));
		
		View v = new View();
		v.getSimulationWindow().display();
	}
}
