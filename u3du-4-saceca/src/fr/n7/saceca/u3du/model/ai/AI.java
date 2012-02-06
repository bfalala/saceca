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
package fr.n7.saceca.u3du.model.ai;

import fr.n7.saceca.u3du.model.console.CommandException;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;

/**
 * The Interface AI.
 * 
 * It represents the Artificial Intelligence module.<br>
 * Any external class that wants to use services from the <tt>ai</tt> package, shall call this
 * interface.
 * 
 * @author Jérôme Dalbert
 */
public interface AI {
	
	/**
	 * Gets the AI simulation.
	 * 
	 * @return the simulation
	 */
	public Simulation getSimulation();
	
	/**
	 * Gets the world.
	 * 
	 * @return the world
	 */
	public World getWorld();
	
	/**
	 * Gets the IO manager.
	 * 
	 * @return the IO manager
	 */
	public IOManager getIOManager();
	
	/**
	 * Tells the AI that an animation is finished.
	 * 
	 * @param finishedAnimation
	 *            the finished animation
	 */
	public void animationFinished(Animation finishedAnimation);
	
	/**
	 * Send command.
	 * 
	 * @param commands
	 *            the commands
	 * @return the optional output of the commands
	 * @throws CommandException
	 *             the command exception
	 */
	public String sendCommands(String commands) throws CommandException;
	
	/**
	 * Sets the entities factory materials.
	 * 
	 * @param entitiesFactoryMaterials
	 *            the new entities factory materials
	 */
	public abstract void setEntitiesFactoryMaterials(EntitiesFactoryMaterials entitiesFactoryMaterials);
	
	/**
	 * Gets the entities factory materials.
	 * 
	 * @return the entities factory materials
	 */
	public abstract EntitiesFactoryMaterials getEntitiesFactoryMaterials();
	
	/**
	 * Sets the entities factory.
	 * 
	 * @param entitiesFactory
	 *            the new entities factory
	 */
	public abstract void setEntitiesFactory(EntitiesFactory entitiesFactory);
	
	/**
	 * Gets the entities factory.
	 * 
	 * @return the entities factory
	 */
	public abstract EntitiesFactory getEntitiesFactory();
	
	public void initAI();
	
}
