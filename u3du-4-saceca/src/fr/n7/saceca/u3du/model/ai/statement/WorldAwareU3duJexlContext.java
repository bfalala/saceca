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

import java.util.Map;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;

/**
 * A Jexl context accepting the "id<myId>_<myProperty>" syntax. The world objects are those presents
 * in the world.
 * 
 * @author Sylvain Cambon
 */
public class WorldAwareU3duJexlContext extends IdBasedU3duJexlContext {
	
	@Override
	protected Map<Long, WorldObject> getWorldObjectMap() {
		return Model.getInstance().getAI().getWorld().getWorldObjects();
	}
	
}
