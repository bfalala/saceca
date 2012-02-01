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
package unitTest.ai.statement;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.statement.CompoundOperatorAssignmentEffect;
import fr.n7.saceca.u3du.model.ai.statement.Condition;
import fr.n7.saceca.u3du.model.ai.statement.DefaultCondition;
import fr.n7.saceca.u3du.model.ai.statement.DefaultEffect;
import fr.n7.saceca.u3du.model.ai.statement.Effect;
import fr.n7.saceca.u3du.model.ai.statement.Statement;
import fr.n7.saceca.u3du.model.ai.statement.StatementFactory;
import fr.n7.saceca.u3du.model.ai.statement.StatementsGroup;
import fr.n7.saceca.u3du.model.ai.statement.U3duExpression;
import fr.n7.saceca.u3du.model.io.ai.statement.StatementsGroupConverter;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamProvider;

import junit.framework.Assert;

public class StatementsGroupIOTest {
	
	@Test
	public void test() {
		List<Effect> actions = new LinkedList<Effect>();
		Effect normalAction = StatementFactory.getInstance().createActionStatement("a=4");
		Effect comOpAsAction = StatementFactory.getInstance().createActionStatement("b+=4");
		actions.add(normalAction);
		actions.add(comOpAsAction);
		
		List<Condition> tests = new LinkedList<Condition>();
		Condition normalTest = StatementFactory.getInstance().createTestStatement("4 <= a");
		tests.add(normalTest);
		
		StatementsGroup group = new StatementsGroup(tests, actions);
		
		XStream xstream = XStreamProvider.getXStreamInstance();
		xstream.processAnnotations(StatementsGroup.class);
		xstream.processAnnotations(Effect.class);
		xstream.processAnnotations(DefaultCondition.class);
		xstream.processAnnotations(CompoundOperatorAssignmentEffect.class);
		xstream.processAnnotations(DefaultEffect.class);
		xstream.processAnnotations(U3duExpression.class);
		xstream.processAnnotations(Statement.class);
		xstream.processAnnotations(StatementsGroup.class);
		xstream.processAnnotations(Condition.class);
		xstream.registerConverter(new StatementsGroupConverter());
		
		String xml = xstream.toXML(group);
		System.out.println("XML =");
		System.out.println(xml);
		System.out.println();
		
		StatementsGroup readGroup = (StatementsGroup) xstream.fromXML(xml);
		System.out.println("Read =");
		System.out.println(readGroup);
		
		Assert.assertEquals(group, readGroup);
	}
}
