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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Curve;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import fr.n7.saceca.u3du.exception.MalformedObjectException;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.graph.WeightedEdge;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.graphics.Graphics;
import fr.n7.saceca.u3du.model.graphics.animation.Animation;
import fr.n7.saceca.u3du.model.graphics.configuration.GraphicsFactory;
import fr.n7.saceca.u3du.model.graphics.configuration.GraphicsFactoryMaterials;
import fr.n7.saceca.u3du.model.graphics.engine3d.Config3D.CameraKey;
import fr.n7.saceca.u3du.model.graphics.engine3d.Config3D.VisualisationMode;
import fr.n7.saceca.u3du.model.io.graphics.GraphicsFactoryMaterialsLoader;
import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

import jme3tools.converters.ImageToAwt;

/**
 * The 3D Engine that renders the world and all its objects and agents.
 * 
 * @author Aurélien Chabot, Anthony Foulfoin, Johann Legaye
 */
public class Engine3D extends SimpleApplication implements Graphics {
	
	/* current weather */
	private Weather weather ;
	
	/**
	 * Contains all the objects and agents visible by the agent. Visible means that the object is
	 * interesting for agents, for instance, objects that offer services.
	 */
	private Spatial selectedObject = null;
	private ChaseCamera chaseCam;
	private Node perceptibleNodes;
	/**
	 * Contains all the objects without any interest for agents, for instance the ground. Other
	 * objects, as pavements, are also there for calculation performance (walkTo service usage is
	 * computed in a specific way).
	 */
	private Node nonPerceptibleNodes;
	
	/** True if the simulation is paused. */
	private boolean isPaused;
	
	/** The GUI controler. */
	private Controller3D listener;
	
	/** World Physics. */
	private BulletAppState bulletAppState = new BulletAppState();
	
	/** List all the observers that have to be notified of object picking. */
	private Set<PickingObserver> pickingObservers;
	
	/** List all the observers that have to be notified of object creation. */
	private Set<NewOrRemovedObjectObserver> newOrRemovedObjectObservers;
	
	/** The factory. */
	private GraphicsFactory factory;
	
	/**
	 * The terrain
	 */
	private TerrainQuad terrain;
	
	/** The terrain size */
	private int terrainSize;
	
	/** True if the sidewalk graph is displayed, false otherwise */
	private boolean walkableGraphDisplayed;
	
	/** Agent list. */
	public Map<Long, GraphicalAgent> agents;
	
	/** The objects. */
	public Map<Long, GraphicalObject> objects;
	
	/** Dynamic object list. */
	public Map<Long, GraphicalDynamicObject> dynObjects;
	
	/**
	 * Edition or simulation mode
	 */
	public VisualisationMode visualisationMode;
	
	/** The logger. */
	public static Logger logger = Logger.getLogger(Engine3D.class);
	
	/**
	 * Instantiates a new engine3 d.
	 */
	public Engine3D() {
		GraphicsFactoryMaterialsLoader graphicsFactoryMaterialsLoader = new GraphicsFactoryMaterialsLoader();
		GraphicsFactoryMaterials materials = graphicsFactoryMaterialsLoader.readAll("data/3d/conf");
		materials.setEngine(this);
		
		this.factory = new GraphicsFactory(materials);
		
		this.objects = new HashMap<Long, GraphicalObject>();
		this.agents = new HashMap<Long, GraphicalAgent>();
		this.dynObjects = new HashMap<Long, GraphicalDynamicObject>();
		this.pickingObservers = new HashSet<PickingObserver>();
		this.newOrRemovedObjectObservers = new HashSet<NewOrRemovedObjectObserver>();
		this.setPauseOnLostFocus(false);
		logger.setLevel(Level.ERROR);
		this.visualisationMode = Config3D.VisualisationMode.SIMULATION;
		this.terrainSize = 512;
		this.walkableGraphDisplayed = false;
		
	}
	
