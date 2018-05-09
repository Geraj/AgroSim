package main;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import control.Control2;

import dao.jdbc.DbException;
import dao.jdbc.Manager;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MainFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = -5155953199961608138L;
	public JMenuBar jMenuBar1;
	public JPanel plantsjPanel;
	public JScrollPane jScrollPane1;
	public JLabel operationsPricejLable;
	public JLabel operationsNamejLable;
	public JButton operationNewjButton;
	public JTable jTable2;
	public JScrollPane jscrollPaneOperatins;
	public JPanel operationsJpanel;
	public JMenuItem machinesjMenuItem;
	private JLabel baseNamejLabel;
	public JScrollPane simulationMachinesjScrollPane;
	public JList simulationMachinesjList;
	public JScrollPane simulationOperationsjScrollPane;
	public JScrollPane simulationFieldsjScrollPane;
	public JButton simulationStartjButton;
	public JList simulationFieldsjList;
	private JLabel jLabel5;
	private JPanel simulationFieldsjPanel;
	private JLabel jLabel4;
	private JPanel simulationMachinesjPanel;
	private JPanel simulationOperations_jPanel;
	public JList simulationOperationsjList;
	private JLabel jLabel3;
	public JPanel simulationjPanel;
	public JMenuItem simulationjMenuItem;
	public JMenuItem mapstartjMenuItem;
	public JMenu mapjMenu;
	public JButton baseOkjButton;
	public JTextField baseLongitudeTextField;
	private JLabel baseLongitudejLabel;
	public JTextField baseLatitudeTextField;
	private JLabel baseLatitudejLabel;
	public JTextField baseNameTextField;
	private JPanel baseButtonsjPanel;
	public JTable jTable4Base;
	public JButton baseNewjButton;
	public JPanel baseNewjPanel;
	private JScrollPane basejScrollPane;
	public JPanel basesjPanel;
	public JMenuItem basesjMenuItem;
	public JButton machineOperationjButton;
	public JComboBox machineOperationSelectjCombobox;
	public JComboBox machineOperationaddJcombobox;
	private JLabel jLabel2;
	private JPanel machineoperationjPanel;
	public JComboBox plantoperationselectjComboBox;
	public JButton plantoperationjButtono;
	public JComboBox plantoperationjComboBox1;
	private JPanel plantOperationjPanel;
	private JLabel jLabel1;
	public JButton machinesConfirm;
	public JTextField machinesWorkSpeedText;
	public JTextField machinesSpeedText;
	public JTextField machinesTypeText;
	public JTextField machinesNewText;
	private JLabel machinesWorkSpeedjLable;
	private JLabel machinesSpeedjLable;
	private JLabel machinesTypejLable;
	private JLabel machinesIdJlable;
	public JPanel machinenewJpanel;
	public JButton machinesNewJbutton;
	public JTable jTable3Machines;
	public JScrollPane jScrollPaneMachines;
	public JPanel machinesJpanel;
	public JMenuItem operationjMenuitem;
	public JButton operationsConfirm;
	public JTextField operationsTimeText;
	public JTextField operationsPriceText;
	public JTextField operationsNameText;
	public JLabel operationsTimejLable;
	public JButton plantsConfirmNewButton;
	public JTextField plantsIncomeText;
	public JTextField plantsTypeText;
	public JTextField plantsSeedExpense;
	public JLabel plantsIncomLable;
	public JLabel plantsSeedLable;
	public JLabel plantsTypeLabel;
	public JButton plantsNewButton;
	public JMenuItem plantsjMenuitem;
	public JTable jTable1;
	public JMenu jMenu1;
	public Control2 cont;
	public int currentBase;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		Logger logger=Logger.getLogger(MainFrame.class);
		PropertyConfigurator.configure("log4j.properties");
		logger.info("APPLICATION Started");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame inst = new MainFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public MainFrame() {
		super("AgroSim");
		cont=new Control2(this);
		try{
			new Manager();
		}catch (DbException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(-1);
		}
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			{
				basesjPanel = new JPanel();
				getContentPane().add(basesjPanel, new AnchorConstraint(11, 1008, 991, 1, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				AnchorLayout basesjPanelLayout = new AnchorLayout();
				basesjPanel.setPreferredSize(new java.awt.Dimension(868, 301));
				basesjPanel.setLayout(basesjPanelLayout);
				{
					basejScrollPane = new JScrollPane();
					basesjPanel.add(basejScrollPane, new AnchorConstraint(1, 595, 33, 7, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL));
					basejScrollPane.setPreferredSize(new java.awt.Dimension(510, 265));
					{
						//String[][] s=cont.getalldataBase();
						TableModel jTable4BaseModel = 
							new DefaultTableModel(null,new String[] { "Name", "Latitude","Longitude" });
						
						jTable4Base = new JTable();
						basejScrollPane.setViewportView(jTable4Base);
						jTable4Base.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jTable4Base.setModel(jTable4BaseModel);
						jTable4Base.addMouseListener(cont);
						jTable4Base.addKeyListener(cont);
					}
				}
				{
					baseButtonsjPanel = new JPanel();
					FlowLayout baseButtonsjPanelLayout = new FlowLayout();
					baseButtonsjPanel.setLayout(baseButtonsjPanelLayout);
					basesjPanel.add(baseButtonsjPanel, new AnchorConstraint(41, 911, 226, 607, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					baseButtonsjPanel.setPreferredSize(new java.awt.Dimension(262, 55));
					{
						baseNewjButton = new JButton();
						baseButtonsjPanel.add(baseNewjButton);
						baseNewjButton.setText("New");
						baseNewjButton.setPreferredSize(new java.awt.Dimension(75, 32));
						baseNewjButton.addActionListener(cont);
					}
				}
				{
					baseNewjPanel = new JPanel();
					baseNewjPanel.setLayout(null);
					basesjPanel.add(baseNewjPanel, new AnchorConstraint(247, 910, 955, 592, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					baseNewjPanel.setPreferredSize(new java.awt.Dimension(244, 213));
					baseNewjPanel.setVisible(false);
					{
						baseNamejLabel = new JLabel();
						baseNewjPanel.add(baseNamejLabel);
						baseNamejLabel.setText("Name:");
						baseNamejLabel.setPreferredSize(new java.awt.Dimension(77, 29));
						baseNamejLabel.setBounds(30, 5, 77, 29);
					}
					{
						baseNameTextField = new JTextField();
						baseNewjPanel.add(baseNameTextField);
						baseNameTextField.setBounds(112, 8, 104, 23);
					}
					{
						baseLatitudejLabel = new JLabel();
						baseNewjPanel.add(baseLatitudejLabel);
						baseLatitudejLabel.setText("Latitude:");
						baseLatitudejLabel.setBounds(30, 40, 87, 20);
					}
					{
						baseLatitudeTextField = new JTextField();
						baseNewjPanel.add(baseLatitudeTextField);
						baseLatitudeTextField.setBounds(112, 39, 104, 23);
					}
					{
						baseLongitudejLabel = new JLabel();
						baseNewjPanel.add(baseLongitudejLabel);
						baseLongitudejLabel.setText("Longitude");
						baseLongitudejLabel.setPreferredSize(new java.awt.Dimension(77, 27));
						baseLongitudejLabel.setBounds(30, 67, 77, 27);
					}
					{
						baseLongitudeTextField = new JTextField();
						baseNewjPanel.add(baseLongitudeTextField);
						baseLongitudeTextField.setBounds(112, 69, 104, 23);
					}
					{
						baseOkjButton = new JButton();
						baseNewjPanel.add(baseOkjButton);
						baseOkjButton.setText("Ok");
						baseOkjButton.setPreferredSize(new java.awt.Dimension(80, 23));
						baseOkjButton.setBounds(82, 99, 80, 23);
						baseOkjButton.addActionListener(cont);
					}
				}
			}
			{
				simulationjPanel = new JPanel();
				getContentPane().add(simulationjPanel, new AnchorConstraint(14, 986, 985, 2, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				simulationjPanel.setPreferredSize(new java.awt.Dimension(848, 298));
				simulationjPanel.setLayout(null);
				simulationjPanel.setVisible(false);
				{
					
					
				}
				{
					simulationOperations_jPanel = new JPanel();
					simulationjPanel.add(simulationOperations_jPanel);
					simulationOperations_jPanel.setBounds(12, 17, 152, 264);
					simulationOperations_jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0,0,0)));
					{
						jLabel3 = new JLabel();
						simulationOperations_jPanel.add(jLabel3);
						jLabel3.setText("Operations:");
						jLabel3.setBounds(12, -5, 81, 43);
						jLabel3.setPreferredSize(new java.awt.Dimension(105, 45));
					}
					{
						simulationOperationsjScrollPane = new JScrollPane();
						simulationOperations_jPanel.add(simulationOperationsjScrollPane);
						simulationOperationsjScrollPane.setPreferredSize(new java.awt.Dimension(91, 196));
						{
							
							//ListModel simulationOperationsjListModel = 
								//	new DefaultComboBoxModel(cont.fillSimulationOperationjList());
							simulationOperationsjList = new JList();
							simulationOperationsjScrollPane.setViewportView(simulationOperationsjList);
							//simulationOperationsjList.setModel(simulationOperationsjListModel);
							simulationOperationsjList.setBounds(12, 38, 91, 164);
							simulationOperationsjList.setPreferredSize(new java.awt.Dimension(79, 176));
							simulationOperationsjList.addListSelectionListener(cont); 
						}
					}
				}
				{
					simulationMachinesjPanel = new JPanel();
					FlowLayout simulationMachinesjPanelLayout = new FlowLayout();
					simulationMachinesjPanel.setLayout(simulationMachinesjPanelLayout);
					simulationjPanel.add(simulationMachinesjPanel);
					simulationMachinesjPanel.setBounds(212, 17, 152, 264);
					simulationMachinesjPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0,0,0)));
					{
						jLabel4 = new JLabel();
						simulationMachinesjPanel.add(jLabel4);
						jLabel4.setText("Machines:");
						jLabel4.setPreferredSize(new java.awt.Dimension(105, 45));
					}
					{
						simulationMachinesjScrollPane = new JScrollPane();
						simulationMachinesjPanel.add(simulationMachinesjScrollPane);

						simulationMachinesjScrollPane.setPreferredSize(new java.awt.Dimension(91, 198));
						{
							ListModel simulationMachinesjListModel = 
								new DefaultComboBoxModel(
										new String[] {});
							simulationMachinesjList = new JList();
							//simulationMachinesjList.resize(1000, 1000);
							simulationMachinesjScrollPane.setViewportView(simulationMachinesjList);
							simulationMachinesjList.setModel(simulationMachinesjListModel);
							simulationMachinesjList.setPreferredSize(new java.awt.Dimension(79, 176));
						}
					}
				}
				{
					simulationFieldsjPanel = new JPanel();
					simulationjPanel.add(simulationFieldsjPanel);
					simulationFieldsjPanel.setBounds(414, 20, 137, 262);
					simulationFieldsjPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0,0,0)));
					simulationFieldsjPanel.setSize(152, 264);
					{
						jLabel5 = new JLabel();
						simulationFieldsjPanel.add(jLabel5);
						jLabel5.setText("Fields:");
						jLabel5.setSize(105, 45);
						jLabel5.setOpaque(true);
						jLabel5.setPreferredSize(new java.awt.Dimension(105, 45));
					}
					{
						simulationFieldsjScrollPane = new JScrollPane();
						simulationFieldsjPanel.add(simulationFieldsjScrollPane);
						simulationFieldsjScrollPane.setPreferredSize(new java.awt.Dimension(91, 194));
						{
							
							ListModel simulationFielsjListModel = 
								new DefaultComboBoxModel(
										new String[] {});
							simulationFieldsjList = new JList();
							simulationFieldsjScrollPane.setViewportView(simulationFieldsjList);
							simulationFieldsjList.setModel(simulationFielsjListModel);
							simulationFieldsjList.setPreferredSize(new java.awt.Dimension(79, 176));
						}
					}
				}
				{
					simulationStartjButton = new JButton();
					simulationjPanel.add(simulationStartjButton);
					simulationStartjButton.setText("Start");
					simulationStartjButton.setBounds(612, 247, 102, 37);
					simulationStartjButton.addActionListener(cont);
				}
			}
			{
				operationsJpanel = new JPanel();
				getContentPane().add(operationsJpanel, new AnchorConstraint(7, 978, 970, 7, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				AnchorLayout operationsJpanelLayout = new AnchorLayout();
				operationsJpanel.setPreferredSize(new java.awt.Dimension(837, 311));
				operationsJpanel.setLayout(operationsJpanelLayout);
				operationsJpanel.setVisible(false);
				{
					jscrollPaneOperatins = new JScrollPane();
					operationsJpanel.add(jscrollPaneOperatins, new AnchorConstraint(20, 676, 985, 6, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jscrollPaneOperatins.setPreferredSize(new java.awt.Dimension(561, 300));
					{
						//String[][] s=cont.getalldataOperations();
						TableModel jTable2Model = 
							new DefaultTableModel(null,new String[] { "Name", "Price/ha","Machines/ha" });
						jTable2 = new JTable();
						jscrollPaneOperatins.setViewportView(jTable2);
						jTable2.setModel(jTable2Model);
						jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jTable2.setColumnSelectionAllowed(false);
						jTable2.addKeyListener(cont);
						jTable2.updateUI();
					}
				}
				{
					operationNewjButton = new JButton();
					operationsJpanel.add(operationNewjButton, new AnchorConstraint(38, 797, 133, 705, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationNewjButton.setText("New");
					operationNewjButton.setPreferredSize(new java.awt.Dimension(77, 28));
					operationNewjButton.addActionListener(cont);
				}
				{
					operationsNamejLable = new JLabel();
					operationsJpanel.add(operationsNamejLable, new AnchorConstraint(162, 801, 271, 705, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsNamejLable.setText("Name:");
					operationsNamejLable.setPreferredSize(new java.awt.Dimension(80, 34));
					operationsNamejLable.setVisible(false);
				}
				{
					operationsPricejLable = new JLabel();
					operationsJpanel.add(operationsPricejLable, new AnchorConstraint(294, 786, 400, 705, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsPricejLable.setText("Price:");
					operationsPricejLable.setPreferredSize(new java.awt.Dimension(68, 33));
					operationsPricejLable.setVisible(false);
				}
				{
					operationsTimejLable = new JLabel();
					operationsJpanel.add(operationsTimejLable, new AnchorConstraint(438, 789, 554, 705, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsTimejLable.setText("Slot(ha):");
					operationsTimejLable.setPreferredSize(new java.awt.Dimension(70, 36));
					operationsTimejLable.setVisible(false);
				}
				{
					operationsNameText = new JTextField();
					operationsJpanel.add(operationsNameText, new AnchorConstraint(165, 991, 242, 821, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsNameText.setText("Operation");
					operationsNameText.setPreferredSize(new java.awt.Dimension(142, 24));
					operationsNameText.setVisible(false);
				}
				{
					operationsPriceText = new JTextField();
					operationsJpanel.add(operationsPriceText, new AnchorConstraint(310, 901, 393, 821, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsPriceText.setText("0");
					operationsPriceText.setPreferredSize(new java.awt.Dimension(67, 26));
					operationsPriceText.setVisible(false);
				}
				{
					operationsTimeText = new JTextField();
					operationsJpanel.add(operationsTimeText, new AnchorConstraint(458, 896, 532, 821, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsTimeText.setText("0");
					operationsTimeText.setPreferredSize(new java.awt.Dimension(63, 23));
					operationsTimeText.setVisible(false);
				}
				{
					operationsConfirm = new JButton();
					operationsJpanel.add(operationsConfirm, new AnchorConstraint(596, 796, 680, 718, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					operationsConfirm.setText("OK");
					operationsConfirm.setPreferredSize(new java.awt.Dimension(65, 26));
					operationsConfirm.setVisible(false);
					operationsConfirm.addActionListener(cont);
				}
			}
			{
				plantsjPanel = new JPanel();
				getContentPane().add(plantsjPanel, new AnchorConstraint(17, 5, 970, 5, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS));
				AnchorLayout jPanel1Layout = new AnchorLayout();
				plantsjPanel.setPreferredSize(new java.awt.Dimension(852, 308));
				plantsjPanel.setLayout(jPanel1Layout);
				plantsjPanel.setVisible(false);
				{
					plantOperationjPanel = new JPanel();
					plantsjPanel.add(plantOperationjPanel, new AnchorConstraint(15, 689, 1015, 451, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantOperationjPanel.setPreferredSize(new java.awt.Dimension(203, 292));
					{
						jLabel1 = new JLabel();
						plantOperationjPanel.add(jLabel1);
						jLabel1.setText("Operation:");
						jLabel1.setPreferredSize(new java.awt.Dimension(63, 23));
					}
					{
						ComboBoxModel jComboBox1Model = 
							new DefaultComboBoxModel(
									new String[] { "Add", "Remove" });
						plantoperationjComboBox1 = new JComboBox();
						plantOperationjPanel.add(plantoperationjComboBox1);
						plantoperationjComboBox1.setModel(jComboBox1Model);
						plantoperationjComboBox1.setPreferredSize(new java.awt.Dimension(66, 23));
						plantoperationjComboBox1.addActionListener(cont);
					}
					{
						ComboBoxModel plantoperationselectjComboBoxModel = 
							new DefaultComboBoxModel(
									new String[] { "Select" });
						plantoperationselectjComboBox = new JComboBox();
						plantOperationjPanel.add(plantoperationselectjComboBox);
						plantoperationselectjComboBox.setModel(plantoperationselectjComboBoxModel);
						plantoperationselectjComboBox.setPreferredSize(new java.awt.Dimension(147, 23));
						plantoperationselectjComboBox.setVisible(false);
						plantoperationselectjComboBox.addActionListener(cont);
					}
					{
						plantoperationjButtono = new JButton();
						plantOperationjPanel.add(plantoperationjButtono);
						plantoperationjButtono.setText("ok");
						plantoperationjButtono.setPreferredSize(new java.awt.Dimension(62, 24));
						plantoperationjButtono.setVisible(false);
						plantoperationjButtono.addActionListener(cont);
					}
				}
				{
					plantsConfirmNewButton = new JButton();
					plantsjPanel.add(plantsConfirmNewButton, new AnchorConstraint(579, 818, 680, 720, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsConfirmNewButton.setText("Go");
					plantsConfirmNewButton.setPreferredSize(new java.awt.Dimension(80, 26));
					plantsConfirmNewButton.setVisible(false);
					plantsConfirmNewButton.setSize(80, 27);
					plantsConfirmNewButton.addActionListener(cont);
				}
				{
					plantsIncomeText = new JTextField();
					plantsjPanel.add(plantsIncomeText, new AnchorConstraint(446, 982, 524, 878, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsIncomeText.setText("0");
					plantsIncomeText.setPreferredSize(new java.awt.Dimension(89, 24));
					plantsIncomeText.setVisible(false);
				}
				{
					plantsSeedExpense = new JTextField();
					plantsjPanel.add(plantsSeedExpense, new AnchorConstraint(342, 980, 420, 873, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsSeedExpense.setText("0");
					plantsSeedExpense.setPreferredSize(new java.awt.Dimension(91, 24));
					plantsSeedExpense.setVisible(false);
				}
				{
					plantsTypeText = new JTextField();
					plantsjPanel.add(plantsTypeText, new AnchorConstraint(215, 979, 293, 856, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsTypeText.setText("Uncultivated");
					plantsTypeText.setPreferredSize(new java.awt.Dimension(105, 24));
					plantsTypeText.setVisible(false);
				}
				{
					plantsIncomLable = new JLabel();
					plantsjPanel.add(plantsIncomLable, new AnchorConstraint(433, 801, 540, 720, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsIncomLable.setText("Income");
					plantsIncomLable.setPreferredSize(new java.awt.Dimension(69, 33));
					plantsIncomLable.setVisible(false);
				}
				{
					plantsSeedLable = new JLabel();
					plantsjPanel.add(plantsSeedLable, new AnchorConstraint(329, 821, 423, 720, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsSeedLable.setText("Seed price:");
					plantsSeedLable.setPreferredSize(new java.awt.Dimension(86, 29));
					plantsSeedLable.setVisible(false);
				}
				{
					plantsTypeLabel = new JLabel();
					plantsjPanel.add(plantsTypeLabel, new AnchorConstraint(219, 843, 287, 722, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsTypeLabel.setText("Name/type:");
					plantsTypeLabel.setPreferredSize(new java.awt.Dimension(103, 21));
					plantsTypeLabel.setVisible(false);
				}
				{
					plantsNewButton = new JButton();
					plantsNewButton.addActionListener(cont);
					plantsjPanel.add(plantsNewButton, new AnchorConstraint(15, 795, 107, 709, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					plantsNewButton.setText("New");
					plantsNewButton.setPreferredSize(new java.awt.Dimension(85, 26));
					plantsNewButton.setSize(85, 27);
				}
				{
					jScrollPane1 = new JScrollPane();
					plantsjPanel.add(jScrollPane1, new AnchorConstraint(4, 444, 1001, 11, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jScrollPane1.setPreferredSize(new java.awt.Dimension(369, 307));
					{
						//String[][] s=cont.getalldataPlants();
						TableModel jTable1Model =
							new DefaultTableModel(null,new String[] { "Plant", "Seed","Income"});
						
						jTable1 = new JTable();
						jTable1.updateUI();
						jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jTable1.setColumnSelectionAllowed(false);
						
						jScrollPane1.setViewportView(jTable1);
						
						jTable1.setModel(jTable1Model);
						jTable1.addKeyListener(cont);
					}
				}
			}
			{
				machinesJpanel = new JPanel();
				getContentPane().add(machinesJpanel, new AnchorConstraint(17, 985, 961, 0, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				AnchorLayout machinesJpanelLayout = new AnchorLayout();
				machinesJpanel.setPreferredSize(new java.awt.Dimension(849, 305));
				machinesJpanel.setLayout(machinesJpanelLayout);
				machinesJpanel.setVisible(false);
				{
					machineoperationjPanel = new JPanel();
					machinesJpanel.add(machineoperationjPanel, new AnchorConstraint(43, 736, 770, 585, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					machineoperationjPanel.setPreferredSize(new java.awt.Dimension(113, 211));
					{
						jLabel2 = new JLabel();
						machineoperationjPanel.add(jLabel2);
						jLabel2.setText("Operation");
						jLabel2.setPreferredSize(new java.awt.Dimension(78, 28));
					}
					{
						ComboBoxModel machineOperationaddJcomboboxModel = 
							new DefaultComboBoxModel(
									new String[] { "Add", "Remove" });
						machineOperationaddJcombobox = new JComboBox();
						machineoperationjPanel.add(machineOperationaddJcombobox);
						machineOperationaddJcombobox.setModel(machineOperationaddJcomboboxModel);
						machineOperationaddJcombobox.setPreferredSize(new java.awt.Dimension(78, 23));
						machineOperationaddJcombobox.addActionListener(cont);
					}
					{
						ComboBoxModel machineOperationSelectjComboboxModel = 
							new DefaultComboBoxModel(
									new String[] { "Select" });
						machineOperationSelectjCombobox = new JComboBox();
						machineoperationjPanel.add(machineOperationSelectjCombobox);
						machineOperationSelectjCombobox.setModel(machineOperationSelectjComboboxModel);
						machineOperationSelectjCombobox.setPreferredSize(new java.awt.Dimension(68, 23));
						machineOperationSelectjCombobox.addActionListener(cont);
					}
					{
						machineOperationjButton = new JButton();
						machineoperationjPanel.add(machineOperationjButton);
						machineOperationjButton.setText("ok");
						machineOperationjButton.setPreferredSize(new java.awt.Dimension(58, 23));
						machineOperationjButton.setVisible(false);
						machineOperationjButton.addActionListener(cont);
					}
				}
				{
					jScrollPaneMachines = new JScrollPane();
					machinesJpanel.add(jScrollPaneMachines, new AnchorConstraint(18, 552, 1001, 10, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jScrollPaneMachines.setPreferredSize(new java.awt.Dimension(406, 285));
					{
						//String[][] s=cont.getalldataMachines();
						TableModel jTable3MachinesModel = 
							new DefaultTableModel(null,new String[] { "ID", "Type","Speed(km/h)","WorkSpeed(ha/h)" });
						jTable3Machines = new JTable();
						jScrollPaneMachines.setViewportView(jTable3Machines);
						jTable3Machines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jTable3Machines.setColumnSelectionAllowed(false);
						jTable3Machines.updateUI();
						jTable3Machines.setModel(jTable3MachinesModel);
						jTable3Machines.addKeyListener(cont);
					}
				}
				{
					machinesNewJbutton = new JButton();
					machinesJpanel.add(machinesNewJbutton, new AnchorConstraint(46, 899, 146, 811, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					machinesNewJbutton.setText("New");
					machinesNewJbutton.setPreferredSize(new java.awt.Dimension(66, 29));
					machinesNewJbutton.addActionListener(cont);
				}
				{
					machinenewJpanel = new JPanel();
					AnchorLayout machinenewJpanelLayout = new AnchorLayout();
					machinesJpanel.add(machinenewJpanel, new AnchorConstraint(201, 1007, 908, 752, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					machinenewJpanel.setLayout(machinenewJpanelLayout);
					machinenewJpanel.setVisible(false);
					machinenewJpanel.setPreferredSize(new java.awt.Dimension(191, 205));
					{
						machinesIdJlable = new JLabel();
						machinenewJpanel.add(machinesIdJlable, new AnchorConstraint(113, 240, 243, 82, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesIdJlable.setText("ID:");
						machinesIdJlable.setPreferredSize(new java.awt.Dimension(39, 28));
					}
					{
						machinesTypejLable = new JLabel();
						machinenewJpanel.add(machinesTypejLable, new AnchorConstraint(298, 378, 442, 82, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesTypejLable.setText("Type:");
						machinesTypejLable.setPreferredSize(new java.awt.Dimension(73, 31));
					}
					{
						machinesSpeedjLable = new JLabel();
						machinenewJpanel.add(machinesSpeedjLable, new AnchorConstraint(469, 297, 613, 82, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesSpeedjLable.setText("Speed:");
						machinesSpeedjLable.setPreferredSize(new java.awt.Dimension(53, 31));
					}
					{
						machinesWorkSpeedjLable = new JLabel();
						machinenewJpanel.add(machinesWorkSpeedjLable, new AnchorConstraint(613, 456, 756, 84, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesWorkSpeedjLable.setText("WorkSpeed:");
						machinesWorkSpeedjLable.setPreferredSize(new java.awt.Dimension(73, 31));
					}
					{
						machinesNewText = new JTextField();
						machinenewJpanel.add(machinesNewText, new AnchorConstraint(127, 771, 243, 414, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesNewText.setPreferredSize(new java.awt.Dimension(88, 25));
					}
					{
						machinesTypeText = new JTextField();
						machinenewJpanel.add(machinesTypeText, new AnchorConstraint(317, 771, 428, 414, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesTypeText.setPreferredSize(new java.awt.Dimension(88, 24));
					}
					{
						machinesSpeedText = new JTextField();
						machinenewJpanel.add(machinesSpeedText, new AnchorConstraint(488, 771, 599, 414, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesSpeedText.setPreferredSize(new java.awt.Dimension(88, 24));
					}
					{
						machinesWorkSpeedText = new JTextField();
						machinenewJpanel.add(machinesWorkSpeedText, new AnchorConstraint(631, 844, 738, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesWorkSpeedText.setPreferredSize(new java.awt.Dimension(70, 23));
					}
					{
						machinesConfirm = new JButton();
						machinenewJpanel.add(machinesConfirm, new AnchorConstraint(798, 690, 923, 240, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						machinesConfirm.setText("OK");
						machinesConfirm.setPreferredSize(new java.awt.Dimension(111, 27));
						machinesConfirm.addActionListener(cont);
					}
				}
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("Manage");
					jMenu1.setEnabled(false);
					{
						basesjMenuItem = new JMenuItem();
						jMenu1.add(basesjMenuItem);
						basesjMenuItem.setText("Bases");
						basesjMenuItem.addActionListener(cont);
					}
					{
						operationjMenuitem = new JMenuItem();
						jMenu1.add(operationjMenuitem);
						operationjMenuitem.addActionListener(cont);
						operationjMenuitem.setText("Operations");
						operationjMenuitem.setBounds(80, 63, 93, 21);
					}
					{
						machinesjMenuItem = new JMenuItem();
						jMenu1.add(machinesjMenuItem);
						machinesjMenuItem.setText("Machines");
						machinesjMenuItem.addActionListener(cont);
					}
					{
						plantsjMenuitem = new JMenuItem();
						jMenu1.add(plantsjMenuitem);
						plantsjMenuitem.setText("Plants");
						plantsjMenuitem.addActionListener(cont);
					}
					{
						simulationjMenuItem = new JMenuItem();
						jMenu1.add(simulationjMenuItem);
						simulationjMenuItem.setText("Simulation");
						simulationjMenuItem.addActionListener(cont);
					}
				}
				{
					mapjMenu = new JMenu();
					jMenuBar1.add(mapjMenu);
					mapjMenu.setText("Map");
					mapjMenu.setEnabled(false);
					{
						mapstartjMenuItem = new JMenuItem();
						mapjMenu.add(mapstartjMenuItem);
						mapstartjMenuItem.setText("Start");
						mapstartjMenuItem.addActionListener(cont);
					}
				}
			}
			pack();
			cont.refreshBaseTable();
			this.setSize(777, 372);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jTable1KeyPressed(KeyEvent evt) {
		System.out.println("jTable1.keyPressed, event="+evt);
		//TODO add your code for jTable1.keyPressed
	}

}
