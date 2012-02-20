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

import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * The Class GraphicalShape.
 */
public class GraphicalShape extends GraphicalObject {
	
	/** The material. */
	private Material material;
	
	/**
	 * Instantiates a new graphical shape.
	 * 
	 * @param id
	 *            the id
	 * @param node
	 *            the node to which the spatial must be attached
	 * @param model
	 *            the model
	 * @param material
	 *            the material
	 * @param engine3D
	 *            the engine3 d
	 */
	public GraphicalShape(long id, Node node, Spatial model/*, Material material*/, Engine3D engine3D) {
		super(id, node, model, engine3D);
		this.material = null;
		/*model.setMaterial(material);*/
		
	}
	
	public GraphicalShape(long id, Node node, Spatial model, Material material, Engine3D engine3D) {
		super(id, node, model, engine3D);
		this.material = null;
		model.setMaterial(material);
		
	}
	
	/**
	 * Gets the material.
	 * 
	 * @return the material
	 */
	public Material getMaterial() {
		return this.material;
	}
	
	/**
	 * Sets the material.
	 * 
	 * @param material
	 *            the new material
	 */
	public void setMaterial(Material material) {
		this.material = material;
		this.getModel().setMaterial(material);
	}
	
}
