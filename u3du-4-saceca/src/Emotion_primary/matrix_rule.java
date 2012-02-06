package Emotion_primary;

import java.util.ArrayList;

public class matrix_rule {
	private int[][] matrice;
	
	public matrix_rule(ArrayList<property_rule> propertyruleTab, ArrayList<rule> ruleTab)

	{
		int size_propertyrule = propertyruleTab.size();
		int i = 0, j = 0;
		this.matrice = new int[size_propertyrule][size_propertyrule];
		for (i = 0; i < size_propertyrule; i++) {
			for (j = 0; j < size_propertyrule; j++) {
				this.matrice[i][j] = 0;
			}
			
			System.out.println();
		}
		for (rule rule1 : ruleTab) {
			j = 0;
			i = 0;
			property_rule premisse = rule1.getPremise();
			property_rule conclusion = rule1.getConclusion();
			
			for (property_rule proprule1 : propertyruleTab) {
				if (premisse.getName().equals(proprule1.getName())) {
					for (property_rule proprule2 : propertyruleTab) {
						if (conclusion.getName().equals(proprule2.getName())) {
							this.matrice[i][j] = 1;
							break;
						}
						j++;
						
					}
					
				}
				i++;
			}
			
		}
		System.out.println("matrice initiale de regle");
		for (i = 0; i < size_propertyrule; i++) {
			for (j = 0; j < size_propertyrule; j++) {
				System.out.print(this.matrice[i][j] + " ");
			}
			
			System.out.println();
		}
		
	}
	
	public int[][] getMatrice() {
		return this.matrice;
	}
	
	public void setMatrice(int[][] matrice) {
		this.matrice = matrice;
	}
	
}
