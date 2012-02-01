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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.controller.AboutController;
import fr.n7.saceca.u3du.controller.ExitController;
import fr.n7.saceca.u3du.controller.edition.AgentSelectionEditionController;
import fr.n7.saceca.u3du.controller.edition.CreateObjectEditionController;
import fr.n7.saceca.u3du.controller.edition.DisplayVisionFieldEditionController;
import fr.n7.saceca.u3du.controller.edition.GraphDisplayEditionController;
import fr.n7.saceca.u3du.controller.edition.LaunchSimulationWindowController;
import fr.n7.saceca.u3du.controller.edition.MapResizeController;
import fr.n7.saceca.u3du.controller.edition.ModelsSelectionEditionController;
import fr.n7.saceca.u3du.controller.edition.NewOrRemovedObjectEditionController;
import fr.n7.saceca.u3du.controller.edition.ObjectKillEditionController;
import fr.n7.saceca.u3du.controller.edition.ObjectSelectionEditionController;
import fr.n7.saceca.u3du.controller.edition.PickingEditionController;
import fr.n7.saceca.u3du.controller.edition.PropertiesTableSelectionController;
import fr.n7.saceca.u3du.controller.edition.SaveController;
import fr.n7.saceca.u3du.controller.edition.WorldSpeedController;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.AgentModel;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.WorldObjectModel;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.PropertyModel;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.Service;
import fr.n7.saceca.u3du.model.util.io.storage.Repository;

/**
 * This class represents the 2D swing view of the application.
 * 
 * @author Sylvain Cambon & Jérome Dalbert & Anthony Foulfoin & Johann Legaye & Aurélien Chabot
 */
public class EditionWindow {
	
	/** The frame. */
	public JFrame frame;
	
	/** The south Object infos pane. */
	private JTabbedPane objectInfosPane;
	
	/** The left agents objects tabded pane. */
	private JTabbedPane agentsObjectsTabdedPane;
	
	/** The simulation. */
	public SimulationWindow simulation;
	
	/** The agents model. */
	private DefaultListModel agentsModel;
	
	/** The agents list. */
	public JList agentsList;
	
	/** The agents scroll pane. */
	private JScrollPane agentsScrollPane;
	
	/** The objects tree root. */
	private DefaultMutableTreeNode objectsTreeRoot;
	
	/** The objects tree. */
	public JTree objectsTree;
	
	/** The objects scroll pane. */
	private JScrollPane objectsScrollPane;
	
	/** The objects tree categories. */
	private Map<String, DefaultMutableTreeNode> objectsTreeCategories;
	
	/** The objects tree leafs. */
	private Map<Long, DefaultMutableTreeNode> objectsTreeLeafs;
	
	/** The agent selection controller. */
	private AgentSelectionEditionController agentSelectionController;
	
	/** The object selection controller. */
	private ObjectSelectionEditionController objectSelectionController;
	
	/** The general infos table model. */
	private TableModelDef generalInfosTableModel;
	
	/** The general infos scroll pane. */
	private JScrollPane generalInfosScrollPane;
	
	/** The categories table model. */
	private TableModelDef categoriesTableModel;
	
	/** The categories scroll pane. */
	private JScrollPane categoriesScrollPane;
	
	/** The services table model. */
	private TableModelDef servicesTableModel;
	
	/** The services scroll pane. */
	private JScrollPane servicesScrollPane;
	
	/** The properties table model. */
	public TableModelDef propertiesTableModel;
	
	/** The properties scroll pane. */
	private JScrollPane propertiesScrollPane;
	
	/** The edit panel. */
	private JPanel editPanel;
	
	/** The edit scroll pane. */
	private JScrollPane editScrollPane;
	
	/** The edit grid bag layout. */
	private GridBagLayout editGridBagLayout;
	
	/** The canvas. */
	private JPanel canvas;
	
	/** Contains the id of the current selected object. Null if there is no selected object. */
	public Long currentSelection;
	
	/** The models model. */
	private DefaultListModel modelsModel;
	
	/** The models list. */
	public JList modelsList;
	
	/** The models scroll pane. */
	private JScrollPane modelsScrollPane;
	
	/** The models selection controller. */
	private ModelsSelectionEditionController modelsSelectionController;
	
	/** The model properties table model. */
	private TableModelDef modelPropertiesTableModel;
	
	/** The model properties scroll pane. */
	private JScrollPane modelPropertiesScrollPane;
	
	/** The model categories table model. */
	private TableModelDef modelCategoriesTableModel;
	
