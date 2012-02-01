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

import javax.swing.event.EventListenerList;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.util.Periodic;
import fr.n7.saceca.u3du.model.util.Periodic.Clock;

/**
 * This class simulates a world evolving in time, with agents and reactive objects inside.
 * 
 * @author Jérôme Dalbert
 */
public class Simulation {
	
	/**
	 * This inner class does the simulation work.
	 */
	public class SimulationThread extends Thread {
		@Override
		public void run() {
			while (!Simulation.this.stop) {
				try {
					// If there is a pause, all the objects are paused, then the thread sleeps
					if (Simulation.this.pause) {
						for (WorldObject object : Simulation.this.world.getWorldObjects().values()) {
							object.setPause(true);
						}
						
						Simulation.this.sleep();
					}
					
					// Otherwise, objects are woken up, i.e. they can do one behavioral loop
					for (WorldObject object : Simulation.this.world.getWorldObjects().values()) {
						object.wakeUp();
					}
					
					// Finally, we increment the time and we sleep during tickPeriod ms
					Simulation.this.time = Periodic.incrementPeriodTime(Simulation.this.time, DAY_MINUTES);
					Simulation.this.fireSimulationChanged();
					Thread.sleep(Simulation.this.getTickPeriod());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (MalformedObjectException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** The Constant DEFAULT_TICK_PERIOD, in ms. */
	public static final long DEFAULT_TICK_PERIOD = 10;
	
	/** The Constant DAY_MINUTES. */
	private static final int DAY_MINUTES = 24 * 60 * (1000 / (int) DEFAULT_TICK_PERIOD);
	
	/** The tick period, in ms. */
	private long tickPeriod;
	
	/** The time in virtual minutes (not real minutes) where 1 virtual minute = 1 real second. */
	private int time;
	
	/** The pause. */
	private boolean pause;
	
	/** The stop. */
	private boolean stop;
	
	/** The world. */
	private World world;
	
	/** The simulation thread. */
	private SimulationThread simulationThread;
	
	/** The listeners of the simulation. */
	private EventListenerList listeners;
	
	/** The Constant EVENT_FIRE_PERIOD. */
	private static final long EVENT_FIRE_PERIOD = 1000;
	
	/** The events clock. */
	private Clock eventsClock;
	
	/**
	 * Instantiates a new simulator.
	 * 
	 * @param world
	 *            the world
	 */
	public Simulation(World world) {
		this.world = world;
		this.tickPeriod = DEFAULT_TICK_PERIOD;
		this.time = 0;
		this.stop = true;
		this.listeners = new EventListenerList();
		this.eventsClock = new Clock(EVENT_FIRE_PERIOD);
	}
	
	/**
	 * Gets the tick period, in ms.
	 * 
	 * @return the tick period, in ms
	 */
	public synchronized long getTickPeriod() {
		return this.tickPeriod;
	}
	
	/**
	 * Sets the tick period, in ms.
	 * 
	 * @param tickPeriod
	 *            the new tick period, in ms
	 */
	public synchronized void setTickPeriod(long tickPeriod) {
		this.tickPeriod = tickPeriod;
	}
	
	/**
	 * Sleeps (with "waits") until someone wakes the thread up (with "notify" or "notifyAll").
	 */
	private synchronized void sleep() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the simulation.
	 */
	public void start() {
		if (this.stop) {
			this.stop = false;
			this.simulationThread = new SimulationThread();
			
			this.simulationThread.start();
		}
	}
	
	/**
	 * Stops the simulation.
	 */
	public void stop() {
		if (!this.stop) {
			this.stop = true;
			
			for (WorldObject object : this.world.getWorldObjects().values()) {
				object.kill();
			}
			
			try {
				this.simulationThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the simulation pause.
	 * 
	 * @param pause
	 *            the new pause
	 */
	public synchronized void setPause(boolean pause) {
		if (pause) {
			this.pause = true;
		} else {
			this.pause = false;
			
			for (WorldObject object : Simulation.this.world.getWorldObjects().values()) {
				object.setPause(false);
			}
			
			this.notifyAll();
		}
	}
	
	/**
	 * Gets the time in virtual minutes (not real minutes) where 1 virtual minute = 1 real second.
	 * 
	 * @return the time
	 */
	public int getTime() {
		return (int) (this.time * DEFAULT_TICK_PERIOD / 1000);
	}
	
	/**
	 * Adds the ai listener.
	 * 
	 * @param simulationListener
	 *            the ai listener
	 */
	public void addSimulationListener(SimulationListener simulationListener) {
		this.listeners.add(SimulationListener.class, simulationListener);
	}
	
	/**
	 * Fire the "Simulation changed" event.
	 */
	private void fireSimulationChanged() {
		// We fire the event if the clock tics
		if (!this.eventsClock.tic()) {
			return;
		}
		
		// We fire the event
		for (SimulationListener listener : this.listeners.getListeners(SimulationListener.class)) {
			listener.worldChanged();
		}
	}
}