	/**
	 * Simple init app.
	 */
	@Override
	/*
	 * Initialization of the viewer
	 */
	public void simpleInitApp() {
		
		// At the start the time is paused
		this.isPaused = true;
		
		// The mouse becomes visible
		this.mouseInput.setCursorVisible(true);
		
		// Register the path of the graphics resources
		this.assetManager.registerLocator("data/3d", FileLocator.class.getName());
		
		// List all the objects visible by the agents
		this.perceptibleNodes = new Node();
		this.perceptibleNodes.setName("WV_visibleNodes");
		// List all the objects non visible by the agents
		this.nonPerceptibleNodes = new Node();
		this.nonPerceptibleNodes.setName("WV_nonVisibleNodes");
		this.rootNode.attachChild(this.perceptibleNodes);
		this.rootNode.attachChild(this.nonPerceptibleNodes);
		
		this.statsView.setCullHint(Spatial.CullHint.Always);
		// this.fpsText.setCullHint(Spatial.CullHint.Always);
		
		// physics
		// this.bulletAppState.getPhysicsSpace().enableDebug(this.assetManager);
		this.bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
		this.stateManager.attach(this.bulletAppState);
		if (this.settings.getRenderer().startsWith("LWJGL")) {
			BasicShadowRenderer bsr = new BasicShadowRenderer(this.assetManager, 512);
			bsr.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
			this.viewPort.addProcessor(bsr);
		}
		
		this.initCamera();
		this.initListener();
		this.initKeys();
		
		this.weather = new Weather(Weather.WeatherState.SUNNY);
		
		this.initObjects();
		this.initAgents();
		// We draw the ground
		this.drawGround();
		// We draw the water
		this.drawWater();
		
		// We create the ball
		this.makeBall();
		
		this.chaseCam = new ChaseCamera(this.cam, this.inputManager);
		
		// We call this method in order that the first picking is immediate
		// this.getPickedObject();
		
		// We draw a grid
		if (Config3D.GRID_DRAW) {
			this.drawGrid(Config3D.GRID_SIZE);
		}
	}
	
	/* This is the main event loop */
	/**
	 * Simple update.
	 * 
	 * @param tpf
	 *            the tpf
	 */
	@Override
	public void simpleUpdate(float tpf) {
		for (GraphicalDynamicObject obj : this.dynObjects.values()) {
			obj.update(tpf);
		}
		
		// weather managing
		
		
	}
	
	/**
	 * Draws the sidewalk graph.
	 */
	public void drawGraph() {
		
		// If the graph is already displayed
		if (this.isWalkableGraphDisplayed()) {
			return;
		}
		this.walkableGraphDisplayed = true;
		UndirectedSparseGraph<WorldObject, WeightedEdge> sidewalkGraph = Model.getInstance().getAI().getWorld()
				.getWalkableGraph();
		Material matCurve = new Material(this.assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
		float y = 0.1f;
		Node graphNode = new Node("graphNode");
		this.nonPerceptibleNodes.attachChild(graphNode);
		for (WeightedEdge edge : sidewalkGraph.getEdges()) {
			Collection<Vector3f> points = new ArrayList<Vector3f>();
			Pair<WorldObject> endpoints = sidewalkGraph.getEndpoints(edge);
			WorldObject wo1 = endpoints.getFirst();
			Oriented2DPosition position1 = wo1.getPosition();
			float x1 = position1.x;
			float y1 = y;
			float z1 = position1.y;
			WorldObject wo2 = endpoints.getSecond();
			Oriented2DPosition position2 = wo2.getPosition();
			float x2 = position2.x;
			float y2 = y;
			float z2 = position2.y;
			float pitch = 1f / 16f;
			for (float k = 0; k <= 1; k += pitch) {
				
				Vector3f midPoint = new Vector3f(x1 + (x2 - x1) * k, y + y1 + (y2 - y1 + 1)
						* FastMath.sqrt(1 - FastMath.sqr(2 * k - 1)), z1 + (z2 - z1) * k);
				points.add(midPoint);
			}
			
			Vector3f[] tab = points.toArray(new Vector3f[] {});
			Curve curve = new Curve(tab, 1);
			
			matCurve.setColor("m_Color", ColorRGBA.Orange);
			Geometry curveGeom = new Geometry("curve", curve);
			curveGeom.setMaterial(matCurve);
			graphNode.attachChild(curveGeom);
		}
	}
	
	/**
	 * Remove the graph
	 */
	public void removeGraph() {
		if (this.isWalkableGraphDisplayed()) {
			this.nonPerceptibleNodes.detachChildNamed("graphNode");
		}
		this.walkableGraphDisplayed = false;
	}
	
	/**
	 * Draw a grid of size x*x, where x is the specified size.
	 * 
	 * @param size
	 *            The size of the grid
	 */
	private void drawGrid(int size) {
		
		Line line;
		Geometry lineG;
		Material mat1;
		
		int start = -size / 2;
		int end = size / 2;
		
		// for each x and z line
		for (int i = start; i <= end; i++) {
			
			mat1 = new Material(this.assetManager, "Common/MatDefs/Misc/SolidColor.j3md");
			
			// We draw the origin in green
			if (i == 0) {
				mat1.setColor("m_Color", ColorRGBA.Green);
			} else {
				mat1.setColor("m_Color", ColorRGBA.Red);
			}
			
			// x lines
			line = new Line(new Vector3f(start, 0, i), new Vector3f(end, 0, i));
			lineG = new Geometry("line", line);
			lineG.setMaterial(mat1);
			this.rootNode.attachChild(lineG);
			
			// z lines
			line = new Line(new Vector3f(i, 0, start), new Vector3f(i, 0, end));
			lineG = new Geometry("line", line);
			lineG.setMaterial(mat1);
			this.rootNode.attachChild(lineG);
		}
	}
	
	/**
	 * Draw water.
	 */
	private void drawWater() {
		
		// we create a water processor
		SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(this.assetManager);
		waterProcessor.setReflectionScene(this.rootNode);
		
		// we set the water plane
		waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, 1));
		this.viewPort.addProcessor(waterProcessor);
		
