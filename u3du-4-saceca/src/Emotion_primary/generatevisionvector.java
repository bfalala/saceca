package Emotion_primary;

import java.util.ArrayList;

public class generatevisionvector {
	private int[][] vector_perception;
	private ArrayList<String> perception;
	int i = 0;
	int j = 0;
	
	public generatevisionvector()

	{
		this.perception = new ArrayList<String>();
		
		String visio1 = "Flower";
		String visio2 = "Dog";
		this.perception.add(visio1);
		this.perception.add(visio2);
	}
	
	public int[][] generate_sawed_property(ArrayList<property_rule> tabpropertyrule) {
		this.vector_perception = new int[1][tabpropertyrule.size()];
		
		for (this.i = 0; this.i < tabpropertyrule.size(); this.i++) {
			
			this.vector_perception[0][this.i] = 0;
			
		}
		// System.out.println("i am here---------");
		for (String vision : this.perception) {
			this.j = 0;
			for (property_rule current_prop : tabpropertyrule) {
				if (vision.equals(current_prop.getName())) {
					this.vector_perception[0][this.j] = 1;
				}
				this.j++;
			}
			
		}
		/*
		 * System.out.println("vecteur perception est  "); for ( int
		 * i=0;i<tabpropertyrule.size();i++) {
		 * 
		 * System.out.print(" " +this.vector_perception[0][i] + " ");
		 * 
		 * }
		 */
		return this.vector_perception;
	}
}