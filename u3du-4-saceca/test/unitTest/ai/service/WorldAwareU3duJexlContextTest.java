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
package unitTest.ai.service;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jexl2.JexlContext;
import org.junit.Before;
import org.junit.Test;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.statement.U3duExpression;
import fr.n7.saceca.u3du.model.ai.statement.WorldAwareU3duJexlContext;

public class WorldAwareU3duJexlContextTest {
	
	private static final String MODELS_PATH = "data/ai";
	private static final String INSTANCES_PATH = "data/ai/instances/unit tests";
	
	private static final long NEO_ID = 0;
	
	private static final int NEW_VALUE = 42;
	
	private JexlContext context;
	
	private U3duExpression<Integer> readExpr;
	
	@Before
	public void setUp() throws Exception {
		Model.getInstance().getAI().getIOManager().loadAI(MODELS_PATH, INSTANCES_PATH);
		this.context = new WorldAwareU3duJexlContext();
		this.readExpr = new U3duExpression<Integer>("id" + NEO_ID + Property.SEPARATOR
				+ Internal.Agent.PERCEPTION_MAX_EYESIGHT_DISTANCE);
	}
	
	@Test
	public void readTest() throws UnknownPropertyException {
		int read = this.readExpr.evaluate(this.context);
		int expected = Model.getInstance().getAI().getWorld().getWorldObjects().get(NEO_ID).getPropertiesContainer()
				.getInt(Internal.Agent.PERCEPTION_MAX_EYESIGHT_DISTANCE);
		assertEquals(expected, read);
	}
	
	@Test
	public void readWrite() {
		U3duExpression<Integer> writeExpr = new U3duExpression<Integer>("id" + NEO_ID + Property.SEPARATOR
				+ Internal.Agent.PERCEPTION_MAX_EYESIGHT_DISTANCE + " = " + NEW_VALUE);
		writeExpr.evaluate(this.context);
		int expected = NEW_VALUE;
		int read = this.readExpr.evaluate(this.context);
		assertEquals(expected, read);
	}
}
