package fr.n7.saceca.u3du.model.ai;

import javax.annotation.Generated;

/**
 * A class which is dynamically built using common models to contain the names of the properties.
 */
@Generated(value = {
	"fr/n7/saceca/u3du/model/ai/CommonPropertiesConstantsGenerator"
})
public final class Internal {

	/**
	 * A container for fields common to all the world object models.
	 */
	public static final class Object {

		/**
		 * <p>The constant value corresponding to <code><b>i_isMisc</b></code>.</p>
		 * <p><b>Type</b> Boolean</p>
		 * <p><b>Default</b> false</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String IS_MISC = "i_isMisc";


		/**
		 * <p>The constant value corresponding to <code><b>i_threaded</b></code>.</p>
		 * <p><b>Type</b> Boolean</p>
		 * <p><b>Default</b> false</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String THREADED = "i_threaded";


		/**
		 * <p>The constant value corresponding to <code><b>i_graphics_conf</b></code>.</p>
		 * <p><b>Type</b> String</p>
		 * <p><b>Default</b> Box</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String GRAPHICS_CONF = "i_graphics_conf";


		/**
		 * <p>The constant value corresponding to <code><b>i_mass</b></code>.</p>
		 * <p><b>Type</b> Integer</p>
		 * <p><b>Default</b> 42</p>
		 * <p><b>Range</b> [0; 10000]</p>
		 * <p><b>Visibility</b> public</p>
		 */
		public static final String MASS = "i_mass";
	}

	/**
	 * A container for fields common to all the agent models.
	 */
	public static final class Agent {

		/**
		 * <p>The constant value corresponding to <code><b>i_decrement_period_thirst</b></code>.</p>
		 * <p><b>Type</b> Integer</p>
		 * <p><b>Default</b> 8</p>
		 * <p><b>Range</b> [0; 1000]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String DECREMENT_PERIOD_THIRST = "i_decrement_period_thirst";


		/**
		 * <p>The constant value corresponding to <code><b>i_decrement_period_default</b></code>.</p>
		 * <p><b>Type</b> Integer</p>
		 * <p><b>Default</b> 7</p>
		 * <p><b>Range</b> [0; 1000]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String DECREMENT_PERIOD_DEFAULT = "i_decrement_period_default";


		/**
		 * <p>The constant value corresponding to <code><b>i_gauge_hunger</b></code>.</p>
		 * <p><b>Type</b> Double</p>
		 * <p><b>Default</b> 90.0</p>
		 * <p><b>Range</b> [0.0; 100.0]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String GAUGE_HUNGER = "i_gauge_hunger";


		/**
		 * <p>The constant value corresponding to <code><b>i_perception_maxHearingDistance</b></code>.</p>
		 * <p><b>Type</b> Integer</p>
		 * <p><b>Default</b> 10</p>
		 * <p><b>Range</b> [0; 1000]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String PERCEPTION_MAX_HEARING_DISTANCE = "i_perception_maxHearingDistance";


		/**
		 * <p>The constant value corresponding to <code><b>i_name</b></code>.</p>
		 * <p><b>Type</b> String</p>
		 * <p><b>Default</b> anonymous</p>
		 * <p><b>Visibility</b> public</p>
		 */
		public static final String NAME = "i_name";


		/**
		 * <p>The constant value corresponding to <code><b>i_graphics_conf</b></code>.</p>
		 * <p><b>Type</b> String</p>
		 * <p><b>Default</b> Box</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String GRAPHICS_CONF = "i_graphics_conf";


		/**
		 * <p>The constant value corresponding to <code><b>i_perception_maxEyesightDistance</b></code>.</p>
		 * <p><b>Type</b> Integer</p>
		 * <p><b>Default</b> 10</p>
		 * <p><b>Range</b> [0; 1000]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String PERCEPTION_MAX_EYESIGHT_DISTANCE = "i_perception_maxEyesightDistance";


		/**
		 * <p>The constant value corresponding to <code><b>i_mass</b></code>.</p>
		 * <p><b>Type</b> Integer</p>
		 * <p><b>Default</b> 42</p>
		 * <p><b>Range</b> [0; 10000]</p>
		 * <p><b>Visibility</b> public</p>
		 */
		public static final String MASS = "i_mass";


		/**
		 * <p>The constant value corresponding to <code><b>i_gauge_thirst</b></code>.</p>
		 * <p><b>Type</b> Double</p>
		 * <p><b>Default</b> 90.0</p>
		 * <p><b>Range</b> [0.0; 100.0]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String GAUGE_THIRST = "i_gauge_thirst";


		/**
		 * <p>The constant value corresponding to <code><b>i_gauge_happiness</b></code>.</p>
		 * <p><b>Type</b> Double</p>
		 * <p><b>Default</b> 90.0</p>
		 * <p><b>Range</b> [0.0; 100.0]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String GAUGE_HAPPINESS = "i_gauge_happiness";


		/**
		 * <p>The constant value corresponding to <code><b>i_gauge_tiredness</b></code>.</p>
		 * <p><b>Type</b> Double</p>
		 * <p><b>Default</b> 90.0</p>
		 * <p><b>Range</b> [0.0; 100.0]</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String GAUGE_TIREDNESS = "i_gauge_tiredness";


		/**
		 * <p>The constant value corresponding to <code><b>i_isMisc</b></code>.</p>
		 * <p><b>Type</b> Boolean</p>
		 * <p><b>Default</b> false</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String IS_MISC = "i_isMisc";


		/**
		 * <p>The constant value corresponding to <code><b>i_threaded</b></code>.</p>
		 * <p><b>Type</b> Boolean</p>
		 * <p><b>Default</b> false</p>
		 * <p><b>Visibility</b> private</p>
		 */
		public static final String THREADED = "i_threaded";
	}
}