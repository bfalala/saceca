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

/**
 * The Class Animation.
 * 
 * It is used to :
 * <ul>
 * <li>send the Graphics the order to execute this animation.<br/>
 * <li>send the animation back to the IA (with a result) when the Graphics estimates the animation
 * is finished.
 * </ul>
 * 
 * @author Aurélien Chabot & Jérôme Dalbert
 */
public abstract class Animation {
	
	/**
	 * The Class Result.
	 */
	public class Result {
		
		/**
		 * Apply the result.
		 */
		public void applyResult() {
		}
		
	}
	
	/** The obj id. */
	protected long objectId;
	
	/** The result of the animation. */
	protected Result result;
	
	/** The stop. */
	private boolean pause;
	
	/**
	 * Instantiates a new animation.
	 * 
	 * @param objectId
	 *            the object id
	 */
	public Animation(long objectId) {
		this.objectId = objectId;
	}
	
	/**
	 * Execute.
	 */
	public abstract void execute();
	
	/**
	 * Gets the obj id.
	 * 
	 * @return the obj id
	 */
	public long getObjectId() {
		return this.objectId;
	}
	
	/**
	 * Sets the obj id.
	 * 
	 * @param objectId
	 *            the new obj id
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	
	/**
	 * callback for the ia, to be call after execute.
	 * 
	 */
	public void finish() {
		this.result = new Result();
		Model.getInstance().getAI().animationFinished(this);
	}
	
	/**
	 * Gets the result of the animation.
	 * 
	 * @return the result of the animation
	 */
	public Result getResult() {
		return this.result;
	}
	
	/**
	 * Pauses the animation.
	 */
	public synchronized void pause() {
		this.pause = true;
	}
	
	/**
	 * Resumes the animation.
	 */
	public synchronized void resume() {
		this.pause = false;
	}
	
	/**
	 * Checks if is stopped.
	 * 
	 * @return true, if is stopped
	 */
	public synchronized boolean isPaused() {
		return this.pause;
	}
}
