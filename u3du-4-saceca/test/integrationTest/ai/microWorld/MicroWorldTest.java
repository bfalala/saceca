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
package integrationTest.ai.microWorld;

import fr.n7.saceca.u3du.model.ai.Simulation;
import fr.n7.saceca.u3du.model.ai.Visibility;
import fr.n7.saceca.u3du.model.ai.World;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.BooleanPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;

public class MicroWorldTest {
	
	public static void main(String[] args) {
		World world = new World();
		
		BooleanPropertyModel booleanPropertyModel = new BooleanPropertyModel("i_threaded", true, Visibility.PRIVATE);
		
		Agent a1 = new Agent("John", 1);
		final AgentBehavior behavior = new AgentBehavior();
		a1.setBehavior(behavior);
		behavior.init(a1);
		a1.getPropertiesContainer().addProperty(new Property<Boolean>(booleanPropertyModel));
		
		Agent a2 = new Agent("Bob", 2);
		final AgentBehavior behavior2 = new AgentBehavior();
		a2.setBehavior(behavior2);
		behavior2.init(a2);
		a2.getPropertiesContainer().addProperty(new Property<Boolean>(booleanPropertyModel));
		
		WorldObject bus = new WorldObject("Foo", 3);
		final BusBehavior behavior3 = new BusBehavior();
		bus.setBehavior(behavior3);
		behavior3.init(bus);
		bus.getPropertiesContainer().addProperty(new Property<Boolean>(booleanPropertyModel));
		
		world.getWorldObjects().put(a1.getId(), a1);
		world.getWorldObjects().put(a2.getId(), a2);
		world.getWorldObjects().put(bus.getId(), bus);
		
		Simulation simulator = new Simulation(world);
		simulator.setTickPeriod(2000);
		simulator.start();
		
		wait(5);
		System.out.println("PAUSE ON ! Fin dans 5 secondes.");
		simulator.setPause(true);
		
		wait(5);
		System.out.println("PAUSE OFF !");
		simulator.setPause(false);
	}
	
	public static void wait(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
