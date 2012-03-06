/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.util;

import java.text.NumberFormat;
import java.util.Locale;

import javax.vecmath.Point2f;

import fr.n7.saceca.u3du.Constants;

/**
 * The Class Oriented2DPosition.
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public class Oriented2DPosition extends Point2f {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The orientation theta. <b>IMPORTANT NOTE :</b> This theta is based on the MathUtil.UNIT_Y
	 * vector. So theta = 0 if the normalized direction vector of the object is equal to
	 * MathUtil.UNIT_Y.
	 * 
	 * @see MathUtil
	 */
	public float theta;
	
	/**
	 * Instantiates a new position.
	 */
	public Oriented2DPosition() {
		this(0, 0, 0);
		NumberFormat.getInstance(Locale.ENGLISH);
	}
	
	/**
	 * Instantiates a new oriented2 d position.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param theta
	 *            the theta
	 */
	public Oriented2DPosition(float x, float y, float theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	
	/**
	 * Clone.
	 * 
	 * @return the position
	 */
	@Override
	public Oriented2DPosition clone() {
		return new Oriented2DPosition(this.x, this.y, this.theta);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + java.lang.Float.floatToIntBits(this.x);
		result = prime * result + java.lang.Float.floatToIntBits(this.y);
		result = prime * result + java.lang.Float.floatToIntBits(this.theta);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Oriented2DPosition) {
			Oriented2DPosition pos = (Oriented2DPosition) obj;
			return this.distance(pos) < Constants.EPSILON;
		}
		return false;
	}
	
	public boolean egal(Oriented2DPosition position) {
		return Math.abs(this.x - position.x) <= 0.1 && Math.abs(this.y - position.y) <= 0.1
				&& Math.abs(this.theta - position.theta) <= 0.1;
	}
	
	@Override
	public String toString() {
		return "(x=" + FormatUtil.format(this.x) + ", y=" + FormatUtil.format(this.y) + ", theta="
				+ FormatUtil.format(this.theta) + ")";
	}
	
	public String toStringForXML() {
		return FormatUtil.format(this.x) + "_" + FormatUtil.format(this.y) + "_" + FormatUtil.format(this.theta);
	}
}