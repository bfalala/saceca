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
package fr.n7.saceca.u3du.model.graphics.engine3d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.n7.saceca.u3du.model.Model;

/**
 * Represents a graphical agent.
 * 
 * @author Aurélien Chabot
 */
public class GraphicalAgent extends GraphicalDynamicObject {
	
	/** Agent's height of eyes. Default value : 0 */
	private float eyesHeight;
	
	/** The vision distance. */
	private int visionDistance;
	
	/** If true, the vision field is displayed. */
	private boolean displayVisionField;
	
	/** The agent's vision field. If the agent move, the vision field must be updated */
	private GraphicalShape visionField;
	
	/** The animation channel. */
	private AnimChannel animationChannel;
	
	/** The animation control. */
	private AnimControl animationControl;
	
	/** The model of the dead agent */
	private Spatial deadModel;
	
	/** The emitted messages. */
	private Set<String> emmitedMessages;
	
	/**
	 * Empty constructor. The properties have their default values
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
	public GraphicalAgent(long id, Node node, Spatial model, BulletAppState bulletAppState, Engine3D engine3D,
			float moveLength) {
		super(id, node, model, bulletAppState, engine3D, moveLength);
		this.engine3D.dynObjects.remove(this);
		this.engine3D.agents.put(id, this);
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
	 * @param eyesHeight
	 *            the eyes height
	 * @param visionDistance
	 *            the vision distance
	 * @param engine3D
	 *            the engine3 d
	 * @param moveLength
	 *            the move length
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public GraphicalAgent(long id, Node node, Spatial model, BulletAppState bulletAppState, float xPosition,
			float zPosition, float orientation, Vector3f scale, float elevation, float eyesHeight, int visionDistance,
			Engine3D engine3D, float moveLength, float correctiveAngle) {
		
		this(id, node, model, bulletAppState, engine3D, moveLength);
		this.setProperties(xPosition, zPosition, orientation, scale, elevation, correctiveAngle);
		this.setVisionProperties(eyesHeight, visionDistance);
		this.computeVisionField();
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
	 * @param eyesHeight
	 *            the eyes height
	 * @param visionDistance
	 *            the vision distance
	 * @param engine3D
	 *            the engine3 d
	 * @param moveLength
	 *            the move length
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public GraphicalAgent(long id, Node node, Spatial model, BulletAppState bulletAppState, float xPosition,
			float zPosition, float orientation, float scale, float elevation, float eyesHeight, int visionDistance,
			Engine3D engine3D, float moveLength, float correctiveAngle) {
		this(id, node, model, bulletAppState, xPosition, zPosition, orientation, new Vector3f(scale, scale, scale),
				elevation, eyesHeight, visionDistance, engine3D, moveLength, correctiveAngle);
	}
	
	/**
	 * Destruct the agent. The agent and its vision field are removed of its node, and of the
	 * objects Map.
	 */
	@Override
	public void destruct() {
		super.destruct();
		// We remove the agent from the agent list
		this.engine3D.agents.remove(this.getId());
		this.visionField.destruct();
	}
	
	/** The action do to when an agent die */
	public void die() {
		this.getNode().detachChild(this.model);
		this.getNode().attachChild(this.deadModel);
		this.deadModel.setLocalTranslation(this.model.getLocalTranslation().getX(), this.model.getLocalTranslation()
				.getY() - 2.3f, this.model.getLocalTranslation().getZ() + 2.5f);
	}
	
	/** The action to do when the agent is put alive */
	public void live() {
		this.getNode().detachChild(this.deadModel);
		this.getNode().attachChild(this.model);
	}
	
	/** To make the agent disappear */
	public void desappear() {
		this.getNode().detachChild(this.model);
	}
	
	/** The action to do when the agent is put alive */
	public void reappear() {
		this.getNode().attachChild(this.model);
	}
	
	/**
	 * Gets the emitted messages.
	 * 
	 * @return the emitted messages
	 */
	public Set<String> getEmmitedMessages() {
		return this.emmitedMessages;
	}
	
	/**
	 * Checks if is if true, the vision field is displayed.
	 * 
	 * @return the if true, the vision field is displayed
	 */
	public boolean isDisplayVisionField() {
		return this.displayVisionField;
	}
	
	/**
	 * Sets the if true, the vision field is displayed.
	 * 
	 * @param displayVisionField
	 *            the new if true, the vision field is displayed
	 */
	public void setDisplayVisionField(boolean displayVisionField) {
		this.displayVisionField = displayVisionField;
		if (displayVisionField) {
			this.computeVisionField();
			this.visionField.setVisible(true);
		} else {
			this.visionField.setVisible(false);
		}
	}
	
