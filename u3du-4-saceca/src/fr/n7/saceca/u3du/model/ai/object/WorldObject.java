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
package fr.n7.saceca.u3du.model.ai.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.vecmath.Vector2f;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.behavior.Behavior;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertiesContainer;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.util.MathUtil;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * The Class WorldObject.
 * 
 * It represents a world object, or an entity. So it gathers reactive objects and agents (who will
 * extend this class).<br/>
 * 
 * Its main attributes are an id, a behavior (that is periodically called by the behavioral thread),
 * a behavioral thread, a model name, some properties, the services the world object can offer, a
 * position, whether it is alive, and the current possible animation.
 * 
 * @author JÃ©rÃ´me Dalbert
 * 
 */
@XStreamAlias("object")
public class WorldObject {
	
	/**
	 * The Class BehavioralThread.
	 */
	private class BehavioralThread extends Thread {
		
		/**
		 * Instantiates a new behavioral thread and names it according to the owning object.
		 */
		public BehavioralThread() {
			super();
			this.setName(WorldObject.this.toShortString() + " thread");
		}
		
		/**
		 * This method represent the classic AI "behavioral loop" in that it calls the behavior
		 * periodically.
		 */
		@Override
		public void run() {
			while (WorldObject.this.alive) {
				WorldObject.this.behave();
				WorldObject.this.sleep();
			}
		}
	}
	
	/** The id. */
	protected long id;
	
	/** The behavior. */
	protected Behavior behavior;
	
	/** The behavioral thread. */
	private BehavioralThread behavioralThread;
	
	/** The belongings. */
	private Collection<WorldObject> belongings;
	
	/** The model. */
	private String modelName;
	
	/** The properties container. */
	private PropertiesContainer propertiesContainer;
	
	/** The categories. */
	private Collection<Category> categories;
	
	/** The services the world object can offer. */
	private Collection<Service> services;
	
	/** The concepts representing the world object */
	private Collection<String> concepts;
	
	/** The position. */
	private Oriented2DPosition position;
	
	/** The pause. */
	protected boolean pause;
	
	/** The alive. */
	protected boolean alive;
	
	/** The current animation. */
	protected Animation animation;
	
	/** The Constant WALKABLE. */
	public static final Category WALKABLE = new Category("Walkable");
	
	/** The Constant HUMAN. */
	public static final Category HUMAN = new Category("Human");
	
	/** The Constant VEHICLE. */
	public static final Category VEHICLE = new Category("Vehicle");
	
	/**
	 * Instantiates a new object.
	 * 
	 * @param modelName
	 *            the model name
	 * @param id
	 *            the id
	 */
	public WorldObject(String modelName, long id) {
		this(modelName, id, true);
	}
	
	/**
	 * Instantiates a new world object.
	 * 
	 * @param modelName
	 *            the model name
	 * @param id
	 *            the id
	 * @param initContent
	 *            Whether the internal attributes should be initialized.
	 */
	private WorldObject(String modelName, long id, boolean initContent) {
		this.modelName = modelName;
		this.id = id;
		this.pause = false;
		this.alive = true;
		this.animation = null;
		
		if (initContent) {
			this.propertiesContainer = new PropertiesContainer();
			this.belongings = new LinkedList<WorldObject>();
			this.categories = new ArrayList<Category>();
			this.services = new LinkedList<Service>();
			this.concepts = new ArrayList<String>();
		}
	}
	
	/**
	 * Behave.
	 */
	private void behave() {
		if (this.behavior != null) {
			this.behavior.behave();
		}
	}
	
	/**
	 * Gets the behavior.
	 * 
	 * @return the behavior
	 */
	public Behavior getBehavior() {
		return this.behavior;
	}
	
	/**
	 * Gets the belongings.
	 * 
	 * @return the belongings
	 */
	public final Collection<WorldObject> getBelongings() {
		return this.belongings;
	}
	
