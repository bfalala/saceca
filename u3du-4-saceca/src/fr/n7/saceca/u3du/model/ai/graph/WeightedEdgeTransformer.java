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
package fr.n7.saceca.u3du.model.ai.graph;

import org.apache.commons.collections15.Transformer;

/**
 * The Class WeightedEdgeTransformer.
 * 
 * This class is used to "feed" the dijkstra algorithm, so that the algorithm knows the weight of
 * the edges in the graph.
 * 
 * @author Jérôme Dalbert
 */
public class WeightedEdgeTransformer implements Transformer<WeightedEdge, Integer> {
	
	@Override
	public Integer transform(WeightedEdge edge) {
		return edge.getWeight();
	}
	
}
