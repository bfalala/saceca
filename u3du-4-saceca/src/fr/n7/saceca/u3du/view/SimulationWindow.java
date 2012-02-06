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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.BorderFactory;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import fr.n7.saceca.u3du.controller.AboutController;
import fr.n7.saceca.u3du.controller.ExitController;
import fr.n7.saceca.u3du.controller.simulation.AIChangedController;
import fr.n7.saceca.u3du.controller.simulation.AgentSelectionController;
import fr.n7.saceca.u3du.controller.simulation.ConsoleSendController;
import fr.n7.saceca.u3du.controller.simulation.DisplayVisionFieldController;
import fr.n7.saceca.u3du.controller.simulation.GraphDisplayController;
import fr.n7.saceca.u3du.controller.simulation.LaunchEditionWindowController;
import fr.n7.saceca.u3du.controller.simulation.NewOrRemovedObjectController;
import fr.n7.saceca.u3du.controller.simulation.ObjectSelectionController;
import fr.n7.saceca.u3du.controller.simulation.PickingController;
import fr.n7.saceca.u3du.controller.simulation.StartSimulationController;
import fr.n7.saceca.u3du.model.Model;
import fr.n7.saceca.u3du.model.ai.Internal;
import fr.n7.saceca.u3du.model.ai.agent.Agent;
import fr.n7.saceca.u3du.model.ai.agent.Emotion;
import fr.n7.saceca.u3du.model.ai.agent.Gauge;
import fr.n7.saceca.u3du.model.ai.agent.memory.Memory;
import fr.n7.saceca.u3du.model.ai.agent.memory.MemoryElement;
import fr.n7.saceca.u3du.model.ai.agent.module.communication.message.Message;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.Plan;
import fr.n7.saceca.u3du.model.ai.agent.module.planning.PlanElement;
import fr.n7.saceca.u3du.model.ai.agent.module.reasoning.MMGoal;
import fr.n7.saceca.u3du.model.ai.category.Category;
import fr.n7.saceca.u3du.model.ai.object.WorldObject;
import fr.n7.saceca.u3du.model.ai.object.properties.Property;
import fr.n7.saceca.u3du.model.ai.object.properties.UnknownPropertyException;
import fr.n7.saceca.u3du.model.ai.service.Service;

// TODO: Auto-generated Javadoc
/**
 * This class represents the 2D swing view of the application.
 * 
 * @author Sylvain Cambon & Jérome Dalbert & Anthony Foulfoin & Johann Legaye & Aurélien Chabot
 */
public class SimulationWindow {
	
	/** The edition window. */
	public EditionWindow editionWindow;
	
	/** The frame. */
	public JFrame frame;
	
	/** The btn start. */
	public JButton btnStart;
	
	/** The time. */
	public JLabel time;
	
	/** The canvas. */
	public JPanel canvas;
	
	/** The agents model. */
	private DefaultListModel agentsModel;
	
	/** The properties table model. */
	private TableModelDef propertiesTableModel;
	
	/** The agents list. */
	public JList agentsList;
	
	/** The south Object infos pane. */
	private JTabbedPane objectInfosPane;
	
	/** The categories table model. */
	private TableModelDef categoriesTableModel;
	
	/** The services table model. */
	private TableModelDef servicesTableModel;
	
	/** The gauges panel. */
	private JPanel gaugesPanel;
	
	/** The emotions panel. */
	private JPanel emotionsPanel;
	
	/** The gauges scroll pane. */
	private JScrollPane gaugesScrollPane;
	
	/** The general infos table model. */
	private TableModelDef generalInfosTableModel;
	
	/** The objects tree. */
	public JTree objectsTree;
	
	/** The objects tree root. */
	private DefaultMutableTreeNode objectsTreeRoot;
	
	/** The objects tree categories. */
	private Map<String, DefaultMutableTreeNode> objectsTreeCategories;
	
	/** The objects tree leafs. */
	private Map<Long, DefaultMutableTreeNode> objectsTreeLeafs;
	
	/** The left agents objects tabded pane. */
	private JTabbedPane agentsObjectsTabdedPane;
	
	/** The general infos scroll pane. */
	private JScrollPane generalInfosScrollPane;
	
	/** The properties scroll pane. */
	private JScrollPane propertiesScrollPane;
	
	/** The services scroll pane. */
	private JScrollPane servicesScrollPane;
	
	/** The categories scroll pane. */
	private JScrollPane categoriesScrollPane;
	
	/** The console panel. */
	private JPanel consolePanel;
	
	/** The belongings table model. */
	private TableModelDef belongingsTableModel;
	
	/** The belongings scroll pane. */
	private JScrollPane belongingsScrollPane;
	
	/** The display panel. */
	private JPanel displayPanel;
	
	/** The display scroll pane. */
	private JScrollPane displayScrollPane;
	
	/** The memory scroll pane. */
	private JScrollPane memoryScrollPane;
	
	/** The messages inbox table model. */
	private TableModelDef messagesInboxTableModel;
	
	/** The messages inbox scroll pane. */
	private JScrollPane messagesInboxScrollPane;
	
	/** The planning table model. */
	private TableModelDef goalsTableModel;
	
	/** The planning scroll pane. */
	private JScrollPane goalsScrollPane;
	
	/** The agent selection controller. */
	private AgentSelectionController agentSelectionController;
	
	/** The object selection controller. */
	private ObjectSelectionController objectSelectionController;
	
