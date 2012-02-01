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

/**
 * Contains the configuration of the graphical side.
 * 
 * @author Sylvain Cambon & Jérome Dalbert & Anthony Foulfoin & Johann Legaye & Aurélien Chabot
 */
public class Config3D {
	
	/** If the value is true, the program display a 2D grid on the floor. */
	public static final boolean GRID_DRAW = false;
	
	/** The size of the displayed grid. */
	public static final int GRID_SIZE = 100;
	
	/** True if we are in a test configuration. */
	public static final boolean TEST_ENABLED = false;
	
	/** The Flycam move speed. */
	public static final int MOVE_SPEED = 50;
	
	/** The prefix used to name graphical objects. */
	public static final String OBJECTS_PREFIX = "WV_OBJECT_";
	
	/** The variable used to assign a unique id for each object. */
	private static int cpt = -1;
	
	/**
	 * Returns a name based on the id. The name will be formated like this : OBJECTS_PREFIX<id>
	 * 
	 * @param id
	 *            the object id
	 * @return a new name base on the id
	 */
	public static String getNewName(long id) {
		return OBJECTS_PREFIX + id;
	}
	
	/**
	 * Return the id of the object, knowing the name given by the getNewName method.
	 * 
	 * @param name
	 *            the name of the object given by the getNewName method
	 * @return the id of the object
	 */
	public static long getIdFromName(String name) {
		String[] split = name.split("_");
		return Long.parseLong(split[2]);
	}
	
	/**
	 * Return an id for an object used only in the 3D engine and not in the AI. The id is negative
	 * 
	 * @return a new graphical internal id
	 */
	public static long getNewGraphicalInternalId() {
		return cpt--;
	}
	
	/**
	 * The Enum CameraKey.
	 */
	public static enum CameraKey {
		/** The UP. */
		UP,
		/** The DOWN. */
		DOWN
	};
	
	/**
	 * The visualisation mode.
	 */
	public static enum VisualisationMode {
		
		/** The SIMULATION. */
		SIMULATION,
		/** The EDITION. */
		EDITION
	};
	
}
