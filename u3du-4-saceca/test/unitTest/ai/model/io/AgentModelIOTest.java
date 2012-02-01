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
package unitTest.ai.model.io;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import fr.n7.saceca.u3du.Constants;
import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.io.ai.model.agent.AgentModelIO;

import junit.framework.Assert;

public class AgentModelIOTest {
	
	private static final String PATH = "data/ai";
	
	private static final String AGENT_MODEL_FILENAME = PATH + "/" + Constants.AGENTS_MODELS_FOLDER_NAME + "/Geek"
			+ Constants.AGENTS_MODELS_EXTENSION;
	
	@Test
	public void test() throws UnknownPropertyException, IOException {
		
		// Building an agent model by hand
		AgentModel model = new AgentModel("Geek");
		
		model.setBehaviorName("GeekBehavior");
		
		Set<String> categories = new HashSet<String>();
		categories.add("Committer");
		model.setCategoriesNames(categories);
		
		Set<PropertyModel<?>> properties = new HashSet<PropertyModel<?>>();
		properties.add(new DoublePropertyModel("i_gauge_geekness", 0.0, 1024.0, 42.0, Visibility.PRIVATE));
		model.setProperties(properties);
		
		AgentModelIO amio = new AgentModelIO();
		String agentModelXML = amio.exportObject(AGENT_MODEL_FILENAME, model);
		System.out.println("Agent model XML =");
		System.out.println(agentModelXML);
		
		AgentModel readModel = amio.importObject(AGENT_MODEL_FILENAME);
		
		System.out.println("\nRead model =");
		System.out.println(readModel);
		System.out.println();
		
		Assert.assertEquals(model, readModel);
	}
}