		// we set wave properties
		waterProcessor.setWaterDepth(0); // transparency of water
		waterProcessor.setDistortionScale(2f); // strength of waves
		waterProcessor.setWaveSpeed(0.05f); // speed of waves
		waterProcessor.setWaterTransparency(1);
		waterProcessor.setWaterColor(ColorRGBA.Brown);		
		
		// we define the wave size by setting the size of the texture coordinates
		Quad quad = new Quad(10000, 10000);
		quad.scaleTextureCoordinates(new Vector2f(2000f, 2000f));
		
		// we create the water geometry from the quad
		Geometry water = new Geometry("water", quad);
		water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
		water.setLocalTranslation(-1000, -1f, 1000);
		water.setShadowMode(ShadowMode.Receive);
		water.setMaterial(waterProcessor.getMaterial());
		this.rootNode.attachChild(water);
	}
	
	/**
	 * Draw or redraw ground.
	 */
	private void drawGround() {
		
		// Create material from Terrain Material Definition
		Material matRock = new Material(this.assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
		// Load alpha map (for splat textures)
		matRock.setTexture("AlphaMap", this.assetManager.loadTexture("Textures/Terrain/splat/red&green.png"));
		// load heightmap image (for the terrain heightmap)
		Texture heightMapImage = this.assetManager.loadTexture("Textures/Terrain/splat/map_64x64.png");
		
		// load grass texture
		Texture grass = this.assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
		grass.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap", grass);
		matRock.setFloat("DiffuseMap_0_scale", 64f);
		
		// load dirt texture
		Texture dirt = this.assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_1", dirt);
		matRock.setFloat("DiffuseMap_1_scale", 32f);
		
		// load sand texture
		Texture rock = this.assetManager.loadTexture("Textures/Terrain/splat/sand.jpg");
		rock.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_2", rock);
		matRock.setFloat("DiffuseMap_2_scale", 128f);
		
		ImageBasedHeightMap heightmap = new ImageBasedHeightMap(ImageToAwt.convert(heightMapImage.getImage(), false,
				true, 0), 1f);
		heightmap.load();
		
		// If we redraw the terrain
		if (this.terrain != null) {
			this.rootNode.detachChild(this.terrain);
			this.bulletAppState.getPhysicsSpace().remove(this.terrain);
		}
		
		this.terrain = new TerrainQuad("terrain", 65, this.terrainSize + 1, heightmap.getHeightMap());
		this.terrain.setMaterial(matRock);
		this.terrain.setModelBound(new BoundingBox());
		this.terrain.updateModelBound();
		this.terrain.setLocalScale(2f, 1f, 2f); // scale to make it less steep
		this.terrain.addControl(new RigidBodyControl(0));
		this.rootNode.attachChild(this.terrain);
		this.bulletAppState.getPhysicsSpace().add(this.terrain);
	}
	
	/**
	 * Gets all the objets from the AI, and put them on the map.
	 */
	private void initObjects() {
		// Loading from AI
		Collection<WorldObject> objects = Model.getInstance().getAI().getWorld().getReactiveObjects();
		for (WorldObject worldObject : objects) {
			try {
				this.factory.createGraphicalObject(worldObject);
			} catch (MalformedObjectException e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * Camera initialization.
	 */
	public void enablechase() {
		this.chaseCam.setEnabled(false);
	}
	
	public void enablefly() {
		this.flyCam.setEnabled(false);
	}
	
	public void initCamera() {
		// Set the location of the cam
		//
		this.flyCam.setEnabled(true);
		this.flyCam.setDragToRotate(true);
		this.flyCam.setMoveSpeed(Config3D.MOVE_SPEED);
		this.cam.setLocation(new Vector3f(250, 250, 0));
		this.cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
	}
	
	/**
	 * Initialisation of agents.
	 */
	private void initAgents() {
		
		Collection<Agent> agents = Model.getInstance().getAI().getWorld().getAgents();
		for (Agent agent : agents) {
			
			try {
				this.factory.createGraphicalAgent(agent);
			} catch (MalformedObjectException e) {
				logger.error(e);
			}
		}
	}
	
	/** Custom Keybinding: Map named actions to inputs. */
	private void initKeys() {
		// We remove several default flycam inputs
		this.inputManager.deleteMapping("FLYCAM_StrafeLeft");
		this.inputManager.deleteMapping("FLYCAM_StrafeRight");
		this.inputManager.deleteMapping("FLYCAM_Forward");
		this.inputManager.deleteMapping("FLYCAM_Backward");
		this.inputManager.deleteMapping("FLYCAM_Rise");
		this.inputManager.deleteMapping("FLYCAM_Lower");
		this.inputManager.deleteMapping("FLYCAM_RotateDrag");
		
		// We map the rotation
		this.inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		// je mappe switch_camera
		this.inputManager.addMapping("Switch_camera", new KeyTrigger(KeyInput.KEY_C));
		// We map the other camera movements
		this.inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_E));
		this.inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_R));
		this.inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_Q), new KeyTrigger(
				KeyInput.KEY_LEFT));
		this.inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(
				KeyInput.KEY_RIGHT));
		this.inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_SPACE), new KeyTrigger(KeyInput.KEY_G));
		this.inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_F));
		
		// We map the movement of the mouse
		this.inputManager.addMapping("MouseMove", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		this.inputManager.addMapping("MouseMove", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		this.inputManager.addMapping("MouseMove", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		this.inputManager.addMapping("MouseMove", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		
		// The ctrl key
		this.inputManager.addMapping("CTRL_key", new KeyTrigger(KeyInput.KEY_LCONTROL));
		this.inputManager.addMapping("CTRL_key", new KeyTrigger(KeyInput.KEY_RCONTROL));
		
		// The del (suppr) key
		this.inputManager.addMapping("DEL_key", new KeyTrigger(KeyInput.KEY_DELETE));
		
		// The shift key
		this.inputManager.addMapping("SHIFT_key", new KeyTrigger(KeyInput.KEY_LSHIFT));
		this.inputManager.addMapping("SHIFT_key", new KeyTrigger(KeyInput.KEY_RSHIFT));
		
		// We map our own custom camera movements (not included by default in JMonkeyEngine)
		this.inputManager
				.addMapping("FLYCAM_StrafeUp", new KeyTrigger(KeyInput.KEY_Z), new KeyTrigger(KeyInput.KEY_UP));
		this.inputManager.addMapping("FLYCAM_StrafeDown", new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(
				KeyInput.KEY_DOWN));
		
		// We map the mouse clic
		this.inputManager.addMapping("MouseLeftClic", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		
		// action listener
		this.inputManager.addListener(this.listener, new String[] { "Escape", "RotateDrag", "Switch_camera",
				"MouseLeftClic", "CTRL_key", "SHIFT_key", "DEL_key" });
		
		// flycam rotation listener
		this.inputManager.addListener(this.flyCam, new String[] { "FLYCAM_RotateDrag", "FLYCAM_StrafeLeft",
				"FLYCAM_StrafeRight", "FLYCAM_Forward", "FLYCAM_Backward", "FLYCAM_Rise", "FLYCAM_Lower" });
		
		// analog listener
		this.inputManager.addListener(this.listener,
				new String[] { "FLYCAM_StrafeUp", "FLYCAM_StrafeDown", "MouseMove" });
		// this.inputManager.addListener(this.actionListener, new String[] { "Switch_camera" });
		
	}
	
	
	
	/**
	 * Controller initialization.
	 */
	private void initListener() {
		this.listener = new Controller3D(this);
	}
		
	
	/**
	 * Rotate the specified object by PI / 2
	 * 
	 * @param id
	 *            The object to rotate
	 */
	public void rotate(long id) {
		GraphicalObject object = this.objects.get(id);
		object.setOrientation((FastMath.PI / 2) + object.getOrientation());
		
		// We update the AI reference
		WorldObject aiObject = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		if (aiObject != null) {
			aiObject.setPosition(new Oriented2DPosition(aiObject.getPosition().x, aiObject.getPosition().y, aiObject
					.getPosition().theta + (FastMath.PI / 2)));
		}
	}
	
	/**
	 * Move the object clone ID according to the mouse position. Returns false while the object has
	 * not a fixed position. An object has a fixed position when it touches the original object, but
	 * is not on it. For instance, when a pavement clone touch the original without being on the
	 * original pavement. This method is used for example to draw pavements or roads.
	 * 
	 * @param originalID
	 *            The original object
	 * @param cloneID
	 *            The clone of the original object, the object that is moved with mouse position
	 * @return true is the position of the clone is correct, false otherwise.
	 */
	public boolean brush(long originalID, long cloneID) {
		Vector2f pickedCoordinates = this.getPickedCoordinates();
		GraphicalObject originalObject = this.objects.get(originalID);
		GraphicalObject cloneObject = this.objects.get(cloneID);
		
		boolean fixed = false;
		
		// We move the object to the mouse coordinates
		if (pickedCoordinates != null) {
			
			// In order to align objects on a virtual grid, we compute on which cell of the grid the
			// object must be placed.
			// The grid is represented by cells with a size of 1x1, objects are placed in the center
			// of the cell, which coordinates are
			// something like (+-x.0,+-x.0), so we take the integer value of the mouse coordinates
			
			int xPosition = (int) pickedCoordinates.x;
			int yPosition = (int) pickedCoordinates.y;
			
			cloneObject.setXPosition(xPosition);
			cloneObject.setZPosition(yPosition);
			
			// We update the AI reference
			WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(cloneID);
			if (object != null) {
				object.setPosition(new Oriented2DPosition(xPosition, yPosition, object.getPosition().theta));
			}
			
			// We check whether the position of the clone is correct or not
			// The objects have to be x or y aligned
			if (originalObject.getXPosition() == cloneObject.getXPosition()
					|| originalObject.getZPosition() == cloneObject.getZPosition()) {
				
				// The distance between their center must be equals to their width
				float width = 2 * ((BoundingBox) originalObject.getModel().getWorldBound()).getXExtent();
				
				if (originalObject.getLocalTranslation().distance(cloneObject.getLocalTranslation()) == width) {
					fixed = true;
				}
			}
			
		}
		return fixed;
	}
	
	/**
	 * Move the specified object to the mouse position
	 * 
	 * @param id
	 *            the object to move
	 */
	public void moveObjectOnMousePosition(long id) {
		Vector2f pickedCoordinates = this.getPickedCoordinates();
		GraphicalObject pickedObject = this.objects.get(id);
		
		// We move the object to the mouse coordinates
		if (pickedCoordinates != null && pickedObject != null) {
			
			// In order to align objects on a virtual grid, we compute on which cell of the grid the
			// object must be placed.
			// The grid is represented by cells with a size of 1x1, objects are placed in the center
			// of the cell, which coordinates are
			// something like (+-x.0,+-x.0), so we take the integer value of the mouse coordinates
			
			int xPosition = (int) pickedCoordinates.x;
			int yPosition = (int) pickedCoordinates.y;
			
			pickedObject.setXPosition(xPosition);
			pickedObject.setZPosition(yPosition);
			
			// We update the AI reference
			WorldObject object = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
			if (object != null) {
				object.setPosition(new Oriented2DPosition(xPosition, yPosition, object.getPosition().theta));
			}
		}
	}
	
	/**
	 * Move the camera at each fps according to the pressed key, we move the camera in its
	 * direction.
	 * 
	 * @param tpf
	 *            The time per frame provided by JME
	 * @param key
	 *            The pressed key
	 */
	public void moveCameraKey(float tpf, CameraKey key) {
		// New camera location
		Vector3f newLocation = this.cam.getLocation().clone();
		Vector3f camDirection = this.cam.getDirection();
		
		// Actions, we compute the new camera position
		if (key == CameraKey.UP) {
			newLocation.z = newLocation.z + camDirection.z * tpf * Config3D.MOVE_SPEED;
			newLocation.x = newLocation.x + camDirection.x * tpf * Config3D.MOVE_SPEED;
		}
		if (key == CameraKey.DOWN) {
			newLocation.z = newLocation.z - camDirection.z * tpf * Config3D.MOVE_SPEED;
			newLocation.x = newLocation.x - camDirection.x * tpf * Config3D.MOVE_SPEED;
		}
		
		this.cam.setLocation(newLocation);
	}
	
	/**
	 * Allows to resize the map size.
	 * 
	 * @param grow
	 *            True to enlarge the map, false to reduce it.
	 */
	public void resizeMap(boolean grow) {
		
		if (this.terrainSize > 2) {
			this.terrainSize = grow ? this.terrainSize * 2 : this.terrainSize / 2;
			this.drawGround();
		}
	}
	
	/**
	 * Make ball.
	 */
	public void makeBall() {
		
		Sphere sphere = new Sphere(32, 32, 2f, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		Geometry ballGeo = new Geometry("cannon ball", sphere);
		Material stoneMat = new Material(this.assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		stoneMat = new Material(this.assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		TextureKey key2 = new TextureKey("Interface/Ball/Ball.jpg");
		key2.setGenerateMips(true);
		Texture tex2 = this.assetManager.loadTexture(key2);
		stoneMat.setTexture("ColorMap", tex2);
		ballGeo.setMaterial(stoneMat);
		this.rootNode.attachChild(ballGeo);
		/** Position the cannon ball and activate shadows */
		ballGeo.setLocalTranslation(-80, 40, -62);
		ballGeo.setShadowMode(ShadowMode.CastAndReceive);
		/** Make the ball physcial with a mass > 0.0f */
		RigidBodyControl ballPhy = new RigidBodyControl(2f);
		
		/** Add physical ball to physics space. */
		ballGeo.addControl(ballPhy);
		this.bulletAppState.getPhysicsSpace().add(ballPhy);
		/** Accelerate the physcical ball. */
		ballPhy.setLinearVelocity(new Vector3f(0, -20, 0));
		ballPhy.setFriction(1f);
	}
	
	/**
	 * Notify all the new object observers of an object creation or deletion
	 * 
	 * @param id
	 *            the created object
	 * @param removed
	 *            true if the object is removed, false if it is created
	 */
	public void notifyNewOrRemovedObject(long id, boolean removed) {
		for (NewOrRemovedObjectObserver observer : this.newOrRemovedObjectObservers) {
			observer.newOrRemovedObject(id, removed);
		}
	}
	
	/**
	 * Send an event to all the pickinObservers signaling that the specified object is picked
	 * 
	 * @param objectId
	 *            The id of the picked object
	 */
	public void notifyMousePicking(Long objectId) {
		
		if (objectId != null) {
			for (PickingObserver pickingObserver : this.pickingObservers) {
				pickingObserver.picked(objectId);
			}
		}
	}
	
	/**
	 * Adds the picking observer.
	 * 
	 * @param controller
	 *            the controller
	 */
	public void addPickingObserver(PickingObserver controller) {
		this.pickingObservers.add(controller);
	}
	
	/**
	 * Remove a picking observer.
	 * 
	 * @param controller
	 *            the controller to remove
	 */
	public void removePickingObserver(PickingObserver controller) {
		this.pickingObservers.remove(controller);
	}
	
	/**
	 * Adds the new object observer.
	 * 
	 * @param controller
	 *            the controller
	 */
	public void addNewObjectObserver(NewOrRemovedObjectObserver controller) {
		this.newOrRemovedObjectObservers.add(controller);
	}
	
	/**
	 * remove the new object observer.
	 * 
	 * @param controller
	 *            the controller
	 */
	public void removeNewObjectObserver(NewOrRemovedObjectObserver controller) {
		this.newOrRemovedObjectObservers.remove(controller);
	}
	
	/**
	 * Create a new GraphicalAgent from an AI agent
	 * 
	 * @param agent
	 *            the AI agent
	 * @return the GraphicalAgent
	 */
	public GraphicalAgent addAgent(Agent agent) {
		
		GraphicalAgent graphicalAgent = null;
		try {
			graphicalAgent = this.factory.createGraphicalAgent(agent);
		} catch (MalformedObjectException e) {
			logger.error(e);
		}
		return graphicalAgent;
	}
	
	/**
	 * Create a new GraphicalObject from an AI object
	 * 
	 * @param object
	 *            the AI object
	 * @return the GraphicalObject
	 */
	public GraphicalObject addObject(WorldObject object) {
		
		GraphicalObject graphicalObject = null;
		try {
			graphicalObject = this.factory.createGraphicalObject(object);
		} catch (MalformedObjectException e) {
			logger.error(e);
		}
		return graphicalObject;
	}
	
	/**
	 * Remove the specified object or agent from the graphic engine
	 * 
	 * @param id
	 *            the object id
	 */
	public void removeObjectOrAgent(long id) {
		// We get the object
		GraphicalObject object = this.objects.get(id);
		// Remove it of JME
		if (object != null) {
			object.destruct();
			this.rootNode.updateGeometricState();
			this.rootNode.updateModelBound();
		}
		
	}
	
	/**
	 * Pause or run the application.
	 * 
	 * @param paused
	 *            true to pause the application, false to run it
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
		Model.getInstance().getAI().getSimulation().setPause(paused);
	}
	
	/**
	 * Return true if the simulation is paused.
	 * 
	 * @return true if paused
	 */
	public boolean isPaused() {
		return this.isPaused;
	}
	
	/**
	 * Return true is the pavements graph is displayed
	 * 
	 * @return true is the pavements graph is displayed
	 */
	public boolean isWalkableGraphDisplayed() {
		return this.walkableGraphDisplayed;
	}
	
	/**
	 * Get the coordinates of the mouse on the 3D ground
	 * 
	 * @return the coordinates of the mouse on the 3D ground
	 */
	public Vector2f getPickedCoordinates() {
		
		Vector2f pickedCoordinates = null;
		Vector2f mousePosition = this.inputManager.getCursorPosition();
		
		Vector2f mouseCoords = new Vector2f(mousePosition.x, mousePosition.y);
		
		Vector3f camPosition = this.getCamera().getWorldCoordinates(mouseCoords, 0).clone();
		Vector3f camDirection = this.getCamera().getWorldCoordinates(mouseCoords, 0.3f).clone();
		camDirection.subtractLocal(camPosition).normalizeLocal();
		// The ray from the mouse position, to the click direction
		Ray ray = new Ray(camPosition, camDirection);
		
		CollisionResults results = new CollisionResults();
		
		// Compute the collisions with the ray
		this.rootNode.getChild("terrain").collideWith(ray, results);
		
		if (results.size() == 0) {
			this.rootNode.getChild("water").collideWith(ray, results);
		}
		
		if (results.size() > 0) {
			// We take the closest collision
			CollisionResult collision = results.getClosestCollision();
			Vector3f coord3D = collision.getContactPoint();
			pickedCoordinates = new Vector2f(coord3D.x, coord3D.z);
		}
		
		return pickedCoordinates;
	}
	
	/**
	 * Compute on which object the user has clicked
	 * 
	 * @return the object on which user has clicked
	 */
	public Long getPickedObject() {
		// reset the previous selected object
		this.selectedObject = null;
		
		// the selected object
		boolean movible = false;
		Long objectId = null;
		Vector2f mousePosition = this.inputManager.getCursorPosition();
		
		Vector2f mouseCoords = new Vector2f(mousePosition.x, mousePosition.y);
		
		Vector3f camPosition = this.getCamera().getWorldCoordinates(mouseCoords, 0).clone();
		Vector3f camDirection = this.getCamera().getWorldCoordinates(mouseCoords, 0.3f).clone();
		camDirection.subtractLocal(camPosition).normalizeLocal();
		// The ray from the mouse position, to the click direction
		Ray ray = new Ray(camPosition, camDirection);
		
		CollisionResults results = new CollisionResults();
		
		// Compute the collisions with the ray
		this.perceptibleNodes.collideWith(ray, results);
		
		if (results.size() == 0) {
			this.nonPerceptibleNodes.collideWith(ray, results);
		}
		
		if (results.size() > 0) {
			// We take the closest collision
			this.selectedObject = results.getClosestCollision().getGeometry();
			// The object is a geometry
			// We may want to select a whole node such as a house or a car instead of only one of
			// its components
			
			// The componant (geometry or node) is named with the convention explained in the
			// Config3D class
			// We look for this name. When the name is found, the whole componant has been found
			
			// While we do not have a componant respecting the convention name
			if (this.selectedObject.getName().startsWith("Man") || this.selectedObject.getName().startsWith("car")
					|| this.selectedObject.getName().startsWith("bus")) {
				// System.out.println("%%%%%%%%%%%%%%%%%%%%%%%" + this.selectedObject.getName());
				// this.setFocusOn(objectId);
				movible = true;
				
			} else {
				// System.out.println("***********************" + this.selectedObject.getName());
				// this.resumecam(objectId);
				movible = false;
			}
			// System.out.println(movible);
			while (!this.selectedObject.getParent().getName().startsWith("Root Node")
					&& !this.selectedObject.getName().startsWith(Config3D.OBJECTS_PREFIX)) {
				this.selectedObject = this.selectedObject.getParent();
			}
		}
		
		if (this.selectedObject != null && this.selectedObject.getName().startsWith(Config3D.OBJECTS_PREFIX)) {
			objectId = Config3D.getIdFromName(this.selectedObject.getName());
			
			// this.flyCam.setEnabled(false);
			// ChaseCamera chaseCam = new ChaseCamera(this.cam, this.selectedObject);
			// this.selectedObject.addControl(this.chaseCam);
		}
		// this.flyCam.setEnabled(true);
		if (movible) {
			// System.out.println(this.selectedObject.getName() + " est agent mobile");
			this.flyCam.setEnabled(false);
			this.chaseCam.setEnabled(true);
			this.selectedObject.addControl(this.chaseCam);
		} else {
			if (objectId != null) {
				this.resumecam(objectId);
			}
			
		}
		
		return objectId;
	}
	
	/**
	 * Gets the world Physics.
	 * 
	 * @return the world Physics
	 */
	public final BulletAppState getBulletAppState() {
		return this.bulletAppState;
	}
	
	/**
	 * Gets the asset manager.
	 * 
	 * @return the asset manager
	 */
	@Override
	public AssetManager getAssetManager() {
		return this.assetManager;
	}
	
	/**
	 * Gets the root node.
	 * 
	 * @return the root node
	 */
	@Override
	public Node getRootNode() {
		return this.rootNode;
	}
	
	/**
	 * Gets all the objects and agents visible par the agent.
	 * 
	 * @return the contains all the objects and agents visible by the agent
	 */
	public final Node getPerceptibleNodes() {
		return this.perceptibleNodes;
	}
	
	/**
	 * Gets the contains all the objects without any interest for agents, for instance the ground.
	 * 
	 * @return the contains all the objects without any interest for agents, for instance the ground
	 */
	public final Node getNonPerceptibleNodes() {
		return this.nonPerceptibleNodes;
	}
	
	/**
	 * Gets the factory.
	 * 
	 * @return the factory
	 */
	public final GraphicsFactory getFactory() {
		return this.factory;
	}
	
	/**
	 * Gets the terrain size.
	 * 
	 * @return the terrain size
	 */
	public final int getTerrainSize() {
		return this.terrainSize;
	}
	
	/**
	 * Sets the terrain size.
	 * 
	 * @param terrainSize
	 *            the new terrain size
	 */
	public final void setTerrainSize(int terrainSize) {
		this.terrainSize = terrainSize;
	}
	
	/**
	 * Gets the engine3 d.
	 * 
	 * @return the engine3 d
	 */
	@Override
	public Engine3D getEngine3D() {
		return this;
	}
	
	/**
	 * Gets the objects in vision field.
	 * 
	 * @param agent
	 *            the agent
	 * @return the objects in vision field
	 */
	@Override
	public List<Long> getObjectsInVisionField(Long agent) {
		GraphicalAgent graphicalAgent = this.agents.get(agent);
		return graphicalAgent.getObjectsInVisionField();
	}
	
	/**
	 * Update orientation.
	 * 
	 * @param object
	 *            the object
	 * @param theta
	 *            the theta
	 */
	@Override
	public void updateOrientation(Long object, float theta) {
		GraphicalObject graphicalObject = this.objects.get(object);
		graphicalObject.setOrientation(graphicalObject.getOrientation() + theta);
	}
	
	/**
	 * Send animation.
	 * 
	 * @param animation
	 *            the animation to be executed
	 */
	@Override
	public void sendAnimation(final Animation animation) {
		Model.getInstance().getGraphics().getEngine3D().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				animation.execute();
				return null;
			}
		});
	}
	
	/**
	 * Show message.
	 * 
	 * @param emitter
	 *            the emitter
	 * @param message
	 *            the message
	 */
	@Override
	public void showMessage(Long emitter, String message) {
		this.agents.get(emitter).getEmmitedMessages().add(message);
	}
	
	/**
	 * Place the specified object on the center of the camera.
	 * 
	 * @param id
	 *            The id of the object
	 */
	
	public void resumecam(long id) {
		this.chaseCam.setEnabled(false);
		this.flyCam.setEnabled(true);
		this.flyCam.setDragToRotate(true);
		this.flyCam.setMoveSpeed(Config3D.MOVE_SPEED);
		GraphicalObject object = this.objects.get(id);
		this.cam.setLocation(new Vector3f(20 + object.getXPosition(), 20, object.getZPosition()));
		this.cam.lookAt(object.getModel().getLocalTranslation(), new Vector3f(0, 1, 0));
		
	}
	
	public void setFocusOn(long id) {
		Spatial select = null;
		GraphicalObject object = this.objects.get(id);
		// /System.out.println("+++++++++++++++++++++++++++++++ the id is " + id +
		// "++++++++++++++++++++++++++++");
		
		if (object == null) {
			object = this.agents.get(id);
			
		}
		
		select = object.getModel();
		// /System.out.println("****************************************the name is " +
		// select.getName()
		// + "***********************");
		
		// this.cam.setLocation(new Vector3f(20 + object.getXPosition(), 20,
		// object.getZPosition()));
		// this.cam.lookAt(object.getModel().getLocalTranslation(), new Vector3f(0, 1, 0));
		
		this.flyCam.setEnabled(false);
		this.chaseCam.setEnabled(true);
		select.addControl(this.chaseCam);
		// this.flyCam.setEnabled(true);
		/*
		 * this.chaseCam.setSmoothMotion(true); this.chaseCam.setMaxDistance(50);
		 * this.chaseCam.setDefaultDistance(20);
		 */
		// System.out.println("---------------------------------------------the name is " +
		// select.getName()
		// + "---------------------------");
		// System.out.println(this.chaseCam.toString());
	}
	
	public Spatial getSelectedObject() {
		return this.selectedObject;
	}
	
	public void setSelectedObject(Spatial selectedObject) {
		this.selectedObject = selectedObject;
	}
	
	public Controller3D getController3D() {
		return this.listener;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}
	
	
	
}
