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
package fr.n7.saceca.u3du.model.ai.agent.module.planning.initialization;

import java.util.ArrayList;

import fr.n7.saceca.u3du.model.ai.service.PropertyLink;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.ai.service.ServiceProperty;

/**
 * The Matrix class
 * 
 * @author Ciprian Munteanu, Mehdi Boukhris
 * 
 */
public class Matrix {
	/** The matrix values */
	private int[][] values;
	
	/** A matrix with the order to satisfy the preconditions */
	private char[][] p_order;
	
	/**
	 * Creates the matrix Services X Effects
	 * 
	 * @param serviceList
	 *            the list of services
	 * @param effectList
	 *            the list of service properties
	 */
	public void createMatrixA(ArrayList<Service> serviceList, ArrayList<ServiceProperty> effectList) {
		int numRows = serviceList.size();
		int numColumns = effectList.size();
		
		this.values = new int[numRows][numColumns];
		
		int i = 0, j = 0;
		
		for (ServiceProperty effect : effectList) {
			j = 0;
			
			for (Service service : serviceList) {
				this.values[j][i] = 0;
				
				for (ServiceProperty effect_plus : service.getServiceEffectsPlus()) {
					if (effect.egal(effect_plus)) {
						this.values[j][i] = 1;
					}
				}
				
				for (ServiceProperty effect_minus : service.getServiceEffectsMinus()) {
					if (effect.egal(effect_minus)) {
						this.values[j][i] = -1;
					}
				}
				j++;
			}
			i++;
		}
		
	}
	
	/**
	 * Creates the matrix Preconditions X Servies
	 * 
	 * @param effectList
	 *            the properties list
	 * @param serviceList
	 *            the service list
	 */
	public void createMatrixP(ArrayList<ServiceProperty> effectList, ArrayList<Service> serviceList) {
		int numRows = effectList.size();
		int numColumns = serviceList.size();
		
		this.values = new int[numRows][numColumns];
		this.p_order = new char[numRows][numColumns];
		
		int i = 0, j = 0;
		
		for (ServiceProperty effect : effectList) {
			j = 0;
			for (Service service : serviceList) {
				this.values[i][j] = 0;
				this.p_order[i][j] = '0';
				
				for (ServiceProperty precondition : service.getServicePreconditions()) {
					if (effect.egal(precondition)) {
						this.values[i][j] = 1;
						this.p_order[i][j] = precondition.getOrder();
					}
				}
				j++;
			}
			i++;
		}
	}
	
	/**
	 * Creates the matrix of links
	 * 
	 * @param linkList
	 *            the list of links
	 */
	public void createMatrixLink(ArrayList<PropertyLink> linkList) {
		this.values = new int[linkList.size()][linkList.size()];
	}
	
	/**
	 * Calculates the transpose of the matrix
	 * 
	 * @return
	 */
	public int[][] transposeMatrix() {
		int rows = this.values.length;
		int cols = this.values[0].length;
		
		int[][] result = new int[cols][rows];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				result[j][i] = this.values[i][j];
			}
		}
		return result;
	}
	
	/**
	 * Clones the matrix of orders
	 * 
	 * @return
	 */
	public char[][] cloneOrders() {
		char[][] orders = new char[this.p_order.length][this.p_order[0].length];
		for (int i = 0; i < this.p_order.length; i++) {
			for (int j = 0; j < this.p_order[0].length; j++) {
				orders[i][j] = this.p_order[i][j];
			}
		}
		return orders;
	}
	
	/**
	 * Gets the values
	 * 
	 * @return
	 */
	public int[][] getValues() {
		return this.values;
	}
	
	/**
	 * Sets the values
	 * 
	 * @param values
	 *            the values
	 */
	public void setValues(int[][] values) {
		this.values = values;
	}
	
	/**
	 * Gets the matrix of orders
	 * 
	 * @return
	 */
	public char[][] getP_order() {
		return this.p_order;
	}
	
	/**
	 * Sets the matrix of orders
	 * 
	 * @param p_order
	 *            the orders matrix
	 */
	public void setP_order(char[][] p_order) {
		this.p_order = p_order;
	}
	
	/**
	 * Sets an element in the order matrix
	 * 
	 * @param i
	 *            the row
	 * @param j
	 *            the column
	 * @param value
	 *            the value
	 */
	public void setP_orderElement(int i, int j, char value) {
		this.p_order[i][j] = value;
	}
	
	/**
	 * Gets an element from the orders matrix
	 * 
	 * @param i
	 *            the row number
	 * @param j
	 *            the column number
	 * @return
	 */
	public char getP_orderElement(int i, int j) {
		return this.p_order[i][j];
	}
	
	/**
	 * Gets the number of rows of values matrix
	 * 
	 * @return
	 */
	public int getValuesRows() {
		return this.values.length;
	}
	
	/**
	 * Gets the number of columns of values matrix
	 * 
	 * @return
	 */
	public int getValuesColumns() {
		return this.values[0].length;
	}
	
	/**
	 * Gets the number of rows of orders matrix
	 * 
	 * @return
	 */
	public int getP_orderRows() {
		return this.p_order.length;
	}
	
	/**
	 * Gets the number of columns of orders matrix
	 * 
	 * @return
	 */
	public int getP_orderColumns() {
		return this.p_order[0].length;
	}
}
