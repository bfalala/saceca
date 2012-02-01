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
package fr.n7.saceca.u3du.model.util.io.clazz.management;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;



/**
 * The class containing utilities for handling Class class instances.
 * 
 * @author Sylvain Cambon
 */
public class ClassUtils {

    /**
     * Gets the all implemented interfaces of the provied class.
     * 
     * @param clazz
     *            the clazz
     * @return the all implemented interfaces
     */
    public static final Set<Class<?>> getAllImplementedInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        Set<Class<?>> alreadyScannedClasses = new HashSet<Class<?>>();
        Queue<Class<?>> classesToScan = new LinkedList<Class<?>>();
        classesToScan.add(clazz);
        while (!classesToScan.isEmpty()) {
            Class<?> current = classesToScan.poll();
            alreadyScannedClasses.add(current);
            if (current.isInterface()) {
                interfaces.add(current);
            }
            Set<Class<?>> currentInterfaces = new HashSet<Class<?>>();
            for (Class<?> itf : current.getInterfaces()) {
                if (!alreadyScannedClasses.contains(itf) && !classesToScan.contains(itf)) {
                    classesToScan.add(itf);
                }
            }

            classesToScan.addAll(currentInterfaces);

            if (current.getSuperclass() != null && !classesToScan.contains(current.getSuperclass())) {
                classesToScan.add(current.getSuperclass());
            }

        }
        return interfaces;
    }
}