	/** the gauges layout. */
	private GridBagLayout gaugesGridBagLayout;
	
	private GridBagLayout emotionsGridLayout;
	
	private GridBagLayout secondaryEmotionsGridLayout;
	
	/** The emmited messages table model. */
	private TableModelDef emmitedMessagesTableModel;
	
	/** The emmited messages scroll pane. */
	private JScrollPane emmitedMessagesScrollPane;
	
	/** The display grid bag layout. */
	private GridBagLayout displayGridBagLayout;
	
	/** The agents scroll pane. */
	private JScrollPane agentsScrollPane;
	
	/** The objects scroll pane. */
	private JScrollPane objectsScrollPane;
	
	/** The memory tree root. */
	private DefaultMutableTreeNode memoryTreeRoot;
	
	/** The memory tree. */
	private JTree memoryTree;
	
	/** Contains the id of the current selected object. Null if there is no selected object. */
	public Long currentSelection;
	
	/** The plan table model. */
	private TableModelDef planTableModel;
	
	/** The plan scroll pane. */
	private JScrollPane planScrollPane;
	
	/** The console text field. */
	public JTextField consoleTextField;
	
	/** The console. */
	public Console console;
	
	/** The console text pane scroll. */
	private JScrollPane consoleTextPaneScroll;
	
	private JPanel secondaryEmotionsPanel;
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(SimulationWindow.class);
	
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
				SimulationWindow window = new SimulationWindow();
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
				SimulationWindow.this.initialize();
				SimulationWindow.this.getFrame().setVisible(true);
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public SimulationWindow() {
		this.initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame("U3DU-4-SACECA - Simulation");
		this.frame.setMinimumSize(new Dimension(900, 762));
		this.frame.setBounds(100, 100, 900, 762);
		
		// Closing operation
		this.frame.addWindowListener(new ExitController());
		
		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);
		
		// Window menu
		JMenu mnMenu = new JMenu("Mode");
		menuBar.add(mnMenu);
		JMenuItem edition = new JMenuItem("Edition");
		mnMenu.add(edition);
		edition.addActionListener(new LaunchEditionWindowController(this));
		
		// Help menu
		JMenu helpMenu = new JMenu("?");
		menuBar.add(helpMenu);
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(about);
		about.addActionListener(new AboutController(this.frame));
		
		// $hide>>$
		if (this.canvas == null) {
			// $hide<<$
			this.canvas = new JPanel();
			// $hide>>$
			this.canvas = new JMECanvas();
			// $hide<<$
			// $hide>>$
		}
		// $hide<<$
		
		// Icon
		java.awt.Image icone = Toolkit.getDefaultToolkit().getImage("./data/gui/agent_icon.png");
		this.frame.setIconImage(icone);
		
		// We register the controller in the AI in order to receive Aichanged
		// notifications
		Model.getInstance().getAI().getSimulation().addSimulationListener(new AIChangedController(this));
		// We register the controller in the engine3D in order to receive object picking
		// notifications
		Model.getInstance().getGraphics().getEngine3D().addPickingObserver(new PickingController(this));
		// We register the controller in the engine3D in order to receive new object creation
		// notifications
		Model.getInstance().getGraphics().getEngine3D().addNewObjectObserver(new NewOrRemovedObjectController(this));
		
		this.btnStart = new JButton("Start simulation");
		this.btnStart.addActionListener(new StartSimulationController());
		
		// The left pane
		this.agentsObjectsTabdedPane = new JTabbedPane(SwingConstants.TOP);
		// The south pane
		this.objectInfosPane = new JTabbedPane(SwingConstants.TOP);
		
		// SIMULATION TIME
		JLabel lblNewLabel = new JLabel("Simulation Time :");
		this.time = new JLabel("0h 0m");
		
		// THE GAUGES
		this.gaugesPanel = new JPanel();
		this.gaugesScrollPane = new JScrollPane(this.gaugesPanel);
		
		this.gaugesGridBagLayout = new GridBagLayout();
		this.gaugesGridBagLayout.columnWidths = new int[] { 400, 200, 200 };
		this.gaugesPanel.setLayout(this.gaugesGridBagLayout);
		
		// THE EMOTIONS
		this.emotionsPanel = new JPanel();
		this.emotionsGridLayout = new GridBagLayout();
		this.emotionsGridLayout.columnWidths = new int[] { 200, 200, 200, 200 };
		this.emotionsPanel.setLayout(this.emotionsGridLayout);
		
		// THE SECONDARY EMOTIONS
		this.secondaryEmotionsPanel = new JPanel();
		this.secondaryEmotionsGridLayout = new GridBagLayout();
		this.secondaryEmotionsGridLayout.columnWidths = new int[] { 200, 200, 200, 200 };
		this.secondaryEmotionsPanel.setLayout(this.secondaryEmotionsGridLayout);
		
		// The display
		this.displayPanel = new JPanel();
		this.displayScrollPane = new JScrollPane(this.displayPanel);
		this.displayGridBagLayout = new GridBagLayout();
		this.displayGridBagLayout.columnWidths = new int[] { 200, 200, 200, 200 };
		this.displayPanel.setLayout(this.displayGridBagLayout);
		
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
		this.propertiesScrollPane = new JScrollPane(propertiesTable);
		
		// Belongings
		this.belongingsTableModel = new TableModelDef(new String[] { "Object model", "Id" });
		JTable belongingsTable = new JTable(this.belongingsTableModel);
		this.belongingsScrollPane = new JScrollPane(belongingsTable);
		
