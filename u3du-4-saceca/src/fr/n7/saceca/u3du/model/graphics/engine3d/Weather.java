package fr.n7.saceca.u3du.model.graphics.engine3d;

import java.util.Vector;

import com.jme3.effect.EmitterBoxShape;
import com.jme3.effect.EmitterSphereShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

import fr.n7.saceca.u3du.model.Model;

/**
 * The class that deals with the weather of the little world. 
 * The weather play a role in the emotion of the agents.
 * 
 * @author Eric BlachÃ¨re
 */
public class Weather {
	
	public enum WeatherState{
		SUNNY,
		RAINY
	}
	
	private class WeatherStateData{
		public Spatial sky ;
		public ColorRGBA lightColor ;
		
		public WeatherStateData(Spatial sky, ColorRGBA lightColor){
			this.sky = sky ;
			this.lightColor = lightColor ;
		}
		
	}
	
	private Vector<WeatherStateData> data = new Vector<WeatherStateData>() ;
	private WeatherState state ;
	private Engine3D engie ;
	private ParticleEmitter points ;
	
	
	private void initData(){
		data.add(new WeatherStateData(SkyFactory
				.createSky(this.engie.getAssetManager(), "Textures/Sky/SunnySky.jpg", true), ColorRGBA.White));
		data.add(new WeatherStateData(SkyFactory
				.createSky(this.engie.getAssetManager(), "Textures/Sky/CloudySky.jpg", true), ColorRGBA.Gray));
	}
	
	public Weather(WeatherState state){
		this.state = state ;
		this.engie = Model.getInstance().getGraphics().getEngine3D();		
		
		// init the data vector with all possible weatherStateData
		this.initData() ;	
		
		// set lights
		this.initLight();			
		 
	    points = 
	            new ParticleEmitter("points", Type.Triangle, 2000);
	    points.setShape(new EmitterBoxShape(new Vector3f(-150, 100, -150), new Vector3f(150, 150, 150)));
	    Material points_mat = new Material(this.engie.getAssetManager(), 
	            "Common/MatDefs/Misc/Particle.j3md");
	    points_mat.setTexture("Texture",this.engie.getAssetManager().loadTexture(
	            "Effects/Pluie/rain.png"));	    
	    points.setMaterial(points_mat);
	    
	    // Adjustments   
	    
	    points.setImagesX(1); // only one image of raindrop
	    points.setLowLife( 0.5f); // how much time does at least a drop survive 
	    points.setHighLife( 0.6f); // how much time does at max a drop survive 
	    points.setParticlesPerSec(1000000); // not accurate (a lot too much)
	    points.setStartSize( 0.5f); // % of size of the drop
        points.setEndSize( 0.5f);
	   
	    points.setStartColor(new ColorRGBA( 80.0f/255, 80.0f/255, 255.0f/255, 255/255)); // blue drop
	    points.setEndColor(new ColorRGBA( 80.0f/255, 80.0f/255, 255.0f/255, 255/255));
	    points.setGravity(1000); // how fast does it fall ?
	 
	    this.engie.getRootNode().attachChild(points);
	    //points.emitAllParticles();
	    
	    // change lights colors and sky
	 	this.change(state);	
		
	}
	
	private Spatial currentSky(){
		return this.data.get(this.state.ordinal()).sky ;
	}
	
	private ColorRGBA currentLightColor(){
		return this.data.get(this.state.ordinal()).lightColor ;
	}
	
	public void change(){
		this.change((this.state==WeatherState.RAINY)?WeatherState.SUNNY:WeatherState.RAINY);
	}
	
	public void  change(WeatherState newState){
		
		// We have to change the sky
		this.engie.getRootNode().detachChild(this.currentSky());
		
		this.state = newState ;
		
		// set the new sky (currentSky now returns the new sky)
		this.engie.getRootNode().attachChild(this.currentSky());
	
		// set the new lights
		LightList allLights = this.engie.getRootNode().getLocalLightList();
		for(Light l : allLights){
			l.setColor(this.currentLightColor());
		}		
		
		// does it rain ?
		if(newState==WeatherState.RAINY)  this.engie.getRootNode().attachChild(points);
		else  this.engie.getRootNode().detachChild(points);
		
	}
		
	/**
	 * Lights initialization.
	 */
	private void initLight() {
		// Create 5 lights
		
		//AmbientLight al = new AmbientLight();		
		//al.setColor(ColorRGBA.White);
		//this.rootNode.addLight(al);
		
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f( 0.0f, -1.0f, 0.0f).normalize());	
		//dl.setColor(ColorRGBA.Blue);
		this.engie.getRootNode().addLight(dl);		
		
		DirectionalLight dl2 = new DirectionalLight();
		dl2.setDirection(new Vector3f( 500.0f, -1.0f, 500.0f).normalize());
		//dl2.setColor(ColorRGBA.Blue);
		this.engie.getRootNode().addLight(dl2);		
		
		DirectionalLight dl3 = new DirectionalLight();
		dl3.setDirection(new Vector3f( 500.0f, -1.0f, -500.0f).normalize());
		//dl3.setColor(ColorRGBA.Blue);
		this.engie.getRootNode().addLight(dl3);		
		
		DirectionalLight dl4 = new DirectionalLight();
		dl4.setDirection(new Vector3f(-500.0f, -1.0f, 500.0f).normalize());
		//dl4.setColor(ColorRGBA.Blue);
		this.engie.getRootNode().addLight(dl4);		
		
		
		DirectionalLight dl5 = new DirectionalLight();
		dl5.setDirection(new Vector3f(-100.0f, -1.0f, -100.0f).normalize());
		//dl5.setColor(ColorRGBA.Blue);
		this.engie.getRootNode().addLight(dl5);
		
		
	}
	
	
}