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

import static org.junit.Assert.assertTrue;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import org.junit.Test;

import fr.n7.saceca.u3du.model.util.MathUtil;

public class MathUtilTest {
	
	@Test
	public void testSignedAngle() {
		assertTrue(Math.PI / 2 == MathUtil.signedAngle(new Vector2f(1, 0), new Vector2f(0, 1)));
		assertTrue(-Math.PI / 2 == MathUtil.signedAngle(new Vector2f(0, 1), new Vector2f(1, 0)));
		
		assertTrue(Math.PI / 2 == MathUtil.signedAngle(new Vector2f(-1, 1), new Vector2f(-1, -1)));
	}
	
	@Test
	public void testAligned() {
		assertTrue(MathUtil.areAlmostAligned(new Point2f(0, 0), new Point2f(0, 1), new Point2f(0, 2)));
	}
}