		// MEMORY
		this.memoryTreeRoot = new DefaultMutableTreeNode();
		this.memoryTree = new JTree(this.memoryTreeRoot);
		this.memoryScrollPane = new JScrollPane(this.memoryTree);
		
		// Goals
		this.goalsTableModel = new TableModelDef(new String[] { "Goal", "Priority", "Reachable", "Reached" });
		JTable goalsTable = new JTable(this.goalsTableModel);
		this.goalsScrollPane = new JScrollPane(goalsTable);
		
		// Planning
		this.planTableModel = new TableModelDef(new String[] { "Service", "Parameters", "Provider" });
		JTable planTable = new JTable(this.planTableModel);
		this.planScrollPane = new JScrollPane(planTable);
		
		// inbox messages
		this.messagesInboxTableModel = new TableModelDef(new String[] { "Sender", "Message" });
		JTable messagesInboxTable = new JTable(this.messagesInboxTableModel);
		this.messagesInboxScrollPane = new JScrollPane(messagesInboxTable);
		
		// emmited messages
		this.emmitedMessagesTableModel = new TableModelDef(new String[] { "Message" });
		JTable emmitedMessagesTable = new JTable(this.emmitedMessagesTableModel);
		this.emmitedMessagesScrollPane = new JScrollPane(emmitedMessagesTable);
		
		// Agents list
		this.agentsModel = new DefaultListModel();
		this.agentsList = new JList(this.agentsModel);
		this.agentsScrollPane = new JScrollPane(this.agentsList);
		this.agentSelectionController = new AgentSelectionController(this);
		this.agentsList.addListSelectionListener(this.agentSelectionController);
		this.agentsObjectsTabdedPane.addTab("Agents", null, this.agentsScrollPane, null);
		
		// Objects Tree
		this.objectsTreeRoot = new DefaultMutableTreeNode();
		this.objectsTree = new JTree(this.objectsTreeRoot);
		this.objectsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.objectSelectionController = new ObjectSelectionController(this);
		this.objectsTree.addTreeSelectionListener(this.objectSelectionController);
		this.objectsScrollPane = new JScrollPane(this.objectsTree);
		this.objectsTreeCategories = new HashMap<String, DefaultMutableTreeNode>();
		this.objectsTreeLeafs = new HashMap<Long, DefaultMutableTreeNode>();
		this.agentsObjectsTabdedPane.addTab("Objects", null, this.objectsScrollPane, null);
		
		// CONSOLE
		this.consolePanel = new JPanel();
		JTextPane consoleTextPane = new JTextPane();
		this.consoleTextPaneScroll = new JScrollPane(consoleTextPane);
		this.consoleTextField = new JTextField();
		this.consoleTextField.setColumns(10);
		ConsoleSendController csc = new ConsoleSendController(this);
		this.consoleTextField.addActionListener(csc);
		JButton consoleBtnSend = new JButton("SEND");
		consoleBtnSend.addActionListener(csc);
		this.console = new Console(consoleTextPane);
		
