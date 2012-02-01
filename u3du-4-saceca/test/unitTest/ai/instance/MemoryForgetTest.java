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
package unitTest.ai.instance;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;

public class MemoryForgetTest {
	
	public static void main(String[] args) {
		Agent a = new Agent("Agent", 10);
		a.setMemory(new Memory(a, 3));
		
		a.getMemory().remember(new Agent("Agent", 1));
		a.getMemory().remember(new Agent("Agent", 2));
		a.getMemory().remember(new Agent("Agent", 3));
		
		System.out.println(a.getMemory());
		
		a.getMemory().remember(new Agent("Agent", 4));
		
		System.out.println(a.getMemory());
		
		a.getMemory().remember(new Agent("Agent", 5));
		
		System.out.println(a.getMemory());
		
		a.getMemory().remember(new Agent("Agent", 0));
		
		System.out.println(a.getMemory());
	}
	
}
