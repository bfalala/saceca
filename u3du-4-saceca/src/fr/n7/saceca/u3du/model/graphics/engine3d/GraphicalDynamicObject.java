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
package fr.n7.saceca.u3du.model.graphics.engine3d;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.n7.saceca.u3du.model.graphics.animation.MoveAnimation;

/**
 * The Class GraphicalDynamicObject. For object with dynamic properties
 * 
 * @author AurÃ©lien Chabot
 */
public abstract class GraphicalDynamicObject extends GraphicalObject {
	
	/** The physics engine of the world. */
	protected BulletAppState bulletAppState;
	
	/** The physics character control. */
	protected CharacterControl control;
	
	/** Mouvement. */
	protected boolean left = false, right = false, up = false, down = false;
	
	/** Physics Properties */
	protected float collisionShapeRadius, collisionShapeHeight, stepHeight;
	
	/** The walk direction character. */
	protected Vector3f direction = new Vector3f(0, 0, 0);
	
	/** The animation. */
	protected MoveAnimation moveAnimation = null;
	
	/** The move length, the objet will be move by this lenght each time. */
	private float moveLength;
	
	/**
	 * Instantiates a new graphical dynamic object model.
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node to which the spatial must be attached
	 * @param model
	 *            the model
	 * @param bulletAppState
	 *            the bullet app state
	 * @param engine3D
	 *            the engine3 d
	 * @param moveLength
	 *            the move length
	 */
	public GraphicalDynamicObject(long id, Node node, Spatial model, BulletAppState bulletAppState, Engine3D engine3D,
			float moveLength) {
		super(id, node, model, engine3D);
		this.bulletAppState = bulletAppState;
		this.init();
		this.moveLength = moveLength;
		this.engine3D.dynObjects.put(id, this);
	}
	
	/**
	 * Constructor with custom values.
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node
	 * @param model
	 *            the model
	 * @param bulletAppState
	 *            the bullet app state
	 * @param xPosition
	 *            the x position
	 * @param zPosition
	 *            the z position
	 * @param orientation
	 *            the orientation
	 * @param scale
	 *            the x scale
	 * @param elevation
	 *            the elevation
	 * @param engine3D
	 *            the engine3 d
	 * @param moveLength
	 *            the move length
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public GraphicalDynamicObject(long id, Node node, Spatial model, BulletAppState bulletAppState, float xPosition,
			float zPosition, float orientation, Vector3f scale, float elevation, Engine3D engine3D, float moveLength,
			float correctiveAngle) {
		
		this(id, node, model, bulletAppState, engine3D, moveLength);
		this.setProperties(xPosition, zPosition, orientation, scale, elevation, correctiveAngle);
	}
	
	/**
	 * Constructor with custom values. the same scale for X, Y and Z
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node
	 * @param model
	 *            the model
	 * @param bulletAppState
	 *            the bullet app state
	 * @param xPosition
	 *            the x position
	 * @param zPosition
	 *            the z position
	 * @param orientation
	 *            the orientation
	 * @param scale
	 *            the scale
	 * @param elevation
	 *            the elevation
	 * @param engine3D
	 *            the engine3 d
	 * @param moveLength
	 *            the move length
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public GraphicalDynamicObject(long id, Node node, Spatial model, BulletAppState bulletAppState, float xPosition,
			float zPosition, float orientation, float scale, float elevation, Engine3D engine3D, float moveLength,
			float correctiveAngle) {
		this(id, node, model, bulletAppState, xPosition, zPosition, orientation, new Vector3f(scale, scale, scale),
				elevation, engine3D, moveLength, correctiveAngle);
		
	}
	
	/**
	 * Constructor with custom values
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node
	 * @param model
	 *            the model
	 * @param bulletAppState
	 *            the bullet app state
	 * @param xPosition
	 *            the x position
	 * @param zPosition
	 *            the z position
	 * @param orientation
	 *            the orientation
	 * @param scale
	 *            the scale
	 * @param elevation
	 *            the elevation
	 * @param engine3D
	 *            the engine3 d
	 * @param collisionShapeRadius
	 *            the collision shape radius
	 * @param collisionShapeHeight
	 *            the collision shape height
	 * @param stepHeight
	 *            the step height
	 * @param moveLength
	 *            the move length
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public GraphicalDynamicObject(long id, Node node, Spatial model, BulletAppState bulletAppState, float xPosition,
			float zPosition, float orientation, Vector3f scale, float elevation, Engine3D engine3D,
			float collisionShapeRadius, float collisionShapeHeight, float stepHeight, float moveLength,
			float correctiveAngle) {
		
		super(id, node, model, engine3D);
		this.bulletAppState = bulletAppState;
		this.setPhysicsProperties(collisionShapeRadius, collisionShapeHeight, stepHeight);
		this.init();
		this.setProperties(xPosition, zPosition, orientation, scale, elevation, correctiveAngle);
		this.moveLength = moveLength;
		this.engine3D.dynObjects.put(id, this);
	}
	
	/**
	 * Set the physics properties of the object
	 * 
	 * @param collisionShapeRadius
	 *            the collision shape radius
	 * @param collisionShapeHeight
	 *            the collision shape height
	 * @param stepHeight
	 *            the step height
	 */
	public void setPhysicsProperties(float collisionShapeRadius, float collisionShapeHeight, float stepHeight) {
		this.collisionShapeRadius = collisionShapeRadius;
		this.collisionShapeHeight = collisionShapeHeight;
		this.stepHeight = stepHeight;
	}
	
