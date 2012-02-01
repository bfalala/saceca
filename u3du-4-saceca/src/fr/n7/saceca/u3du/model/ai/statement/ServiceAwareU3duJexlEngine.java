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
package fr.n7.saceca.u3du.model.ai.statement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.JexlEngine;

/**
 * An enhanced JexlEngine with functions:
 * <ul>
 * <li><code>owner:has('modelName')</code> and <code>owner:has('modelName',
 * 'expressionToBeVerifiedByThe$InstanceOfTheModel')</code>;</li>
 * <li><code>receiver:takes('modelName')</code> and <code>receiver:takes('modelName',
 * 'expressionToBeVerifiedByThe$InstanceOfTheModel')</code>;</li>
 * <li><code>eval:string('myStringToBeEvaluated')</code>.</li>
 * <li><code>loc:isOn(what, where)</code>.</li>
 * <li><code>loc:setNear(what, where)</code>.</li>
 * 
 * </ul>
 * 
 * @author Sylvain Cambon
 */
public final class ServiceAwareU3duJexlEngine extends JexlEngine {
	
	/** The engine. */
	private static ServiceAwareU3duJexlEngine engine = null;
	
	/**
	 * Instantiates a new Configured Jexl Engine.
	 */
	private ServiceAwareU3duJexlEngine() {
		super();
	}
	
	/**
	 * Gets the singleton instance of ServiceAwareU3duJexlEngine. The functors may have been
	 * injected before and thus may require to be modified.
	 * 
	 * @return The single instance of ServiceAwareU3duJexlEngine.
	 */
	public synchronized static ServiceAwareU3duJexlEngine getInstance() {
		if (engine == null) {
			engine = new ServiceAwareU3duJexlEngine();
			// Every evaluation performs the whole calculation, to prevent from
			// not recomputing something that may have changed
			engine.setCache(0);
		}
		return engine;
	}
	
	/**
	 * Sets the functors parameters.
	 * 
	 * @param context
	 *            The evaluation context.
	 */
	public void setFunctorsParameters(ServiceAwareU3duJexlContext context) {
		Map<String, Object> conf = new HashMap<String, Object>();
		conf.put(ServiceAwareU3duJexlContext.PROVIDER_PREFIX,
				new BelongingFunctor(context.getProvider(), context.getConsumer()));
		conf.put(ServiceAwareU3duJexlContext.CONSUMER_PREFIX,
				new BelongingFunctor(context.getConsumer(), context.getProvider()));
		conf.put(StringEvaluationFunctor.NAMESPACE, new StringEvaluationFunctor(context));
		conf.put(LocationFunctor.NAMESPACE, new LocationFunctor(context));
		this.setFunctions(conf);
	}
}
