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
package fr.n7.saceca.u3du.model.ai.agent.module.planning;

/**
 * A interface for plans. A plan is an ordered collection of elements to be used to reach a goal.
 * This interface is fluent, i.e. methods <code>cons</code> and <code>append</code> return the
 * mutable plan they modify (once modified).
 */
public interface Plan extends Iterable<PlanElement> {
	
	/**
	 * Adds an element at the beginning of the plan.
	 * 
	 * @param element
	 *            the element
	 * @return "this"
	 */
	public Plan cons(PlanElement element);
	
	/**
	 * Adds an element at the ends of the plan.
	 * 
	 * @param element
	 *            the element
	 * @return "this"
	 */
	public Plan append(PlanElement element);
	
	/**
	 * Gets an element
	 * 
	 * @param i
	 *            the i
	 * @return the service
	 */
	public PlanElement get(int i);
	
	/**
	 * Gets the size.
	 * 
	 * @return the int
	 */
	public int size();
	
}
