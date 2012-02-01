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
package fr.n7.saceca.u3du.controller.edition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class ObjectKillEditionController.
 */
public class ObjectKillEditionController implements ActionListener {
	
	/** The id. */
	private long id;
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new object kill edition controller.
	 * 
	 * @param id
	 *            the id
	 * @param ew
	 *            the ew
	 */
	public ObjectKillEditionController(long id, EditionWindow ew) {
		this.id = id;
		this.ew = ew;
	}
	
	/**
	 * Action performed.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// We destruct the object
		
		// In the 2D interface
		ObjectKillEditionController.this.ew.removeObject(ObjectKillEditionController.this.id);
		
		// In the AI
		Animation animation = Model.getInstance().getAI().getWorld().getWorldObjects().get(
				ObjectKillEditionController.this.id).getAnimation();
		if (animation != null) {
			animation.pause();
		}
		Model.getInstance().getAI().getWorld().getWorldObjects().get(ObjectKillEditionController.this.id).killThread();
		Model.getInstance().getAI().getWorld().getWorldObjects().remove(ObjectKillEditionController.this.id);
		
		// In the 3D Engine
		// We must wrap the call to JME so that it will be executed from the OpenGL thread
		Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Model.getInstance().getGraphics().getEngine3D()
						.removeObjectOrAgent(ObjectKillEditionController.this.id);
				return null;
			}
		});
		
	}
}
