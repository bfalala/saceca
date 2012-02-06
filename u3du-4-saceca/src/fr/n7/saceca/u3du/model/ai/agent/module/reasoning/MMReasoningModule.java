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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.manageGoal.CreateGaugeGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.manageGoal.CreatePerceptionGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.manageGoal.Rule;

/**
 * The MMReasoningModule class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class MMReasoningModule implements ReasoningModule {
	/** The list of rules */
	private List<Rule> rules;
	
	/** The agent */
	private Agent agent;
	
	/** The Constant MAX_REASONING_TIME, in ms. */
	private static final int MAX_REASONING_TIME = 500;
	
	/** The constant TOP_PRIORITY */
	public static final int TOP_PRIORITY = 100;
	
	/** The Constant GAUGE_REFILL. */
	public static final double GAUGE_REFILL = 0.80;
	
	/** The Constant UNREACHABLE_GOAL_PRIORITY. */
	public static final int UNREACHABLE_GOAL_PRIORITY = 0;
	
	/**
	 * Constructor
	 * 
	 * @param agent
	 *            the agent
	 */
	public MMReasoningModule(Agent agent) {
		this.agent = agent;
		this.rules = new ArrayList<Rule>();
		
		// creates the list of rules
		this.addRules();
	}
	
	@Override
	public void reason() {
		// checks the stack of goals
		this.checkGoalStack();
		
		// applies the rules
		this.applyRules();
		
		// sorts the stack of goals by their priority
		this.sortGoals();
	}
	
	/**
	 * Applies the rules
	 */
	public void applyRules() {
		if (this.rules.isEmpty()) {
			return;
		}
		boolean finished = false;
		
		int i = 0, nbRules = this.rules.size(), nbTestedRules = 0;
		long elapsedTime = 0, startTime = System.currentTimeMillis();
		
		Rule rule = this.rules.get(0);
		rule.reset();
		
		while (!finished) {
			if (rule.isApplicable()) {
				rule.apply();
			}
			
			i = (i + 1) % nbRules;
			rule = this.rules.get(i);
			rule.reset();
			
			nbTestedRules++;
			elapsedTime = System.currentTimeMillis() - startTime;
			
			finished = (nbTestedRules == nbRules) || (elapsedTime > MAX_REASONING_TIME);
		}
	}
	
	/**
	 * Creates the list of rules
	 */
	private void addRules() {
		// add the goals from gauges values
		this.rules.add(new CreateGaugeGoal(this.agent));
		
		// creates goals from perception objects
		this.rules.add(new CreatePerceptionGoal(this.agent));
		
		// create goals from emotions values
		// this.rules.add(new CreateEmotionGoal(this.agent));
	}
	
	/**
	 * Sorts the stack of goal
	 */
	private void sortGoals() {
		this.agent.getMemory().getGoalStack().sortGoalStack();
	}
	
	/**
	 * Check the stack of goals
	 */
	private void checkGoalStack() {
		if (this.agent.getMemory().getGoalStack().isEmpty()) {
			return;
		}
		for (Iterator<MMGoal> it_goal = this.agent.getMemory().getGoalStack().iterator(); it_goal.hasNext();) {
			MMGoal goal = it_goal.next();
			if (goal.isReached() == true) {
				it_goal.remove();
			} else if (goal.isReachable() == false) {
				goal.setPriority(0);
			}
		}
	}
	
	/**
	 * Gets the storage label.
	 * 
	 * @return the storage label
	 */
	@Override
	public String getStorageLabel() {
		return DefaultReasoningModule.class.getCanonicalName();
	}
}
