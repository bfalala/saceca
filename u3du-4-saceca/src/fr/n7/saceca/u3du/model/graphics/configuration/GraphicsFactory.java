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
package fr.n7.saceca.u3du.model.graphics.configuration;

import org.apache.log4j.Logger;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.graphics.engine3d.Engine3D;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalAgent;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalObject;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalShape;
import fr.n7.saceca.u3du.model.graphics.engine3d.GraphicalVehicule;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;
import fr.n7.saceca.u3du.util.Log;

/**
 * A factory to create graphical objects.
 */
public class GraphicsFactory {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(GraphicsFactory.class);
	
	/** The materials. */
	private GraphicsFactoryMaterials materials;
	
	/**
	 * Instantiates a new graphics factory.
	 * 
	 * @param materials
	 *            the materials
	 */
	public GraphicsFactory(GraphicsFactoryMaterials materials) {
		super();
		this.materials = materials;
	}
	
	/**
	 * Creates a new Graphical Agent object.
	 * 
	 * @param aiAgent
	 *            the ai agent
	 * @return the graphical agent
	 * @throws MalformedObjectException
	 *             If a required property is missing in the agent.
	 */
	public GraphicalAgent createGraphicalAgent(Agent aiAgent) throws MalformedObjectException {
		String configurationName = null;
		try {
			configurationName = aiAgent.getPropertiesContainer().getString(Internal.Agent.GRAPHICS_CONF);
		} catch (UnknownPropertyException upe) {
			throw new MalformedObjectException(aiAgent, Internal.Agent.GRAPHICS_CONF, upe);
		}
		AgentConfiguration agentConfiguration = this.materials.getAgentConfigurationRepository().get(configurationName);
		if (agentConfiguration == null) {
			logger.warn("No graphical agent configuration named\"" + configurationName
					+ "\" can be found. Creation of this object escaped.");
			return null;
		}
		Engine3D engine = this.materials.getEngine();
		
		long id = aiAgent.getId();
		Node rootNode = agentConfiguration.isVisible() ? engine.getPerceptibleNodes() : engine.getNonPerceptibleNodes();
		String modelName = agentConfiguration.getPathModel();
		Spatial model = engine.getAssetManager().loadModel(modelName);
		
		Oriented2DPosition position = aiAgent.getPosition();
		float orientation = position.theta + agentConfiguration.getCorrectiveAngle(); // TODO
		// consider
		// corrective
		// distance
		// everywhere
		
		BulletAppState bulletAppState = engine.getBulletAppState();
		
		float graphicalX = position.x;
		float graphicalZ = position.y;
		
		Vector3f scale = new Vector3f(agentConfiguration.getScaleX(), agentConfiguration.getScaleY(),
				agentConfiguration.getScaleZ());
		float elevation = agentConfiguration.getElevation();
		float eyexHeight = agentConfiguration.getEyesHeight();
		int visionDistance = 0;
		try {
			visionDistance = aiAgent.getPropertiesContainer().getInt(Internal.Agent.PERCEPTION_MAX_EYESIGHT_DISTANCE);
			
		} catch (UnknownPropertyException upe) {
			throw new MalformedObjectException(aiAgent, Internal.Agent.PERCEPTION_MAX_EYESIGHT_DISTANCE, upe);
		}
		
		float walkSpeed = 0;
		try {
			walkSpeed = (float) aiAgent.getPropertiesContainer().getDouble("c_Human_walk_speed");
		} catch (UnknownPropertyException e) {
			e.printStackTrace();
		}
		
		GraphicalAgent graphicalAgent = new GraphicalAgent(id, rootNode, model, bulletAppState, graphicalX, graphicalZ,
				orientation, scale, elevation, eyexHeight, visionDistance, engine, walkSpeed,
				agentConfiguration.getCorrectiveAngle());
		
		return  graphicalAgent;
		
	}
	
