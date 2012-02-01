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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.BooleanPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;
import fr.n7.saceca.u3du.model.ai.statement.CompoundOperatorAssignmentEffect;
import fr.n7.saceca.u3du.model.ai.statement.Condition;
import fr.n7.saceca.u3du.model.ai.statement.DefaultCondition;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.ai.statement.ServiceAwareU3duJexlContext;
import fr.n7.saceca.u3du.model.ai.statement.ServiceAwareU3duJexlEngine;
import fr.n7.saceca.u3du.model.ai.statement.Statement;
import fr.n7.saceca.u3du.model.ai.statement.U3duExpression;

public class ExpressionStatementTest {
	
	ServiceAwareU3duJexlContext context;
	
	final double EPSILON = 0.00001;
	
	static final int nose = 5;
	
	static final String noseString = "nose";
	
	static final double height = 1.82;
	
	static final String heightString = "height";
	
	static final String name = "Bob";
	
	static final String nameString = "name";
	
	static final boolean cool = true;
	
	static final String coolString = "cool";
	
	static final EnumElement blond = new EnumElement("Blond");
	
	static final EnumElement black = new EnumElement("Black");
	
	static final EnumElement red = new EnumElement("Red");
	
	static final EnumElement hair = black;
	
	static final String hairString = "hairColor";
	
	static final String belongingModelName = "key";
	
	static final String belongingPropertyName = "lockType";
	
	static final int belongingPropertyValue1 = 42;
	
	static final int belongingPropertyValue2 = 123;
	
	@Before
	public void setUp() throws Exception {
		// First object
		PropertyModel<Integer> pm1 = new IntegerPropertyModel(ExpressionStatementTest.noseString, 0, 15, 3,
				Visibility.PUBLIC);
		Property<Integer> p1 = new Property<Integer>(pm1);
		p1.setValue(ExpressionStatementTest.nose);
		WorldObject object1 = new WorldObject("Object1", 1);
		PropertiesContainer pr1 = object1.getPropertiesContainer();
		pr1.addProperty(p1);
		
		IntegerPropertyModel ipm = new IntegerPropertyModel(belongingPropertyName, 0, 1024, 1, Visibility.PUBLIC);
		Property<Integer> prop1 = new Property<Integer>(ipm);
		prop1.setValue(belongingPropertyValue1);
		WorldObject key1 = new WorldObject(belongingModelName, 2);
		key1.getPropertiesContainer().addProperty(prop1);
		object1.getBelongings().add(key1);
		
		Property<Integer> prop2 = new Property<Integer>(ipm);
		prop1.setValue(belongingPropertyValue2);
		WorldObject key2 = new WorldObject(belongingModelName, 3);
		key2.getPropertiesContainer().addProperty(prop2);
		object1.getBelongings().add(key2);
		
		// Second object
		PropertyModel<Double> pm2 = new DoublePropertyModel(ExpressionStatementTest.heightString, (double) 0, 2.50,
				1.75, Visibility.PUBLIC);
		Property<Double> p2 = new Property<Double>(pm2);
		p2.setValue(ExpressionStatementTest.height);
		
		PropertyModel<String> pm3 = new StringPropertyModel(ExpressionStatementTest.nameString, "John",
				Visibility.PUBLIC);
		Property<String> p3 = new Property<String>(pm3);
		p3.setValue(ExpressionStatementTest.name);
		
		PropertyModel<Boolean> pm4 = new BooleanPropertyModel(ExpressionStatementTest.coolString, false,
				Visibility.PUBLIC);
		Property<Boolean> p4 = new Property<Boolean>(pm4);
		p4.setValue(ExpressionStatementTest.cool);
		
		PropertyModel<EnumElement> pm5 = new EnumPropertyModel(ExpressionStatementTest.hairString,
				ExpressionStatementTest.blond, new EnumElement[] { ExpressionStatementTest.blond,
						ExpressionStatementTest.black, ExpressionStatementTest.red }, Visibility.PUBLIC);
		Property<EnumElement> p5 = new Property<EnumElement>(pm5);
		p5.setValue(ExpressionStatementTest.hair);
		
		WorldObject object2 = new WorldObject("Object2", 4);
		
		PropertiesContainer pr2 = object2.getPropertiesContainer();
		pr2.addProperty(p2);
		pr2.addProperty(p3);
		pr2.addProperty(p4);
		pr2.addProperty(p5);
		
		this.context = new ServiceAwareU3duJexlContext(object1, object2, null, ExecutionMode.REAL);
		ServiceAwareU3duJexlEngine.getInstance().setFunctorsParameters(this.context);
	}
	
	@Test
	public void testCompoundDivision() {
		int div = 2;
		int expectedResult = nose / div;
		String name = "this" + Property.SEPARATOR + ExpressionStatementTest.noseString;
		Statement<Void> statement = new CompoundOperatorAssignmentEffect(name + "/=" + div);
		statement.evaluate(this.context);
		Statement<Integer> statement2 = new U3duExpression<Integer>(name);
		int result = statement2.evaluate(this.context);
		assertEquals(result, expectedResult);
	}
	
