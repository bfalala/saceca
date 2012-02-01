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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import fr.n7.saceca.u3du.controller.edition.CancelPropertyEditionController;
import fr.n7.saceca.u3du.controller.edition.PropertyUpdatedController;
import fr.n7.saceca.u3du.model.ai.object.properties.EnumElement;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertyEditionDialog.
 */
public class PropertyEditionDialog extends JDialog {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The content panel. */
	private final JPanel contentPanel = new JPanel();
	
	/** The lbl new label. */
	private JLabel lblNewLabel;
	
	/** The property value. */
	public Component propertyValue;
	
	/** The property. */
	private Property<?> property;
	
	/** The ew. */
	private EditionWindow ew;
	
	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		
		PropertyEditionDialog dialog = new PropertyEditionDialog();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		
	}
	
	/**
	 * Create the dialog.
	 */
	public PropertyEditionDialog() {
		this.initialize();
	}
	
	/**
	 * Instantiates a new property edition dialog.
	 * 
	 * @param property
	 *            the property
	 * @param ew
	 *            the ew
	 */
	public PropertyEditionDialog(Property<?> property, EditionWindow ew) {
		super(ew.frame, "Property Edition", true);
		this.property = property;
		this.ew = ew;
		this.setLocationRelativeTo(ew.frame);
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		this.setBounds(100, 100, 450, 167);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		SpringLayout slContentPanel = new SpringLayout();
		this.contentPanel.setLayout(slContentPanel);
		
		this.lblNewLabel = new JLabel("Property Name");
		slContentPanel.putConstraint(SpringLayout.NORTH, this.lblNewLabel, 13, SpringLayout.NORTH, this.contentPanel);
		slContentPanel.putConstraint(SpringLayout.WEST, this.lblNewLabel, 10, SpringLayout.WEST, this.contentPanel);
		this.contentPanel.add(this.lblNewLabel);
		
		JLabel lblNewLabel1 = new JLabel("Property type");
		slContentPanel.putConstraint(SpringLayout.NORTH, lblNewLabel1, 6, SpringLayout.SOUTH, this.lblNewLabel);
		slContentPanel.putConstraint(SpringLayout.WEST, lblNewLabel1, 0, SpringLayout.WEST, this.lblNewLabel);
		this.contentPanel.add(lblNewLabel1);
		
		JLabel lblNameValue = new JLabel("NameValue");
		// $hide>>$
		lblNameValue.setText(this.property.getModel().getName());
		// $hide<<$
		
		slContentPanel.putConstraint(SpringLayout.NORTH, lblNameValue, 0, SpringLayout.NORTH, this.lblNewLabel);
		slContentPanel.putConstraint(SpringLayout.WEST, lblNameValue, 26, SpringLayout.EAST, this.lblNewLabel);
		this.contentPanel.add(lblNameValue);
		
		JLabel lblTypevalue = new JLabel("TypeValue");
		// $hide>>$
		lblTypevalue.setText(this.property.getModel().getType().getName());
		// $hide<<$
		slContentPanel.putConstraint(SpringLayout.NORTH, lblTypevalue, 0, SpringLayout.NORTH, lblNewLabel1);
		slContentPanel.putConstraint(SpringLayout.WEST, lblTypevalue, 0, SpringLayout.WEST, lblNameValue);
		this.contentPanel.add(lblTypevalue);
		
		Component lblPropertyValue = new JLabel("Property value");
		slContentPanel.putConstraint(SpringLayout.NORTH, lblPropertyValue, 6, SpringLayout.SOUTH, lblNewLabel1);
		slContentPanel.putConstraint(SpringLayout.WEST, lblPropertyValue, 0, SpringLayout.WEST, this.lblNewLabel);
		this.contentPanel.add(lblPropertyValue);
		
		// DEFAULT VALUE : JTEXTFIELD with a String "Value"
		JTextField textPropertyValue = new JTextField("Value");
		textPropertyValue.setColumns(10);
		this.propertyValue = textPropertyValue;
		
		// $hide>>$
		// if the property is a String
		if (this.property.getModel().getType().getName().equals(String.class.getName())) {
			Property<String> stringProperty = (Property<String>) this.property;
			textPropertyValue.setText(stringProperty.getValue());
		}
		// if the property is an Integer
		else if (this.property.getModel().getType().getName().equals(Integer.class.getName())) {
			Property<Integer> integerProperty = (Property<Integer>) this.property;
			textPropertyValue.setText("" + integerProperty.getValue());
		}
		// if the property is a Double
		else if (this.property.getModel().getType().getName().equals(Double.class.getName())) {
			Property<Double> doubleProperty = (Property<Double>) this.property;
			textPropertyValue.setText("" + doubleProperty.getValue());
		}
		// if the property is an EnumElement
		else if (this.property.getModel().getType().getName().equals(EnumElement.class.getName())) {
			Property<EnumElement> enumProperty = (Property<EnumElement>) this.property;
			textPropertyValue.setText("" + enumProperty.getValue().toString());
		}
		// If the value is a boolean
		else if (this.property.getModel().getType().getName().equals(Boolean.class.getName())) {
			// The property is displayed thanks to a chackbox
			JCheckBox checkPropertyValue = new JCheckBox();
			Property<Boolean> booleanProperty = (Property<Boolean>) this.property;
			checkPropertyValue.setSelected(booleanProperty.getValue());
			this.propertyValue = checkPropertyValue;
		}
		// $hide<<$
		
		slContentPanel.putConstraint(SpringLayout.NORTH, this.propertyValue, 6, SpringLayout.SOUTH, lblTypevalue);
		slContentPanel.putConstraint(SpringLayout.WEST, this.propertyValue, 0, SpringLayout.WEST, lblNameValue);
		this.contentPanel.add(this.propertyValue);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new PropertyUpdatedController(this.ew, this.property, this));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new CancelPropertyEditionController(this));
		
	}
}
