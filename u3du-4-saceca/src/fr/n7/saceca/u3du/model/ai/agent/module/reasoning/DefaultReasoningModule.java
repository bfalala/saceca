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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule.ChooseGoalToReach;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule.CreateGaugeGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule.PenalizeUnreachableGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule.RemoveGoalSameGauge;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule.RemoveReachedGoal;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.rule.Rule;

/**
 * The Class DefaultReasoningModule.
 * 
 * @author Jérôme Dalbert
 */
public class DefaultReasoningModule implements ReasoningModule {
	
	/** The Constant NB_MAX_RULE_TESTS. */
	private static final int NB_MAX_RULES_TESTS = 100;
	
	/** The Constant MAX_REASONING_TIME, in ms. */
	private static final int MAX_REASONING_TIME = 1000;
	
	/** The Constant GOAL_TO_REACH_PRIORITY. */
	public static final int TOP_PRIORITY = 100;
	
	/** The Constant MAXIMUM_PRIORITY. */
	public static final int EMERGENCY_PRIORITY = 150;
	
	/** The Constant GAUGE_REFILL. */
	public static final double GAUGE_REFILL = 0.80;
	
	/** The Constant SURVIVAL_GAUGE_THRESHOLD. */
	public static final double SURVIVAL_GAUGE_THRESHOLD = 0.20;
	
	/** The Constant UNREACHABLE_GOAL_PRIORITY. */
	public static final int UNREACHABLE_GOAL_PRIORITY = 0;
	
	/** The Constant UNREACHABLE_GOAL_PRIORITY. */
	public static final int SURROUNDING_DISTANCE = 20;
	
	/** The rules. */
	private List<Rule> rules;
	
	/** The agent. */
	private Agent agent;
	
	/**
	 * Instantiates a new default reasoning module.
	 * 
	 * @param agent
	 *            the agent
	 */
	public DefaultReasoningModule(Agent agent) {
		this.agent = agent;
		this.rules = new ArrayList<Rule>();
		
		this.rules.add(new CreateGaugeGoal(agent));
		this.rules.add(new RemoveGoalSameGauge(agent));
		this.rules.add(new PenalizeUnreachableGoal(agent));
		this.rules.add(new RemoveReachedGoal(agent));
		this.rules.add(new ChooseGoalToReach(agent));
	}
	
	/**
	 * Reason.
	 */
	@Override
	public void reason() {
		this.applyRules();
		
		this.sortGoals();
	}
	
	/**
	 * Apply rules.
	 */
	public void applyRules() {
		if (this.rules.isEmpty()) {
			return;
		}
		
		boolean finished = false;
		int i = 0;
		Rule rule = this.rules.get(0);
		rule.reset();
		int nbRules = this.rules.size();
		int nbUnapplicableRules = 0;
		int nbRulesTests = 0;
		long elapsedTime = 0;
		long startTime = System.currentTimeMillis();
		
		while (!finished) {
			if (rule.isApplicable()) {
				rule.apply();
			} else {
				i = (i + 1) % nbRules;
				rule = this.rules.get(i);
				rule.reset();
				
				if (i == 0) {
					nbUnapplicableRules = 1;
				} else {
					nbUnapplicableRules++;
				}
			}
			
			nbRulesTests++;
			elapsedTime = System.currentTimeMillis() - startTime;
			finished = (nbUnapplicableRules == nbRules || nbRulesTests > NB_MAX_RULES_TESTS || elapsedTime > MAX_REASONING_TIME);
		}
	}
	
	/**
	 * Sort the goals.
	 */
	public void sortGoals() {
		List<Goal> goals = this.agent.getMemory().getGoals();
		if (goals.isEmpty()) {
			return;
		}
		
		Goal previousMaxGoal = goals.get(0);
		
		// We sort the goals by comparing their priority
		Collections.sort(goals, Collections.reverseOrder(new Comparator<Goal>() {
			@Override
			public int compare(Goal g1, Goal g2) {
				return g1.getPriority().compareTo(g2.getPriority());
			}
		}));
		
		// If the top goals priorities are equals, we keep the previous max goal
		// as the first element of the list, so that its plan is not canceled
		if (previousMaxGoal.getPriority().equals(goals.get(0).getPriority())) {
			goals.remove(previousMaxGoal);
			goals.add(0, previousMaxGoal);
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
	
	@Override
	public boolean isAlive() {
		return false;
	}
	
	@Override
	public void start() {
		this.reason();
	}
}
