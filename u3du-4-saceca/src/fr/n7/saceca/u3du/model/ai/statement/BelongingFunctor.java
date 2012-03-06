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

import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * A class to add the "has(...)" and "takes(...)" methods to the Jexl Engine.
 * 
 * @author Sylvain Cambon
 */
public class BelongingFunctor {
	
	/** The provider. */
	private WorldObject provider;
	
	/** The consumer. */
	private WorldObject consumer;
	
	/**
	 * Instantiates the new functor.
	 * 
	 * @param provider
	 *            the provider
	 * @param consumer
	 *            the consumer
	 */
	public BelongingFunctor(WorldObject provider, WorldObject consumer) {
		super();
		this.provider = provider;
		this.consumer = consumer;
	}
	
	/**
	 * Checks whether the provider owns an instance of the model.
	 * 
	 * @param modelName
	 *            the model name
	 * @return true, if such an instance is owned
	 */
	public boolean has(String modelName) {
		for (WorldObject object : this.provider.getBelongings()) {
			if (modelName.equals(object.getModelName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the provider owns an instance of the model which verifies the expression. The
	 * prefix to be used in order to represent the object in the expression is "$_". Recursive usage
	 * should be avoided as all the '$' are replaced during the first evaluation.
	 * 
	 * @param modelName
	 *            the model name
	 * @param expressionString
	 *            the expression string to check model instances against.
	 * @return true, if such an instance is owned
	 */
	public boolean has(String modelName, String expressionString) {
		// This trick is a workaround to avoid the special meaning of '$' in
		// regexps
		String correctedString = expressionString.replaceAll("\\$", "this");
		Condition statement = new DefaultCondition(correctedString);
		for (WorldObject object : this.provider.getBelongings()) {
			if (modelName.equals(object.getModelName())) {
				ServiceAwareU3duJexlContext context = new ServiceAwareU3duJexlContext(object);
				if (statement.check(context)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the provider owns an instance of the model. If such an instance is found, the
	 * consumer takes it.
	 * 
	 * @param modelName
	 *            the model name
	 * @return true, if such an instance has been taken
	 */
	public boolean takes(String modelName) {
		for (WorldObject object : this.provider.getBelongings()) {
			if (modelName.equals(object.getModelName())) {
				this.consumer.getBelongings().remove(object);
				this.provider.getBelongings().add(object);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the provider owns an instance of the model which verifies the expression. The
	 * prefix to be used in order to represent the object in the expression is "$_". Recursive usage
	 * should be avoided as all the '$' are replaced during the first evaluation. If such an
	 * instance is found, the consumer takes it.
	 * 
	 * @param modelName
	 *            the model name
	 * @param expressionString
	 *            the expression string to check model instances against.
	 * @return true, if such an instance has been taken
	 */
	public boolean takes(String modelName, String expressionString) {
		// This trick is a workaround to avoid the special meaning of '$' in
		// regexps
		String correctedString = expressionString.replaceAll("\\$", "this");
		Condition statement = new DefaultCondition(correctedString);
		for (WorldObject object : this.provider.getBelongings()) {
			if (modelName.equals(object.getModelName())) {
				ServiceAwareU3duJexlContext context = new ServiceAwareU3duJexlContext(object);
				if (statement.check(context)) {
					this.consumer.getBelongings().remove(object);
					this.provider.getBelongings().add(object);
					return true;
				}
			}
		}
		return false;
	}
}