	/**
	 * Sets the physics character control.
	 * 
	 * @param character
	 *            the new physics character control
	 */
	@Override
	public void setCharacter(CharacterControl character) {
		this.control = character;
	}
	
	/**
	 * Gets the physics character control.
	 * 
	 * @return the physics character control
	 */
	@Override
	public CharacterControl getCharacter() {
		return this.control;
	}
	
	/**
	 * Gets the eyes height.
	 * 
	 * @return the eyes height
	 */
	public float getEyesHeight() {
		return this.eyesHeight - this.model.getLocalTranslation().getY();
	}
	
	/**
	 * Sets the eyes height.
	 * 
	 * @param eyesHeight
	 *            the new eyes height
	 */
	public void setEyesHeight(float eyesHeight) {
		this.eyesHeight = eyesHeight + this.model.getLocalTranslation().getY();
	}
	
	/**
	 * Sets the vision distance.
	 * 
	 * @param visionDistance
	 *            the new vision distance
	 */
	public void setVisionDistance(int visionDistance) {
		this.visionDistance = visionDistance;
	}
	
	/**
	 * Set the vision properties.
	 * 
	 * @param eyesHeight
	 *            the eyes height
	 * @param visionDistance
	 *            the vision distance
	 */
	public void setVisionProperties(float eyesHeight, int visionDistance) {
		this.setEyesHeight(eyesHeight + this.model.getLocalTranslation().getY());
		this.setVisionDistance(visionDistance);
	}
	
