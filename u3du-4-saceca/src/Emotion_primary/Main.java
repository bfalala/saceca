package Emotion_primary;

public class Main {
	public static void main(String[] args) {
		
		generatevisionvector vision = new generatevisionvector();
		generate_propertyrule_tab tabs = new generate_propertyrule_tab("data/ai/knowlage.xml");
		matrix_rule mat = new matrix_rule(tabs.getProperty_rule_Tab(), tabs.getRuleTab());
		long start = System.nanoTime();
		
		int[][] vision_property = vision.generate_sawed_property(tabs.getProperty_rule_Tab());
		forward_chaining forw1 = new forward_chaining(mat.getMatrice(), vision_property);
		// forward_chaining forw2 = new
		// forward_chaining(mat.getMatrice(),forw1.getVector_forwarded());
		double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		System.out.print("\n\n\nAlgorithm execution time: " + elapsedTimeInSec + " sec\n\n");
		
	}
}