	@Test
	public void testEval() {
		boolean expectedValue = ExpressionStatementTest.cool;
		String expr = "eval:string('other" + Property.SEPARATOR + "'+'" + ExpressionStatementTest.coolString + "')";
		Statement<Boolean> statement = new U3duExpression<Boolean>(expr);
		boolean result = statement.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testGetBoolean() {
		boolean expectedValue = ExpressionStatementTest.cool;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.coolString;
		Statement<Boolean> statement = new U3duExpression<Boolean>(name);
		boolean result = statement.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testGetDoubleWithCalculation() {
		double addend = 0.1;
		double initValue = ExpressionStatementTest.height;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.heightString;
		Statement<Double> statement = new U3duExpression<Double>(name + "+" + addend);
		double result = statement.evaluate(this.context);
		assertEquals(result, initValue + addend, this.EPSILON);
	}
	
	@Test
	public void testGetEnum() {
		EnumElement expectedValue = ExpressionStatementTest.hair;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.hairString;
		Statement<EnumElement> statement = new U3duExpression<EnumElement>(name);
		EnumElement result = statement.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testGetIntegerWithCalculation() {
		int addend = 10;
		int initValue = ExpressionStatementTest.nose;
		String name = "this" + Property.SEPARATOR + ExpressionStatementTest.noseString;
		Statement<Integer> statement = new U3duExpression<Integer>(name + "+" + addend);
		int result = statement.evaluate(this.context);
		assertEquals(result, initValue + addend);
	}
	
	@Test
	public void testGetString() {
		String expectedValue = ExpressionStatementTest.name;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.nameString;
		Statement<String> statement = new U3duExpression<String>(name);
		String result = statement.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testHas() {
		Condition statement = new DefaultCondition("this:has('" + belongingModelName + "')");
		boolean result = statement.check(this.context);
		assert (result);
	}
	
	@Test
	public void testHasNot() {
		Condition statement = new DefaultCondition("this:has('foo')");
		boolean result = statement.check(this.context);
		assert (!result);
	}
	
	@Test
	public void testHasNotWithExpression() {
		Condition statement = new DefaultCondition("this:has('" + belongingModelName + "', '$_" + belongingPropertyName
				+ "==512')");
		boolean result = statement.check(this.context);
		assert (!result);
	}
	
	@Test
	public void testHasWithExpression() {
		Condition statement = new DefaultCondition("this:has('" + belongingModelName + "', '$_" + belongingPropertyName
				+ "==" + belongingPropertyValue1 + "')");
		boolean result = statement.check(this.context);
		assert (result);
	}
	
	@Test
	public void testSetBoolean() {
		Boolean expectedValue = false;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.coolString;
		Statement<Boolean> statement = new U3duExpression<Boolean>(name + "=" + expectedValue);
		statement.evaluate(this.context);
		Statement<Boolean> statement2 = new U3duExpression<Boolean>(name);
		Boolean result = statement2.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testSetEnum() {
		EnumElement expectedValue = ExpressionStatementTest.red;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.hairString;
		Statement<EnumElement> statement = new U3duExpression<EnumElement>(name + "=" + "'" + red.toString() + "'");
		statement.evaluate(this.context);
		Statement<EnumElement> statement2 = new U3duExpression<EnumElement>(name);
		EnumElement result = statement2.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testSetIntegerOutOfInterval() {
		int value = -5;
		String name = "this" + Property.SEPARATOR + ExpressionStatementTest.noseString;
		Statement<Integer> statement = new U3duExpression<Integer>(name + "=" + value);
		statement.evaluate(this.context);
		Statement<Integer> statement2 = new U3duExpression<Integer>(name);
		int result = statement2.evaluate(this.context);
		assertEquals(result, 0);
	}
	
	@Test
	public void testSetIntegerWithCalculation() {
		int addend = 10;
		int initValue = ExpressionStatementTest.nose;
		String name = "this" + Property.SEPARATOR + ExpressionStatementTest.noseString;
		Statement<Integer> statement = new U3duExpression<Integer>(name + "=" + name + "+" + addend);
		statement.evaluate(this.context);
		Statement<Integer> statement2 = new U3duExpression<Integer>(name);
		int result = statement2.evaluate(this.context);
		assertEquals(result, initValue + addend);
	}
	
	@Test
	public void testSetStringWithCalculation() {
		String expectedValuePart1 = "To";
		String expectedValuePart2 = "to";
		String expectedValue = expectedValuePart1 + expectedValuePart2;
		String name = "other" + Property.SEPARATOR + ExpressionStatementTest.nameString;
		Statement<String> statement = new U3duExpression<String>(name + "=" + "'" + expectedValuePart1 + "'+'"
				+ expectedValuePart2 + "'");
		statement.evaluate(this.context);
		Statement<String> statement2 = new U3duExpression<String>(name);
		String result = statement2.evaluate(this.context);
		assertEquals(result, expectedValue);
	}
	
	@Test
	public void testTakes() {
		Condition statement = new DefaultCondition("this:takes('" + belongingModelName + "')");
		boolean result = statement.check(this.context);
		assert (result);
	}
	
	@Test
	public void testTakesNot() {
		Condition statement = new DefaultCondition("this:takes('foo')");
		boolean result = statement.check(this.context);
		assert (!result);
	}
	
	@Test
	public void testTakesNotWithExpression() {
		Condition statement = new DefaultCondition("this:takes('" + belongingModelName + "', '$_"
				+ belongingPropertyName + "==512')");
		boolean result = statement.check(this.context);
		assert (!result);
	}
	
	@Test
	public void testTakesWithExpression() {
		Condition statement = new DefaultCondition("this:takes('" + belongingModelName + "', '$_"
				+ belongingPropertyName + "==" + belongingPropertyValue1 + "')");
		boolean result = statement.check(this.context);
		assert (result);
	}
}
