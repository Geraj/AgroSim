package control;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import core.Base;
import core.Machines;
import core.Operations;
import core.Parcel;
import core.Plants;
import appmap.ApplicationMap;
import calculations.LogicCaller;
import calculations.LogicCallerInterface;
import dao.jdbc.BaseJdbcDAO;
import dao.jdbc.DbException;

import main.MainFrame;

/**
 * Control class responds to events in the MainFrame, and communicates with the
 * DB using abstract DAO factory pattern
 * 
 * @author Gergely Meszaros
 * 
 */
public class Control2 implements ActionListener, ListSelectionListener,
		MouseListener, KeyListener {
	private MainFrame frame;
	private LogicCallerInterface caller;
	private static Logger logger = Logger.getLogger(Control2.class);

	public Control2(MainFrame newJFrame) {
		this.frame = newJFrame;
		this.caller = new LogicCaller();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// menuitem events
		if (e.getSource() == frame.mapstartjMenuItem) {
			logger.info("Action perforemed on start menu item");
			if (!ApplicationMap.isMapRunning()) {
				ApplicationMap.baseID = frame.currentBase;
				ApplicationMap.getMapInstance();
				frame.jTable4Base.setEnabled(false);
				logger.info("[F1.8] Virtual globe started");
			} else {
				JOptionPane.showMessageDialog(frame, "Map already running");
			}
		}
		if (e.getSource() == frame.plantsjMenuitem) {
			logger.info("Action perforemed on plants menu item");
			refreshPlantTable();
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(true);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(false);
		}
		if (e.getSource() == frame.operationjMenuitem) {
			logger.info("Action perforemed on operations menu item");
			refreshOperationsTable();
			frame.operationsJpanel.setVisible(true);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(false);
		}
		if (e.getSource() == frame.machinesjMenuItem) {
			logger.info("Action perforemed on machines menu item");
			refreshMachinesTable();
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(true);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(false);
			refreshMachinesTable();
		}
		if (e.getSource() == frame.basesjMenuItem) {
			logger.info("Action perforemed on bases menu item");
			refreshBaseTable();
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(true);
			frame.simulationjPanel.setVisible(false);
		}
		if (e.getSource() == frame.simulationjMenuItem) {
			logger.info("Action perforemed on simulation menu item");
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(true);
			initSimulationLists();

		}
		// plantoperation event
		if (e.getSource() == frame.plantoperationjComboBox1) {
			if (frame.plantoperationjComboBox1.getSelectedItem().toString()
					.equals("Add")) {
				logger
						.info("[F1.6]'Add' selected from plant operation combobox");
				comboboxWithOperationsPlants();
				frame.plantoperationselectjComboBox.setVisible(true);
				frame.plantoperationjButtono.setVisible(false);
			} else {
				logger
						.info("[F1.6] 'Remove' selected from plant operation combobox");
				if (frame.jTable1.getSelectedRowCount() != 1) {
					logger
							.info("[F1.6] 'Remove' canceled as no row was selected");
					JOptionPane.showMessageDialog(frame, "Select a row first!");
				} else {
					frame.plantoperationselectjComboBox.setVisible(true);
					frame.plantoperationjButtono.setVisible(false);
					comboboxWithPlantsOperations();
				}
			}
		}

		if (e.getSource() == frame.plantoperationselectjComboBox) {
			if (frame.plantoperationselectjComboBox.getSelectedItem() != null) {
				logger.info(frame.plantoperationselectjComboBox
						.getSelectedItem()
						+ " selected from plant operation combobox");
			}
			frame.plantoperationjButtono.setVisible(true);
		}
		if (e.getSource() == frame.plantoperationjButtono) {
			String logString = new String();
			logString += "OK button pushed with operation "
					+ frame.plantoperationjComboBox1.getSelectedItem()
							.toString() + " ";
			if (frame.plantoperationjComboBox1.getSelectedItem().toString()
					.equals("Add")) {
				if (frame.jTable1.getSelectedRowCount() != 1) {
					JOptionPane.showMessageDialog(frame, "Select a row first!");
					logString += "but no line was selected from table ";
				} else {
					if (frame.plantoperationselectjComboBox.equals("Select")) {
						logString += "but no operation was selected";
						JOptionPane.showMessageDialog(frame,
								"Select an operation first!");
					} else {
						String plantName = frame.jTable1.getValueAt(
								frame.jTable1.getSelectedRow(), 0).toString();
						logString += "for action "
								+ frame.plantoperationselectjComboBox
										.getSelectedItem().toString()
								+ " selected plant:" + plantName;
						frame.plantoperationselectjComboBox.setVisible(false);
						frame.plantoperationjButtono.setVisible(false);
						insertIntoPlantOperations();
					}
				}
			} else {
				if (frame.plantoperationselectjComboBox.equals("Select")) {
					JOptionPane.showMessageDialog(frame,
							"Select an operation first!");
					logString += "no operation was selected";
				} else {
					String plantName = frame.jTable1.getValueAt(
							frame.jTable1.getSelectedRow(), 0).toString();
					logString += " "
							+ frame.plantoperationselectjComboBox
									.getSelectedItem().toString()
							+ " will be removed from plant" + plantName;
					frame.plantoperationselectjComboBox.setVisible(false);
					frame.plantoperationjButtono.setVisible(false);
					deletefromPlantOperations();
				}
			}
			logger.info("[F1.6]" + logString);

		}

		// machineOperation event
		if (e.getSource() == frame.machineOperationaddJcombobox) {
			String loggerString = new String();
			loggerString += "Action on machine Operation combobox ";
			loggerString += frame.machineOperationaddJcombobox
					.getSelectedItem().toString()
					+ " selected ";
			if (frame.machineOperationaddJcombobox.getSelectedItem().toString()
					.equals("Add")) {
				comboboxWithOperationsMachines();
				frame.machineOperationSelectjCombobox.setVisible(true);
				frame.machineOperationjButton.setVisible(false);
			} else {
				if (frame.jTable3Machines.getSelectedRowCount() != 1) {
					loggerString += " no rows selected from table";
					JOptionPane.showMessageDialog(frame, "Select a row first!");
				} else {
					frame.machineOperationSelectjCombobox.setVisible(true);
					frame.machineOperationjButton.setVisible(false);
					comboboxWithMachinesOperations();
				}
			}
			logger.info("[F1.5]" + loggerString);
		}

		if (e.getSource() == frame.machineOperationSelectjCombobox) {
			if (frame.machineOperationSelectjCombobox.getSelectedItem() != null
					&& !frame.machineOperationSelectjCombobox.getSelectedItem()
							.equals("Select")) {
				logger.info(frame.machineOperationSelectjCombobox
						.getSelectedItem()
						+ " selected from machine operation combobox");
			}
			frame.machineOperationjButton.setVisible(true);
		}
		if (e.getSource() == frame.machineOperationjButton) {
			String loggerString = new String();
			loggerString += "OK button pushed with operation "
					+ frame.machineOperationSelectjCombobox.getSelectedItem()
							.toString() + " ";
			if (frame.machineOperationaddJcombobox.getSelectedItem().toString()
					.equals("Add")) {
				if (frame.jTable3Machines.getSelectedRowCount() != 1) {
					loggerString += "but no line was selected from table ";
					JOptionPane.showMessageDialog(frame, "Select a row first!");
				} else {
					if (frame.machineOperationSelectjCombobox.equals("Select")) {
						loggerString += "but no operation was selected";
						JOptionPane.showMessageDialog(frame,
								"Select an operation first!");
					} else {
						String vehicleName = frame.jTable3Machines.getValueAt(
								frame.jTable3Machines.getSelectedRow(), 1)
								.toString();
						loggerString += "for action "
								+ frame.machineOperationaddJcombobox
										.getSelectedItem().toString()
								+ " selected machine:" + vehicleName;
						frame.machineOperationSelectjCombobox.setVisible(false);
						frame.machineOperationjButton.setVisible(false);
						insertIntoMachineTypeOperations();
					}
				}
			} else {
				if (frame.machineOperationSelectjCombobox.equals("Select")) {
					JOptionPane.showMessageDialog(frame,
							"Select an operation first!");
					loggerString += "but no operation was selected";
				} else {
					String vehicleName = frame.jTable3Machines.getValueAt(
							frame.jTable3Machines.getSelectedRow(), 1)
							.toString();
					loggerString += "for action "
							+ frame.machineOperationaddJcombobox
									.getSelectedItem().toString()
							+ " selected machine:" + vehicleName;
					frame.machineOperationSelectjCombobox.setVisible(false);
					frame.machineOperationjButton.setVisible(false);
					deletefromMachineTypeOperations();
				}
			}
			logger.info("[F1.5]" + loggerString);
		}

		// base button events
		if (e.getSource() == frame.baseNewjButton) {
			logger.info("[F1.1] New base button pressed");
			frame.baseNewjPanel.setVisible(true);
		}
		if (e.getSource() == frame.baseOkjButton) {
			logger.info("[F1.1] Confirm plant base insert button pressed");
			baseInsert();
		}
		// plant button events
		if (e.getSource() == frame.plantsNewButton) {
			logger.info("[F1.4] New plant button pressed");
			setPlantsLableTextVisibility(true);
		}
		if (e.getSource() == frame.plantsConfirmNewButton) {
			logger.info("[F1.4] Confirm plant insert button pressed");
			plantsInsert();
		}

		// operation button events
		if (e.getSource() == frame.operationNewjButton) {
			logger.info("[F1.2] New operation button pressed");
			setOperationsLableTextVisibility(true);
		}
		if (e.getSource() == frame.operationsConfirm) {
			logger.info("[F1.2] Confirm operation insert button pressed");
			operationsInsert();
		}

		if (e.getSource() == frame.machinesNewJbutton) {
			logger.info("[F1.3] New machine button pressed");
			setMachinesLableTextVisibility(true);
		}
		if (e.getSource() == frame.machinesConfirm) {
			logger.info("[F1.3] Confirm machine insert button pressed");
			machinesInsert();
		}
		// simulation events
		if (e.getSource() == frame.simulationStartjButton) {
			logger.info("[F1.7] Simulation start button pressed");
			simulate();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == frame.simulationOperationsjList) {
			Object[] ob = new Object[100];
			ob = frame.simulationOperationsjList.getSelectedValues();
			String selected[] = new String[ob.length];
			String loggerString = new String();
			loggerString += "Items selected from operations populate list with compatible equipments [";
			for (int i = 0; i < ob.length; i++) {
				selected[i] = ob[i].toString();
				loggerString += ob[i].toString() + " ";
			}
			loggerString += "]";
			// try{
			ListModel simulationMachinesjListModel = new DefaultComboBoxModel(
					fillSimulationMachinesList(selected));
			simulationMachinesjListModel.getSize();
			frame.simulationMachinesjList = new JList(
					simulationMachinesjListModel);
			frame.simulationMachinesjScrollPane
					.setViewportView(frame.simulationMachinesjList);
			if (e.getValueIsAdjusting())
				logger.info(loggerString);
		}

	}

	/**
	 * Retrieves all Base data from db and converts it to a string matrix
	 * 
	 * @return string matrix with base data
	 */
	private String[][] getalldataBase() {
		ArrayList<Base> basesArrayList = new ArrayList<Base>();
		try {
			basesArrayList = (ArrayList<Base>) caller.getalldataBase();
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = basesArrayList.size();
		String[][] data = new String[row][];
		for (int i = 0; i < row; i++) {
			data[i] = basesArrayList.get(i).toStringArray();
		}
		return data;
	}

	/**
	 * Retrieves all plants data from db and converts it to a string matrix
	 * 
	 * @return string matrix with plants data
	 */
	private String[][] getalldataPlants() {
		ArrayList<Plants> plantsArrayList = new ArrayList<Plants>();
		try {
			plantsArrayList = (ArrayList<Plants>) caller.getalldataPlants();
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = plantsArrayList.size();
		String[][] data = new String[row][];
		for (int i = 0; i < row; i++) {
			data[i] = plantsArrayList.get(i).toStringArrayWithoutID();
		}
		return data;
	}

	/**
	 * Retrieves all operations data from db and converts it to a string matrix
	 * 
	 * @return string matrix with operations data
	 */
	private String[][] getalldataOperations() {
		ArrayList<Operations> operationsArrayList = new ArrayList<Operations>();
		try {
			operationsArrayList = (ArrayList<Operations>) caller
					.getalldataOperations();
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = operationsArrayList.size();
		String[][] data = new String[row][];
		for (int i = 0; i < row; i++) {
			data[i] = operationsArrayList.get(i).toStringArrayWithoutID();
		}
		return data;
	}

	/**
	 * Retrieves all machines data belonging to currently selected base from db
	 * and converts it to a string matrix
	 * 
	 * @return string matrix with machines data
	 */
	private String[][] getalldataMachines() {
		ArrayList<Machines> machinesArrayList = new ArrayList<Machines>();
		try {
			machinesArrayList = (ArrayList<Machines>) caller
					.getalldataMachines(frame.currentBase);
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = machinesArrayList.size();
		String[][] data = new String[row][];
		for (int i = 0; i < row; i++) {
			data[i] = machinesArrayList.get(i).toStringArray();
		}
		return data;
	}

	/**
	 * Fills propper combobox with operation data
	 */
	private void comboboxWithOperationsPlants() {
		ArrayList<Operations> operationsAL = new ArrayList<Operations>();
		try {
			operationsAL = (ArrayList<Operations>) caller
					.getalldataOperations();
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = operationsAL.size();
		frame.plantoperationselectjComboBox.removeAllItems();
		frame.plantoperationselectjComboBox.addItem("Select");
		for (int i = 0; i < row; i++) {
			frame.plantoperationselectjComboBox.addItem(operationsAL.get(i)
					.getName());
		}
	}

	/**
	 * Fills proper combobox with operation data
	 */
	private void comboboxWithOperationsMachines() {
		ArrayList<Operations> operationsAL = new ArrayList<Operations>();
		try {
			operationsAL = (ArrayList<Operations>) caller
					.getalldataOperations();
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = operationsAL.size();
		frame.machineOperationSelectjCombobox.removeAllItems();
		frame.machineOperationSelectjCombobox.addItem("Select");
		for (int i = 0; i < row; i++) {
			frame.machineOperationSelectjCombobox.addItem(operationsAL.get(i)
					.getName());
		}
	}

	/**
	 * Fills proper combobox with operations required by a plant
	 */
	private void comboboxWithPlantsOperations() {
		String type;
		ArrayList<Operations> operationsAL = new ArrayList<Operations>();
		type = frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(), 0)
				.toString();
		try {
			operationsAL = (ArrayList<Operations>) caller
					.getAllOperationForPlantType(type);
			int row = operationsAL.size();
			frame.plantoperationselectjComboBox.removeAllItems();
			frame.plantoperationselectjComboBox.addItem("Select");
			for (int i = 0; i < row; i++) {
				frame.plantoperationselectjComboBox.addItem(operationsAL.get(i)
						.getName());
			}
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Fills proper combobox with operations that can be performed by a machine
	 * type
	 */
	private void comboboxWithMachinesOperations() {
		String machineType;
		ArrayList<Operations> operationsAL = new ArrayList<Operations>();
		machineType = frame.jTable3Machines.getValueAt(
				frame.jTable3Machines.getSelectedRow(), 1).toString();
		try {
			operationsAL = (ArrayList<Operations>) caller
					.getMachinesOperations(machineType);
			int row = operationsAL.size();
			frame.machineOperationSelectjCombobox.removeAllItems();
			frame.machineOperationSelectjCombobox.addItem("Select");
			for (int i = 0; i < row; i++) {
				frame.machineOperationSelectjCombobox.addItem(operationsAL.get(
						i).getName());
			}
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Insert a base into DB based on user input
	 */
	private void baseInsert() {
		frame.baseNewjPanel.setVisible(false);
		try {
			String name = frame.baseNameTextField.getText();
			double latitude = Double.parseDouble(frame.baseLatitudeTextField
					.getText());
			double longitude = Double.parseDouble(frame.baseLongitudeTextField
					.getText());
			if ((Math.abs(latitude) > 90) || (Math.abs(longitude) > 180)
					|| (name.equals(""))) {
				logger
						.info("[F1.1] Unsuccessful base insert due to invalid input data with name "
								+ name
								+ " @ longitude "
								+ longitude
								+ " latitudde " + latitude);
				JOptionPane.showMessageDialog(frame, "Invalid input data!");
			} else {
				caller.baseInsert(new Base(name, latitude, longitude));
				refreshBaseTable();
				logger.info("[F1.1] base inserted with name " + name
						+ " @ longitude " + longitude + " latitudde " + latitude);
			}			
		} catch (NumberFormatException e) {
			logger
					.info("[F1.1] Unsuccessful base insert due to invalid input data "
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, "Invalid input data!");
		} catch (DbException e) {
			logger.info("[F1.1] Unsuccessful base insert due to DB exception "
					+ e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Insert a plant into DB using user input
	 */
	public void plantsInsert() {
		setPlantsLableTextVisibility(false);
		try {
			String type = frame.plantsTypeText.getText();
			int seed = Integer.parseInt(frame.plantsSeedExpense.getText());
			int income = Integer.parseInt(frame.plantsIncomeText.getText());
			if ((type.equals("")) || (seed < 0)) {
				logger
						.info("[F1.4] Unsuccessful Plant insert  due to invalid input data with type "
								+ type
								+ "  seed price "
								+ seed
								+ " income/ha "
								+ income);
				JOptionPane.showMessageDialog(frame, "Invalid input data!");
			} else {
				caller.plantsInsert(new Plants(0, type, seed, income));
				refreshPlantTable();
				logger.info("[F1.4] Plant inserted with type " + type
						+ "  seed price " + seed + " income/ha " + income);
			}		
		} catch (NumberFormatException e) {
			logger
					.info("[F1.4] Unsuccessful Plant insert  due to invalid input data "
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, "Invalid input data!");
		} catch (DbException e) {
			logger.info("[F1.4] Unsuccessful Plant insert  due to DBException "
					+ e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Insert an operation into DB using user input
	 */
	private void operationsInsert() {
		setOperationsLableTextVisibility(false);
		try {
			String name = frame.operationsNameText.getText();

			int price = Integer.parseInt(frame.operationsPriceText.getText());
			double time = Double
					.parseDouble(frame.operationsTimeText.getText());
			if (time < 1) {
				time = 1;
			}
			if (name.equals("") || (price < 0)) {
				logger
						.info("[F1.2] Unsuccessful Operation insert due to invalid data with name "
								+ name
								+ "  price/ha "
								+ price
								+ " time/ha "
								+ time);
				JOptionPane.showMessageDialog(frame, "Invalid input data!");
			} else {
				Operations operation = new Operations(name, price, time);
				caller.operationsInsert(operation);
				refreshOperationsTable();
				logger.info("[F1.2] Operation inserted with name " + name
						+ "  price/ha " + price + " time/ha " + time);
			}			
		} catch (NumberFormatException e) {
			logger
					.info("[F1.2] Unsuccessful Operation insert due to invalid data:"
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, "Invalid input data!");
		} catch (DbException e) {
			logger
					.info("[F1.2] Unsuccessful Operation insert due to DB exception "
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Insert an operation into DB using user input
	 */
	private void machinesInsert() {
		setMachinesLableTextVisibility(false);
		try {
			String id = frame.machinesNewText.getText();
			String type = frame.machinesTypeText.getText();
			int speed = Integer.parseInt(frame.machinesSpeedText.getText());
			double workspeed = Double.parseDouble(frame.machinesWorkSpeedText
					.getText());
			if (id.equals("") || type.equals("") || (speed <= 0)
					|| (workspeed <= 0)) {
				logger
						.info("[F1.3] Unsuccessful Machine insert due to invalid data with name "
								+ id
								+ " type "
								+ type
								+ " speed km/h "
								+ speed
								+ " work speed ha/h " + workspeed);
				JOptionPane.showMessageDialog(frame, "Invalid input data!");
			} else {
				Machines machine = new Machines(id, type, speed, workspeed);
				caller.machinesInsert(machine, frame.currentBase);
				refreshMachinesTable();
				logger.info("[F1.3] Machine inserted with name " + id + " type "
						+ type + " speed km/h " + speed + " work speed ha/h "
						+ workspeed);
			}			
		} catch (NumberFormatException e) {
			logger
					.info("[F1.3] Unsuccessful Machine insert due to invalid data "
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, "Invalid input data!");
		} catch (DbException e) {
			logger
					.info("[F1.3] Unsuccessful Machine insert due to DbException "
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Refresh the plant table with data from DB
	 */
	private void refreshPlantTable() {
		String s[][] = getalldataPlants();
		TableModel jTable1Model = new DefaultTableModel(s, new String[] {
				"Plant", "Seed", "Income" });
		frame.jTable1.setModel(jTable1Model);
	}

	/**
	 * Refresh operations table with data from DB
	 */
	private void refreshOperationsTable() {
		String s[][] = getalldataOperations();
		TableModel jTable1Model = new DefaultTableModel(s, new String[] {
				"Name", "Price/ha", "Machines/ha" });
		frame.jTable2.setModel(jTable1Model);
	}

	/**
	 * Refresh machines table with data from DB
	 */
	private void refreshMachinesTable() {
		String s[][] = getalldataMachines();
		TableModel jTable1Model = new DefaultTableModel(s, new String[] { "ID",
				"Type", "Speed(km/h)", "WorkSpeed(ha/h)" });
		frame.jTable3Machines.setModel(jTable1Model);
	}

	/**
	 * Refresh base table with data from DB
	 */
	public void refreshBaseTable() {
		String s[][] = getalldataBase();
		TableModel jTable1Model = new DefaultTableModel(s, new String[] {
				"Name", "Latitude", "Longitude" });
		frame.jTable4Base.setModel(jTable1Model);
	}

	/**
	 * Set plant's component visibility
	 * 
	 * @param b
	 */
	private void setPlantsLableTextVisibility(boolean b) {
		frame.plantsIncomLable.setVisible(b);
		frame.plantsIncomeText.setVisible(b);
		frame.plantsSeedExpense.setVisible(b);
		frame.plantsSeedLable.setVisible(b);
		frame.plantsTypeText.setVisible(b);
		frame.plantsTypeLabel.setVisible(b);
		frame.plantsConfirmNewButton.setVisible(b);
	}

	/**
	 * Set operation's component visibility
	 * 
	 * @param b
	 */
	private void setOperationsLableTextVisibility(boolean b) {
		frame.operationsNamejLable.setVisible(b);
		frame.operationsNameText.setVisible(b);
		frame.operationsPricejLable.setVisible(b);
		frame.operationsPriceText.setVisible(b);
		frame.operationsTimejLable.setVisible(b);
		frame.operationsTimeText.setVisible(b);
		frame.operationsConfirm.setVisible(b);

	}

	/**
	 * set the visibility of machines JPanel
	 * 
	 * @param b
	 */
	private void setMachinesLableTextVisibility(boolean b) {
		frame.machinenewJpanel.setVisible(b);
	}

	/**
	 * Delete selected base
	 * 
	 * @return boolean
	 */
	private boolean baseDelete() {
		boolean deleted = true;
		String name = frame.jTable4Base.getValueAt(
				frame.jTable4Base.getSelectedRow(), 0).toString();
		try {
			deleted = caller.baseDelete(name, frame.currentBase);
			refreshBaseTable();
		} catch (DbException exc) {
			logger.info("[F1.1] Unsuccessful Delete base with name " + name
					+ " due to" + exc.getMessage());
			JOptionPane.showMessageDialog(frame, "Error on delete!");
		}
		if (deleted) {
			logger.info("[F1.1] Deleted base with name " + name);
		} else {
			logger.info("[F1.1] Unsuccessful Delete base with name " + name);
		}
		return deleted;
	}

	/**
	 * delete selected plant
	 */
	private void plantsDelete() {
		String type = frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(),
				0).toString();
		try {
			caller.plantsDelete(type);
			refreshPlantTable();
			logger.info("[F1.4] Deleted plant with type " + type);
		} catch (DbException exc) {
			logger.info("[F1.4] Unsuccessful Delete plant with type " + type
					+ "due to " + exc.getMessage());
			JOptionPane.showMessageDialog(frame, exc.getMessage());
		}

	}

	/**
	 * delet selected operation
	 */
	private void operationsDelete() {
		String name = frame.jTable2.getValueAt(frame.jTable2.getSelectedRow(),
				0).toString();
		try {
			caller.operationsDelete(name);
			refreshOperationsTable();
			logger.info("[F1.2] Deleted operation with name " + name);
		} catch (Exception exc) {
			logger.info("[F1.2] Unsuccessful Delete operation with name "
					+ name + " due to " + exc.getMessage());
			JOptionPane.showMessageDialog(frame, "Error on delete!");
		}
	}

	/**
	 * delete selected machine
	 */
	private void machinesDelete() {
		String name = frame.jTable3Machines.getValueAt(
				frame.jTable3Machines.getSelectedRow(), 0).toString();
		try {
			caller.machinesDelete(name, frame.currentBase);
			refreshMachinesTable();
			logger.info("[F1.3] Deleted machine with id " + name);
		} catch (Exception exc) {
			logger.info("[F1.3] Unsuccessful Delete machine with name " + name
					+ " due to " + exc.getMessage());
			JOptionPane.showMessageDialog(frame, "Error on delete!");
		}
	}

	/**
	 * Updates machines database table based on user input in machines jtable
	 */
	private void machinesUpdate() {
		try {
			String id = frame.jTable3Machines.getValueAt(
					frame.jTable3Machines.getSelectedRow(), 0).toString();
			String type = frame.jTable3Machines.getValueAt(
					frame.jTable3Machines.getSelectedRow(), 1).toString();
			double speed = Double.parseDouble(frame.jTable3Machines.getValueAt(
					frame.jTable3Machines.getSelectedRow(), 2).toString());
			double workspeed = Double.parseDouble(frame.jTable3Machines
					.getValueAt(frame.jTable3Machines.getSelectedRow(), 3)
					.toString());
			if ((type.equals("")) || (speed <= 0) || (workspeed <= 0)) {
				logger
						.info("[F1.3] Unsuccessful update machine due to invalid input data with name "
								+ id
								+ " type "
								+ type
								+ " speed "
								+ speed
								+ " workspeed " + workspeed);
				JOptionPane.showMessageDialog(frame,
						"Error on update!Incorrect input data!");
			} else {
				caller.machinesUpdate(new Machines(id, type, (int) speed,
						workspeed));
				logger.info("[F1.3] updated machine with id " + id);
			}
			refreshMachinesTable();		
		} catch (NumberFormatException exc) {
			logger.info("[F1.3] Unsuccessful update machine due to " + exc.getMessage());
			JOptionPane.showMessageDialog(frame,
					"Error on update!Incorrect input data!");
		} catch (Exception exc) {
			logger.info("[F1.3] Unsuccessful update machine due to " + exc.getMessage());
			exc.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error on update!");
		}
	}

	/**
	 * Updates plants database table based on user input in plants jtable
	 */
	private void plantsUpdate() {
		try {
			String type = frame.jTable1.getValueAt(
					frame.jTable1.getSelectedRow(), 0).toString();
			int seed = Integer.parseInt(frame.jTable1.getValueAt(
					frame.jTable1.getSelectedRow(), 1).toString());
			int income = Integer.parseInt(frame.jTable1.getValueAt(
					frame.jTable1.getSelectedRow(), 2).toString());
			if ((type.equals("")) || (seed < 0)) {
				logger.info("[F1.4] Unsuccessful update plant due to invalid data with type " + type + " seed " + seed);
				JOptionPane.showMessageDialog(frame,
						"Error on update!Incorrect input data!");
			} else {
				String s[][] = getalldataPlants();
				String oldType = s[frame.jTable1.getSelectedRow()][0];			
				caller.plantsUpdate(oldType, new Plants(0, type, seed, income));
				logger.info("[F1.4] Updated plant with type " + type);
			}
			refreshPlantTable();
			
		} catch (NumberFormatException exc) {
			logger.info("[F1.4] Unsuccessful update plant due to " + exc.getMessage());
			JOptionPane.showMessageDialog(frame,
					"Error on update!Incorrect input data!");
		} catch (DbException exc) {
			logger.info("[F1.4] Unsuccessful update plant due to " + exc.getMessage());
			JOptionPane.showMessageDialog(frame, "Error on update!");
		}
	}

	/**
	 * Updates operations database table based on user input in operations
	 * jtable
	 */
	private void operationsUpdate() {
		try {
			Operations operation = new Operations();
			operation.setName(frame.jTable2.getValueAt(
					frame.jTable2.getSelectedRow(), 0).toString());
			operation.setPrice(Integer.parseInt(frame.jTable2.getValueAt(
					frame.jTable2.getSelectedRow(), 1).toString()));
			if ((operation.getPrice() < 0) || (operation.getName().equals(""))) {
				logger.info("[F1.2] Unsuccessful update operation due to invalid input data name" + operation.getName() + " price "+ operation.getPrice());
				JOptionPane.showMessageDialog(frame,
						"Error on update!Incorrect input data!");
				return;
			}
			double time = Double.parseDouble(frame.jTable2.getValueAt(
					frame.jTable2.getSelectedRow(), 2).toString());
			if (time < 1)
				time = 1;
			double t = time;
			operation.setTime(t);
			String s[][] = getalldataOperations();
			String oldName = s[frame.jTable2.getSelectedRow()][0];
			caller.operationsUpdate(operation, oldName);
			refreshOperationsTable();
			logger.info("[F1.2] Updated operation with name " + oldName);
		} catch (NumberFormatException exc) {
			logger.info("[F1.2] Unsuccessful update operation due " + exc.getMessage());
			JOptionPane.showMessageDialog(frame,
					"Error on update!Incorrect input data!");
		} catch (Exception exc) {
			logger.info("[F1.2] Unsuccessful update operation due " + exc.getMessage());
			JOptionPane.showMessageDialog(frame, "Error on update!");
		}
	}

	/**
	 * Inserts a row into the databases operations table
	 */
	private void insertIntoPlantOperations() {
		try {
			String operationName = frame.plantoperationselectjComboBox
					.getSelectedItem().toString();
			String plantName = null;
			if (operationName.equals("Select")) {
				JOptionPane.showMessageDialog(frame, "Select an operation!");
				logger.info("[F1.6] Unsuccessful assigning of operation to plant as no operation was selected");
				return;
			} else {
				plantName = frame.jTable1.getValueAt(
						frame.jTable1.getSelectedRow(), 0).toString();
			}
			caller.insertIntoPlantOperations(operationName, plantName);
			logger.info("[F1.6] " + operationName
					+ " operation assigned to plant " + plantName);
		} catch (DbException e) {
			logger.info("[F1.6] Unsuccessful assigning of operation to plant due to " + e.getMessage());
			JOptionPane.showMessageDialog(frame, "Allready added");
		}
	}

	/**
	 * Assigns a machineType to a operation based on user input
	 */
	private void insertIntoMachineTypeOperations() {
		try {
			String operationName = frame.machineOperationSelectjCombobox
					.getSelectedItem().toString();
			if (operationName.equals("Select")) {
				logger.info("[F1.5] Unsuccessful assigning of operation to machine as no operation was selected");
				JOptionPane.showMessageDialog(frame, "Select an operation!");
			} else {
				String machinetype = frame.jTable3Machines.getValueAt(
						frame.jTable3Machines.getSelectedRow(), 1).toString();
				caller.insertIntoMachineTypeOperations(operationName,
						machinetype);
				logger.info("[F1.5] " + operationName
						+ " operation can be performed by machine type "
						+ machinetype);
			}
		} catch (DbException e) {
			logger.info("[F1.5] Unsuccessful assigning of operation to machine due to " + e.getMessage());
			JOptionPane.showMessageDialog(frame, "Allready added");
		}
	}

	/**
	 * Delete a operation-plant association
	 */
	private void deletefromPlantOperations() {
		try {
			String operationName = frame.plantoperationselectjComboBox
					.getSelectedItem().toString();
			if (operationName.equals("Select")) {
				logger.info("[F1.6] Unsuccessful removal of operation to plant as no operation was selected");
				JOptionPane.showMessageDialog(frame, "Select an operation!");
			} else {
				String plantName = frame.jTable1.getValueAt(
						frame.jTable1.getSelectedRow(), 0).toString();
				caller.deletefromPlantOperations(operationName, plantName);
				logger
						.info("[F1.6] "
								+ operationName
								+ " operation can no longer be performed on plant type "
								+ plantName);
			}
		} catch (DbException e) {
			logger.info("[F1.6] Unsuccessful removal of operation to plant due to " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Delete a operation-machine type association
	 */
	private void deletefromMachineTypeOperations() {
		try {
			String operationName = frame.machineOperationSelectjCombobox
					.getSelectedItem().toString();
			if (operationName.equals("Select")) {
				logger.info("[F1.5] Unsuccessful removal of operation assigned to machine as no operation was selected");
				JOptionPane.showMessageDialog(frame, "Select an operation!");
			} else {
				String machineName = frame.jTable3Machines.getValueAt(
						frame.jTable3Machines.getSelectedRow(), 1).toString();
				caller.deletefromMachineTypeOperations(operationName,
						machineName);
				logger
						.info("[F1.5] "
								+ operationName
								+ " operation can no longer be performed executed by vehicle type "
								+ machineName);
			}
		} catch (DbException e) {
			logger.info("[F1.5] Unsuccessful removal of operation to machine due to " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Gets data inputed by user for simulation and based on input calls
	 * simulation logic
	 */
	private void simulate() {

		// Get all the selected items using the indices from operation listbox
		int[] selectedIx = frame.simulationOperationsjList.getSelectedIndices();
		ArrayList<String> operationAL = new ArrayList<String>();
		for (int i = 0; i < selectedIx.length; i++) {
			Object sel = frame.simulationOperationsjList.getModel()
					.getElementAt(selectedIx[i]);
			operationAL.add(sel.toString());
		}
		// all selected machines
		selectedIx = null;
		selectedIx = frame.simulationMachinesjList.getSelectedIndices();
		ArrayList<String> machinesAL = new ArrayList<String>();
		for (int i = 0; i < selectedIx.length; i++) {
			Object sel = frame.simulationMachinesjList.getModel().getElementAt(
					selectedIx[i]);
			machinesAL.add(sel.toString());
		}
		// selected fields
		selectedIx = null;
		selectedIx = frame.simulationFieldsjList.getSelectedIndices();
		ArrayList<String> parcelAL = new ArrayList<String>();
		for (int i = 0; i < selectedIx.length; i++) {
			Object sel = frame.simulationFieldsjList.getModel().getElementAt(
					selectedIx[i]);
			parcelAL.add(sel.toString());
		}

		try {
			logger.info("[F1.7] Simulation started wih operations"
					+ operationAL + " using machines " + machinesAL
					+ " on parcells " + parcelAL);
			caller.simulate(operationAL, machinesAL, parcelAL,
					frame.currentBase);
		} catch (DbException e) {
			logger
					.info("[F1.7] Unsuccessful Simulation start due to DB exception "
							+ e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Gets elements to add to simulation operation list
	 * 
	 * @return operations to display
	 */
	private String[] fillSimulationOperationjList() {
		ArrayList<Operations> opAL = new ArrayList<Operations>();
		try {
			opAL = (ArrayList<Operations>) caller.getalldataOperations();
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row = opAL.size();
		String[] s = new String[row];
		for (int i = 0; i < row; i++) {
			s[i] = opAL.get(i).getName();
		}
		// TODO better way needed
		// frame.simulationOperationsjList.setPreferredSize(new
		// java.awt.Dimension(75,s.length*20));
		return s;
	}

	/**
	 * Gets machines that can be used for selected operations and returns their
	 * ID's in a string array
	 * 
	 * @param selected
	 * @return string array containing machine id's
	 */
	private String[] fillSimulationMachinesList(String[] selected) {
		ArrayList<Machines> machines = new ArrayList<Machines>();
		try {
			// s=caller.fillSimulationMachinesList(selected, frame.currentBase);
			machines = (ArrayList<Machines>) caller.simulationMachines(
					selected, frame.currentBase);
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		String s[] = new String[machines.size()];
		Iterator<Machines> iterator = machines.iterator();
		int k = 0;
		while (iterator.hasNext()) {
			s[k] = iterator.next().getID();
			k++;
		}
		return s;

	}

	/**
	 * Select a base
	 */
	private void selectBase() {
		String name = frame.jTable4Base.getValueAt(
				frame.jTable4Base.getSelectedRow(), 0).toString();
		try {
			Base base = caller.selectBase(name);
			;
			frame.currentBase = base.getID();
			frame.jMenu1.setEnabled(true);
			frame.mapjMenu.setEnabled(true);
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	/**
	 * Initialise simulation jPanel's list's
	 */
	private void initSimulationLists() {
		ArrayList<Parcel> parcelAL = new ArrayList<Parcel>();
		try {
			parcelAL = (ArrayList<Parcel>) caller
					.getBaseParcells(frame.currentBase);
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		Iterator<Parcel> iterator = parcelAL.iterator();
		String[] s = new String[parcelAL.size()];
		int i = 0;
		while (iterator.hasNext()) {
			s[i] = iterator.next().getName();
			i++;
		}
		ListModel simulationFieldsjListModel = new DefaultComboBoxModel(s);
		frame.simulationFieldsjList = new JList(simulationFieldsjListModel);
		frame.simulationFieldsjScrollPane
				.setViewportView(frame.simulationFieldsjList);

		// frame.simulationMachinesjList.setPreferredSize(new
		// java.awt.Dimension(75,s.length*20));
		// frame.simulationFieldsjList.setModel(simulationFieldsjListModel);
		ListModel simulationOperationsjListModel = new DefaultComboBoxModel(
				fillSimulationOperationjList());
		frame.simulationOperationsjList = new JList(
				simulationOperationsjListModel);
		frame.simulationOperationsjList.addListSelectionListener(this);
		frame.simulationOperationsjScrollPane
				.setViewportView(frame.simulationOperationsjList);
		// frame.simulationOperationsjList.setModel(simulationOperationsjListModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == frame.jTable4Base) {
			selectBase();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == frame.jTable4Base) {
			if (!ApplicationMap.isMapRunning()) {
				frame.jTable4Base.setEnabled(true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// no logic required

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// no logic required

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// no logic required

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == Event.DELETE) {
			if (event.getSource() == frame.jTable4Base) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected base?") == 0) {
					baseDelete();
				}else{
					logger.info("[F1.1] cancel delete of base" );
				}
			}
			if (event.getSource() == frame.jTable3Machines) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected machine?") == 0) {
					machinesDelete();
				}else{
					logger.info("[F1.3] cancel delete of machine" );
				}
			}
			if (event.getSource() == frame.jTable1) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected plant?") == 0) {
					plantsDelete();
				}else{
					logger.info("[F1.4] cancel delete of plant" );
				}
			}
			if (event.getSource() == frame.jTable2) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected operation?") == 0) {
					operationsDelete();
				}else{
					logger.info("[F1.2] cancel delete of operation" );
				}
			}
		}
		if (event.getKeyCode() == Event.ENTER) {
			if (event.getSource() == frame.jTable3Machines) {
				if (JOptionPane
						.showConfirmDialog(frame, "Update selected row?") == 0) {
					machinesUpdate();
				}else{
					logger.info("[F1.3] cancel update of machine" );
				}
			}
			if (event.getSource() == frame.jTable1) {
				if (JOptionPane
						.showConfirmDialog(frame, "Update selected row?") == 0) {
					plantsUpdate();
				}else{
					logger.info("[F1.4] cancel update of plant" );
				}
			}
			if (event.getSource() == frame.jTable2) {
				if (JOptionPane
						.showConfirmDialog(frame, "Update selected row?") == 0) {
					operationsUpdate();
				}else{
					logger.info("[F1.2] cancel update of operation" );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// no logic

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// no logic

	}

}