	/**
	 * Sets the physics character control.
	 * 
	 * @param character
	 *            the new physics character control
	 */
	public void setCharacter(CharacterControl character) {
		this.control = character;
	}
	
	/**
	 * Gets the physics character control.
	 * 
	 * @return the physics character control
	 */
	public CharacterControl getCharacter() {
		return this.control;
	}
	
	/**
	 * Destruct the object. The object is removed of its node, and of the objects Map.
	 */
	@Override
	public void destruct() {
		this.moveAnimation = null;
		super.destruct();
	}
	
	/**
	 * Inits the properties of the object. This function have to create the CharacterControl
	 */
	protected abstract void init();
	
	/**
	 * On action.
	 * 
	 * @param binding
	 *            the binding
	 * @param value
	 *            the value
	 * @param tpf
	 *            the tpf
	 */
	// public abstract void onAction(String binding, boolean value, float tpf);
	
	/** The animation of the mouvement */
	public abstract void animate();
	
	/**
	 * Update the direction with the local direction action
	 */
	public void updateLocalDirection() {
		if (this.left) {
			this.direction.addLocal(new Vector3f(0, 0, 0.1f));
		}
		if (this.right) {
			this.direction.addLocal(new Vector3f(0, 0, -0.1f));
		}
		if (this.up) {
			this.direction.addLocal(new Vector3f(-0.1f, 0, 0));
		}
		if (this.down) {
			this.direction.addLocal(new Vector3f( 0.1f, 0, 0));
		}
	}
	
	/**
	 * Update.
	 * 
	 * @param tps
	 *            the tps
	 */
	public void update(float tps) {
		if (!this.engine3D.isPaused()) {
			this.updateLocalDirection();
			
			this.animate();
			this.control.setWalkDirection(this.direction);
			
			// Reset
			this.direction = new Vector3f(0, 0, 0);
			
			if (this.moveAnimation != null) {
				if (this.moveAnimation.isPaused()) {
					return;
				}
				if (this.isAtPosition(this.moveAnimation.getDestinationPoint().x,
						this.moveAnimation.getDestinationPoint().y)) {
					this.moveAnimation.finish(this.control.getPhysicsLocation());
				} else {
					this.moveAnimation.updatePosition(this.control.getPhysicsLocation());
					// this.move(this.moveAnimation);
					this.engine3D.sendAnimation(this.moveAnimation);
				}
			}
		} else {
			Vector3f savedDirection = this.direction;
			this.direction = new Vector3f(0, 0, 0);
			this.control.setWalkDirection(this.direction);
			this.animate();
			this.direction = savedDirection;
			
			if (this.moveAnimation != null) {
				this.moveAnimation.updatePosition(this.control.getPhysicsLocation());
			}
		}
		
	}
	
	/**
	 * Move.
	 * 
	 * @param moveAnimation
	 *            the move animation
	 */
	public void move(MoveAnimation moveAnimation) {
		
		this.moveAnimation = moveAnimation;
		
		Vector3f direction = this.control
				.getPhysicsLocation()
				.subtract(moveAnimation.getDestinationPoint().x, this.control.getPhysicsLocation().getY(),
						moveAnimation.getDestinationPoint().y).negate();
		
		float length = direction.x * direction.x + direction.y * direction.y + direction.z * direction.z;
		if (length > this.moveLength) {
			length = this.moveLength / FastMath.sqrt(length);
			direction.multLocal(length);
		}
		this.move(direction);
	}
	
	/**
	 * Move.
	 * 
	 * @param direction
	 *            the direction
	 */
	public void move(Vector3f direction) {
		
		this.direction = direction;
		
		// Log.debug("Object (id=" + this.id + ") position actuelle : " +
		// this.control.getPhysicsLocation().toString());
		// Log.debug("Object (id=" + this.id + ") direction : " + this.direction.toString());
	}
	
	@Override
	public float getXPosition() {
		return this.control.getPhysicsLocation().x;
	}
	
	@Override
	public float getYPosition() {
		return this.control.getPhysicsLocation().y;
	}
	
	@Override
	public float getZPosition() {
		return this.control.getPhysicsLocation().z;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setLocalTranslation(Vector3f v) {
		this.control.setPhysicsLocation(v);
	}
	
	/** {@inheritDoc} */
	@Override
	public void setLocalTranslation(float x, float y, float z) {
		this.setLocalTranslation(new Vector3f(x, y, z));
	}
	
	/** {@inheritDoc} */
	@Override
	public void setXPosition(float position) {
		this.setLocalTranslation(new Vector3f(position, this.getYPosition(), this.getZPosition()));
	}
	
	/** {@inheritDoc} */
	@Override
	public void setYPosition(float position) {
		this.setLocalTranslation(new Vector3f(this.getXPosition(), position, this.getZPosition()));
	}
	
	/** {@inheritDoc} */
	@Override
	public void setZPosition(float position) {
		this.setLocalTranslation(new Vector3f(this.getXPosition(), this.getYPosition(), position));
	}
	
	/** {@inheritDoc} */
	@Override
	public void setOrientation(float orientation) {
		this.control.setViewDirection(new Vector3f(FastMath.sin(orientation), 0, FastMath.cos(orientation)));
	}
	
	/**
	 * Face direction.
	 * 
	 * @param direction
	 *            the direction
	 */
	protected void faceDirection(Vector3f direction) {
		float directionOrientation = this.direction.normalize().angleBetween(new Vector3f(0, 0, 1));
		
		if (this.direction.getX() < 0) {
			directionOrientation = -directionOrientation;
		}
		
		this.setOrientation(directionOrientation + this.correctiveAngle);
	}
}