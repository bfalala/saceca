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
package unitTest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

public class Oriented2DPositionTest {
	
	@Test
	public void testClone() {
		Oriented2DPosition p1 = new Oriented2DPosition();
		Oriented2DPosition p2 = p1.clone();
		p1.theta = 5;
		assertTrue(p1.theta == 5);
		assertTrue(p2.theta == 0);
	}
	
	@Test
	public void testConstructors() {
		Oriented2DPosition p = new Oriented2DPosition();
		assertTrue(p.x == 0 && p.y == 0 && p.theta == 0);
		
		p = new Oriented2DPosition(1, 2, 3);
		assertTrue(p.x == 1 && p.y == 2 && p.theta == 3);
	}
	
	@Test
	public void testEquals() {
		Oriented2DPosition p1 = new Oriented2DPosition(1, 2, 3);
		Oriented2DPosition p2 = new Oriented2DPosition(1, 2, 3);
		assertEquals(p1, p2);
	}
	
	@Test
	public void testDistance() {
		Oriented2DPosition p1 = new Oriented2DPosition(0, 0, 0);
		Oriented2DPosition p2 = new Oriented2DPosition(0, 2, 0);
		assertTrue(p1.distance(p2) == 2);
	}
}
