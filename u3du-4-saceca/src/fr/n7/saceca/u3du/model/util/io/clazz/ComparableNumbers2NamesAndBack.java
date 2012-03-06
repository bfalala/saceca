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
package fr.n7.saceca.u3du.model.util.io.clazz;

import java.math.BigDecimal;
import java.math.BigInteger;

import fr.n7.saceca.u3du.model.util.Couple;



/**
 * A class to relate (Numeric and Comparable) classes to their short name in a
 * bidirectional way.
 * 
 * <table border="1">
 * <tr>
 * <td>Class</td>
 * <td>Short name</td>
 * </tr>
 * <tr>
 * <td>java.math.bigDecimal</td>
 * <td>bigDec</td>
 * </tr>
 * <tr>
 * <td>java.math.BigInteger</td>
 * <td>bigInt</td>
 * </tr>
 * <tr>
 * <td>java.lang.Byte</td>
 * <td>byte</td>
 * </tr>
 * <tr>
 * <td>java.lang.Double</td>
 * <td>double</td>
 * </tr>
 * <tr>
 * <td>java.lang.Float</td>
 * <td>float</td>
 * </tr>
 * <tr>
 * <td>java.lang.Integer</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>java.lang.Long</td>
 * <td>long</td>
 * </tr>
 * <tr>
 * <td>java.lang.Short</td>
 * <td>short</td>
 * </tr>
 * </table>
 * 
 * @author Sylvain Cambon
 */
public final class ComparableNumbers2NamesAndBack {

    /** The Constant UNKNOWN_CLASS if no class is found. */
    public static final Class<?> UNKNOWN_CLASS = null;

    /** The Constant UNKNOWN_SHORT_NAME if no short name is found. */
    public static final String UNKNOWN_SHORT_NAME = null;

    /** The couples. */
    @SuppressWarnings("rawtypes")
    private static Couple[] couples = {
            new Couple<Class<BigDecimal>, String>(BigDecimal.class, "bigDec"),
            new Couple<Class<BigInteger>, String>(BigInteger.class, "bigInt"),
            new Couple<Class<Byte>, String>(Byte.class, "byte"),
            new Couple<Class<Double>, String>(Double.class, "double"),
            new Couple<Class<Float>, String>(Float.class, "float"),
            new Couple<Class<Integer>, String>(Integer.class, "int"),
            new Couple<Class<Long>, String>(Long.class, "long"),
            new Couple<Class<Short>, String>(Short.class, "short")
    };


    /**
     * Gets the class after its short name.
     * 
     * @param shortName
     *            The short name.
     * @return The corresponding class if found, UNKNOWN_CLASS otherwise.
     */
    public static Class<?> getClassFor(String shortName) {
        for (Couple<Class<?>, String> nameCouple : couples) {
            if (nameCouple.getSecondElement().equals(shortName)) {
                return nameCouple.getFirstElement();
            }

        }
        return UNKNOWN_CLASS;
    }


    /**
     * Gets the short name corresponding to the given class.
     * 
     * @param clazz
     *            The class.
     * @return The corresponding short name if found, UNKNOWN_SHORT_NAME
     *         otherwise.
     */
    public static String getShortNameFor(Class<?> clazz) {
        for (Couple<Class<?>, String> nameCouple : couples) {
            if (nameCouple.getFirstElement().equals(clazz)) {
                return nameCouple.getSecondElement();
            }
        }
        return UNKNOWN_SHORT_NAME;
    }

}