		GroupLayout groupLayout = new GroupLayout(this.frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addComponent(this.agentsObjectsTabdedPane, GroupLayout.PREFERRED_SIZE, 198,
										GroupLayout.PREFERRED_SIZE)
								.addGap(6)
								.addGroup(
										groupLayout
												.createParallelGroup(Alignment.LEADING)
												.addComponent(this.canvas, GroupLayout.PREFERRED_SIZE, 680,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(
														groupLayout
																.createSequentialGroup()
																.addComponent(lblNewLabel)
																.addGap(10)
																.addComponent(this.time, GroupLayout.PREFERRED_SIZE,
																		288, GroupLayout.PREFERRED_SIZE)
																.addGap(4)
																.addComponent(this.btnStart,
																		GroupLayout.PREFERRED_SIZE, 204,
																		GroupLayout.PREFERRED_SIZE))))
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
																		.addGap(6)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblNewLabel,
																								GroupLayout.PREFERRED_SIZE,
																								23,
																								GroupLayout.PREFERRED_SIZE)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGap(1)
																										.addComponent(
																												this.time,
																												GroupLayout.PREFERRED_SIZE,
																												21,
																												GroupLayout.PREFERRED_SIZE))
																						.addComponent(this.btnStart))))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(this.objectInfosPane, GroupLayout.PREFERRED_SIZE, 188,
												GroupLayout.PREFERRED_SIZE).addContainerGap(0, Short.MAX_VALUE)));
		
		this.frame.getContentPane().setLayout(groupLayout);
		
		GroupLayout glConsolePanel = new GroupLayout(this.consolePanel);
		glConsolePanel.setHorizontalGroup(glConsolePanel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						glConsolePanel
								.createSequentialGroup()
								.addComponent(this.consoleTextField, GroupLayout.PREFERRED_SIZE, 403,
										GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(consoleBtnSend).addContainerGap(377, Short.MAX_VALUE))
				.addComponent(this.consoleTextPaneScroll, GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE));
		glConsolePanel.setVerticalGroup(glConsolePanel.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				glConsolePanel
						.createSequentialGroup()
						.addComponent(this.consoleTextPaneScroll, GroupLayout.PREFERRED_SIZE, 126,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(
								glConsolePanel
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(this.consoleTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(consoleBtnSend))));
		this.consolePanel.setLayout(glConsolePanel);
		
		this.objectInfosPane.addTab("Console", null, this.consolePanel, null);
		
		// $hide>>$
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
	 * Initialize or reinitialize the agent selection list. Invoked later.
	 */
	public void initAgentsList() {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SimulationWindow.this.agentsModel.clear();
				
				Collection<Agent> agents = Model.getInstance().getAI().getWorld().getAgents();
				
				for (Agent agent : agents) {
					try {
						SimulationWindow.this.agentsModel.addElement(agent.getPropertiesContainer().getString(
								Internal.Agent.NAME)
								+ " (#" + agent.getId() + ")");
					} catch (UnknownPropertyException e) {
						logger.error("Cannot properly select the agent.", e);
					}
				}
				
			}
		});
		
	}
	
	/**
	 * Initialize or reinitialize the object selection tree. The objects are sorted by model.
	 * Invoked later
	 */
	public void initObjectsTree() {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// We remove the object of its category node
				SimulationWindow.this.objectsTreeRoot = new DefaultMutableTreeNode();
				SimulationWindow.this.objectsTreeCategories.clear();
				SimulationWindow.this.objectsTreeLeafs.clear();
				((DefaultTreeModel) SimulationWindow.this.objectsTree.getModel())
						.setRoot(SimulationWindow.this.objectsTreeRoot);
				
				Collection<WorldObject> objects = Model.getInstance().getAI().getWorld().getReactiveObjects();
				
				for (WorldObject worldObject : objects) {
					// We check whether the model node has already been created or not
					DefaultMutableTreeNode modelTreeNode = SimulationWindow.this.objectsTreeCategories.get(worldObject
							.getModelName());
					if (modelTreeNode == null) {
						modelTreeNode = new DefaultMutableTreeNode(worldObject.getModelName());
						SimulationWindow.this.objectsTreeRoot.add(modelTreeNode);
						SimulationWindow.this.objectsTreeCategories.put(worldObject.getModelName(), modelTreeNode);
					}
					DefaultMutableTreeNode objectTreeNode = new DefaultMutableTreeNode(worldObject.getId());
					modelTreeNode.add(objectTreeNode);
					SimulationWindow.this.objectsTreeLeafs.put(worldObject.getId(), objectTreeNode);
				}
				
				// We expand the root node so that the categories become visible
				SimulationWindow.this.objectsTree.expandRow(0);
			}
		});
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
		SimulationWindow.this.showObjectInfos(agent, update);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				if (!update) {
					SimulationWindow.this.objectInfosPane.addTab("Gauges", null,
							SimulationWindow.this.gaugesScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Emotions", null, SimulationWindow.this.emotionsPanel,
							null);
					SimulationWindow.this.objectInfosPane.addTab("Secondary Emotions", null,
							SimulationWindow.this.secondaryEmotionsPanel, null);
					SimulationWindow.this.objectInfosPane.addTab("Memory", null,
							SimulationWindow.this.memoryScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Message inbox", null,
							SimulationWindow.this.messagesInboxScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Emmited messages", null,
							SimulationWindow.this.emmitedMessagesScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Goals", null, SimulationWindow.this.goalsScrollPane,
							null);
					SimulationWindow.this.objectInfosPane.addTab("Planning", null,
							SimulationWindow.this.planScrollPane, null);
				}
				
				// We show the gauges
				SimulationWindow.this.showAgentGauges(agent);
				
				SimulationWindow.this.showAgentEmotions(agent);
				
				SimulationWindow.this.showAgentSecondaryEmotions(agent);
				
				// We show the memory
				SimulationWindow.this.showAgentMemory(agent);
				
				// We show the message inbox
				SimulationWindow.this.showAgentMessageInbox(agent);
				
				// We show the emmited messages
				SimulationWindow.this.showAgentEmmitedMessages(agent);
				
				// We show the goals
				SimulationWindow.this.showAgentGoals(agent);
				
				// We show the planning
				SimulationWindow.this.showAgentPlan(agent);
			}
		});
	}
	
	/**
	 * Display all the informations tabs existing for a given object. This method automatically
	 * removes all the tabs from the objectInfos tabs pane, and rebuild the tabs
	 * 
	 * @param object
	 *            the object for which we want to display the information tabs
	 * @param update
	 *            False if the tabs must be removed and recreated (to display a different object).
	 *            True if the tabs must just be updated (the object is still the same.
	 */
	private void showObjectInfos(final WorldObject object, final boolean update) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// We remove all the tabs
				if (!update) {
					// Remove tabs
					SimulationWindow.this.objectInfosPane.removeAll();
					
					// Recreate tabs
					SimulationWindow.this.objectInfosPane.addTab("General infos", null,
							SimulationWindow.this.generalInfosScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Properties", null,
							SimulationWindow.this.propertiesScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Categories", null,
							SimulationWindow.this.categoriesScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Services", null,
							SimulationWindow.this.servicesScrollPane, null);
					SimulationWindow.this.objectInfosPane.addTab("Belongings", null,
							SimulationWindow.this.belongingsScrollPane, null);
					
					SimulationWindow.this.objectInfosPane.addTab("Console", null, SimulationWindow.this.consolePanel,
							null);
					SimulationWindow.this.objectInfosPane.addTab("Display", null,
							SimulationWindow.this.displayScrollPane, null);
					
					// We show the edit
					SimulationWindow.this.showObjectEdit(object);
					
					// We show the console
					SimulationWindow.this.showObjectConsole(object);
				}
				
				// We show the general infos
				SimulationWindow.this.showObjectGeneralInfos(object);
				
				// We show the properties
				SimulationWindow.this.showObjectProperties(object);
				
				// We show the categories
				SimulationWindow.this.showObjectCategories(object);
				
				// We show the services
				SimulationWindow.this.showObjectServices(object);
				
				// We show the belongings
				SimulationWindow.this.showObjectBelongings(object);
				
			}
		});
		
	}
	
	/**
	 * Display the goals tab of an agent.
	 * 
	 * @param agent
	 *            The agent for which we want to display the planning
	 */
	private void showAgentGoals(Agent agent) {
		
		this.goalsTableModel.clear();
		
		// We make a copy of goals list to avoid concurrent exceptions
		int row = 0;
		synchronized (agent.getMemory().getGoalStack()) {
			for (MMGoal goal : agent.getMemory().getGoalStack()) {
				this.goalsTableModel.setValueAt(goal.displayName(), row, 0);
				this.goalsTableModel.setValueAt(goal.getPriority(), row, 1);
				this.goalsTableModel.setValueAt(goal.isReachable(), row, 2);
				this.goalsTableModel.setValueAt(goal.isReached(), row, 3);
				row++;
			}
		}
		
	}
	
	/**
	 * Display the plan tab of an agent.
	 * 
	 * @param agent
	 *            The agent for which we want to display the planning
	 */
	private void showAgentPlan(Agent agent) {
		
		this.planTableModel.clear();
		
		Plan plan = agent.getPlanningModule().getPlan();
		int row = 0;
		if (plan != null) {
			for (PlanElement planElement : plan) {
				
				String parameters = "";
				
				String serviceName = planElement.getService().getName();
				// If the service is the service that is currently executed, we place a star after
				// its name
				serviceName = agent.getPlanningModule().getCurrentIndex() == row ? serviceName + " *" : serviceName;
				this.planTableModel.setValueAt(serviceName, row, 0);
				this.planTableModel.setValueAt(parameters, row, 1);
				WorldObject provider = planElement.getProvider();
				if (provider != null) {
					this.planTableModel.setValueAt(
							provider.getModelName() + " #" + provider.getId() + " @(" + provider.getPosition().x + ","
									+ provider.getPosition().y + ")", row, 2);
				} else {
					this.planTableModel.setValueAt("", row, 2);
				}
				
				row++;
			}
		}
		
	}
	
	/**
	 * Display the planning tab of an agent.
	 * 
	 * @param agent
	 *            The agent for which we want to display the planning
	 */
	private void showAgentEmmitedMessages(Agent agent) {
		
		this.emmitedMessagesTableModel.clear();
		
		Set<String> messages = Model.getInstance().getGraphics().getEngine3D().agents.get(agent.getId())
				.getEmmitedMessages();
		int row = 0;
		for (String message : messages) {
			this.emmitedMessagesTableModel.setValueAt(message, row, 0);
			row++;
		}
	}
	
	/**
	 * Display the memory tab of an agent.
	 * 
	 * @param agent
	 *            The agent for which we want to display the memory
	 */
	private void showAgentMemory(Agent agent) {
		
		this.memoryTreeRoot = new DefaultMutableTreeNode("Memory elements");
		((DefaultTreeModel) this.memoryTree.getModel()).setRoot(this.memoryTreeRoot);
		
		Memory m = agent.getMemory();
		
		Map<Long, MemoryElement> memoryElements = m.getMemoryElements();
		
		DefaultMutableTreeNode longTermNode = new DefaultMutableTreeNode("LONG-TERM MEMORY");
		this.memoryTreeRoot.add(longTermNode);
		
		DefaultMutableTreeNode shortTermNode = new DefaultMutableTreeNode("SHORT-TERM MEMORY");
		this.memoryTreeRoot.add(shortTermNode);
		
		for (MemoryElement memoryElement : memoryElements.values().toArray(new MemoryElement[0])) {
			
			WorldObject worldObject = memoryElement.getWorldObject();
			
			String objectNodeName = "#" + worldObject.getId();
			
			if (worldObject.getId() == agent.getId()) {
				objectNodeName += " (Me)";
			} else {
				objectNodeName += " - " + worldObject.getModelName();
			}
			
			DefaultMutableTreeNode objectNode = new DefaultMutableTreeNode(objectNodeName);
			if (memoryElement.getPlace().equals("long")) {
				longTermNode.add(objectNode);
			} else {
				shortTermNode.add(objectNode);
			}
			
			objectNode.add(new DefaultMutableTreeNode("Forgettable : " + memoryElement.isForgettable()));
			
			objectNode.add(new DefaultMutableTreeNode("Nb references : " + memoryElement.getNbReferences()));
			
			DefaultMutableTreeNode propertiesNode = new DefaultMutableTreeNode("Properties");
			objectNode.add(propertiesNode);
			
			Collection<Property<?>> properties = worldObject.getPropertiesContainer().getProperties();
			for (Property<?> property : properties) {
				propertiesNode.add(new DefaultMutableTreeNode(property.getModel().getName() + " : "
						+ property.getValue()));
			}
			
			DefaultMutableTreeNode belongingsNode = new DefaultMutableTreeNode("Belongings");
			objectNode.add(belongingsNode);
			
			Collection<WorldObject> belongings = worldObject.getBelongings();
			
			for (WorldObject belonging : belongings) {
				belongingsNode.add(new DefaultMutableTreeNode(belonging.getModelName() + " (#" + belonging.getId()
						+ ")"));
			}
			
			DefaultMutableTreeNode servicesNode = new DefaultMutableTreeNode("Services");
			objectNode.add(servicesNode);
			Collection<Service> services = worldObject.getServices();
			
			for (Service service : services) {
				servicesNode.add(new DefaultMutableTreeNode(service.getName()));
			}
			
			DefaultMutableTreeNode positionNode = new DefaultMutableTreeNode("Position : "
					+ worldObject.getPosition().toString());
			objectNode.add(positionNode);
		}
		this.memoryTree.expandRow(0);
	}
	
	/**
	 * Display the messages inbox tab of an agent.
	 * 
	 * @param agent
	 *            The agent for which we want to display the messages inbox
	 */
	private void showAgentMessageInbox(Agent agent) {
		
		this.messagesInboxTableModel.clear();
		
		Queue<Message> messages = agent.getMemory().getMessageInbox();
		
		int row = 0;
		for (Message element : messages) {
			try {
				this.messagesInboxTableModel.setValueAt(
						element.getSender().getPropertiesContainer().getString(Internal.Agent.NAME) + " (#"
								+ element.getSender().getId() + ")", row, 0);
			} catch (UnknownPropertyException upe) {
				logger.error("Cannot properly select the agent.", upe);
			}
			this.messagesInboxTableModel.setValueAt(element.toString(), row, 1);
			row++;
		}
	}
	
	/**
	 * Display the edit tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectEdit(WorldObject object) {
		
		this.displayPanel.removeAll();
		
		JLabel graphLabel = new JLabel("Graph");
		String buttonText = Model.getInstance().getGraphics().getEngine3D().isWalkableGraphDisplayed() ? "Mask"
				: "Draw";
		JButton graphButton = new JButton(buttonText);
		graphButton.addActionListener(new GraphDisplayController());
		
		this.displayPanel.add(graphLabel);
		this.displayPanel.add(graphButton);
		
		GridBagConstraints graphLabelConstraint = new GridBagConstraints();
		graphLabelConstraint.gridy = 0;
		graphLabelConstraint.gridx = 0;
		graphLabelConstraint.gridwidth = 1;
		graphLabelConstraint.gridheight = 1;
		this.displayGridBagLayout.setConstraints(graphLabel, graphLabelConstraint);
		
		GridBagConstraints graphButtonConstraint = new GridBagConstraints();
		graphButtonConstraint.gridy = 0;
		graphButtonConstraint.gridx = 1;
		graphButtonConstraint.gridwidth = 1;
		graphButtonConstraint.gridheight = 1;
		this.displayGridBagLayout.setConstraints(graphButton, graphButtonConstraint);
		
		if (object instanceof Agent) {
			JLabel visionFieldLabel = new JLabel("Vision field");
			JButton visionFieldButton = new JButton("Display");
			visionFieldButton.addActionListener(new DisplayVisionFieldController(object.getId()));
			this.displayPanel.add(visionFieldLabel);
			this.displayPanel.add(visionFieldButton);
			
			GridBagConstraints visionFieldLabelConstraint = new GridBagConstraints();
			visionFieldLabelConstraint.gridy = 0;
			visionFieldLabelConstraint.gridx = 2;
			visionFieldLabelConstraint.gridwidth = 1;
			visionFieldLabelConstraint.gridheight = 1;
			this.displayGridBagLayout.setConstraints(visionFieldLabel, visionFieldLabelConstraint);
			
			GridBagConstraints visionFieldButtonConstraint = new GridBagConstraints();
			visionFieldButtonConstraint.gridy = 0;
			visionFieldButtonConstraint.gridx = 3;
			visionFieldButtonConstraint.gridwidth = 1;
			visionFieldButtonConstraint.gridheight = 1;
			this.displayGridBagLayout.setConstraints(visionFieldButton, visionFieldButtonConstraint);
		}
	}
	
	/**
	 * Display the console tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectConsole(WorldObject object) {
		
	}
	
	/**
	 * Display the belonging tab of an object.
	 * 
	 * @param object
	 *            The object for which we want to display the informations
	 */
	private void showObjectBelongings(WorldObject object) {
		// We show the objects
		this.belongingsTableModel.clear();
		
		Collection<WorldObject> objects = object.getBelongings();
		int row = 0;
		for (WorldObject wObject : objects) {
			this.belongingsTableModel.setValueAt(wObject.getModelName(), row, 0);
			this.belongingsTableModel.setValueAt(wObject.getId(), row, 1);
			row++;
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
	 * Display the gauges tab of an agent.
	 * 
	 * @param agent
	 *            The agent for which we want to display the gauges
	 */
	private void showAgentGauges(Agent agent) {
		// We show the gauges
		this.gaugesPanel.removeAll();
		
		JPanel panelPrimordial = new JPanel();
		JPanel panelSecurity = new JPanel();
		JPanel panelSocial = new JPanel();
		
		GridBagLayout gaugesPrimordialLayout = new GridBagLayout();
		GridBagLayout gaugesSecurityLayout = new GridBagLayout();
		GridBagLayout gaugesSocialLayout = new GridBagLayout();
		gaugesPrimordialLayout.columnWidths = new int[] { 120, 80, 120, 80 };
		gaugesSecurityLayout.columnWidths = new int[] { 120, 80 };
		gaugesSocialLayout.columnWidths = new int[] { 120, 80 };
		
		panelPrimordial.setLayout(gaugesPrimordialLayout);
		panelSecurity.setLayout(gaugesSecurityLayout);
		panelSocial.setLayout(gaugesSocialLayout);
		
		panelPrimordial.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.black));
		panelSecurity.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.black));
		
		this.gaugesPanel.setLayout(this.gaugesGridBagLayout);
		
		this.gaugesPanel.add(panelPrimordial);
		this.gaugesPanel.add(panelSecurity);
		this.gaugesPanel.add(panelSocial);
		
		List<Gauge> gauges = agent.getGauges();
		
		int nr_survival = 0, nr_security = 0, nr_social = 0;
		
		for (int i = 0; i < gauges.size(); i++) {
			Gauge gauge = gauges.get(i);
			
			JLabel label = new JLabel(gauge.getNameSuffix());
			JProgressBar progressBar = new JProgressBar((int) gauge.getMinValue(), (int) gauge.getMaxValue());
			progressBar.setValue(gauge.getValue().intValue());
			progressBar.setStringPainted(true);
			
			if (gauge.isSurvival()) {
				GridBagConstraints labelConstraint = new GridBagConstraints();
				labelConstraint.gridx = 0;
				labelConstraint.gridy = 0;
				labelConstraint.gridwidth = 1;
				labelConstraint.gridheight = 1;
				
				GridBagConstraints barConstraint = new GridBagConstraints();
				barConstraint.gridy = 0;
				barConstraint.gridx = 1;
				barConstraint.gridwidth = 1;
				barConstraint.gridheight = 1;
				
				if (nr_survival % 2 == 0) {
					labelConstraint.gridy = nr_survival / 2 + 1;
					labelConstraint.gridx = 0;
					
					barConstraint.gridy = nr_survival / 2 + 1;
					barConstraint.gridx = 1;
					
				} else {
					labelConstraint.gridy = nr_survival / 2 + 1;
					labelConstraint.gridx = 2;
					
					barConstraint.gridy = nr_survival / 2 + 1;
					barConstraint.gridx = 3;
				}
				
				gaugesPrimordialLayout.setConstraints(label, labelConstraint);
				gaugesPrimordialLayout.setConstraints(progressBar, barConstraint);
				
				panelPrimordial.add(label);
				panelPrimordial.add(progressBar);
				
				nr_survival++;
			} else if (gauge.isSecurityNeed()) {
				GridBagConstraints labelConstraint = new GridBagConstraints();
				labelConstraint.gridx = 0;
				labelConstraint.gridy = 0;
				labelConstraint.gridwidth = 1;
				labelConstraint.gridheight = 1;
				
				GridBagConstraints barConstraint = new GridBagConstraints();
				barConstraint.gridy = 0;
				barConstraint.gridx = 1;
				barConstraint.gridwidth = 1;
				barConstraint.gridheight = 1;
				
				labelConstraint.gridy = nr_security;
				labelConstraint.gridx = 0;
				
				barConstraint.gridy = nr_security;
				barConstraint.gridx = 1;
				
				gaugesSecurityLayout.setConstraints(label, labelConstraint);
				gaugesSecurityLayout.setConstraints(progressBar, barConstraint);
				
				panelSecurity.add(label);
				panelSecurity.add(progressBar);
				
				nr_security++;
			} else if (gauge.isSocialNeed()) {
				
				GridBagConstraints labelConstraint = new GridBagConstraints();
				labelConstraint.gridx = 0;
				labelConstraint.gridy = 0;
				labelConstraint.gridwidth = 1;
				labelConstraint.gridheight = 1;
				
				GridBagConstraints barConstraint = new GridBagConstraints();
				barConstraint.gridy = 0;
				barConstraint.gridx = 1;
				barConstraint.gridwidth = 1;
				barConstraint.gridheight = 1;
				
				labelConstraint.gridy = nr_social;
				labelConstraint.gridx = 0;
				
				barConstraint.gridy = nr_social;
				barConstraint.gridx = 1;
				gaugesSocialLayout.setConstraints(label, labelConstraint);
				gaugesSocialLayout.setConstraints(progressBar, barConstraint);
				
				panelSocial.add(label);
				panelSocial.add(progressBar);
				nr_social++;
			}
		}
		
	}
	
	private void showAgentEmotions(Agent agent) {
		this.emotionsPanel.removeAll();
		
		List<Emotion> emotions = agent.getEmotions();
		
		for (int i = 0; i < emotions.size(); i++) {
			Emotion emotion = emotions.get(i);
			JLabel label = new JLabel(emotion.getName());
			JProgressBar progressBar = new JProgressBar((int) emotion.getMinValue(), (int) emotion.getMaxValue());
			progressBar.setValue(emotion.getValue().intValue());
			progressBar.setStringPainted(true);
			
			GridBagConstraints labelConstraint = new GridBagConstraints();
			labelConstraint.gridx = 0;
			labelConstraint.gridy = 0;
			labelConstraint.gridwidth = 1;
			labelConstraint.gridheight = 1;
			
			GridBagConstraints barConstraint = new GridBagConstraints();
			barConstraint.gridy = 0;
			barConstraint.gridx = 1;
			barConstraint.gridwidth = 1;
			barConstraint.gridheight = 1;
			
			if (i != 0) {
				if (i % 2 == 0) {
					labelConstraint.gridy = i / 2;
					labelConstraint.gridx = 0;
					
					barConstraint.gridy = i / 2;
					barConstraint.gridx = 1;
					
				} else {
					labelConstraint.gridy = i / 2;
					labelConstraint.gridx = 2;
					
					barConstraint.gridy = i / 2;
					barConstraint.gridx = 3;
				}
			}
			this.emotionsGridLayout.setConstraints(label, labelConstraint);
			this.emotionsGridLayout.setConstraints(progressBar, barConstraint);
			
			this.emotionsPanel.add(label);
			this.emotionsPanel.add(progressBar);
		}
	}
	
	private void showAgentSecondaryEmotions(Agent agent) {
		this.secondaryEmotionsPanel.removeAll();
		
		List<Emotion> emotions = agent.getSecondaryEmotions();
		
		for (int i = 0; i < emotions.size(); i++) {
			Emotion emotion = emotions.get(i);
			JLabel label = new JLabel(emotion.getSecondaryEmotionName());
			JProgressBar progressBar = new JProgressBar((int) emotion.getMinValue(), (int) emotion.getMaxValue());
			progressBar.setValue(emotion.getValue().intValue());
			progressBar.setStringPainted(true);
			
			GridBagConstraints labelConstraint = new GridBagConstraints();
			labelConstraint.gridx = 0;
			labelConstraint.gridy = 0;
			labelConstraint.gridwidth = 1;
			labelConstraint.gridheight = 1;
			
			GridBagConstraints barConstraint = new GridBagConstraints();
			barConstraint.gridy = 0;
			barConstraint.gridx = 1;
			barConstraint.gridwidth = 1;
			barConstraint.gridheight = 1;
			
			if (i != 0) {
				if (i % 2 == 0) {
					labelConstraint.gridy = i / 2;
					labelConstraint.gridx = 0;
					
					barConstraint.gridy = i / 2;
					barConstraint.gridx = 1;
					
				} else {
					labelConstraint.gridy = i / 2;
					labelConstraint.gridx = 2;
					
					barConstraint.gridy = i / 2;
					barConstraint.gridx = 3;
				}
			}
			this.secondaryEmotionsGridLayout.setConstraints(label, labelConstraint);
			this.secondaryEmotionsGridLayout.setConstraints(progressBar, barConstraint);
			
			this.secondaryEmotionsPanel.add(label);
			this.secondaryEmotionsPanel.add(progressBar);
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
	public void setSelection(long id, boolean focusJMEOnSelection) {
		
		WorldObject worldObject = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		if (worldObject != null) {
			
			boolean update = false; // True if we have update the current object tabs, false if a
			// new object is selected
			
			if (this.currentSelection != null && this.currentSelection == id) {
				update = true;
			}
			
			this.currentSelection = id;
			if (worldObject instanceof Agent) {
				// If this is a new selection
				if (!update) {
					// If an object is selected, we unselect it
					this.objectsTree.clearSelection();
					// We disable the listener otherwise the new selection in the list will
					// throw an new event, and then, the controller will ask JME to focus on the
					// selected object even if user just clicked on it in the JME canvas
					this.agentsList.removeListSelectionListener(SimulationWindow.this.agentSelectionController);
					// We select the agent
					try {
						this.agentsList.setSelectedValue(
								worldObject.getPropertiesContainer().getString(Internal.Agent.NAME) + " (#"
										+ worldObject.getId() + ")", true);
					} catch (UnknownPropertyException upe) {
						logger.error("Cannot properly select the agent.", upe);
					}
					// We enable the listener so as the controller can detect the selection
					// of a new agent from a user click.
					this.agentsList.addListSelectionListener(SimulationWindow.this.agentSelectionController);
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
					this.objectsTree.removeTreeSelectionListener(this.objectSelectionController);
					// We select the object
					TreePath path = new TreePath(this.objectsTreeLeafs.get(id).getPath());
					this.objectsTree.setSelectionPath(path);
					// We enable the listener so as the controller can detect the selection
					// of
					// a new object from a user clic.
					this.objectsTree.addTreeSelectionListener(SimulationWindow.this.objectSelectionController);
					// We open the object tab
					this.agentsObjectsTabdedPane.setSelectedIndex(1);
				}
				// We show the agent infos
				this.showObjectInfos(worldObject, update);
				
			}
			if (focusJMEOnSelection) {
				// We set the focus on the object id
				
				Model.getInstance().getGraphics().getEngine3D().setFocusOn(id);
				
			}
		}
	}
	
	/**
	 * Remove the specified object from the swing interface. Must be called before the AI
	 * destruction
	 * 
	 * @param id
	 *            the object id
	 */
	public void removeObject(long id) {
		
		WorldObject worldObject = Model.getInstance().getAI().getWorld().getWorldObjects().get(id);
		this.currentSelection = null;
		// if it is an agent
		if (worldObject instanceof Agent) {
			// We remove the agent from the agent list
			try {
				this.agentsModel.removeElement(worldObject.getPropertiesContainer().getString(Internal.Agent.NAME)
						+ " (#" + worldObject.getId() + ")");
			} catch (UnknownPropertyException e) {
				logger.error("Cannot properly select the agent.", e);
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
	
	/**
	 * Sets the edition window.
	 * 
	 * @param editionWindow
	 *            the new edition window
	 */
	public void setEditionWindow(EditionWindow editionWindow) {
		this.editionWindow = editionWindow;
	}
}
