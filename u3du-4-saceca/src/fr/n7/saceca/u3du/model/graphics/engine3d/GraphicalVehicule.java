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

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * The Class GraphicalVehicule.
 */
public class GraphicalVehicule extends GraphicalDynamicObject {
	
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
	public GraphicalVehicule(long id, Node node, Spatial model, BulletAppState bulletAppState, float xPosition,
			float zPosition, float orientation, Vector3f scale, float elevation, Engine3D engine3D,
			float collisionShapeRadius, float collisionShapeHeight, float stepHeight, float moveLength,
			float correctiveAngle) {
		super(id, node, model, bulletAppState, xPosition, zPosition, orientation, scale, elevation, engine3D,
				collisionShapeRadius, collisionShapeHeight, stepHeight, moveLength, correctiveAngle);
		this.epsilon = 1.2f;
	}
	
	/**
	 * Inits the.
	 */
	@Override
	public void init() {
		// Load model and get chassis Geometry
		this.model.setShadowMode(ShadowMode.Cast);
		
		// Create a hull collision shape
		// CollisionShape vehiculeHull = CollisionShapeFactory.createSingleBoxShape(this.model);
		CollisionShape vehiculeHull = new CapsuleCollisionShape(this.collisionShapeRadius, this.collisionShapeHeight);
		
		this.control = new CharacterControl(vehiculeHull, this.stepHeight);
		this.model.addControl(this.control);
		this.bulletAppState.getPhysicsSpace().add(this.control);
		
	}
	
	/** {@inheritDoc} */
	/*
	 * @Override public void onAction(String binding, boolean value, float tpf) {
	 * 
	 * if (binding.equals("vehiculeLeft")) { this.left = value; } else if
	 * (binding.equals("vehiculeRight")) { this.right = value; } else if
	 * (binding.equals("vehiculeUp")) { this.up = value; } else if (binding.equals("vehiculeDown"))
	 * { this.down = value; } else if (binding.equals("vehiculeTest")) { this.test(); }
	 * 
	 * }
	 */
	
	/**
	 * Test.
	 */
	public void test() {
		this.move(new Vector3f(60, 0, 20));
	}
	
	@Override
	public void animate() {
		if (this.direction.length() != 0) {
			this.faceDirection(this.direction);
		}
	}
}
