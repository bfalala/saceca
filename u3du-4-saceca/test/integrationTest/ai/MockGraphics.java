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
package integrationTest.ai;

import java.util.ArrayList;
import java.util.List;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.graphics.Graphics;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.animation.MoveHumanAnimation;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

public class MockGraphics implements Graphics {
	
	private List<Long> visionField = new ArrayList<Long>();
	
	@Override
	public List<Long> getObjectsInVisionField(Long agent) {
		return this.visionField;
	}
	
	@Override
	public void updateOrientation(Long object, float theta) {
		System.out.println("Object (id=" + object + "): updated orientation to=" + theta);
	}
	
	@Override
	public void sendAnimation(final Animation animation) {
		System.out.println("Object (id=" + animation.getObjectId() + ") : animation started.");
		System.out.println("Object (id=" + animation.getObjectId() + ") : animation finished, callback!");
		if (animation instanceof MoveHumanAnimation) {
			MoveHumanAnimation animation2 = (MoveHumanAnimation) animation;
			Oriented2DPosition newPosition = (Oriented2DPosition) animation2.getDestinationPoint();
			WorldObject worldObject = Model.getInstance().getAI().getWorld().getWorldObjects()
					.get(animation2.getObjectId());
			worldObject.setPosition(newPosition);
			((Agent) worldObject).getMemory().getKnowledgeAbout(worldObject).setPosition(newPosition);
			worldObject.setAnimation(null);
		} else {
			System.out.println("Animation of class: " + animation.getClass().getCanonicalName() + " executed");
			// animation.callback();
		}
	}
	
	@Override
	public void showMessage(Long emitter, String message) {
		System.out.println("Message: " + message);
	}
	
	@Override
	public Engine3D getEngine3D() {
		return null;
	}
	
	public List<Long> getVisionField() {
		return this.visionField;
	}
	
	public void setVisionField(List<Long> visionField) {
		this.visionField = visionField;
	}
	
}
