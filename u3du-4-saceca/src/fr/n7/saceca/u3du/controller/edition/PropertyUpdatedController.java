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
package fr.n7.saceca.u3du.controller.edition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fr.n7.saceca.u3du.model.ai.object.properties.BooleanPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.DoublePropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.IntegerPropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.StringPropertyModel;
import fr.n7.saceca.u3du.view.EditionWindow;
import fr.n7.saceca.u3du.view.PropertyEditionDialog;

/**
 * The Class PropertyUpdatedController.
 */
public class PropertyUpdatedController implements ActionListener {
	
	/** The edition window **/
	private EditionWindow ew;
	
	/** The property. */
	private Property<?> property;
	
	/** The property edition dialog. */
	private PropertyEditionDialog propertyEditionDialog;
	
	/**
	 * Instantiates a new property updated controller.
	 * 
	 * @param ew
	 *            the ew
	 * @param property
	 *            the property
	 * @param propertyEditionDialog
	 *            the property edition dialog
	 */
	public PropertyUpdatedController(EditionWindow ew, Property<?> property, PropertyEditionDialog propertyEditionDialog) {
		this.ew = ew;
		this.property = property;
		this.propertyEditionDialog = propertyEditionDialog;
	}
	
	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String errorMessage = "";
		boolean error = false;
		
		if (this.property.getModel().getType().getName().equals(String.class.getName())) {
			@SuppressWarnings("unchecked")
			Property<String> property = (Property<String>) this.property;
			StringPropertyModel propertyModel = (StringPropertyModel) property.getModel();
			JTextField textPropertyValue = (JTextField) this.propertyEditionDialog.propertyValue;
			String value = textPropertyValue.getText();
			errorMessage = "The property value is not correct";
			if (propertyModel.isAcceptable(value)) {
				property.setValue(value);
			} else {
				error = true;
			}
		}
		// if the property is an Integer
		else if (this.property.getModel().getType().getName().equals(Integer.class.getName())) {
			@SuppressWarnings("unchecked")
			Property<Integer> property = (Property<Integer>) this.property;
			IntegerPropertyModel propertyModel = (IntegerPropertyModel) property.getModel();
			JTextField textPropertyValue = (JTextField) this.propertyEditionDialog.propertyValue;
			errorMessage = "The property value must be an Integer range " + propertyModel.getMinValue() + " to "
					+ propertyModel.getMaxValue();
			try {
				Integer value = Integer.parseInt(textPropertyValue.getText());
				if (propertyModel.isAcceptable(value)) {
					property.setValue(value);
				} else {
					error = true;
				}
			} catch (NumberFormatException ex) {
				error = true;
			}
		}
		// if the property is a Double
		else if (this.property.getModel().getType().getName().equals(Double.class.getName())) {
			@SuppressWarnings("unchecked")
			Property<Double> property = (Property<Double>) this.property;
			DoublePropertyModel propertyModel = (DoublePropertyModel) property.getModel();
			JTextField textPropertyValue = (JTextField) this.propertyEditionDialog.propertyValue;
			errorMessage = "The property value must be an Double range " + propertyModel.getMinValue() + " to "
					+ propertyModel.getMaxValue();
			try {
				Double value = Double.parseDouble(textPropertyValue.getText());
				if (propertyModel.isAcceptable(value)) {
					property.setValue(value);
				} else {
					error = true;
				}
			} catch (NumberFormatException ex) {
				error = true;
			}
		}
		// if the property is an EnumElement
		else if (this.property.getModel().getType().getName().equals(EnumElement.class.getName())) {
			@SuppressWarnings("unchecked")
			Property<EnumElement> property = (Property<EnumElement>) this.property;
			EnumPropertyModel propertyModel = (EnumPropertyModel) property.getModel();
			JTextField textPropertyValue = (JTextField) this.propertyEditionDialog.propertyValue;
			EnumElement value = new EnumElement(textPropertyValue.getText());
			errorMessage = "The property value is not correct";
			if (propertyModel.isAcceptable(value)) {
				property.setValue(value);
			} else {
				error = true;
			}
		}
		// If the value is a boolean
		else if (this.property.getModel().getType().getName().equals(Boolean.class.getName())) {
			// The property is displayed thanks to a chackbox
			JCheckBox checkPropertyValue = (JCheckBox) this.propertyEditionDialog.propertyValue;
			@SuppressWarnings("unchecked")
			Property<Boolean> property = (Property<Boolean>) this.property;
			BooleanPropertyModel propertyModel = (BooleanPropertyModel) property.getModel();
			errorMessage = "The property value is not correct";
			Boolean value = checkPropertyValue.isSelected();
			if (propertyModel.isAcceptable(value)) {
				property.setValue(value);
			} else {
				error = true;
			}
		}
		if (error) {
			JOptionPane.showMessageDialog(this.propertyEditionDialog, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// We have to update the swing interface with the newValue
			PropertyUpdatedController.this.ew.setAgentObjectSelection(
					PropertyUpdatedController.this.ew.currentSelection, false);
			
			// We disable the dialog window
			this.propertyEditionDialog.dispose();
		}
	}
}
