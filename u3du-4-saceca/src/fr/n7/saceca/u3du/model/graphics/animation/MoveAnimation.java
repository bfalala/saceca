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

import javax.vecmath.Point2f;

import com.jme3.math.Vector3f;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Simulation;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * The Class MoveAnimation.
 * 
 * @author Aurélien Chabot
 */
public abstract class MoveAnimation extends Animation {
	
	/**
	 * The Class MoveResult.
	 */
	public class MoveResult extends Result {
		
		/** The final position . */
		private Point2f position;
		
		/**
		 * Instantiates a new move status.
		 * 
		 * @param position
		 *            the position
		 */
		public MoveResult(Point2f position) {
			super();
			this.position = position;
		}
		
		@Override
		public void applyResult() {
			WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects()
					.get(MoveAnimation.this.objectId);
			object.getPosition().x = this.position.x;
			object.getPosition().y = this.position.y;
		}
	}
	
	/** The destination point. */
	protected Point2f destinationPoint;
	
	/** The last update time. */
	protected long lastUpdateTime;
	
	/**
	 * Instantiates a new move animation.
	 * 
	 * @param objId
	 *            the obj id
	 * @param destinationPoint
	 *            the destination point
	 */
	public MoveAnimation(long objId, Point2f destinationPoint) {
		super(objId);
		this.setDestinationPoint(destinationPoint);
		this.lastUpdateTime = System.currentTimeMillis();
	}
	
	/**
	 * Sets the destination point.
	 * 
	 * @param destinationPoint
	 *            the new destination point
	 */
	public void setDestinationPoint(Point2f destinationPoint) {
		this.destinationPoint = destinationPoint;
	}
	
	/**
	 * Gets the destination point.
	 * 
	 * @return the destination point
	 */
	public Point2f getDestinationPoint() {
		return this.destinationPoint;
	}
	
	/**
	 * callback for the ia, to be call after execute.
	 * 
	 * @param position
	 *            the position
	 */
	public void finish(Vector3f position) {
		this.result = new MoveResult(new Point2f(position.x, position.z));
		Model.getInstance().getAI().animationFinished(this);
	}
	
	/**
	 * Updates the AI position periodically.
	 * 
	 * @param position
	 *            the position
	 */
	public void updatePosition(Vector3f position) {
		long currentTime = System.currentTimeMillis();
		
		if (currentTime - this.lastUpdateTime >= Simulation.DEFAULT_TICK_PERIOD) {
			WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(this.objectId);
			object.setPosition(new Oriented2DPosition(position.x, position.z, object.getPosition().theta));
			
			this.lastUpdateTime = System.currentTimeMillis();
		}
	}
	
}
