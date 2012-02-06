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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.action.Action;
import fr.n7.saceca.u3du.model.ai.statement.Condition;
import fr.n7.saceca.u3du.model.ai.statement.Effect;
import fr.n7.saceca.u3du.model.ai.statement.ExecutionMode;
import fr.n7.saceca.u3du.model.ai.statement.StatementFactory;
import fr.n7.saceca.u3du.model.ai.statement.StatementsGroup;
import fr.n7.saceca.u3du.model.io.ai.service.ServiceIO;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;
import fr.n7.saceca.u3du.model.util.io.storage.StorableClassWrapper;

public class ServiceTest {
	
	private Service drinkACan;
	
	private String drinkACanName = "drinkACan";
	
	private String humanCategoryName = "Human";
	
	private String cash = "c_" + this.humanCategoryName + "_cash";
	
	private String thirst = "i_gauge_thirst";
	
	private String cons = "other_";
	
	private WorldObject human;
	
	private WorldObject machine;
	
	private Repository<StorableClassWrapper<Action>> repository;
	
	private int price = 120; // That's quite expensive!
	
	private int deltaThirst = 20;
	
	private int initThirst = 30;
	
	private int initMoney = 150;
	
	@Before
	public void setUp() throws Exception {
		
		// 1. Building the categories
		Set<Category> consumerCategories = new HashSet<Category>();
		consumerCategories.add(new Category(this.humanCategoryName));
		Set<Category> providerCategories = new HashSet<Category>();
		
		// 2. Building the service
		// 2.1. Building the statements
		// 2.1.1. Building the preconditions
		StatementFactory factory = StatementFactory.getInstance();
		Condition condition = factory.createTestStatement(this.cons + this.cash + ">=" + this.price);
		List<Condition> tests = new LinkedList<Condition>();
		tests.add(condition);
		
		// 2.1.2. Building the effects
		Effect action1 = factory.createActionStatement(this.cons + this.cash + "-=" + this.price);
		Effect action2 = factory.createActionStatement(this.cons + this.thirst + "+=" + this.deltaThirst);
		List<Effect> actions = new LinkedList<Effect>();
		actions.add(action1);
		actions.add(action2);
		// 2.1.3. Building the statement group itself
		StatementsGroup statements = new StatementsGroup(tests, actions);
		
		// 2.2. Building the service itself
		// this.drinkACan = new Service(this.drinkACanName, true, 10, providerCategories,
		// consumerCategories, statements,
		// null);
		
		// 3. Building the provider
		this.machine = new WorldObject("machine", 1);
		this.machine.getServices().add(this.drinkACan);
		
		// 4. Builder the consumer
		this.human = new Agent("human", 2);
		PropertyModel<Integer> thirstModel = new IntegerPropertyModel(this.thirst, 0, 100, this.initThirst,
				Visibility.PRIVATE);
		Property<Integer> thirst = new Property<Integer>(thirstModel);
		PropertyModel<Integer> cash = new IntegerPropertyModel(this.cash, 0, 10000, this.initMoney, Visibility.PRIVATE);
		Property<Integer> money = new Property<Integer>(cash);
		this.human.getPropertiesContainer().addProperty(thirst);
		this.human.getPropertiesContainer().addProperty(money);
		this.human.getCategories().add(new Category(this.humanCategoryName));
	}
	
	@Test
	public void testCategories() {
		this.human.getCategories().remove(new Category(this.humanCategoryName));
		if (this.drinkACan.isUsable(this.machine, this.human, null)) {
			fail();
		}
	}
	
	@Test
	public void testPreconditions() throws Exception {
		this.human.getPropertiesContainer().setInt(this.cash, 0);
		if (this.drinkACan.isUsable(this.machine, this.human, null)) {
			fail();
		}
	}
	
	@Test
	public void testWorkingUsage() throws Exception {
		if (this.drinkACan.isUsable(this.machine, this.human, null)) {
			this.drinkACan.execute(this.machine, this.human, null, ExecutionMode.REAL);
			assertEquals(this.initThirst + this.deltaThirst, this.human.getPropertiesContainer().getInt(this.thirst));
			assertEquals(this.initMoney - this.price, this.human.getPropertiesContainer().getInt(this.cash));
		} else {
			fail();
		}
	}
	
	@Test
	public void testXStream() throws IOException {
		final String filename = "data/ai/services/drinkACan.serv.xml";
		ServiceIO io = new ServiceIO(this.repository);
		
		String xml = io.exportObject(filename, this.drinkACan);
		System.out.println("XML =");
		System.out.println(xml);
		
		Service read = io.importObject(filename);
		System.out.println();
		System.out.println("Read =");
		System.out.println(read);
	}
}
