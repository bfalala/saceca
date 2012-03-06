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
package fr.n7.saceca.u3du.model.ai.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * This class contains two kinds of statements:
 * <ul>
 * <li>conditions, to check the preconditions of the execution;</li>
 * <li>effects, to be executed.</li>
 * </ul>
 * Lists are used to ensure the constant ordering of expressions.
 * 
 * @author Sylvain Cambon
 */
@XStreamAlias("statements")
public class StatementsGroup {
	
	/** The conditions. */
	@XStreamAlias("conditions")
	private List<Condition> conditions;
	
	/** The effects. */
	@XStreamAlias("effects")
	private List<Effect> effects;
	
	/**
	 * Instantiates a new statements group.
	 * 
	 * @param conditions
	 *            the conditions
	 * @param effects
	 *            the effects
	 */
	public StatementsGroup(List<Condition> conditions, List<Effect> effects) {
		super();
		this.conditions = conditions;
		this.effects = effects;
	}
	
	/**
	 * Checks the conditions.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @return true, if successful
	 */
	public boolean checkConditions(WorldObject provider, WorldObject consumer, Map<String, Object> parameters) {
		return this.getFalseStatements(provider, consumer, parameters).isEmpty();
	}
	
	/**
	 * Execute the effects statements. For performance, the preconditions check is not performed
	 * again.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @param mode
	 *            the mode
	 */
	public void executeEffects(WorldObject provider, WorldObject consumer, Map<String, Object> parameters,
			ExecutionMode mode) {
		ServiceAwareU3duJexlContext context = new ServiceAwareU3duJexlContext(provider, consumer, parameters, mode);
		ServiceAwareU3duJexlEngine.getInstance().setFunctorsParameters(context);
		for (Statement<?> action : this.effects) {
			action.evaluate(context);
		}
	}
	
	/**
	 * Gets the effects.
	 * 
	 * @return the effects
	 */
	public final List<Effect> getActions() {
		return this.effects;
	}
	
	/**
	 * Gets the statements that are evaluated to false.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 * @param parameters
	 *            the parameters
	 * @return the false statements
	 */
	public Collection<Condition> getFalseStatements(WorldObject provider, WorldObject consumer,
			Map<String, Object> parameters) {
		Collection<Condition> missedTests = new ArrayList<Condition>();
		ServiceAwareU3duJexlContext context = new ServiceAwareU3duJexlContext(provider, consumer, parameters,
				ExecutionMode.REAL);
		ServiceAwareU3duJexlEngine.getInstance().setFunctorsParameters(context);
		for (Condition test : this.conditions) {
			if (!test.check(context)) {
				missedTests.add(test);
			}
		}
		return missedTests;
	}
	
	/**
	 * Gets the conditions.
	 * 
	 * @return the conditions
	 */
	public final List<Condition> getTests() {
		return this.conditions;
	}
	
	/**
	 * Sets the effects.
	 * 
	 * @param actions
	 *            the new actions
	 */
	public final void setActions(List<Effect> actions) {
		this.effects = actions;
	}
	
	/**
	 * Sets the conditions.
	 * 
	 * @param tests
	 *            the new tests
	 */
	public final void setTests(List<Condition> tests) {
		this.conditions = tests;
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatementsGroup\n\tTests:\n");
		for (Condition cond : this.conditions) {
			builder.append("\t\t");
			builder.append(cond);
			builder.append("\n");
		}
		builder.append("\tEffects:\n");
		for (Effect action : this.effects) {
			builder.append("\t\t");
			builder.append(action);
			builder.append("\n");
		}
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.conditions == null) ? 0 : this.conditions.hashCode());
		result = prime * result + ((this.effects == null) ? 0 : this.effects.hashCode());
		return result;
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
		StatementsGroup other = (StatementsGroup) obj;
		if (this.conditions == null) {
			if (other.conditions != null) {
				return false;
			}
		} else if (!this.conditions.containsAll(other.conditions) || !other.conditions.containsAll(this.conditions)) {
			return false;
		}
		if (this.effects == null) {
			if (other.effects != null) {
				return false;
			}
		} else if (!this.effects.equals(other.effects)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Helps to recover the object.
	 * 
	 * @return the statements group
	 */
	public StatementsGroup readResolve() {
		if (this.conditions == null) {
			this.conditions = new ArrayList<Condition>();
		}
		if (this.effects == null) {
			this.effects = new ArrayList<Effect>();
		}
		return this;
	}
}