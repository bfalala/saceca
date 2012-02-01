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

import javax.swing.SwingUtilities;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.EntitiesFactory;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalAgent;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalObject;
import fr.n7.saceca.u3du.view.EditionWindow;

/**
 * The Class CreateObjectEditionController.
 */
public class CreateObjectEditionController implements ActionListener {
	
	/** The model. */
	private WorldObjectModel model;
	
	/** The edition window **/
	private EditionWindow ew;
	
	/**
	 * Instantiates a new creates the object edition controller.
	 * 
	 * @param model
	 *            the model
	 * @param ew
	 *            the ew
	 */
	public CreateObjectEditionController(WorldObjectModel model, EditionWindow ew) {
		this.model = model;
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
		// We have to create a new model instance
		// We create an IDProvider
		final EntitiesFactory entitiesFactory = Model.getInstance().getAI().getEntitiesFactory();
		
		// If it is an Agent
		if (this.model instanceof AgentModel) {
			// We must wrap the call to JME so that it will be executed from the OpenGL thread
			Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					// Creating AI agent
					final Agent agent = entitiesFactory.createAgent(CreateObjectEditionController.this.model.getName());
					
					// We continue only if the agent creation worked
					if (agent == null) {
						return null;
					}
					
					// Creating 3D agent
					GraphicalAgent graphicalAgent = Model.getInstance().getGraphics().getEngine3D().addAgent(agent);
					
					// We continue only if the graphical agent creation worked
					if (graphicalAgent == null) {
						return null;
					}
					
					// Adding it to AI objects list
					Model.getInstance().getAI().getWorld().getWorldObjects().put(agent.getId(), agent);
					
					// Creating 2D agent
					CreateObjectEditionController.this.ew.initAgentsList();
					CreateObjectEditionController.this.ew.simulation.initAgentsList();
					
					// Select the new agent
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							CreateObjectEditionController.this.ew.setAgentObjectSelection(agent.getId(), true);
						}
					});
					
					return null;
				}
			});
		} else {
			// We must wrap the call to JME so that it will be executed from the OpenGL thread
			Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					// Creating AI object
					final WorldObject object = entitiesFactory
							.createWorldObject(CreateObjectEditionController.this.model.getName());
					
					// We continue only if the object creation worked
					if (object == null) {
						return null;
					}
					
					// Creating 3D object
					GraphicalObject graphicalObject = Model.getInstance().getGraphics().getEngine3D().addObject(object);
					
					// We continue only if the graphical object creation worked
					if (graphicalObject == null) {
						return null;
					}
					
					// Adding it to AI objects list
					Model.getInstance().getAI().getWorld().getWorldObjects().put(object.getId(), object);
					
					// Creating 2D object
					CreateObjectEditionController.this.ew.initObjectsTree();
					CreateObjectEditionController.this.ew.simulation.initObjectsTree();
					
					// Select the new object
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							CreateObjectEditionController.this.ew.setAgentObjectSelection(object.getId(), true);
						}
					});
					
					return null;
				}
			});
			
		}
	}
}