	/** The model categories scroll pane. */
	private JScrollPane modelCategoriesScrollPane;
	
	/** The model services table model. */
	private TableModelDef modelServicesTableModel;
	
	/** The model services scroll pane. */
	private JScrollPane modelServicesScrollPane;
	
	/** The model edit panel. */
	private JPanel modelEditPanel;
	
	/** The model edit scroll pane. */
	private JScrollPane modelEditScrollPane;
	
	/** The model edit grid bag layout. */
	private GridBagLayout modelEditGridBagLayout;
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(EditionWindow.class);
	
	/** The file menu. */
	private JMenu fileMenu;
	
	/** The save as menu item. */
	private JMenuItem saveAsMenuItem;
	
	/** The size up button. */
	private JButton sizeUpButton;
	
	/** The size down button. */
	private JButton sizeDownButton;
	
	/** The speed field. */
	public JTextField speedField;
	
	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				EditionWindow window = new EditionWindow(null);
				window.frame.setVisible(true);
			}
		});
	}
	
	/**
	 * Display.
	 */
	public void display() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				EditionWindow.this.initialize();
				EditionWindow.this.getFrame().setVisible(true);
			}
		});
	}
	
	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public EditionWindow() {
		this.initialize();
	}
	
	/**
	 * Create the application.
	 * 
	 * @param sw
	 *            the sw
	 * @wbp.parser.entryPoint
	 */
	public EditionWindow(SimulationWindow sw) {
		this.simulation = sw;
	}
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		this.frame = new JFrame("U3DU-4-SACECA - Edition");
		this.frame.setMinimumSize(new Dimension(900, 762));
		this.frame.setBounds(100, 100, 900, 762);
		
		// Closing operation
		this.frame.addWindowListener(new ExitController());
		
		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);
		
		this.fileMenu = new JMenu("File");
		this.fileMenu.setMnemonic('f');
		menuBar.add(this.fileMenu);
		
		this.saveAsMenuItem = new JMenuItem("Save");
		this.fileMenu.add(this.saveAsMenuItem);
		this.saveAsMenuItem.addActionListener(new SaveController(this, true));
		
		this.saveAsMenuItem = new JMenuItem("Save As...");
		this.fileMenu.add(this.saveAsMenuItem);
		this.saveAsMenuItem.addActionListener(new SaveController(this, false));
		
		JMenu mnMenu = new JMenu("Mode");
		menuBar.add(mnMenu);
		JMenuItem edition = new JMenuItem("Simulation");
		mnMenu.add(edition);
		edition.addActionListener(new LaunchSimulationWindowController(this));
		
		// Help menu
		JMenu helpMenu = new JMenu("?");
		menuBar.add(helpMenu);
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(about);
		about.addActionListener(new AboutController(this.frame));
		
		this.canvas = new JPanel();
		// $hide>>$
		this.canvas = this.simulation.canvas;
		// $hide<<$
		
		// We register the controller in the engine3D in order to receive object picking
		// notifications
		Model.getInstance().getGraphics().getEngine3D().addPickingObserver(new PickingEditionController(this));
		// We register the controller in the engine3D in order to receive new object creation
		// notifications
		Model.getInstance().getGraphics().getEngine3D()
				.addNewObjectObserver(new NewOrRemovedObjectEditionController(this));
		
		// Icon
		java.awt.Image icone = Toolkit.getDefaultToolkit().getImage("./resources/agent_icon.png");
		this.frame.setIconImage(icone);
		
		// The left pane
		this.agentsObjectsTabdedPane = new JTabbedPane(SwingConstants.TOP);
		// The south pane
		this.objectInfosPane = new JTabbedPane(SwingConstants.TOP);
		
		// Agents list
		this.agentsModel = new DefaultListModel();
		this.agentsList = new JList(this.agentsModel);
		this.agentsScrollPane = new JScrollPane(this.agentsList);
		this.agentSelectionController = new AgentSelectionEditionController(this);
		this.agentsList.addListSelectionListener(this.agentSelectionController);
		this.agentsObjectsTabdedPane.addTab("Agents", null, this.agentsScrollPane, null);
		
		// Objects Tree
		this.objectsTreeRoot = new DefaultMutableTreeNode();
		this.objectsTree = new JTree(this.objectsTreeRoot);
		this.objectsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.objectSelectionController = new ObjectSelectionEditionController(this);
		this.objectsTree.addTreeSelectionListener(this.objectSelectionController);
		this.objectsScrollPane = new JScrollPane(this.objectsTree);
		this.objectsTreeCategories = new HashMap<String, DefaultMutableTreeNode>();
		this.objectsTreeLeafs = new HashMap<Long, DefaultMutableTreeNode>();
		this.agentsObjectsTabdedPane.addTab("Objects", null, this.objectsScrollPane, null);
		
		// Models list
		this.modelsModel = new DefaultListModel();
		this.modelsList = new JList(this.modelsModel);
		this.modelsScrollPane = new JScrollPane(this.modelsList);
		this.modelsSelectionController = new ModelsSelectionEditionController(this);
		this.modelsList.addListSelectionListener(this.modelsSelectionController);
		this.agentsObjectsTabdedPane.addTab("Models", null, this.modelsScrollPane, null);
		
		// MODEL PROPERTIES
		this.modelPropertiesTableModel = new TableModelDef(new String[] { "Name", "Default value" });
		JTable modelPropertiesTable = new JTable(this.modelPropertiesTableModel);
		this.modelPropertiesScrollPane = new JScrollPane(modelPropertiesTable);
		
		// MODEL CATEGORIES
		this.modelCategoriesTableModel = new TableModelDef(new String[] { "Name" });
		JTable modelCategoriesTable = new JTable(this.modelCategoriesTableModel);
		this.modelCategoriesScrollPane = new JScrollPane(modelCategoriesTable);
		
		// MODEL SERVICES
		this.modelServicesTableModel = new TableModelDef(new String[] { "Name" });
		JTable modelServicesTable = new JTable(this.modelServicesTableModel);
		this.modelServicesScrollPane = new JScrollPane(modelServicesTable);
		
		// MODEL EDIT
		this.modelEditPanel = new JPanel();
		this.modelEditScrollPane = new JScrollPane(this.modelEditPanel);
		this.modelEditGridBagLayout = new GridBagLayout();
		this.modelEditGridBagLayout.columnWidths = new int[] { 200, 200, 200, 200 };
		this.modelEditPanel.setLayout(this.modelEditGridBagLayout);
		
		// GENERAL INFOS
		this.generalInfosTableModel = new TableModelDef(new String[] { "Name", "Value" });
		JTable generalInfosTable = new JTable(this.generalInfosTableModel);
		this.generalInfosScrollPane = new JScrollPane(generalInfosTable);
		
		// CATEGORIES
		this.categoriesTableModel = new TableModelDef(new String[] { "Name", "Storage label" });
		JTable categoriesTable = new JTable(this.categoriesTableModel);
		this.categoriesScrollPane = new JScrollPane(categoriesTable);
		
		// SERVICES
		this.servicesTableModel = new TableModelDef(new String[] { "Name", "Is active" });
		JTable servicesTable = new JTable(this.servicesTableModel);
		this.servicesScrollPane = new JScrollPane(servicesTable);
		
		// PROPERTIES
		this.propertiesTableModel = new TableModelDef(new String[] { "Property", "Value" });
		JTable propertiesTable = new JTable(this.propertiesTableModel);
		ListSelectionModel listSelectionModel = propertiesTable.getSelectionModel();
		listSelectionModel.addListSelectionListener(new PropertiesTableSelectionController(this));
		this.propertiesScrollPane = new JScrollPane(propertiesTable);
		
		// The edit
		this.editPanel = new JPanel();
		this.editScrollPane = new JScrollPane(this.editPanel);
		this.editGridBagLayout = new GridBagLayout();
		this.editGridBagLayout.columnWidths = new int[] { 200, 200, 200, 200 };
		this.editPanel.setLayout(this.editGridBagLayout);
		
		// Terrain size
		JLabel sizeLabel = new JLabel("Terrain size :");
		this.sizeUpButton = new JButton("+");
		this.sizeDownButton = new JButton("-");
		MapResizeController resizeController = new MapResizeController();
		this.sizeUpButton.addActionListener(resizeController);
		this.sizeDownButton.addActionListener(resizeController);
		
		// WORLD SPEED
		JLabel speedLabel = new JLabel("World speed : ");
		this.speedField = new JTextField(Model.getInstance().getAI().getSimulation().getTickPeriod() + "");
		this.speedField.setColumns(10);
		
		JButton speedOk = new JButton("OK");
		speedOk.addActionListener(new WorldSpeedController(this));
		
		GroupLayout groupLayout = new GroupLayout(this.frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addComponent(this.agentsObjectsTabdedPane, GroupLayout.PREFERRED_SIZE, 198,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(
										groupLayout
												.createParallelGroup(Alignment.LEADING)
												.addGroup(
														groupLayout
																.createSequentialGroup()
																.addGap(6)
																.addComponent(this.canvas, GroupLayout.PREFERRED_SIZE,
																		680, GroupLayout.PREFERRED_SIZE))
												.addGroup(
														groupLayout
																.createSequentialGroup()
																.addGap(33)
																.addComponent(sizeLabel)
																.addGap(18)
																.addComponent(this.sizeUpButton)
																.addPreferredGap(ComponentPlacement.RELATED)
																.addComponent(this.sizeDownButton)
																.addGap(30)
																.addComponent(speedLabel)
																.addPreferredGap(ComponentPlacement.RELATED)
																.addComponent(this.speedField,
																		GroupLayout.PREFERRED_SIZE, 44,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(speedOk))))
				.addComponent(this.objectInfosPane, GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(Alignment.LEADING)
														.addComponent(this.agentsObjectsTabdedPane,
																GroupLayout.PREFERRED_SIZE, 509,
																GroupLayout.PREFERRED_SIZE)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(this.canvas,
																				GroupLayout.PREFERRED_SIZE, 480,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(this.sizeUpButton)
																						.addComponent(sizeLabel)
																						.addComponent(
																								this.sizeDownButton)
																						.addComponent(speedLabel)
																						.addComponent(
																								this.speedField,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(speedOk))))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(this.objectInfosPane, GroupLayout.PREFERRED_SIZE, 188,
												GroupLayout.PREFERRED_SIZE).addContainerGap(0, Short.MAX_VALUE)));
		
		this.frame.getContentPane().setLayout(groupLayout);
		
		// $hide>>$
		this.initModelsList();
		this.initAgentsList();
		this.initObjectsTree();
		// $hide<<$
	}
	
	/**
	 * Return the frame.
	 * 
	 * @return the frame
	 */
	private JFrame getFrame() {
		return this.frame;
	}
	
	/**
	 * Initialize or reinitialize the models selection list. Invoked later.
	 */
	public void initModelsList() {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				EditionWindow.this.modelsList.removeAll();
				
				Repository<WorldObjectModel> objectsRepository = Model.getInstance().getAI()
						.getEntitiesFactoryMaterials().getWorldObjectModelRepository();
				Repository<AgentModel> agentsRepository = Model.getInstance().getAI().getEntitiesFactoryMaterials()
						.getAgentModelRepository();
				
				for (WorldObjectModel worldObjectModel : objectsRepository) {
					EditionWindow.this.modelsModel.addElement(worldObjectModel.getName());
				}
				
				for (AgentModel agentModel : agentsRepository) {
					EditionWindow.this.modelsModel.addElement(agentModel.getName());
				}
			}
		});
	}
	
	/**
	 * Initialize or reinitialize the agent selection list. Invoked later.
	 */
	public void initAgentsList() {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				EditionWindow.this.agentsModel.clear();
				
				Collection<Agent> agents = Model.getInstance().getAI().getWorld().getAgents();
				
				for (Agent agent : agents) {
					try {
						EditionWindow.this.agentsModel.addElement(agent.getPropertiesContainer().getString(
								Internal.Agent.NAME)
								+ " (#" + agent.getId() + ")");
					} catch (UnknownPropertyException e) {
						logger.error("Cannot properly add a name to the elements. A default name is provided.", e);
						EditionWindow.this.agentsModel.addElement("<Unknown Name> #" + agent.getId() + ")");
					}
				}
				
			}
		});
		
	}
	
	/**
	 * Initialize or reinitialize the object selection tree. The objects are sorted by model.
	 * Invoked later.
	 */
	public void initObjectsTree() {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// We remove the object of its category node
				EditionWindow.this.objectsTreeRoot = new DefaultMutableTreeNode();
				EditionWindow.this.objectsTreeCategories.clear();
				EditionWindow.this.objectsTreeLeafs.clear();
				((DefaultTreeModel) EditionWindow.this.objectsTree.getModel())
						.setRoot(EditionWindow.this.objectsTreeRoot);
				
				Collection<WorldObject> objects = Model.getInstance().getAI().getWorld().getReactiveObjects();
				
				for (WorldObject worldObject : objects) {
					// We check whether the model node has already been created or not
					DefaultMutableTreeNode modelTreeNode = EditionWindow.this.objectsTreeCategories.get(worldObject
							.getModelName());
					if (modelTreeNode == null) {
						modelTreeNode = new DefaultMutableTreeNode(worldObject.getModelName());
						EditionWindow.this.objectsTreeRoot.add(modelTreeNode);
						EditionWindow.this.objectsTreeCategories.put(worldObject.getModelName(), modelTreeNode);
					}
					DefaultMutableTreeNode objectTreeNode = new DefaultMutableTreeNode(worldObject.getId());
					modelTreeNode.add(objectTreeNode);
					EditionWindow.this.objectsTreeLeafs.put(worldObject.getId(), objectTreeNode);
				}
				
				// We expand the root node so that the categories become visible
				EditionWindow.this.objectsTree.expandRow(0);
			}
		});
		
	}
	
	/**
	 * Display the information tabs of the related model.
	 * 
	 * @param model
	 *            The model for which we want to display the informations
	 */
	private void showModelInfos(final WorldObjectModel model) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// We remove all the tabs
				EditionWindow.this.objectInfosPane.removeAll();
				
				// We show the edit
				EditionWindow.this.showModelEdit(model);
				
				// We show the properties
				EditionWindow.this.showModelProperties(model);
				
				// We show the categories
				EditionWindow.this.showModelCategories(model);
				
				// We EditionWindow the services
				EditionWindow.this.showModelServices(model);
			}
		});
	}
	
	/**
	 * Display the general properties tab of a model.
	 * 
	 * @param model
	 *            The model for which we want to display the informations
	 */
	private void showModelProperties(WorldObjectModel model) {
		// We show the properties
		this.objectInfosPane.addTab("Properties", null, this.modelPropertiesScrollPane, null);
		
		this.modelPropertiesTableModel.clear();
		
		Set<PropertyModel<?>> properties = model.getProperties();
		int row = 0;
		for (PropertyModel<?> property : properties) {
			this.modelPropertiesTableModel.setValueAt(property.getName(), row, 0);
			this.modelPropertiesTableModel.setValueAt(property.getDefaultValue().toString(), row, 1);
			row++;
		}
	}
	
	/**
	 * Display the categories tab of a model.
	 * 
	 * @param model
	 *            The model for which we want to display the informations
	 */
	private void showModelCategories(WorldObjectModel model) {
		// We show the properties
		this.objectInfosPane.addTab("Categories", null, this.modelCategoriesScrollPane, null);
		
		this.modelCategoriesTableModel.clear();
		
		Set<String> properties = model.getCategoriesNames();
		int row = 0;
		for (String property : properties) {
			this.modelCategoriesTableModel.setValueAt(property, row, 0);
			row++;
		}
	}
	
	/**
	 * Display the services tab of a model.
	 * 
	 * @param model
	 *            The model for which we want to display the informations
	 */
	private void showModelServices(WorldObjectModel model) {
		// We show the properties
		this.objectInfosPane.addTab("Services", null, this.modelServicesScrollPane, null);
		
		this.modelServicesTableModel.clear();
		
		Set<String> properties = model.getServicesNames();
		int row = 0;
		for (String property : properties) {
			this.modelServicesTableModel.setValueAt(property, row, 0);
			row++;
		}
	}
	
	/**
	 * Display the edit tab of a model.
	 * 
	 * @param model
	 *            The model for which we want to display the informations
	 */
	private void showModelEdit(WorldObjectModel model) {
		
		this.objectInfosPane.addTab("Edit", null, this.modelEditScrollPane, null);
		
		this.modelEditPanel.removeAll();
		
		JLabel createLabel = new JLabel("Create a new instance");
		JButton createButton = new JButton("Create");
		createButton.addActionListener(new CreateObjectEditionController(model, this));
		
		this.modelEditPanel.add(createLabel);
		this.modelEditPanel.add(createButton);
		
		GridBagConstraints createLabelConstraint = new GridBagConstraints();
		createLabelConstraint.gridy = 0;
		createLabelConstraint.gridx = 0;
		createLabelConstraint.gridwidth = 1;
		createLabelConstraint.gridheight = 1;
		this.modelEditGridBagLayout.setConstraints(createLabel, createLabelConstraint);
		
		GridBagConstraints createButtonConstraint = new GridBagConstraints();
		createButtonConstraint.gridy = 0;
		createButtonConstraint.gridx = 1;
		createButtonConstraint.gridwidth = 1;
		createButtonConstraint.gridheight = 1;
		this.modelEditGridBagLayout.setConstraints(createButton, createButtonConstraint);
	}
	
	/**
	 * Display all the informations tabs existing for a given agent. This method automatically call
	 * the showObjectInfos method given that an agent is also an object, and and agent-specifics
	 * tabs.
	 * 
	 * @param agent
	 *            the agent for which we want to display the information tabs
	 * @param update
	 *            False if the tabs must be removed and recreated (to display a different agent).
	 *            True if the tabs must just be updated (the agent is still the same.
	 */
	private void showAgentInfos(final Agent agent, final boolean update) {
		
		// We show the tabs common with the objects
		EditionWindow.this.showObjectInfos(agent, update);
		
	}
	
	/**
	 * Display all the informations tabs existing for a given object. This method automatically
	 * remove all the tabs from the objectInfos tabs pane, and rebuild the tabs
	 * 
	 * @param object
	 *            the object for which we want to display the information tabs
	 * @param update
	 *            False if the tabs must be removed and recreated (to display a different object).
	 *            True if the tabs must just be updated (the object is still the same).
	 */
	private void showObjectInfos(final WorldObject object, final boolean update) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// We remove all the tabs
				if (!update) {
					EditionWindow.this.objectInfosPane.removeAll();
					// Recreate tabs
					EditionWindow.this.objectInfosPane.addTab("General infos", null,
							EditionWindow.this.generalInfosScrollPane, null);
					EditionWindow.this.objectInfosPane.addTab("Properties", null,
							EditionWindow.this.propertiesScrollPane, null);
					EditionWindow.this.objectInfosPane.addTab("Categories", null,
							EditionWindow.this.categoriesScrollPane, null);
					EditionWindow.this.objectInfosPane.addTab("Services", null, EditionWindow.this.servicesScrollPane,
							null);
					EditionWindow.this.objectInfosPane.addTab("Display", null, EditionWindow.this.editScrollPane, null);
					
					// We show the edit
					EditionWindow.this.showObjectEdit(object);
				}
				
				// We show the properties
				EditionWindow.this.showObjectProperties(object);
				
				// We show the categories
				EditionWindow.this.showObjectCategories(object);
				
				// We EditionWindow the services
				EditionWindow.this.showObjectServices(object);
				
				// We show the general infos
				EditionWindow.this.showObjectGeneralInfos(object);
				
			}
		});
	}
	
	/**
	 * Display the edit tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectEdit(WorldObject object) {
		
		this.editPanel.removeAll();
		
		JLabel destructLabel = new JLabel("Destruct Object");
		JButton destructButton = new JButton("Remove");
		destructButton.addActionListener(new ObjectKillEditionController(object.getId(), this));
		
		this.editPanel.add(destructLabel);
		this.editPanel.add(destructButton);
		
		GridBagConstraints destructLabelConstraint = new GridBagConstraints();
		destructLabelConstraint.gridy = 0;
		destructLabelConstraint.gridx = 0;
		destructLabelConstraint.gridwidth = 1;
		destructLabelConstraint.gridheight = 1;
		this.editGridBagLayout.setConstraints(destructLabel, destructLabelConstraint);
		
		GridBagConstraints destructButtonConstraint = new GridBagConstraints();
		destructButtonConstraint.gridy = 0;
		destructButtonConstraint.gridx = 1;
		destructButtonConstraint.gridwidth = 1;
		destructButtonConstraint.gridheight = 1;
		this.editGridBagLayout.setConstraints(destructButton, destructButtonConstraint);
		
		JLabel graphLabel = new JLabel("Graph");
		String buttonText = Model.getInstance().getGraphics().getEngine3D().isWalkableGraphDisplayed() ? "Mask"
				: "Draw";
		JButton graphButton = new JButton(buttonText);
		graphButton.addActionListener(new GraphDisplayEditionController());
		
		this.editPanel.add(graphLabel);
		this.editPanel.add(graphButton);
		
		GridBagConstraints graphLabelConstraint = new GridBagConstraints();
		graphLabelConstraint.gridy = 0;
		graphLabelConstraint.gridx = 2;
		graphLabelConstraint.gridwidth = 1;
		graphLabelConstraint.gridheight = 1;
		this.editGridBagLayout.setConstraints(graphLabel, graphLabelConstraint);
		
		GridBagConstraints graphButtonConstraint = new GridBagConstraints();
		graphButtonConstraint.gridy = 0;
		graphButtonConstraint.gridx = 3;
		graphButtonConstraint.gridwidth = 1;
		graphButtonConstraint.gridheight = 1;
		this.editGridBagLayout.setConstraints(graphButton, graphButtonConstraint);
		
		if (object instanceof Agent) {
			JLabel visionFieldLabel = new JLabel("Vision field");
			JButton visionFieldButton = new JButton("Display");
			visionFieldButton.addActionListener(new DisplayVisionFieldEditionController(object.getId()));
			this.editPanel.add(visionFieldLabel);
			this.editPanel.add(visionFieldButton);
			
			GridBagConstraints visionFieldLabelConstraint = new GridBagConstraints();
			visionFieldLabelConstraint.gridy = 1;
			visionFieldLabelConstraint.gridx = 0;
			visionFieldLabelConstraint.gridwidth = 1;
			visionFieldLabelConstraint.gridheight = 1;
			this.editGridBagLayout.setConstraints(visionFieldLabel, visionFieldLabelConstraint);
			
			GridBagConstraints visionFieldButtonConstraint = new GridBagConstraints();
			visionFieldButtonConstraint.gridy = 1;
			visionFieldButtonConstraint.gridx = 1;
			visionFieldButtonConstraint.gridwidth = 1;
			visionFieldButtonConstraint.gridheight = 1;
			this.editGridBagLayout.setConstraints(visionFieldButton, visionFieldButtonConstraint);
		}
	}
	
	/**
	 * Display the general infos tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectGeneralInfos(WorldObject object) {
		
		// We show the infos
		this.generalInfosTableModel.clear();
		
		this.generalInfosTableModel.setValueAt("Model name", 0, 0);
		this.generalInfosTableModel.setValueAt(object.getModelName(), 0, 1);
		
		this.generalInfosTableModel.setValueAt("Position", 1, 0);
		this.generalInfosTableModel.setValueAt(object.getPosition().toString(), 1, 1);
		
		this.generalInfosTableModel.setValueAt("Is paused", 2, 0);
		this.generalInfosTableModel.setValueAt(object.isPause(), 2, 1);
		
		this.generalInfosTableModel.setValueAt("Object id", 3, 0);
		this.generalInfosTableModel.setValueAt(object.getId(), 3, 1);
	}
	
	/**
	 * Display the categories tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectCategories(WorldObject object) {
		
		// We show the categories
		
		this.categoriesTableModel.clear();
		
		Collection<Category> categories = object.getCategories();
		int row = 0;
		for (Category category : categories) {
			this.categoriesTableModel.setValueAt(category.getName(), row, 0);
			this.categoriesTableModel.setValueAt(category.getStorageLabel(), row, 1);
			row++;
		}
	}
	
	/**
	 * Display the services tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectServices(WorldObject object) {
		// We show the services
		
		this.servicesTableModel.clear();
		
		Collection<Service> services = object.getServices();
		int row = 0;
		for (Service service : services) {
			this.servicesTableModel.setValueAt(service.getName(), row, 0);
			this.servicesTableModel.setValueAt(service.isActive(), row, 1);
			row++;
		}
	}
	
	/**
	 * Display the general properties tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectProperties(WorldObject object) {
		// We show the properties
		
		this.propertiesTableModel.clear();
		
		Collection<Property<?>> properties = object.getPropertiesContainer().getProperties();
		int row = 0;
		for (Property<?> property : properties) {
			this.propertiesTableModel.setValueAt(property.getModel().getName(), row, 0);
			this.propertiesTableModel.setValueAt(property.getValue(), row, 1);
			row++;
		}
	}
	
	/**
	 * This method is the only way for an external class to show tabs informations on a given
	 * object. This method has to be called from both 2D and 3D controllers if a new object is
	 * selected. If the given object is not selected in the swing agent or object list, this method
	 * select it. The selection of the object in the agent list or the object tree does not produce
	 * events. The tabs corresponding to the given object are automatically displayed. If the method
	 * has to ask JME to put the focus on the selected object, the boolean has to be true.
	 * 
	 * @param id
	 *            The object id
	 * @param focusJMEOnSelection
	 *            True if JME must focus on the selected object
	 */
	public void setAgentObjectSelection(long id, boolean focusJMEOnSelection) {
		
		WorldObject worldObject = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		if (worldObject != null) {
			
			boolean update = false; // True if we have update the current object tabs, false if a
			// new object is selected
			
			if (this.currentSelection != null && this.currentSelection == id) {
				update = true;
			}
			this.currentSelection = id;
			// If a model is selected, we unselect it
			this.modelsList.clearSelection();
			if (worldObject instanceof Agent) {
				// If this is a new selection
				if (!update) {
					// If an object is selected, we unselect it
					this.objectsTree.clearSelection();
					// We disable the listener otherwise the new selection in the list will
					// throw an new event, and then, the controller will ask JME to focus on the
					// selected object even if user just clicked on it in the JME canvas
					this.agentsList.removeListSelectionListener(this.agentSelectionController);
					// We select the agent
					try {
						this.agentsList.setSelectedValue(
								worldObject.getPropertiesContainer().getString(Internal.Agent.NAME) + " (#"
										+ worldObject.getId() + ")", true);
					} catch (UnknownPropertyException e) {
						logger.error("Cannot properly select the agent.", e);
					}
					// We enable the listener so as the controller can detect the selection
					// of
					// a new agent from a user clic.
					this.agentsList.addListSelectionListener(this.agentSelectionController);
					// We open the agent tab
					this.agentsObjectsTabdedPane.setSelectedIndex(0);
				}
				
				// We show the agent infos
				this.showAgentInfos((Agent) worldObject, update);
				
			} else {
				// If this is a new selection
				if (!update) {
					// If an agent is selected, we unselect it
					this.agentsList.clearSelection();
					// We disable the listener otherwise the new selection in the list will
					// throw
					// an new event, and then, the controller will ask JME to focus on the
					// selected
					// object
					// even if user just clicked on it in the JME canvas
					this.objectsTree.removeTreeSelectionListener(EditionWindow.this.objectSelectionController);
					// We select the object
					TreePath path = new TreePath(this.objectsTreeLeafs.get(id).getPath());
					this.objectsTree.setSelectionPath(path);
					// We enable the listener so as the controller can detect the selection
					// of
					// a new object from a user clic.
					this.objectsTree.addTreeSelectionListener(this.objectSelectionController);
					// We open the object tab
					this.agentsObjectsTabdedPane.setSelectedIndex(1);
				}
				// We show the object infos
				this.showObjectInfos(worldObject, update);
				
			}
			if (focusJMEOnSelection) {
				// We set the focus on the object id
				Model.getInstance().getGraphics().getEngine3D().setFocusOn(id);
			}
		}
	}
	
	/**
	 * This method has to be called when a model is selected in the models list.
	 * 
	 * @param modelName
	 *            The model name
	 */
	public void setModelSelection(String modelName) {
		
		this.currentSelection = null;
		// If an agent is selected, we unselect it
		this.agentsList.clearSelection();
		// If an object is selected, we unselect it
		this.objectsTree.clearSelection();
		// we show the model tabs
		WorldObjectModel objectModel = Model.getInstance().getAI().getEntitiesFactoryMaterials()
				.getWorldObjectModelRepository().get(modelName);
		if (objectModel == null) {
			// if the model is not a world object, it's maybe an agent
			objectModel = Model.getInstance().getAI().getEntitiesFactoryMaterials().getAgentModelRepository()
					.get(modelName);
		}
		if (objectModel != null) {
			// If the model exists
			this.showModelInfos(objectModel);
		}
	}
	
	/**
	 * Remove the specified object from the swing interface. Must be called before the AI
	 * destruction
	 * 
	 * @param id
	 *            the object id
	 */
	public void removeObject(final long id) {
		
		WorldObject worldObject = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		this.currentSelection = null;
		// if it is an agent
		if (worldObject instanceof Agent) {
			// We remove the agent from the agent list
			try {
				this.agentsModel.removeElement(worldObject.getPropertiesContainer().getString(Internal.Agent.NAME)
						+ " (#" + worldObject.getId() + ")");
			} catch (UnknownPropertyException e) {
				logger.error("The object may have not been removed.", e);
			}
		} else {
			// We remove the object from the object tree
			DefaultMutableTreeNode objectNode = this.objectsTreeLeafs.get(id);
			
			// If it is the only child of its parent (the category), we also remove the
			// parent.
			DefaultMutableTreeNode category = (DefaultMutableTreeNode) objectNode.getParent();
			if (category.getChildCount() == 1) {
				// We remove the model category
				((DefaultTreeModel) this.objectsTree.getModel()).removeNodeFromParent(category);
				this.objectsTreeCategories.remove(category.getUserObject());
			}
			// We remove the object of its category node
			((DefaultTreeModel) this.objectsTree.getModel()).removeNodeFromParent(objectNode);
			// Of the nodes map
			this.objectsTreeLeafs.remove(id);
		}
		this.objectInfosPane.removeAll();
		
	}
}