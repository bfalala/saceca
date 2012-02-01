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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Represents a Graphical object.
 * 
 * @author Anthony Foulfoin & Johann Legaye & Aurélien Chabot
 */
public class GraphicalObject {
	
	/** The children. */
	protected Map<String, GraphicalObject> children;
	
	/** The parent. */
	protected GraphicalObject parent;
	
	/** The precision of the position */
	protected float epsilon = 0.4f;
	
	/** The graphical model. */
	protected Spatial model;
	
	/** The graphical node containing the object. */
	protected Node node;
	
	/** The 3D engine. */
	protected Engine3D engine3D;
	
	/** The id. */
	protected long id;
	
	/** True if the spatial is attached to a node => graphically visible. */
	protected boolean visible;
	
	/** The initial orientation. */
	protected float correctiveAngle;
	
	/**
	 * Empty constructor. The properties have their default values
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node to which the spatial must be attached
	 * @param model
	 *            the model
	 * @param engine3D
	 *            the engine3 d
	 * @param composites
	 *            the composites
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public GraphicalObject(long id, Node node, Spatial model, Engine3D engine3D,
			Map<String, GraphicalObject> composites, GraphicalObject parent) {
		this.node = node;
		this.model = model;
		this.id = id;
		this.engine3D = engine3D;
		model.setName(Config3D.getNewName(id));
		node.attachChild(model);
		engine3D.objects.put(id, this);
		this.visible = true;
		this.children = composites;
		this.parent = parent;
	}
	
	/**
	 * Instantiates a new graphical object.
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node
	 * @param model
	 *            the model
	 * @param engine3D
	 *            the engine3 d
	 */
	public GraphicalObject(long id, Node node, Spatial model, Engine3D engine3D) {
		this.node = node;
		this.model = model;
		this.id = id;
		this.engine3D = engine3D;
		model.setName(Config3D.getNewName(id));
		node.attachChild(model);
		// This may lead to problems in compound graphical objects, as the parts overwrite the ID of
		// the container. Thus the container ID must be set again after its sub elements were
		// created.
		engine3D.objects.put(id, this);
		this.visible = true;
		this.children = new HashMap<String, GraphicalObject>();
		this.parent = null;
	}
	
	/**
	 * If true, the spatial become graphically visible. If false, the spatial become graphically
	 * invisible.
	 * 
	 * @param visible
	 *            If true, the spatial become graphically visible. If false, the spatial become
	 *            graphically invisible.
	 */
	public void setVisible(boolean visible) {
		// if (!this.visible && visible) {
		if (visible) {
			this.node.attachChild(this.model);
			// } else if (this.visible && !visible) {
		} else if (!visible) {
			this.node.detachChild(this.model);
		}
		this.visible = visible;
	}
	
	/**
	 * true if the spatial is graphically visible. False if the spatial is graphically invisible.
	 * 
	 * @return true if the spatial is graphically visible. False if the spatial is graphically
	 *         invisible.
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * Destruct the object. The object is removed of its node, and of the objects Map.
	 */
	public void destruct() {
		for (GraphicalObject child : this.children.values()) {
			child.destruct();
		}
		this.getNode().detachChild(this.model);
		this.engine3D.objects.remove(this.id);
	}
	
	/**
	 * The object id
	 * 
	 * @return the object id
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Sets the properties.
	 * 
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
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public void setProperties(float xPosition, float zPosition, float orientation, Vector3f scale, float elevation,
			float correctiveAngle) {
		for (GraphicalObject child : this.children.values()) {
			child.setProperties(xPosition, zPosition, orientation, scale, elevation, correctiveAngle);
		}
		this.setScale(scale);
		this.setOrientation(orientation);
		this.correctiveAngle = correctiveAngle;
		this.setLocalTranslation(xPosition, elevation, zPosition);
		this.model.updateModelBound();
	}
	
	/**
	 * Sets the properties with the same scale for X, Y and Z.
	 * 
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
	 * @param correctiveAngle
	 *            the corrective angle of the model
	 */
	public void setProperties(float xPosition, float zPosition, float orientation, float scale, float elevation,
			float correctiveAngle) {
		this.setProperties(xPosition, zPosition, orientation, new Vector3f(scale, scale, scale), elevation,
				correctiveAngle);
	}
	
	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	
	public Spatial getModel() {
		return this.model;
	}
	
	/**
	 * Sets the model.
	 * 
	 * @param model
	 *            the new model
	 */
	public void setModel(Spatial model) {
		this.model = model;
	}
	
	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public Node getNode() {
		return this.node;
	}
	
	/**
	 * Sets the node.
	 * 
	 * @param node
	 *            the new node
	 */
	public void setNode(Node node) {
		for (GraphicalObject child : this.children.values()) {
			child.setNode(node);
		}
		this.getNode().detachChild(this.model);
		this.node = node;
		// This add the current object to the node only if visible.
		this.setVisible(this.visible);
	}
	
	/**
	 * Gets the scale.
	 * 
	 * @return the scale
	 */
	public Vector3f getScale() {
		return this.getModel().getLocalScale();
	}
	
