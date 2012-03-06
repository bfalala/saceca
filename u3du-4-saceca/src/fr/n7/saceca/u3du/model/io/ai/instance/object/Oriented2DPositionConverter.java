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
package fr.n7.saceca.u3du.model.io.ai.instance.object;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fr.n7.saceca.u3du.model.util.Oriented2DPosition;

/**
 * A class to handle oriented 2D position for XStream.
 */
@XStreamAlias("position")
public class Oriented2DPositionConverter implements Converter {
	
	/** The Constant X_ATRIBUTE. */
	private static final String X_ATRIBUTE = "x";
	
	/** The Constant Y_ATRIBUTE. */
	private static final String Y_ATRIBUTE = "y";
	
	/** The Constant THETA_ATRIBUTE. */
	private static final String THETA_ATRIBUTE = "theta";
	
	/**
	 * Can convert.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return true, if successful
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return Oriented2DPosition.class.equals(clazz);
	}
	
	/**
	 * Marshal.
	 * 
	 * @param object
	 *            the object
	 * @param writer
	 *            the writer
	 * @param context
	 *            the context
	 */
	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Oriented2DPosition position = (Oriented2DPosition) object;
		writer.addAttribute(X_ATRIBUTE, "" + position.x);
		writer.addAttribute(Y_ATRIBUTE, "" + position.y);
		writer.addAttribute(THETA_ATRIBUTE, "" + position.theta);
	}
	
	/**
	 * Unmarshal.
	 * 
	 * @param reader
	 *            the reader
	 * @param arg1
	 *            the arg1
	 * @return the object
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext arg1) {
		float x, y, theta;
		
		// x
		String xString = reader.getAttribute(X_ATRIBUTE);
		if (xString == null) {
			throw new ConversionException("The attribute \"" + X_ATRIBUTE + "\" was missing in a position.");
		}
		try {
			x = Float.parseFloat(xString);
		} catch (NumberFormatException nfe) {
			throw new ConversionException("The content of the attribute \"" + X_ATRIBUTE
					+ "\" in a position cannot be parsed to a float: \"" + xString + "\".", nfe);
		}
		
		// y
		String yString = reader.getAttribute(Y_ATRIBUTE);
		if (yString == null) {
			throw new ConversionException("The attribute \"" + Y_ATRIBUTE + "\" was missing in a position.");
		}
		try {
			y = Float.parseFloat(yString);
		} catch (NumberFormatException nfe) {
			throw new ConversionException("The content of the attribute \"" + Y_ATRIBUTE
					+ "\" in a position cannot be parsed to a float: \"" + yString + "\".", nfe);
		}
		
		// theta
		String thetaString = reader.getAttribute(THETA_ATRIBUTE);
		if (thetaString == null) {
			throw new ConversionException("The attribute \"" + THETA_ATRIBUTE + "\" was missing in a position.");
		}
		try {
			theta = Float.parseFloat(thetaString);
		} catch (NumberFormatException nfe) {
			throw new ConversionException("The content of the attribute \"" + THETA_ATRIBUTE
					+ "\" in a position cannot be parsed to a float: \"" + thetaString + "\".", nfe);
		}
		
		return new Oriented2DPosition(x, y, theta);
	}
}