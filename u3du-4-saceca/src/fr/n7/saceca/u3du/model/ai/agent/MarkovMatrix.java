package fr.n7.saceca.u3du.model.ai.agent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.bidimap.DualHashBidiMap;

import fr.n7.saceca.u3du.model.util.io.storage.Repository;

/**
 * The markov matrix is used to bind concepts or find emotions.
 * 
 * @author Eric Blachère
 */

public class MarkovMatrix {
	
	public static final String CCPOWERMATRIX = "data\\ai\\CCPOWER.txt";
	public static final String CONCEPT_LIST = "data\\ai\\concept.txt";
	
	/*
	 * public static void main(final String[] args) { final MarkovMatrix mm = new MarkovMatrix();
	 * 
	 * }
	 */
	
	/** the power matrix itself */
	private float[][] matrix;
	
	/** all concepts names with the matching id in the matrix (Id/String) */
	private DualHashBidiMap conceptsMap;
	
	/** For each emotion the ids of all its synonyms */
	private Map<String, Set<Integer>> emotionSynonymIds;
	
	/** For each emotion the ids of all its opposites */
	private Map<String, Set<Integer>> emotionOppositeIds;
	
	/** For each emotion word the ids of all its synonyms */
	private Map<String, Set<Integer>> emotionWordNumbers;
	
	public MarkovMatrix(Repository<MarkovEmotion> markovEmotionRepository, Repository<EmotionWord> emotionWordRepository) {
		this.loadConceptList();
		this.loadMatrix();
		
		this.emotionSynonymIds = new HashMap<String, Set<Integer>>();
		this.emotionOppositeIds = new HashMap<String, Set<Integer>>();
		this.emotionWordNumbers = new HashMap<String, Set<Integer>>();
		
		// we set the ids of emotion synonyms and opposites.
		for (MarkovEmotion me : markovEmotionRepository) {
			HashSet<Integer> synonymsIds = new HashSet<Integer>();
			for (String synonym : me.getSynonyms()) {
				synonymsIds.add((Integer) this.conceptsMap.get(synonym));
			}
			this.emotionSynonymIds.put(me.getStorageLabel(), synonymsIds);
			
			HashSet<Integer> oppositesIds = new HashSet<Integer>();
			for (String opposite : me.getOpposites()) {
				oppositesIds.add((Integer) this.conceptsMap.get(opposite));
			}
			this.emotionOppositeIds.put(me.getStorageLabel(), oppositesIds);
		}
		
		// the ids of emotion words concepts
		for (EmotionWord ew : emotionWordRepository) {
			HashSet<Integer> synonymsIds = new HashSet<Integer>();
			for (String synonym : ew.getConcepts()) {
				synonymsIds.add((Integer) this.conceptsMap.get(synonym));
			}
			this.emotionWordNumbers.put(ew.getStorageLabel(), synonymsIds);
			
		}
		
	}
	
	public Map<String, Float> generateEmotions(Collection<String> objectsConcepts) {
		HashMap<String, Float> emotionsScores = new HashMap<String, Float>();
		
		for (String emotionName : this.emotionSynonymIds.keySet()) {
			emotionsScores.put(emotionName, 0f);
		}
		
		for (String oc : objectsConcepts) { // for each concept in entry
			int numCol = (Integer) this.conceptsMap.get(oc);
			
			// for each emotion we compute its score by adding
			// the score of each synonym and subtracting the scores of opposites
			
			for (String emotionName : this.emotionSynonymIds.keySet()) {
				float emotionScore = 0;
				
				Set<Integer> synIds = this.emotionSynonymIds.get(emotionName);
				// int count = synIds.size();
				for (int i : synIds) {
					emotionScore += this.matrix[numCol][i]; // / count;
				}
				
				Set<Integer> oppIds = this.emotionOppositeIds.get(emotionName);
				// count = oppIds.size();
				for (int i : oppIds) {
					emotionScore -= this.matrix[numCol][i]; // / count;
				}
				emotionsScores.put(emotionName, emotionsScores.get(emotionName) + emotionScore);
			}
			
		}
		
		return emotionsScores;
	}
	
	public Map<String, Float> generateEmotionWords(Collection<String> objectsConcepts) {
		HashMap<String, Float> emowScores = new HashMap<String, Float>();
		
		for (String emowName : this.emotionWordNumbers.keySet()) {
			emowScores.put(emowName, 0f);
		}
		
		for (String oc : objectsConcepts) { // for each concept in entry
			int numCol = (Integer) this.conceptsMap.get(oc);
			
			// for each emotion word we compute its score by adding
			// the score of each of its concept
			
			for (String emowName : this.emotionWordNumbers.keySet()) {
				float emowScore = 0;
				for (int i : this.emotionWordNumbers.get(emowName)) {
					emowScore += this.matrix[numCol][i];
				}
				emowScores.put(emowName, emowScores.get(emowName) + emowScore);
			}
			
		}
		
		return emowScores;
	}
	
	private void loadConceptList() {
		
		this.conceptsMap = new DualHashBidiMap();
		
		try {
			
			final BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(
					MarkovMatrix.CONCEPT_LIST)));
			
			String line = null;
			int count = 0;
			while ((line = d.readLine()) != null) {
				
				final String concept = line.split(":")[1];
				this.conceptsMap.put(concept, count);
				
				count++;
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("The concept list doesn\'t seem to be in the right place...");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadMatrix() {
		
		final int numberConcepts = this.conceptsMap.size();
		this.matrix = new float[numberConcepts][numberConcepts];
		
		try {
			
			final BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(
					MarkovMatrix.CCPOWERMATRIX)));
			
			for (int i = 0; i < numberConcepts; i++) {
				
				final String[] values = d.readLine().split("\t");
				
				for (int j = 0; j < numberConcepts; j++) {
					this.matrix[i][j] = Float.parseFloat(values[j]);
					// if(i==0) System.out.println(this.matrix[i][j]);
				}
			}
			
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("The concept matrix doesn\'t seem to be in the right place...");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
