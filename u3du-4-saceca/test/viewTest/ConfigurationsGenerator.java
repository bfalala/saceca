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
package viewTest;

import fr.n7.saceca.u3du.model.graphics.configuration.AgentConfiguration;

public class ConfigurationsGenerator {
	public static void main(String[] args) {
		AgentConfiguration agent = new AgentConfiguration();
		agent.setCorrectiveAngle(0); // -FastMath.PI / 4 for AI agents
		agent.setEyesHeight(0.35f);
		agent.setName("Human");
		agent.setPathModel("Models/Human.mesh.xml");
		agent.setScaleX(0);
	}
}