	/**
	 * Gets the categories.
	 * 
	 * @return the categories
	 */
	public final Collection<Category> getCategories() {
		return this.categories;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public final String getModelName() {
		return this.modelName;
	}
	
	/**
	 * Gets the properties container.
	 * 
	 * @return the properties container.
	 */
	public final PropertiesContainer getPropertiesContainer() {
		return this.propertiesContainer;
	}
	
	/**
	 * Gets the services.
	 * 
	 * @return the services
	 */
	public final Collection<Service> getServices() {
		return this.services;
	}
	
	/**
	 * Checks if is the pause.
	 * 
	 * @return the pause
	 */
	public boolean isPause() {
		return this.pause;
	}
	
	/**
	 * Sets the behavior.
	 * 
	 * @param behavior
	 *            the new behavior
	 */
	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
	
	/**
	 * Sets the belongings.
	 * 
	 * @param belongings
	 *            the new belongings
	 */
	public final void setBelongings(Collection<WorldObject> belongings) {
		this.belongings = belongings;
	}
	
	/**
	 * Sets the categories.
	 * 
	 * @param categories
	 *            the new categories
	 */
	public final void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Sets the model.
	 * 
	 * @param modelName
	 *            the new model
	 */
	public final void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	/**
	 * Sets the pause.
	 * 
	 * @param pause
	 *            the new pause
	 */
	public synchronized void setPause(boolean pause) {
		if (pause) {
			this.pause = true;
		} else {
			this.pause = false;
			this.notifyAll();
		}
	}
	
	/**
	 * Sets the properties.
	 * 
	 * @param propertiesContainer
	 *            the new properties container
	 */
	public final void setPropertiesContainer(PropertiesContainer propertiesContainer) {
		this.propertiesContainer = propertiesContainer;
	}
	
	/**
	 * Sets the services.
	 * 
	 * @param services
	 *            the new services
	 */
	public final void setServices(Collection<Service> services) {
		this.services = services;
	}
	
	/**
	 * Sleep.
	 */
	public synchronized void sleep() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			; // The object got interrupted by the kill() method, there is nothing special to do
		}
	}
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.toShortString());
		builder.append(" | ");
		builder.append(this.getPosition().toString());
		builder.append('\n');
		if (this.behavior != null) {
			builder.append(this.behavior.getClass().getCanonicalName());
		}
		builder.append("\nCategories:");
		for (Category category : this.categories) {
			builder.append("\n\t");
			builder.append(category.getName());
		}
		builder.append("\nServices:");
		for (Service service : this.services) {
			builder.append("\n\t");
			builder.append(service.getName());
		}
		builder.append('\n');
		builder.append(this.propertiesContainer);
		builder.append("\nBelongings:");
		for (WorldObject wo : this.belongings) {
			builder.append("\n\t");
			builder.append(wo.toShortString());
		}
		return builder.toString();
	}
	
	/**
	 * Wake up.
	 * 
	 * @throws MalformedObjectException
	 *             If the property "i_threaded" cannot be found.
	 */
	public synchronized void wakeUp() throws MalformedObjectException {
		try {
			if (this.getPropertiesContainer().getBoolean(Internal.Object.THREADED)) {
				// We create a new thread if needed
				if (this.alive && (this.behavioralThread == null || !this.behavioralThread.isAlive())) {
					this.behavioralThread = new BehavioralThread();
					this.behavioralThread.start();
				}
				
				// We tell the thread to stop waiting
				this.notifyAll();
			}
		} catch (UnknownPropertyException upe) {
			throw new MalformedObjectException(this, "i_threaded", upe);
		}
	}
	
	/**
	 * Kills the world object.
	 */
	public void kill() {
		this.killThread();
	}
	
	/**
	 * Kills the world object thread.
	 */
	public void killThread() {
		if (this.alive) {
			this.alive = false;
			
			if (this.behavioralThread != null) {
				// We interrupt the BehavioralThread in case it was sleeping
				this.behavioralThread.interrupt();
			}
		}
	}
	
	/**
	 * Revive.
	 */
	public void revive() {
		if (!this.alive) {
			this.alive = true;
		}
	}
	
	/**
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public synchronized Oriented2DPosition getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the position.
	 * 
	 * @param position
	 *            the new position
	 */
	public synchronized void setPosition(Oriented2DPosition position) {
		this.position = position;
	}
	
	/**
	 * Gets the empty clone of of the object. It only contains the id and the model name.
	 * 
	 * @return the empty clone
	 */
	public WorldObject getEmptyClone() {
		return new WorldObject(this.modelName, this.id);
	}
	
	/**
	 * Creates a deep data clone. This clone has the same categories and services as well as clones
	 * of its properties and belongings.
	 * 
	 * @return the world object
	 */
	public WorldObject deepDataClone() {
		WorldObject object = new WorldObject(this.getModelName(), this.getId());
		object.setBehavior(null);
		object.setPause(true);
		
		// Belongings
		Collection<WorldObject> belongings = new ArrayList<WorldObject>();
		for (WorldObject belonging : this.belongings) {
			belongings.add(belonging.deepDataClone());
		}
		object.setBelongings(belongings);
		
		// Categories: instances are shared among all the instances and thus the collection only
		// needs copying, not its elements
		Collection<Category> categories = new ArrayList<Category>();
		for (Category category : this.categories) {
			categories.add(category);
		}
		object.setCategories(categories);
		
		// Position
		object.setPosition(this.position.clone());
		
		//
		object.setPropertiesContainer(this.propertiesContainer.clone());
		
		// Services: instances are shared among all the instances and thus the collection only
		// needs copying, not its elements.
		Collection<Service> services = new ArrayList<Service>();
		for (Service service : this.services) {
			services.add(service);
		}
		object.setServices(services);
		
		return object;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		result = prime * result + ((this.modelName == null) ? 0 : this.modelName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		WorldObject other = (WorldObject) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.modelName == null) {
			if (other.modelName != null) {
				return false;
			}
		} else if (!this.modelName.equals(other.modelName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the object is walkable.
	 * 
	 * @return true, if it is walkable
	 */
	public boolean isWalkable() {
		return this.categories.contains(WALKABLE);
	}
	
	/**
	 * Sets the alive.
	 * 
	 * @param alive
	 *            the new alive
	 */
	public final void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/**
	 * check if alive.
	 * 
	 * @return true, if it is alive
	 */
	public boolean isAlive() {
		return this.alive;
	}
	
	/**
	 * A short string representation of the object, of the form "Object <ModelName> (id=<id>)".
	 * 
	 * @return the short string
	 */
	public String toShortString() {
		return "Object " + this.modelName + " (id=" + this.id + ")";
	}
	
	/**
	 * Helps to rebuild the object.
	 * 
	 * @return the corrected world object
	 */
	public WorldObject readResolve() {
		if (this.belongings == null) {
			this.belongings = new ArrayList<WorldObject>();
		}
		if (this.categories == null) {
			this.categories = new ArrayList<Category>();
		}
		if (this.services == null) {
			this.services = new ArrayList<Service>();
		}
		return this;
	}
	
	/**
	 * Gets the current animation.
	 * 
	 * @return the current animation
	 */
	public Animation getAnimation() {
		return this.animation;
	}
	
	/**
	 * Sets the current animation.
	 * 
	 * @param animation
	 *            the new current animation
	 */
	public synchronized void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	/**
	 * Interrupts the current animation, if it exists.
	 */
	public void interruptAnimation() {
		if (this.animation != null) {
			this.animation.pause();
		}
		
		this.setAnimation(null);
	}
	
	/**
	 * Gets the initial direction of the world object (based on its theta).
	 * 
	 * @return the initial direction
	 */
	public Vector2f getInitialDirection() {
		return MathUtil.rotate(MathUtil.UNIT_Y, this.position.theta);
	}
	
	/**
	 * Gets the concept list (for the markov method)
	 * 
	 * @return the concept list
	 * */
	public Collection<String> getConcepts() {
		return this.concepts;
	}
	
	/**
	 * Sets the collection of concepts
	 * 
	 * @param concepts
	 *            collection of concepts
	 * */
	public void setConcepts(Collection<String> concepts) {
		this.concepts = concepts;
	}
	
}