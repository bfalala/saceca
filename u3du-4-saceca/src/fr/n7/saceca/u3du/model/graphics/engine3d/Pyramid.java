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
// $Id: Pyramid.java 4131 2009-03-19 20:15:28Z blaine.dev $
package fr.n7.saceca.u3du.model.graphics.engine3d;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 * A four sided pyramid.
 * <p>
 * A pyramid is defined by a width at the base and a height. The pyramid is a 4-sided pyramid with
 * the center at (0,0), it will be axis aligned with the peak being on the positive y axis and the
 * base being in the x-z plane.
 * <p>
 * The texture that defines the look of the pyramid has the top point of the pyramid as the top
 * center of the texture, with the remaining texture wrapping around it.
 * 
 * @author Mark Powell
 * @version $Revision: 4131 $, $Date: 2009-03-19 16:15:28 -0400 (Thu, 19 Mar 2009) $
 */
public class Pyramid extends Mesh {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The height. */
	private float height;
	
	/** The width. */
	private float width;
	
	/**
	 * Instantiates a new pyramid.
	 */
	public Pyramid() {
	}
	
	/**
	 * Constructor instantiates a new <code>Pyramid</code> object. The base width and the height are
	 * provided.
	 * 
	 * @param width
	 *            the base width of the pyramid.
	 * @param height
	 *            the height of the pyramid from the base to the peak.
	 */
	public Pyramid(float width, float height) {
		this.updateGeometry(width, height);
	}
	
	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public float getHeight() {
		return this.height;
	}
	
	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public float getWidth() {
		return this.width;
	}
	
	/**
	 * Read.
	 * 
	 * @param e
	 *            the e
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public void read(JmeImporter e) throws IOException {
		super.read(e);
		InputCapsule capsule = e.getCapsule(this);
		this.height = capsule.readFloat("height", 0);
		this.width = capsule.readFloat("width", 0);
	}
	
	/**
	 * Update geometry.
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void updateGeometry(float width, float height) {
		this.width = width;
		this.height = height;
		
		// Update the vertex buffer
		float pkx = 0, pky = height / 2, pkz = 0;
		float vx0 = -width / 2, vy0 = -height / 2, vz0 = -width / 2;
		float vx1 = width / 2, vy1 = -height / 2, vz1 = -width / 2;
		float vx2 = width / 2, vy2 = -height / 2, vz2 = width / 2;
		float vx3 = -width / 2, vy3 = -height / 2, vz3 = width / 2;
		FloatBuffer verts = BufferUtils.createVector3Buffer(16);
		verts.put(new float[] { vx3, vy3, vz3, vx2, vy2, vz2, vx1, vy1, vz1, vx0, vy0, vz0, // base
				vx0, vy0, vz0, vx1, vy1, vz1, pkx, pky, pkz, // side 1
				vx1, vy1, vz1, vx2, vy2, vz2, pkx, pky, pkz, // side 2
				vx2, vy2, vz2, vx3, vy3, vz3, pkx, pky, pkz, // side 3
				vx3, vy3, vz3, vx0, vy0, vz0, pkx, pky, pkz
		// side 4
				});
		verts.rewind();
		this.setBuffer(Type.Position, 3, verts);
		
		// Update the normals buffer
		FloatBuffer norms = BufferUtils.createVector3Buffer(16);
		float pn = 0.70710677f, nn = -0.70710677f;
		norms.put(new float[] { 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, // top
				0, pn, nn, 0, pn, nn, 0, pn, nn, // back
				pn, pn, 0, pn, pn, 0, pn, pn, 0, // right
				0, pn, pn, 0, pn, pn, 0, pn, pn, // front
				nn, pn, 0, nn, pn, 0, nn, pn, 0
		// left
				});
		norms.rewind();
		this.setBuffer(Type.Normal, 3, norms);
		
		// Update the texture buffer
		FloatBuffer texCoords = BufferUtils.createVector2Buffer(16);
		texCoords.put(new float[] { 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0.75f, 0, 0.5f, 1, 0.75f, 0, 0.5f, 0, 0.5f, 1, 0.5f,
				0, 0.25f, 0, 0.5f, 1, 0.25f, 0, 0, 0, 0.5f, 1 });
		texCoords.rewind();
		this.setBuffer(Type.TexCoord, 2, texCoords);
		
		// Update the indices buffer
		IntBuffer indices = BufferUtils.createIntBuffer(18);
		indices.put(new int[] { 3, 2, 1, 3, 1, 0, 6, 5, 4, 9, 8, 7, 12, 11, 10, 15, 14, 13 });
		indices.rewind();
		this.setBuffer(Type.Index, 3, indices);
	}
	
	/**
	 * Write.
	 * 
	 * @param e
	 *            the e
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public void write(JmeExporter e) throws IOException {
		super.write(e);
		OutputCapsule capsule = e.getCapsule(this);
		capsule.write(this.height, "height", 0);
		capsule.write(this.width, "width", 0);
	}
	
}