	/**
	 * Computes the vision field, i.e. the location of the pyramid
	 * 
	 */
	public void computeVisionField() {
		Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				
				// Initial position : the pyramid is in front of the agent
				float pyramidXPosition = GraphicalAgent.this.getXPosition();
				
				float pyramidZPosition = GraphicalAgent.this.visionDistance / 2 + GraphicalAgent.this.getZPosition();
				
				float agentOrientation = GraphicalAgent.this.getOrientation() - GraphicalAgent.this.correctiveAngle;
				
				// We have to turn the pyramid around the agent so that the head of the pyramid
				// is in front of the eyes of the agent
				// It depends of the orientation of the agent
				
				// The center must turn around the agent c the new center will be :
				// x' = cos(theta)*(x-xc) - sin(theta)*(y-yc) + xc
				// y' = sin(theta)*(x-xc) + cos(theta)*(y-yc) + yc
				float newPyramidXPosition = ((pyramidXPosition - GraphicalAgent.this.getXPosition()) * FastMath
						.cos(-agentOrientation))
						- ((pyramidZPosition - GraphicalAgent.this.getZPosition()) * FastMath.sin(-agentOrientation))
						+ GraphicalAgent.this.getXPosition();
				
				float newPyramidZPosition = ((pyramidZPosition - GraphicalAgent.this.getZPosition()) * FastMath
						.cos(-agentOrientation))
						+ ((pyramidXPosition - GraphicalAgent.this.getXPosition()) * FastMath.sin(-agentOrientation))
						+ GraphicalAgent.this.getZPosition();
				
				if (GraphicalAgent.this.visionField == null) {
					Geometry pyramid = new Geometry("pyramid", new Pyramid(GraphicalAgent.this.visionDistance,
							GraphicalAgent.this.visionDistance));
					Material mat = new Material(GraphicalAgent.this.engine3D.getAssetManager(),
							"Common/MatDefs/Misc/WireColor.j3md");
					mat.setColor("m_Color", ColorRGBA.Yellow);
					GraphicalAgent.this.visionField = new GraphicalShape(Config3D.getNewGraphicalInternalId(),
							GraphicalAgent.this.engine3D.getPerceptibleNodes(), pyramid, mat,
							GraphicalAgent.this.engine3D);
					GraphicalAgent.this.visionField.setVisible(false);
				}
				
				// // New coordinates
				GraphicalAgent.this.visionField.setLocalTranslation(newPyramidXPosition,
						GraphicalAgent.this.eyesHeight, newPyramidZPosition);
				
				// New rotation
				GraphicalAgent.this.visionField.setRotation(-FastMath.PI / 2, agentOrientation, 0);
				
				GraphicalAgent.this.visionField.getModel().updateGeometricState();
				GraphicalAgent.this.visionField.getModel().updateModelBound();
				GraphicalAgent.this.engine3D.getRootNode().updateGeometricState();
				return null;
			}
		});
		
	}
	
	/**
	 * Return the vision field. The vision field has to be compute each time the agent move thanks
	 * to the method computeVisionField.
	 * 
	 * @return the vision field
	 */
	public GraphicalShape getVisionField() {
		return this.visionField;
	}
	
	/**
	 * Gets the objects in vision field.
	 * 
	 * @return the objects in vision field
	 */
	public List<Long> getObjectsInVisionField() {
		
		List<Long> objectsInField = new ArrayList<Long>();
		
		Future<List<Long>> f = Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<List<Long>>() {
			@Override
			public List<Long> call() throws Exception {
				Geometry visionField = (Geometry) GraphicalAgent.this.getVisionField().getModel();
				
				// All the viewable objects in the world
				List<Spatial> children = GraphicalAgent.this.engine3D.getPerceptibleNodes().getChildren();
				// Used for the results of the collision algorithm
				CollisionResults collisionResults = new CollisionResults();
				// Contains all the objects in the vision field
				List<Long> objectsInField = new ArrayList<Long>();
				
				// We compute what are the objects the more close of the agent thanks to the
				// objectbound
				for (Spatial s : children) {
					// s.updateModelBound();
					if (visionField.getWorldBound().intersects(s.getWorldBound())) {
						// results contains all the objects near to the agent
						// We compute which of theses objects are in its vision field
						// with a more precise algorithm
						
						// Inside
						// We test if a ray traced from the center of the objectto the agent's
						// position intersects The pyramid representing the vision field
						collisionResults.clear();
						visionField.collideWith(new Ray(s.getLocalTranslation(), GraphicalAgent.this.getModel()
								.getLocalTranslation()), collisionResults);
						if (collisionResults.size() > 0) {
							// There is a result, the object is inside the vision field
							long id = Config3D.getIdFromName(s.getName());
							// If it is not an 3D engine internal object
							if (id >= 0) {
								objectsInField.add(Config3D.getIdFromName(s.getName()));
							}
						}
						// Limits
						// If the object is not inside the vision field, maybe it is at
						// the limits of the
						// Vision field
						else {
							collisionResults.clear();
							visionField.collideWith(s.getWorldBound(), collisionResults);
							if (collisionResults.size() > 0) {
								// There is a result, the objects is on the limit of the
								// vision field
								long id = Config3D.getIdFromName(s.getName());
								// If it is not an 3D engine internal object
								if (id >= 0) {
									objectsInField.add(Config3D.getIdFromName(s.getName()));
								}
							}
						}
					}
				}
				
				return objectsInField;
			}
		});
		
		try {
			objectsInField = f.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return objectsInField;
	}
	
	/**
	 * Gets the vision distance.
	 * 
	 * @return the vision distance
	 */
	public int getVisionDistance() {
		return this.visionDistance;
	}
	
	/**
	 * Inits the.
	 */
	@Override
	public void init() {
		
		
		
		this.control = new CharacterControl(new CapsuleCollisionShape(1.5f, 2f), 0.2f);
		this.model.addControl(this.control);
		this.bulletAppState.getPhysicsSpace().add(this.control);
		
		this.emmitedMessages = new HashSet<String>();
		
		// Dead Model
		this.deadModel = this.engine3D.getAssetManager().loadModel("Models/RIP/rip.mesh.xml");
		this.deadModel.setName(this.model.getName());
		
		// Initialisation du controleur d'animation
		this.animationControl = this.model.getControl(AnimControl.class);
		this.animationChannel = this.animationControl.createChannel();
	}
	
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
	/*
	 * @Override public void onAction(String binding, boolean value, float tpf) {
	 * 
	 * if (binding.equals("agentLeft")) { this.left = value; } else if
	 * (binding.equals("agentRight")) { this.right = value; } else if (binding.equals("agentUp")) {
	 * this.up = value; } else if (binding.equals("agentDown")) { this.down = value; } else if
	 * (binding.equals("agentTest")) { this.test(); }
	 * 
	 * }
	 */
	
	/**
	 * Trigger the animation
	 */
	@Override
	public void animate() {
		
		if (this.direction.length() == 0) {
			if (!"Idle".equals(this.animationChannel.getAnimationName())) {
				this.animationChannel.setAnim("Idle", 0.5f);
			}
		} else {
			this.faceDirection(this.direction);
			
			if (!"Walk".equals(this.animationChannel.getAnimationName())) {
				this.animationChannel.setAnim("Walk", 0.7f);
			}
			
			this.computeVisionField();
			
		}
	}
	
	/**
	 * Test.
	 */
	public void test() {
		this.move(new Vector3f(60, 0, 20));
	}
	
}