	/**
	 * Scale of the object. With a value of 0.5 the object will be 2 times smaller With a value of 2
	 * the object will be 2 times bigger default value : 1
	 * 
	 * @param scale
	 *            the new scale
	 */
	public void setScale(Vector3f scale) {
		for (GraphicalObject child : this.children.values()) {
			child.setScale(scale);
		}
		this.getModel().setLocalScale(scale);
	}
	
	/**
	 * Position of the object on the x axis.
	 * 
	 * @return the x position
	 */
	public float getXPosition() {
		return this.getModel().getLocalTranslation().getX();
	}
	
	/**
	 * The translation needed on the y axis so that the object touch the floor.
	 * 
	 * @return the y position
	 */
	public float getYPosition() {
		return this.getModel().getLocalTranslation().getY();
	}
	
	/**
	 * Position of the object on the z axis.
	 * 
	 * @return the z position
	 */
	public float getZPosition() {
		return this.getModel().getLocalTranslation().getZ();
	}
	
	/**
	 * Sets the x position.
	 * 
	 * @param position
	 *            the new x position
	 */
	public void setXPosition(float position) {
		for (GraphicalObject child : this.children.values()) {
			child.setXPosition(position);
		}
		this.getModel().setLocalTranslation(position, this.getYPosition(), this.getZPosition());
	}
	
	/**
	 * Sets the y position.
	 * 
	 * @param position
	 *            the new y position
	 */
	public void setYPosition(float position) {
		for (GraphicalObject child : this.children.values()) {
			child.setYPosition(position);
		}
		this.getModel().setLocalTranslation(this.getXPosition(), position, this.getZPosition());
	}
	
	/**
	 * Sets the z position.
	 * 
	 * @param position
	 *            the new z position
	 */
	public void setZPosition(float position) {
		for (GraphicalObject child : this.children.values()) {
			child.setZPosition(position);
		}
		this.getModel().setLocalTranslation(this.getXPosition(), this.getYPosition(), position);
	}
	
	/**
	 * Sets the local translation.
	 * 
	 * @param v
	 *            the new local translation
	 */
	public void setLocalTranslation(Vector3f v) {
		for (GraphicalObject child : this.children.values()) {
			child.setLocalTranslation(v);
		}
		this.getModel().setLocalTranslation(v);
	}
	
	/**
	 * Return the local translation
	 * 
	 * @return the local translation
	 */
	public Vector3f getLocalTranslation() {
		return this.model.getLocalTranslation();
	}
	
	/**
	 * Sets the local translation.
	 * 
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param z
	 *            z position
	 */
	public void setLocalTranslation(float x, float y, float z) {
		for (GraphicalObject child : this.children.values()) {
			child.setLocalTranslation(x, y, z);
		}
		this.getModel().setLocalTranslation(x, y, z);
	}
	
	/**
	 * Gets the orientation.
	 * 
	 * @return the orientation
	 */
	public float getOrientation() {
		return this.getRotation()[1];
	}
	
	/**
	 * Sets the orientation.
	 * 
	 * @param orientation
	 *            the new orientation
	 */
	public void setOrientation(float orientation) {
		for (GraphicalObject child : this.children.values()) {
			child.setOrientation(orientation);
		}
		this.setRotation(0, orientation, 0);
	}
	
	/**
	 * Gets the rotation.
	 * http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler
	 * 
	 * @return the rotation, array of size 3 : yaw, roll, pitch
	 */
	public float[] getRotation() {
		return this.getModel().getLocalRotation().toAngles(null);
	}
	
	/**
	 * Set the rotation.
	 * 
	 * @param yaw
	 *            x rotation
	 * @param roll
	 *            y rotation
	 * @param pitch
	 *            z rotation
	 */
	public void setRotation(float yaw, float roll, float pitch) {
		for (GraphicalObject child : this.children.values()) {
			child.setRotation(yaw, roll, pitch);
		}
		float[] angles = new float[3];
		angles[0] = yaw;
		angles[1] = roll;
		angles[2] = pitch;
		this.getModel().setLocalRotation(new Quaternion(angles));
	}
	
	/**
	 * Check if the object is at the position given in the parameters
	 * 
	 * @param x
	 *            x position
	 * @param z
	 *            z position
	 * @return a is at the position
	 */
	public boolean isAtPosition(float x, float z) {
		return FastMath.abs(this.getXPosition() - x) < this.epsilon
				&& FastMath.abs(this.getZPosition() - z) < this.epsilon;
	}
	
	/**
	 * Gets the children. This map is read-only.
	 * 
	 * @return the children
	 */
	public Map<String, GraphicalObject> getChildren() {
		return Collections.unmodifiableMap(this.children);
	}
	
	/**
	 * Adds a child to this graphical object. The child is also attached to the node represented by
	 * this object.
	 * 
	 * @param name
	 *            the name
	 * @param child
	 *            the graphical object to be added.
	 */
	public void addSubElement(String name, GraphicalObject child) {
		this.children.put(name, child);
		child.parent = this;
		child.setNode(this.node);
	}
	
	@Override
	public String toString() {
		return "GraphicalObject [children=" + this.children + ", id=" + this.id + ", visible=" + this.visible + "]";
	}
	
}
