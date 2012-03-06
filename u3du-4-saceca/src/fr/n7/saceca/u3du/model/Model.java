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
package fr.n7.saceca.u3du.model;

import fr.n7.saceca.u3du.model.ai.AI;
import fr.n7.saceca.u3du.model.ai.AIImpl;
import fr.n7.saceca.u3du.model.graphics.Graphics;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;

/**
 * The Class Model.
 * 
 * <p>
 * It is the main class of the <tt>model</tt> package. It represents the model in the MVC pattern.<br>
 * Any class that wants to use services from the <tt>model</tt> package, shall call this class.
 * 
 * <p>
 * One can consider this class as a <i>middleware</i> in that any class can access to it, whether
 * this class is in the <tt>model</tt> package or in any other external package.
 * 
 * <p>
 * In concrete terms, this class gives access to the two main parts of the model:
 * <ul>
 * <li>the artificial intelligence
 * <li>the graphics
 * </ul>
 * 
 * @author JÃ©rÃ´me Dalbert
 */
public final class Model {
	
	/** The instance. */
	private static Model instance = new Model();
	
	/** The graphics. */
	private Graphics graphics;
	
	/** The ai. */
	private AI ai;
	
	/**
	 * Instantiates the model.
	 */
	private Model() {
		this.graphics = new Engine3D();
		this.ai = new AIImpl();
	}
	
	/**
	 * Gets the single instance of Model. Model is a Singleton.
	 * 
	 * @return single instance of Model
	 */
	public static final Model getInstance() {
		return instance;
	}
	
	/**
	 * Gets the graphics.
	 * 
	 * @return the graphics
	 */
	public Graphics getGraphics() {
		return this.graphics;
	}
	
	/**
	 * Sets the graphics.
	 * 
	 * @param graphics
	 *            the new graphics
	 */
	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}
	
	/**
	 * Gets the artificial intelligence.
	 * 
	 * @return the artificial intelligence
	 */
	public AI getAI() {
		return this.ai;
	}
	
	/**
	 * Sets the AI.
	 * 
	 * @param ai
	 *            the new ai
	 */
	public void setAI(AI ai) {
		this.ai = ai;
	}
	
}