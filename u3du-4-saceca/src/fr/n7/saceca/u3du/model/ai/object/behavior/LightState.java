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
package fr.n7.saceca.u3du.model.ai.object.behavior;

import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;

/**
 * The possible Light states.
 */
public enum LightState {
	/** The GREEN, from 0s to 7s */
	GREEN("GREEN", 800),
	/** The ORANGE, from 8s to 10s. */
	ORANGE("ORANGE", 1000),
	/** The RED, from 11s to 22s. */
	RED("RED", 2200);
	
	/** The Constant NUMBER_OF_ELEMENTS_IN_A_PERIOD. */
	static final int NUMBER_OF_ELEMENTS_IN_A_PERIOD;
	static {
		int temp = 0;
		for (LightState state : LightState.values()) {
			temp = Math.max(temp, state.phaseEnd);
		}
		NUMBER_OF_ELEMENTS_IN_A_PERIOD = temp + 1;
	}
	
	/** The corresponding enum element. */
	private EnumElement enumElement;
	
	/** The element denoting the phase end. */
	private int phaseEnd;
	
	/**
	 * Gets the corresponding enum element.
	 * 
	 * @return the corresponding enum element
	 */
	public final EnumElement getEnumElement() {
		return this.enumElement;
	}
	
	/**
	 * Gets the element denoting the phase end.
	 * 
	 * @return the element denoting the phase end
	 */
	public final int getPhaseEnd() {
		return this.phaseEnd;
	}
	
	/**
	 * Instantiates a new light state.
	 * 
	 * @param enumElementName
	 *            the enum element name
	 * @param phaseEnd
	 *            the phase end
	 */
	private LightState(String enumElementName, int phaseEnd) {
		this.enumElement = new EnumElement(enumElementName);
		this.phaseEnd = phaseEnd;
	}
	
	/**
	 * Gets the next light state.
	 * 
	 * @return the next
	 */
	public LightState getNext() {
		switch (this) {
			case GREEN:
				return ORANGE;
			case ORANGE:
				return RED;
			case RED:
				return GREEN;
			default:
				return null;
		}
	}
}