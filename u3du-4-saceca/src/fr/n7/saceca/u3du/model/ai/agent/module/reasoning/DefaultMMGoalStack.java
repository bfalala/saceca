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
package fr.n7.saceca.u3du.model.ai.agent.module.reasoning;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;

/**
 * The DefaultMMGoalStack class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class DefaultMMGoalStack implements MMGoalStack {
	/** The list of goals */
	private List<MMGoal> goalList;
	
	/** The stack size */
	private static final int MAX_SIZE = 15;
	
	/**
	 * Constructor
	 */
	public DefaultMMGoalStack() {
		super();
		this.goalList = Collections.synchronizedList(new LinkedList<MMGoal>());
	}
	
	/**
	 * Gets the iterator
	 */
	@Override
	public Iterator<MMGoal> iterator() {
		return this.goalList.iterator();
	}
	
	/**
	 * Adds a goal in the begining of the stack
	 */
	@Override
	public MMGoalStack addBegining(MMGoal goal) {
		synchronized (this) {
			if (this.goalList.size() == MAX_SIZE) {
				((LinkedList<MMGoal>) this.goalList).removeLast();
			}
			((LinkedList<MMGoal>) this.goalList).addFirst(goal);
			return this;
		}
	}
	
	/**
	 * Adds a goal in the end of the stack
	 */
	@Override
	public MMGoalStack addEnd(MMGoal goal) {
		synchronized (this) {
			if (this.goalList.size() == MAX_SIZE) {
				((LinkedList<MMGoal>) this.goalList).removeLast();
			}
			((LinkedList<MMGoal>) this.goalList).addLast(goal);
			return this;
		}
	}
	
	/**
	 * Adds a goal in the middle of the stack
	 */
	@Override
	public MMGoalStack addMiddle(MMGoal goal, String mode) {
		synchronized (this) {
			int position = -1;
			// if the goal comes from a guage
			if (mode.equals("gauge")) {
				if ((position = this.existGoal(goal)) != -1 && !this.goalList.get(position).isReachable()) {
					this.goalList.remove(position);
				} else if ((position = this.existGoal(goal)) != -1 && this.goalList.get(position).isReachable()) {
					int old_priority = this.goalList.get(position).getPriority();
					if (goal.getPriority() >= old_priority
							|| old_priority - goal.getPriority() >= ServiceProperty.MAX_ATTRACTIVITY) {
						this.goalList.get(position).setPriority(goal.getPriority());
					}
				} else if (position == -1) {
					this.goalList.add(goal);
				}
				// if the goal comes from perception
			} else if (mode.equals("perception")) {
				if ((position = this.existGoal(goal)) != -1 && this.goalList.get(position).isReachable()) {
					this.goalList.get(position).setPriority(
							this.goalList.get(position).getPriority() + goal.getPriority());
				} else if (position != -1 && !this.goalList.get(position).isReachable()) {
					this.goalList.get(position).setReachable(true);
					this.goalList.get(position).setPriority(goal.getPriority());
				} else if (position == -1) {
					this.goalList.add(goal);
				}
				// if the goal comes from an emotion
			} else if (mode.equals("emotion")) {
				this.goalList.add(goal);
			}
			return this;
		}
	}
	
	/**
	 * Removes the first element from the stack
	 */
	@Override
	public MMGoalStack removeFirst() {
		synchronized (this) {
			((LinkedList<MMGoal>) this.goalList).removeFirst();
			return this;
		}
	}
	
	/**
	 * Removes the first element from middle of the stack
	 */
	@Override
	public MMGoalStack removeMiddle(MMGoal goal) {
		synchronized (this) {
			((LinkedList<MMGoal>) this.goalList).remove(goal);
			return this;
		}
	}
	
	/**
	 * Removes the element from end of the stack
	 */
	@Override
	public MMGoalStack removeLast() {
		synchronized (this) {
			((LinkedList<MMGoal>) this.goalList).removeLast();
			return this;
		}
	}
	
	/**
	 * Return a goal
	 */
	@Override
	public MMGoal get(int index) {
		return ((LinkedList<MMGoal>) this.goalList).get(index);
	}
	
	/**
	 * Return the size of the stack
	 */
	@Override
	public int size() {
		return this.goalList.size();
	}
	
	/**
	 * Set the list of goals
	 * 
	 * @param goalList
	 *            the list of goals
	 */
	public void setGoalList(List<MMGoal> goalList) {
		synchronized (this) {
			this.goalList = Collections.synchronizedList(goalList);
		}
	}
	
	/**
	 * Gets the list of goals
	 * 
	 * @return
	 */
	public List<MMGoal> getGoalList() {
		return this.goalList;
	}
	
	/**
	 * Checks if a goal exist in the stack
	 * 
	 * @param goal
	 *            the goal to check
	 * @return -1 if doesn't exist and the index in the stack if exists
	 */
	private int existGoal(MMGoal goal) {
		synchronized (this) {
			int index = 0;
			for (MMGoal mmGoal : this.goalList) {
				if (mmGoal.getSuccessCondition().egal(goal.getSuccessCondition())) {
					return index;
				}
				index++;
			}
			
			return -1;
		}
	}
	
	/**
	 * Sorts the stack list using in a reverse order of priorities
	 */
	@Override
	public void sortGoalStack() {
		synchronized (this) {
			if (this.goalList.isEmpty()) {
				return;
			}
			
			MMGoal previousMaxGoal = this.goalList.get(0);
			
			// We sort the goals by comparing their priority
			
			Collections.sort(this.goalList, Collections.reverseOrder(new Comparator<MMGoal>() {
				@Override
				public int compare(MMGoal g1, MMGoal g2) {
					return g1.getPriority().compareTo(g2.getPriority());
				}
			}));
			
			if (previousMaxGoal.getPriority().equals(this.goalList.get(0).getPriority())) {
				this.goalList.remove(previousMaxGoal);
				this.goalList.add(0, previousMaxGoal);
			}
			
			while (this.goalList.size() > MAX_SIZE) {
				((LinkedList<MMGoal>) this.goalList).removeLast();
			}
		}
	}
	
	/**
	 * Checks if the stack is empty or contains only unreachable goals
	 */
	@Override
	public boolean isEmpty() {
		synchronized (this) {
			if (this.goalList.size() == 0) {
				return true;
			}
			for (MMGoal goal : this.goalList) {
				if (goal.isReachable() == true) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Gets the list of goals
	 */
	@Override
	public List<MMGoal> getElementList() {
		return this.goalList;
	}
	
}