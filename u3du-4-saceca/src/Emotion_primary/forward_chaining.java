package Emotion_primary;

import java.util.ArrayList;

public class forward_chaining {
	int[][] matrice2;
	int j, i = 0;
	int[][] vector_forwarded;
	int[][] vector_forwarded_result;
	
	public forward_chaining(ArrayList<String> perception, ArrayList<property_rule> tabpropertyrule) {
		
	}
	
	public int[][] getVector_forwarded_result() {
		return this.vector_forwarded_result;
	}
	
	public void setVector_forwarded_result(int[][] vector_forwarded_result) {
		this.vector_forwarded_result = vector_forwarded_result;
	}
	
	public forward_chaining(int[][] matrice, int[][] vision_vector) {
		
		int f = 0;
		this.vector_forwarded_result = vision_vector;
		while (!this.equalzero(vision_vector)) {
			f++;
			this.vector_forwarded = this.multiplyMatrix(vision_vector, matrice);
			// this.vector_forwarded = this.normalize(this.vector_forwarded);
			/*
			 * for (this.i = 0; this.i < this.vector_forwarded.length; this.i++) { for (this.j = 0;
			 * this.j < this.vector_forwarded[0].length; this.j++) { System.out.print(" " + f +
			 * " "); System.out.print(" " + this.vector_forwarded[this.i][this.j]); } }
			 */
			this.vector_forwarded_result = this.orMatrix(this.vector_forwarded_result, this.vector_forwarded);
			vision_vector = this.vector_forwarded;
			
		}
		
		// System.out.println("");
		
		/*
		 * System.out.println(); /*System.out.println("les elements evoquer sont : ");
		 * 
		 * /* for (this.i = 0; this.i < this.vector_forwarded_result.length; this.i++) { for (this.j
		 * = 0; this.j < this.vector_forwarded_result[0].length; this.j++) { System.out.print(" " +
		 * this.vector_forwarded_result[this.i][this.j]); } }
		 */

	}
	
	public int[][] getVector_forwarded() {
		return this.vector_forwarded;
	}
	
	public void setVector_forwarded(int[][] vector_forwarded) {
		this.vector_forwarded = vector_forwarded;
	}
	
	public int[][] multiplyMatrix(int[][] matrix1, int[][] matrix2) {
		int m1rows = matrix1.length;
		int m1cols = matrix1[0].length;
		int m2cols = matrix2[0].length;
		int i = 0;
		
		int[][] result = new int[m1rows][m2cols];
		
		for (i = 0; i < m1rows; i++) {
			for (int j = 0; j < m2cols; j++) {
				for (int k = 0; k < m1cols; k++) {
					result[i][j] += matrix1[i][k] * matrix2[k][j];
				}
			}
		}
		
		return result;
		
	}
	
	public int[][] transposeMatrix(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		
		int[][] result = new int[cols][rows];
		// System.out.println("la transpose du vecteur est:");
		for (this.i = 0; this.i < rows; this.i++) {
			// System.out.println();
			for (this.j = 0; this.j < cols; this.j++) {
				result[this.j][this.i] = matrix[this.i][this.j];
				// System.out.print(result[this.i][this.j]);
			}
		}
		// System.out.println();
		// System.out.println("le nombre de ligne du transpose est" + result.length);
		// System.out.println("le nombre de colone du transpose est" + result[0].length);
		return result;
	}
	
	public boolean equalzero(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		boolean bool = true;
		for (this.i = 0; this.i < rows; this.i++) {
			
			for (this.j = 0; this.j < cols; this.j++) {
				if (matrix[this.i][this.j] != 0) {
					bool = false;
				}
			}
			
		}
		// System.out.println(bool);
		return bool;
	}
	
	public int[][] normalize(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		
		for (this.i = 0; this.i < rows; this.i++) {
			
			for (this.j = 0; this.j < cols; this.j++) {
				if (matrix[this.i][this.j] != 0) {
					matrix[this.i][this.j] = 1;
				}
			}
			
		}
		return matrix;
	}
	
	public int[][] AddMatrix(int[][] matrixa, int[][] matrixb) {
		int m1rowsa = matrixa.length;
		int m1colsa = matrixa[0].length;
		
		int i = 0;
		
		int[][] result = new int[m1rowsa][m1colsa];
		// System.out.println("l'addition des deux matrice est ");
		for (i = 0; i < m1rowsa; i++) {
			// System.out.println();
			for (this.j = 0; this.j < m1colsa; this.j++) {
				result[i][this.j] = matrixa[i][this.j] + matrixb[i][this.j];
				// System.out.print(" " + result[i][this.j]);
			}
			// System.out.println();
		}
		
		return result;
	}
	
	public int[][] orMatrix(int[][] matrixa, int[][] matrixb) {
		int m1rowsa = matrixa.length;
		int m1colsa = matrixa[0].length;
		
		int i = 0;
		
		int[][] result = new int[m1rowsa][m1colsa];
		// System.out.println("l'or des deux matrice est ");
		for (i = 0; i < m1rowsa; i++) {
			System.out.println();
			for (this.j = 0; this.j < m1colsa; this.j++) {
				result[i][this.j] = matrixa[i][this.j] | matrixb[i][this.j];
				// System.out.print(" " + result[i][this.j]);
			}
			System.out.println();
		}
		
		return result;
	}
}
