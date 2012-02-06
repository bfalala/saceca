package Emotion_secondary;

import java.util.List;

import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;
import fr.n7.saceca.u3du.model.util.Couple;

public class update_secondary {
	public update_secondary() {
	}
	
	public int[][] potentialsecondary(Service service) {
		int i = 0;
		int[][] effectcurentaction = new int[1][Model.getInstance().getAI().getWorld().getServiceEffectList().size()];
		for (Couple<String, ServiceProperty> couple : Model.getInstance().getAI().getWorld().getServiceEffectList()) {
			if (couple.getFirstElement().equals(service.getName())) {
				effectcurentaction[0][i] = 1;
			}
			i++;
		}
		return this.multiplyMatrix(effectcurentaction, Model.getInstance().getAI().getWorld()
				.getMatrix_effect_emotions());
	}
	
	public int[][] multiplyMatrix(int[][] matrix1, int[][] matrix2) {
		int m1rows = matrix1.length;
		int m1cols = matrix1[0].length;
		int m2cols = matrix2[0].length;
		
		int[][] result = new int[m1rows][m2cols];
		
		for (int i = 0; i < m1rows; i++) {
			for (int j = 0; j < m2cols; j++) {
				for (int k = 0; k < m1cols; k++) {
					if (matrix1[i][k] != 0) {
						result[i][j] += matrix1[i][k] * matrix2[k][j];
					}
				}
			}
		}
		return result;
	}
	
	public int[][] multiplyMatrixDouble(double[][] matrix1, double[][] matrix2) {
		int m1rows = matrix1.length;
		int m1cols = matrix1[0].length;
		int m2cols = matrix2[0].length;
		double aux = 0;
		int[][] result = new int[m1rows][m2cols];
		
		for (int i = 0; i < m1rows; i++) {
			for (int j = 0; j < m2cols; j++) {
				aux = 0;
				for (int k = 0; k < m1cols; k++) {
					if (matrix1[i][k] != 0) {
						aux += matrix1[i][k] * matrix2[k][j];
					}
				}
				result[i][j] = (int) (Math.round(aux));
			}
		}
		return result;
	}
	
	public int[][] potentialvaluesecondary(List<Emotion> emotionList) {
		double[][] valuePrimary = new double[1][emotionList.size()];
		int i = 0;
		for (Emotion emotion : emotionList) {
			valuePrimary[0][i] = emotion.getValue();
			i++;
		}
		return this.multiplyMatrixDouble(valuePrimary, Model.getInstance().getAI().getWorld()
				.getMatrix_primary_secondary());
		
	}
	
	public int[][] resultingSecondaryEmotions(int[][] secondaryEffect, int[][] secondaryValues) {
		int[][] result = new int[1][secondaryEffect[0].length];
		for (int i = 0; i < secondaryEffect[0].length; i++) {
			result[0][i] = (secondaryEffect[0][i] * secondaryValues[0][i] > 100) ? 100 : secondaryEffect[0][i]
					* secondaryValues[0][i];
		}
		return result;
	}
}