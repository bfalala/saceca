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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class ClassicalPlan implements Plan {
	/** The backing list. */
	private List<PlanElement> backingCollection;
	
	/**
	 * Instantiates a new list plan.
	 */
	public ClassicalPlan() {
		super();
		this.backingCollection = new LinkedList<PlanElement>();
	}
	
	@Override
	public Iterator<PlanElement> iterator() {
		return this.backingCollection.iterator();
	}
	
	@Override
	public ClassicalPlan cons(PlanElement planElement) {
		((LinkedList<PlanElement>) this.backingCollection).addFirst(planElement);
		return this;
	}
	
	@Override
	public ClassicalPlan append(PlanElement planElement) {
		((LinkedList<PlanElement>) this.backingCollection).addLast(planElement);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Plan:\n");
		int num = 0;
		for (PlanElement planElement : this.backingCollection) {
			builder.append("\t#");
			builder.append(num);
			builder.append(' ');
			builder.append(planElement.toString());
			builder.append('\n');
			num++;
		}
		return builder.toString();
	}
	
	@Override
	public PlanElement get(int i) {
		return this.backingCollection.get(i);
	}
	
	@Override
	public int size() {
		return this.backingCollection.size();
	}
	
	@Override
	public int getIndex(PlanElement pe) {
		return this.backingCollection.lastIndexOf(pe);
	}
}
