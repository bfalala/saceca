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
package fr.n7.saceca.u3du.model.graphics.animation;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalAgent;

/**
 * The Class DieAnimation.
 * 
 * @author Aurélien Chabot & Jérôme Dalbert
 */
public class DieAnimation extends Animation {
	
	/**
	 * Instantiates a new die animation.
	 * 
	 * @param objId
	 *            the obj id
	 */
	public DieAnimation(long objId) {
		super(objId);
	}
	
	/**
	 * Execute.
	 */
	@Override
	public void execute() {
		Engine3D engine3d = Model.getInstance().getGraphics().getEngine3D();
		GraphicalAgent graphicalAgent = engine3d.agents.get(this.objectId);
		graphicalAgent.die();
	}
	
}