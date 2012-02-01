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
package fr.n7.saceca.u3du.model.io.ai.model.object;

import com.thoughtworks.xstream.XStream;

import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.ai.object.properties.AbstractNumericPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.AbstractPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.BooleanPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.BooleanPropertyModelConverter;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.DoublePropertyModelConverter;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.EnumPropertyModelConverter;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.IntegerPropertyModelConverter;
import fr.n7.saceca.u3du.model.io.ai.model.object.properties.StringPropertyModelConverter;
import fr.n7.saceca.u3du.model.io.common.xstream.XStreamIO;

/**
 * A class to deal with World Object Model I/O.
 * 
 * @author Sylvain Cambon
 */
public class WorldObjectModelIO extends XStreamIO<WorldObjectModel> {
	
	/**
	 * Whether XStream has been configured correctly in order to properly handle models.
	 */
	private static boolean configured = false;
	
	@Override
	protected synchronized void configureIfNecessary(final XStream xStream) {
		if (!configured) {
			xStream.processAnnotations(WorldObjectModel.class);
			xStream.processAnnotations(AgentModel.class);
			xStream.processAnnotations(AbstractPropertyModel.class);
			xStream.processAnnotations(BooleanPropertyModel.class);
			xStream.processAnnotations(StringPropertyModel.class);
			xStream.processAnnotations(EnumPropertyModel.class);
			xStream.processAnnotations(AbstractNumericPropertyModel.class);
			xStream.processAnnotations(IntegerPropertyModel.class);
			xStream.processAnnotations(DoublePropertyModel.class);
			xStream.registerConverter(new WorldObjectModelConverter());
			xStream.registerConverter(new BooleanPropertyModelConverter());
			xStream.registerConverter(new StringPropertyModelConverter());
			xStream.registerConverter(new EnumPropertyModelConverter());
			xStream.registerConverter(new IntegerPropertyModelConverter());
			xStream.registerConverter(new DoublePropertyModelConverter());
			configured = true;
		}
	}
	
	@Override
	protected synchronized boolean isConfigured() {
		return WorldObjectModelIO.configured;
	}
}
