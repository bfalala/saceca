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

import com.jme3.math.FastMath;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * A class for location functions in Jexl.
 * 
 * @author Sylvain Cambon
 */
public class LocationFunctor {
	
	/** The Constant NAMESPACE. */
	public static final String NAMESPACE = "loc";
	
	/** The context. */
	private ServiceAwareU3duJexlContext context;
	
	/**
	 * Instantiates a new location functor.
	 * 
	 * @param context
	 *            the context
	 */
	public LocationFunctor(ServiceAwareU3duJexlContext context) {
		this.context = context;
	}
	
	/**
	 * Checks if something is on something else. Jexl usage: <code>loc:isOn(what, where)</code>.
	 * 
	 * @param what
	 *            the what
	 * @param where
	 *            the where
	 * @return true, if is on
	 * @throws MalformedObjectException
	 *             If the property "c_Walkable_unit" is absent in the "where" object.
	 */
	public boolean isOn(WorldObject what, WorldObject where) throws MalformedObjectException {
		if (!where.getCategories().contains(new Category("Walkable"))) {
			return false;
		}
		if (this.context.getExecutionMode() == ExecutionMode.REAL) {
			return true;
		}
		Oriented2DPosition whatPosition = what.getPosition();
		Oriented2DPosition wherePosition = where.getPosition();
		float size = 1;
		try {
			// What has to be in the square around the center of what
			size = (float) (where.getPropertiesContainer().getDouble("c_Walkable_unit") / 2);
			float xWhat = whatPosition.x;
			float yWhat = whatPosition.y;
			float xWhere = wherePosition.x;
			float yWhere = wherePosition.y;
			float dx = xWhere - xWhat;
			float dy = yWhere - yWhat;
			return FastMath.abs(dx) < size && FastMath.abs(dy) < size;
		} catch (UnknownPropertyException upe) {
			throw new MalformedObjectException(where, "c_Walkable_unit", upe);
		}
	}
	
	/**
	 * Sets the near. Jexl usage: <code>loc:setNear(what, where)</code>.
	 * 
	 * @param object
	 *            the object
	 * @param destination
	 *            the destination
	 */
	public void setNear(WorldObject object, WorldObject destination) {
		switch (this.context.getExecutionMode()) {
			case VIRTUAL:
				object.setPosition(destination.getPosition());
				break;
			
			case REAL:
				// Otherwise NOP, this is Java-code dependent, and certainly 3D dependent, so
				// nothing is
				// done.
				break;
			
			default:
				break;
		}
		
	}
}