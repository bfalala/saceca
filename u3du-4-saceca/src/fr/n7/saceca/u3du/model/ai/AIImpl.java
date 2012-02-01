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
import fr.n7.saceca.u3du.model.console.Console;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;

/**
 * The implantation of the AI interface.
 * 
 * @author Jérôme Dalbert
 */
public class AIImpl implements AI {
	
	/** The world. */
	private World world;
	
	/** The simulation. */
	private Simulation simulation;
	
	/** The console. */
	private Console console;
	
	/** The entities factory. */
	private EntitiesFactory entitiesFactory;
	
	/** The entities factory materials. */
	private EntitiesFactoryMaterials entitiesFactoryMaterials;
	
	/** The IO manager. */
	private IOManager ioManager;
	
	/**
	 * Instantiates a new AI implantation.
	 */
	public AIImpl() {
		this.world = new World();
		this.simulation = new Simulation(this.world);
		this.console = new Console();
		this.ioManager = new IOManager();
	}
	
	/**
	 * Tells the object that its animation is finished.
	 * 
	 * @param finishedAnimation
	 *            the finished animation
	 */
	@Override
	public void animationFinished(Animation finishedAnimation) {
		this.world.getWorldObjects().get(finishedAnimation.getObjectId()).setAnimation(null);
		finishedAnimation.getResult().applyResult();
	}
	
	/**
	 * Gets the world.
	 * 
	 * @return the world
	 */
	@Override
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Send commands.
	 * 
	 * @param commands
	 *            the commands
	 * @return the string
	 * @throws CommandException
	 *             the command exception
	 */
	@Override
	public String sendCommands(String commands) throws CommandException {
		return this.console.executeCommands(commands);
	}
	
	@Override
	public Simulation getSimulation() {
		return this.simulation;
	}
	
	@Override
	public IOManager getIOManager() {
		return this.ioManager;
	}
	
	/**
	 * Gets the entities factory.
	 * 
	 * @return the entities factory
	 */
	@Override
	public final EntitiesFactory getEntitiesFactory() {
		return this.entitiesFactory;
	}
	
	@Override
	public final void setEntitiesFactory(EntitiesFactory entitiesFactory) {
		this.entitiesFactory = entitiesFactory;
	}
	
	@Override
	public final EntitiesFactoryMaterials getEntitiesFactoryMaterials() {
		return this.entitiesFactoryMaterials;
	}
	
	@Override
	public final void setEntitiesFactoryMaterials(EntitiesFactoryMaterials entitiesFactoryMaterials) {
		this.entitiesFactoryMaterials = entitiesFactoryMaterials;
	}
}
