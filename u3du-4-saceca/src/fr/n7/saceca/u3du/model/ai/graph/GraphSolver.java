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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.UndirectedGraph;

import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.util.MathUtil;

/**
 * A class target solve graph-based problems. A caching system should accelerate calculations.
 * 
 * @author Sylvain Cambon & Jerome Dalbert
 */
public final class GraphSolver {
	
	/** The map source a graph target its solver, for caching purpose. */
	private static Map<UndirectedGraph<WorldObject, WeightedEdge>, GraphSolver> graph2ShortestPathSolver;
	
	/** The solver. */
	private DijkstraShortestPath<WorldObject, WeightedEdge> solver;
	
	/** The graph. */
	private UndirectedGraph<WorldObject, WeightedEdge> graph;
	
	/**
	 * Instantiates a new graph solver. The exception is <b>ALWAYS</b> thrown.
	 * 
	 * @throws InstantiationException
	 *             the instantiation exception
	 */
	private GraphSolver() throws InstantiationException {
		throw new InstantiationException("It is prohibited target use the default constructargetr of "
				+ GraphSolver.class.getCanonicalName());
	}
	
	/**
	 * Instantiates a new graph solver.
	 * 
	 * @param graph
	 *            the graph
	 */
	private GraphSolver(UndirectedGraph<WorldObject, WeightedEdge> graph) {
		this.solver = new DijkstraShortestPath<WorldObject, WeightedEdge>(graph, new WeightedEdgeTransformer(), true);
		this.graph = graph;
	}
	
	/**
	 * Selects the right instance target handle the graph.
	 * 
	 * @param graph
	 *            the graph
	 * @return the graph solver
	 */
	public static GraphSolver forGraph(UndirectedGraph<WorldObject, WeightedEdge> graph) {
		if (graph2ShortestPathSolver == null) {
			graph2ShortestPathSolver = new HashMap<UndirectedGraph<WorldObject, WeightedEdge>, GraphSolver>();
		}
		if (!graph2ShortestPathSolver.containsKey(graph)) {
			graph2ShortestPathSolver.put(graph, new GraphSolver(graph));
		}
		return graph2ShortestPathSolver.get(graph);
	}
	
	/**
	 * Tells whether a path exists between source and target.
	 * 
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @return true, if a path exists.
	 */
	public boolean existPath(WorldObject source, WorldObject target) {
		return this.solver.getDistance(source, target) != null;
	}
	
	/**
	 * Gets the shortest path from source to target.
	 * 
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @return the path
	 */
	public List<WorldObject> getShortestPath(WorldObject source, WorldObject target) {
		List<WeightedEdge> edgePath = null;
		try {
			edgePath = this.solver.getPath(source, target);
		} catch (Exception e) {
		}
		if (edgePath != null) {
			List<WorldObject> verticesPath = new ArrayList<WorldObject>(edgePath.size() + 1);
			WorldObject outNode = source;
			for (WeightedEdge edge : edgePath) {
				verticesPath.add(outNode);
				outNode = this.graph.getOpposite(outNode, edge);
			}
			verticesPath.add(outNode);
			
			this.cleanBetweenStraightLines(verticesPath);
			
			return verticesPath;
		}
		return null;
	}
	
	/**
	 * Removes all path elements that are between the two ends of a straight line. Only elements
	 * corresponding to pavements are cleaned.
	 * 
	 * @param path
	 *            the path
	 */
	private void cleanBetweenStraightLines(List<WorldObject> path) {
		if (path.size() < 3) {
			return;
		}
		
		/* We use an array so as to have a stable index when removing elements */
		WorldObject[] arrayPath = path.toArray(new WorldObject[0]);
		
		WorldObject firstElem = arrayPath[0];
		WorldObject secondElem = arrayPath[1];
		WorldObject currentElem;
		
		for (int i = 2; i < arrayPath.length; i++) {
			currentElem = arrayPath[i];
			
			// We clean only pavements
			if (secondElem != null && secondElem.getModelName().equals("Pavement")) {
				/* If the three elems are aligned, we remove the second one */
				if (MathUtil.areAligned(firstElem.getPosition(), secondElem.getPosition(), currentElem.getPosition())) {
					path.remove(secondElem);
					secondElem = null;
				}
			}
			
			if (secondElem != null) {
				firstElem = secondElem;
			}
			secondElem = currentElem;
		}
	}
	
}
