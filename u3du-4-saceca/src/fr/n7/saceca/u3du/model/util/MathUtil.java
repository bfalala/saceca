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
package fr.n7.saceca.u3du.model.util;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

/**
 * The Class MathUtil is based on the float type.
 * 
 * @author Jérôme Dalbert
 */
public class MathUtil {
	
	/** The Constant PI. Be careful when using it, because you lose a lot of precision. */
	public static final float PI = (float) Math.PI;
	
	/** The Constant UNIT_Y. */
	public static final Vector2f UNIT_Y = new Vector2f(0, 1);
	
	/** The Constant EPSILON. */
	public static final float EPSILON = 0.01f;
	
	/** The Constant BIG_EPSILON. */
	public static final float BIG_EPSILON = 0.15f;
	
	/**
	 * Determines if two points are aligned, with a EPSILON margin
	 * 
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @param p3
	 *            the p3
	 * @return true, if successful
	 */
	public static boolean areAlmostAligned(Point2f p1, Point2f p2, Point2f p3) {
		Vector2f v1 = new Vector2f(p2.x - p1.x, p2.y - p1.y);
		Vector2f v2 = new Vector2f(p3.x - p1.x, p3.y - p1.y);
		
		return v1.angle(v2) < EPSILON;
	}
	
	/**
	 * Determines if two points are aligned
	 * 
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @param p3
	 *            the p3
	 * @return true, if successful
	 */
	public static boolean areAligned(Point2f p1, Point2f p2, Point2f p3) {
		Vector2f v1 = new Vector2f(p2.x - p1.x, p2.y - p1.y);
		Vector2f v2 = new Vector2f(p3.x - p1.x, p3.y - p1.y);
		
		return v1.angle(v2) == 0;
	}
	
	/**
	 * Gets the signed angle from v1 to v2
	 * 
	 * @param v1
	 *            the v1
	 * @param v2
	 *            the v2
	 * @return the signed angle
	 */
	public static double signedAngle(Vector2f v1, Vector2f v2) {
		double angle = Math.atan2(v2.y, v2.x) - Math.atan2(v1.y, v1.x);
		
		if (Math.abs(angle) > Math.PI) {
			angle += 2 * Math.PI;
		}
		
		return angle;
	}
	
	/**
	 * Rotates a vector with a theta angle.
	 * 
	 * @param v
	 *            the v
	 * @param theta
	 *            the theta
	 * @return the vector2f
	 */
	public static Vector2f rotate(Vector2f v, float theta) {
		// 2D matrix rotation
		float x = (float) (Math.cos(theta) * v.x - Math.sin(theta) * v.y);
		float y = (float) (Math.sin(theta) * v.x + Math.cos(theta) * v.y);
		
		return new Vector2f(x, y);
	}
}
