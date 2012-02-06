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

/**
 * A class to use a pair of elements.
 * 
 * @param <A>
 *            The generic type of the first parameter.
 * @param <B>
 *            The generic type of the second parameter.
 * @author Sylvain Cambon
 */
public final class Couple<A, B> {
	
	/** The first element. */
	private final A firstElement;
	
	/** The second element. */
	private final B secondElement;
	
	/**
	 * Instantiates a new couple.
	 * 
	 * @param firstElement
	 *            The first element.
	 * @param secondElement
	 *            The second element.
	 */
	public Couple(A firstElement, B secondElement) {
		super();
		this.firstElement = firstElement;
		this.secondElement = secondElement;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		Couple other = (Couple) obj;
		if (this.firstElement == null) {
			if (other.firstElement != null) {
				return false;
			}
		} else if (!this.firstElement.equals(other.firstElement)) {
			return false;
		}
		if (this.secondElement == null) {
			if (other.secondElement != null) {
				return false;
			}
		} else if (!this.secondElement.equals(other.secondElement)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the first element.
	 * 
	 * @return the first element
	 */
	public final A getFirstElement() {
		return this.firstElement;
	}
	
	/**
	 * Gets the second element.
	 * 
	 * @return the second element
	 */
	public final B getSecondElement() {
		return this.secondElement;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.firstElement == null) ? 0 : this.firstElement.hashCode());
		result = prime * result + ((this.secondElement == null) ? 0 : this.secondElement.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "(" + this.firstElement + ", " + this.secondElement + ")";
	}
}