	/**
	 * Creates a new graphical box.
	 * 
	 * @param object
	 *            the object
	 * @param boxConfiguration
	 *            the box configuration
	 * @return the graphical shape
	 */
	private GraphicalShape createGraphicalBox(WorldObject object, BoxConfiguration boxConfiguration) {
		Engine3D engine = this.materials.getEngine();
		
		long id = object.getId();
		
		String pathTexture = boxConfiguration.getPathTexture();
		Material mat = new Material(this.materials.getEngine().getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");
		final Texture texture = this.materials.getEngine().getAssetManager().loadTexture(pathTexture);
		mat.setTexture("DiffuseMap", texture);
		
		Oriented2DPosition position = object.getPosition();
		float graphicalX = position.x;
		float graphicalY = boxConfiguration.getElevation();
		float graphicalZ = position.y;
		Vector3f center = new Vector3f(graphicalX, graphicalY, graphicalZ);
		float orientation = position.theta + boxConfiguration.getCorrectiveAngle(); // TODO
		// consider
		// corrective
		// distance
		// everywhere
		float height = boxConfiguration.getHeight();
		float length = boxConfiguration.getLength();
		float width = boxConfiguration.getWidth();
		Box box = new Box(Vector3f.ZERO, length / 2, height / 2, width / 2);
		
		Geometry boxGeo = new Geometry(null, box);
		/* The rotation is applied: The object rolls by orientation. */
		boxGeo.getLocalRotation().fromAngleNormalAxis(orientation, Vector3f.UNIT_Y);
		boxGeo.setLocalTranslation(center);
		
		boxGeo.setMaterial(mat);
		
		// Visibility
		Node node = (boxConfiguration.isVisible() ? engine.getPerceptibleNodes() : engine.getNonPerceptibleNodes());
		GraphicalShape boxGS = new GraphicalShape(id, node, boxGeo, engine);
		
		return boxGS;
	}
	
	/**
	 * Creates a new Graphic vehicle.
	 * 
	 * @param vehicle
	 *            the object
	 * @param configuration
	 *            the configuration for the vehicle
	 * @return the graphical object
	 * @throws MalformedObjectException
	 *             If the object is missing a property among {i_graphics_conf, i_mass}.
	 */
	public GraphicalVehicule createGraphicalVehicle(WorldObject vehicle, VehicleConfiguration configuration)
			throws MalformedObjectException {
		
		String configurationName = null;
		try {
			configurationName = vehicle.getPropertiesContainer().getString(Internal.Object.GRAPHICS_CONF);
		} catch (UnknownPropertyException upe) {
			throw new MalformedObjectException(vehicle, Internal.Object.GRAPHICS_CONF, upe);
		}
		
		VehicleConfiguration vehicleConfiguration = this.materials.getVehicleConfigurationRepository().get(
				configurationName);
		if (vehicleConfiguration == null) {
			logger.warn("No graphical vehicle configuration named\"" + configurationName
					+ "\" can be found. Creation of this object escaped.");
			return null;
		}
		Engine3D engine = this.materials.getEngine();
		
		long id = vehicle.getId();
		Node rootNode = vehicleConfiguration.isVisible() ? engine.getPerceptibleNodes() : engine
				.getNonPerceptibleNodes();
		String modelName = vehicleConfiguration.getPathModel();
		Spatial model = engine.getAssetManager().loadModel(modelName);
		
		Oriented2DPosition position = vehicle.getPosition();
		float orientation = position.theta + vehicleConfiguration.getCorrectiveAngle(); // TODO
		// consider
		// corrective
		// distance
		// everywhere
		
		BulletAppState bulletAppState = engine.getBulletAppState();
		
		float graphicalX = position.x;
		float graphicalZ = position.y;
		
		Vector3f scale = new Vector3f(vehicleConfiguration.getScaleX(), vehicleConfiguration.getScaleY(),
				vehicleConfiguration.getScaleZ());
		float elevation = vehicleConfiguration.getElevation();
		
		float collisionShapeRadius = vehicleConfiguration.getCollisionShapeRadius();
		float collisionShapeHeight = vehicleConfiguration.getCollisionShapeHeight();
		float stepHeight = vehicleConfiguration.getStepHeight();
		
		float driveSpeed = 0;
		try {
			driveSpeed = (float) vehicle.getPropertiesContainer().getDouble("c_Vehicle_drive_speed");
		} catch (UnknownPropertyException e) {
			e.printStackTrace();
		}
			
		
		GraphicalVehicule graphicalVehicle = new GraphicalVehicule(id, rootNode, model, bulletAppState, graphicalX,
				graphicalZ, orientation, scale, elevation, engine, collisionShapeRadius, collisionShapeHeight,
				stepHeight, driveSpeed, vehicleConfiguration.getCorrectiveAngle());
		
		return graphicalVehicle;
	}
	
	/**
	 * Creates a new Graphics object.
	 * 
	 * @param object
	 *            the object
	 * @param configuration
	 *            the configuration
	 * @return the graphical object
	 */
	private GraphicalObject createGraphicalWorldObject(WorldObject object, SpatialConfiguration configuration) {
		Engine3D engine = this.materials.getEngine();
		
		long id = object.getId();
		Node rootNode = (configuration.isPerceptible() ? engine.getPerceptibleNodes() : engine.getNonPerceptibleNodes());
		String modelName = configuration.getPathModel();
		Spatial model = engine.getAssetManager().loadModel(modelName);	
		
		
		float xScale = configuration.getScaleX();
		float yScale = configuration.getScaleY();
		float zScale = configuration.getScaleZ();
		model.setLocalScale(xScale, yScale, zScale);
		
		Oriented2DPosition position = object.getPosition();
		float graphicalX = position.x;
		float graphicalY = configuration.getElevation();
		float graphicalZ = position.y;
		float orientation = position.theta + configuration.getCorrectiveAngle();
		model.center();
		model.getLocalRotation().fromAngleAxis(orientation, Vector3f.UNIT_Y);
		model.setLocalTranslation(graphicalX, graphicalY, graphicalZ);
		
		//landscape.getPhysicsRotation().fromAngleAxis(orientation, Vector3f.UNIT_Y);
		//landscape.setPhysicsLocation(new Vector3f(graphicalX, graphicalY, graphicalZ));		
		
		
		//CollisionShape sceneShape = CollisionShapeFactory.createSingleBoxShape((Node) model);
		//RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);	   
		
		//model.addControl(landscape);
		//rootNode.attachChild(model);
		//engine.getBulletAppState().getPhysicsSpace().add(landscape);
		
		// BulletAppState bulletAppState = engine.getBulletAppState(); // TODO		
				
		GraphicalObject graphicalObject = new GraphicalObject(id, rootNode, model, engine);
		
		// Build and add the children
		for (SpatialConfiguration child : configuration.getChildren().values()) {
			GraphicalObject childObject = this.createGraphicalWorldObject(object, child);
			graphicalObject.addSubElement(child.getName(), childObject);
			Log.debug("Added child: " + child.getName() + ", visible=" + child.isVisible() + " toString()="
					+ childObject);
		}
		
		engine.objects.put(id, graphicalObject);
		
		// Set visible if needed
		graphicalObject.setVisible(configuration.isVisible());	   
		
		return graphicalObject;
	}
	
	/**
	 * Creates a new Graphics object.
	 * 
	 * @param object
	 *            the object
	 * @return the graphical object
	 * @throws MalformedObjectException
	 *             If the property "i_graphics_conf" is absent.
	 */
	public GraphicalObject createGraphicalObject(WorldObject object) throws MalformedObjectException {
		String configurationName;
		try {
			configurationName = object.getPropertiesContainer().getString(Internal.Object.GRAPHICS_CONF);
		} catch (UnknownPropertyException upe) {
			throw new MalformedObjectException(object, Internal.Agent.MASS, upe);
		}
		
		// Spatial
		Configuration configuration = this.materials.getSpatialConfigurationRepository().get(configurationName);
		if (configuration != null) {
			return this.createGraphicalWorldObject(object, (SpatialConfiguration) configuration);
		}
		
		// Vehicle
		configuration = this.materials.getVehicleConfigurationRepository().get(configurationName);
		if (configuration != null) {
			return this.createGraphicalVehicle(object, (VehicleConfiguration) configuration);
		}
		
		// Box
		configuration = this.materials.getBoxConfigurationRepository().get(configurationName);
		if (configuration != null) {
			return this.createGraphicalBox(object, (BoxConfiguration) configuration);
		}
		
		logger.warn("No graphical spatial configuration named\"" + configurationName
				+ "\" can be found. Creation of this object escaped.");
		return null;
	}
	
	
	
	/*public GraphicalObject setMaterial(GraphicalShape grapho, BoxConfiguration boxConfiguration){
		Material mat = new Material(this.materials.getEngine().getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");
		String pathTexture = boxConfiguration.getPathTexture();
		final Texture texture = this.materials.getEngine().getAssetManager().loadTexture(pathTexture);
		mat.setTexture("ColorMap", texture);
		grapho.getModel().setMaterial(mat);
		return grapho ;
	}*/
	
	
	
	/*public GraphicalObject setMaterial(GraphicalObject grapho){
		Material mat = new Material(this.materials.getEngine().getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");
		grapho.getModel().setMaterial(mat);
		return grapho ;
	}*/
	
}