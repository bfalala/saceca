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

import java.util.concurrent.Callable;

import javax.swing.SwingUtilities;

import com.jme3.input.controls.AnalogListener;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.engine3d.Config3D.CameraKey;

/**
 * Defines the controller of the 3D part.
 * 
 * @author Sylvain Cambon & Jérome Dalbert & Anthony Foulfoin & Johann Legaye & Aurélien Chabot
 */
public class Controller3D implements AnalogListener, com.jme3.input.controls.ActionListener {
	
	/** A reference on the 3D main class. */
	private Engine3D engine3D;
	
	/** The mouse pressed boolean. */
	private boolean mousePressed;
	
	/** The selected object. */
	private Long selectedObject;
	
	/** True if user is using the brushMode **/
	private boolean brushMode;
	
	/** Id of the last object created with the brush **/
	private Long brushedObject;
	
	/**
	 * Instantiates a new controller3 d.
	 * 
	 * @param engine
	 *            the engine
	 */
	public Controller3D(Engine3D engine) {
		this.engine3D = engine;
		this.mousePressed = false;
		this.brushMode = false;
	}
	
	/**
	 * 
	 * If an action happened.
	 * 
	 * @param name
	 *            the name of the event
	 * @param keyPressed
	 *            true is the key is pressed
	 * @param tpf
	 *            The time per frame
	 */
	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		if (name.equals("MouseLeftClic")) {
			// If the mouse key is pressed, we memorize it, and then we get the selected object
			if (keyPressed) {
				this.mousePressed = true;
				this.selectedObject = this.engine3D.getPickedObject();
			}
			// If the mouse key is released, we memorize it, and then we call the mousePicking
			// method that will
			// inform the picking observers that an object has been picked (if the selected object
			// is not null)
			if (!keyPressed) {
				this.engine3D.notifyMousePicking(this.selectedObject);
				this.mousePressed = false;
			}
		}
		// If user press the control key in the edition mode, while the mouse key is pressed, and an
		// object is selected, we have to rotate the selected object
		if (this.engine3D.visualisationMode == Config3D.VisualisationMode.EDITION && name.equals("CTRL_key")
				&& !keyPressed && this.selectedObject != null) {
			this.engine3D.rotate(this.selectedObject);
		}
		
		if (name.equals("SHIFT_key")) {
			if (keyPressed) {
				// We enable the brushMode
				this.brushMode = true;
			} else {
				// We disable the brushMode
				this.brushMode = false;
				// the latest brushed object become the current selected object
				this.selectedObject = this.brushedObject;
				this.brushedObject = null;
			}
		}
		
		if (name.equals("DEL_key")) {
			if (!keyPressed && this.engine3D.visualisationMode == Config3D.VisualisationMode.EDITION
					& this.selectedObject != null) {
				
				// REMOVE THE OBJECT
				
				// Wrap the call for 2D view
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						
						WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(
								Controller3D.this.selectedObject);
						
						// If the object exists in the AI (the object may exists only in the 3D)
						if (object != null) {
							// In the 2D interface
							Controller3D.this.engine3D.notifyNewOrRemovedObject(Controller3D.this.selectedObject, true);
							
							// In the AI
							object.killThread();
							Animation animation = object.getAnimation();
							if (animation != null) {
								animation.pause();
							}
							Model.getInstance().getAI().getWorld().getWorldObjects().remove(
									Controller3D.this.selectedObject);
						}
						
						// We must wrap the call to JME so that it will be executed from the OpenGL
						// thread
						Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
							public Void call() throws Exception {
								
								// In the 3D Engine
								Model.getInstance().getGraphics().getEngine3D().removeObjectOrAgent(
										Controller3D.this.selectedObject);
								return null;
							}
						});
						
					}
				});
				
			}
		}
	}
	
	/**
	 * Method that is used to brush a model
	 */
	public void brush() {
		// if a brushed object has already been created and is not yet correctly placed
		// We ask to engine3D to move the latest brushedObject according to the mouse position
		if (this.brushedObject != null) {
			boolean placed = this.engine3D.brush(this.selectedObject, this.brushedObject);
			// If engine3D tells that the object has been correctly placed
			// The brushedObject become the current selected Object
			if (placed) {
				this.selectedObject = this.brushedObject;
				this.brushedObject = null;
			}
		}
		// If there is no brushed object to place, we create a new one according to the selected
		// object
		else {
			// We make a copy of the selected object
			
			// ORIGINAL OBJECT
			GraphicalObject selectedGraphicalObject = this.engine3D.objects.get(this.selectedObject);
			WorldObject selectedWorldObject = Model.getInstance().getAI().getWorld().getWorldObjects().get(
					this.selectedObject);
			
			// If the selected object doesn't exist in the AI we abort
			if (selectedWorldObject == null) {
				Engine3D.logger.error("The object must exist in the AI");
				return;
			}
			// The selected object must be an instance of GraphicalShape
			if (selectedGraphicalObject instanceof GraphicalShape) {
				
				// we make an AI COPY
				String modelName = selectedWorldObject.getModelName();
				WorldObject newWorldObject = Model.getInstance().getAI().getEntitiesFactory().createWorldObject(
						modelName);
				
				// At creation, the new object has the same position and orientation
				newWorldObject.getPosition().x = selectedWorldObject.getPosition().x;
				newWorldObject.getPosition().y = selectedWorldObject.getPosition().y;
				newWorldObject.getPosition().theta = selectedWorldObject.getPosition().theta;
				
				// Adding it to AI objects list
				Model.getInstance().getAI().getWorld().getWorldObjects().put(newWorldObject.getId(), newWorldObject);
				
				try {
					// GRAPHICAL COPY
					this.brushedObject = this.engine3D.getFactory().createGraphicalObject(newWorldObject).getId();
					// move the object on mouse position
					this.engine3D.brush(this.selectedObject, this.brushedObject);
					// We notify the observers (2D view) that a new object has been created
					this.engine3D.notifyNewOrRemovedObject(this.brushedObject, false);
				} catch (MalformedObjectException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * If an action happened.
	 * 
	 * @param name
	 *            the name of the event
	 * @param value
	 *            more the key has been pressed, more the value is big
	 * @param tpf
	 *            The time per frame
	 */
	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (name.equals("FLYCAM_StrafeUp")) {
			this.engine3D.moveCameraKey(tpf, CameraKey.UP);
		}
		if (name.equals("FLYCAM_StrafeDown")) {
			this.engine3D.moveCameraKey(tpf, CameraKey.DOWN);
		}
		// If we are in the edition mode and the mouse key is pressed, and an object is selected, we
		// have to move the selected object according to the mouse position
		if (this.engine3D.visualisationMode == Config3D.VisualisationMode.EDITION && name.equals("MouseMove")
				&& this.mousePressed && this.selectedObject != null && !this.brushMode) {
			this.engine3D.moveObjectOnMousePosition(this.selectedObject);
		}
		// Brush mode
		// If user is in edition mode, if the mouse has been moved, an object is selected, and
		// brushMode enabled
		if (this.engine3D.visualisationMode == Config3D.VisualisationMode.EDITION && name.equals("MouseMove")
				&& this.selectedObject != null && this.brushMode) {
			this.brush();
		}
	}
}
