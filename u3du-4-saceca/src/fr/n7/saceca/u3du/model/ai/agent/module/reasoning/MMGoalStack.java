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
package fr.n7.saceca.u3du.model.ai.agent.module.reasoning;

import java.util.List;

/**
 * The MMGoalStack interface
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public interface MMGoalStack extends Iterable<MMGoal> {
	/**
	 * Adds a goal at the begining of the stack
	 * 
	 * @param goal
	 *            the goal
	 * @return the stack of goals
	 */
	public MMGoalStack addBegining(MMGoal goal);
	
	/**
	 * Adds a goal at the end of the stack
	 * 
	 * @param goal
	 *            the goal
	 * @return the stack of goals
	 */
	public MMGoalStack addEnd(MMGoal goal);
	
	/**
	 * Adds a goal at the middle of the stack
	 * 
	 * @param goal
	 *            the goal
	 * @param mode
	 *            the source of the goal (perception , emotion or gauge)
	 * @return the stack of goals
	 */
	public MMGoalStack addMiddle(MMGoal goal, String mode);
	
	/**
	 * Removes the goal from the begining of the stack
	 * 
	 * @return
	 */
	public MMGoalStack removeFirst();
	
	/**
	 * Removes the goal from the middle of the stack
	 * 
	 * @param goal
	 *            the goal to remove
	 * @return
	 */
	public MMGoalStack removeMiddle(MMGoal goal);
	
	/**
	 * Removes the goal from the end of the stack
	 * 
	 * @return
	 */
	public MMGoalStack removeLast();
	
	/**
	 * Gets the goal of a certain position in the stack
	 * 
	 * @param index
	 *            the position in the stack
	 * @return
	 */
	public MMGoal get(int index);
	
	/**
	 * Gets the size of the stack
	 * 
	 * @return
	 */
	public int size();
	
	/**
	 * The stack is empty
	 * 
	 * @return true if yes
	 */
	public boolean isEmpty();
	
	/**
	 * Sorts the stack of goals in a reverse order of priorities
	 */
	public void sortGoalStack();
	
	/**
	 * Gets the list of goals
	 * 
	 * @return the list of goals
	 */
	public List<MMGoal> getElementList();
	
}
