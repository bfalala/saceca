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
package fr.n7.saceca.u3du.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * The Class TableModelDef.
 */
public class TableModelDef extends AbstractTableModel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The label colonne. */
	private String[] labelColonne;
	
	/** The data. */
	private List<Object>[] data;
	
	/** The editable columns. */
	private List<Integer> editableCol;
	
	/**
	 * Instantiates a new table model def.
	 * 
	 * @param titres
	 *            the titres
	 */
	@SuppressWarnings("unchecked")
	public TableModelDef(String[] titres) {
		this.labelColonne = titres;
		// On initialise la structure de donnée
		this.data = new ArrayList[this.labelColonne.length];
		for (int i = 0; i < this.labelColonne.length; i++) {
			this.data[i] = new ArrayList<Object>();
		}
		this.editableCol = new ArrayList<Integer>();
	}
	
	/**
	 * set the editable columns of the table
	 * 
	 * @param cols
	 *            the editable columns
	 */
	public void setEditableCol(int... cols) {
		
		for (int col : cols) {
			this.editableCol.add(col);
		}
	}
	
	// Fonctions qu'il est nécéssaire de surcharger
	
	/**
	 * Gets the column name.
	 * 
	 * @param col
	 *            the col
	 * @return the column name
	 */
	@Override
	public String getColumnName(int col) {
		return this.labelColonne[col].toString();
	}
	
	/**
	 * Gets the row count.
	 * 
	 * @return the row count
	 */
	@Override
	public int getRowCount() {
		return this.data[0].size();
	}
	
	/**
	 * Gets the column count.
	 * 
	 * @return the column count
	 */
	@Override
	public int getColumnCount() {
		return this.labelColonne.length;
	}
	
	/**
	 * Checks if is cell editable.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @return true, if is cell editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return this.editableCol.contains(col);
	}
	
	/**
	 * Gets the value at.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @return the value at
	 */
	@Override
	public Object getValueAt(int row, int col) {
		return this.data[col].get(row);
	}
	
	// On ajoute , on remplace ou on supprimer suivant les valeurs entrées...
	/**
	 * Sets the value at.
	 * 
	 * @param value
	 *            the value
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {
		if (value != null) {
			if (row >= this.data[col].size()) {
				this.data[col].add(row, value);
			} else {
				this.data[col].set(row, value);
			}
		} else {
			for (int i = 0; i < this.getColumnCount(); i++) {
				this.data[i].remove(row);
			}
		}
		this.fireTableCellUpdated(row, col);
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		for (int i = 0; i < this.labelColonne.length; i++) {
			this.data[i].clear();
		}
		this.fireTableDataChanged();
	}
}
