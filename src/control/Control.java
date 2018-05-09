package control;

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
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import core.Base;
import core.Machines;
import core.Operations;
import core.Parcel;
import core.Plants;

import appmap.ApplicationMap;
import calculations.simulation.Simulate;
import dao.BaseDAO;
import dao.DAOFactory;
import dao.MachinesDAO;
import dao.OperationsDAO;
import dao.ParcelDAO;
import dao.PlantsDAO;
import dao.jdbc.DbException;

import main.MainFrame;
/**
 * Control class responds to events in the MainFrame, and comunicates with the DB using abstract DAO factory pattern 
 * @author Gergely Meszaros
 *
 */
public class Control implements ActionListener, ListSelectionListener, MouseListener, KeyListener{
	private MainFrame frame;
	public Control(MainFrame newJFrame) {
		this.frame=newJFrame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//menuitem events
		if (e.getSource()==frame.mapstartjMenuItem){
			if (!ApplicationMap.isMapRunning()){
				ApplicationMap.baseID=frame.currentBase;
				ApplicationMap.getMapInstance();
				frame.jTable4Base.setEnabled(false);
				}else{
					JOptionPane.showMessageDialog(frame, "Map already running");
				}
		}
		if (e.getSource()==frame.plantsjMenuitem){
			refreshPlantTable();
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(true);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(false);
		}
		if (e.getSource()==frame.operationjMenuitem){
			refreshOperationsTable();
			frame.operationsJpanel.setVisible(true);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(false);
		}
		if (e.getSource()==frame.machinesjMenuItem){
			refreshMachinesTable();
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(true);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(false);
			refreshMachinesTable();
		}
		if (e.getSource()==frame.basesjMenuItem){
			refreshBaseTable();
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(true);
			frame.simulationjPanel.setVisible(false);
		}
		if (e.getSource()==frame.simulationjMenuItem){
			frame.operationsJpanel.setVisible(false);
			frame.plantsjPanel.setVisible(false);
			frame.machinesJpanel.setVisible(false);
			frame.basesjPanel.setVisible(false);
			frame.simulationjPanel.setVisible(true);
			initSimulationLists();
			
		}
		//plantoperation event
		if (e.getSource()==frame.plantoperationjComboBox1){
			if (frame.plantoperationjComboBox1.getSelectedItem().toString().equals("Add")){
				comboboxWithOperationsPlants();
				frame.plantoperationselectjComboBox.setVisible(true);
				frame.plantoperationjButtono.setVisible(false);
			}
			else{
				if (frame.jTable1.getSelectedRowCount()!=1){
					JOptionPane.showMessageDialog(frame,"Select a row first!");
				}else{
					frame.plantoperationselectjComboBox.setVisible(true);
					frame.plantoperationjButtono.setVisible(false);
					comboboxWithPlantsOperations();
				}
			}
		}
		
		if(e.getSource()==frame.plantoperationselectjComboBox){
			frame.plantoperationjButtono.setVisible(true);
		}
		if(e.getSource()==frame.plantoperationjButtono){
			if (frame.plantoperationjComboBox1.getSelectedItem().toString().equals("Add")){
				if (frame.jTable1.getSelectedRowCount()!=1){
					JOptionPane.showMessageDialog(frame,"Select a row first!");
				}else{
					if (frame.plantoperationselectjComboBox.equals("Select")){
						JOptionPane.showMessageDialog(frame,"Select an operation first!");
					}
					else{
						frame.plantoperationselectjComboBox.setVisible(false);
						frame.plantoperationjButtono.setVisible(false);
						insertIntoPlantOperations();
					}
				}
			}else{
				if (frame.plantoperationselectjComboBox.equals("Select")){
					JOptionPane.showMessageDialog(frame,"Select an operation first!");
				}
				else{
					frame.plantoperationselectjComboBox.setVisible(false);
					frame.plantoperationjButtono.setVisible(false);
					deletefromPlantOperations();
				}
			}
			
		}
		
		//machineOperation event
		if (e.getSource()==frame.machineOperationaddJcombobox){
			if (frame.machineOperationaddJcombobox.getSelectedItem().toString().equals("Add")){
				comboboxWithOperationsMachines();
				frame.machineOperationSelectjCombobox.setVisible(true);
				frame.machineOperationjButton.setVisible(false);
			}
			else{
				if (frame.jTable3Machines.getSelectedRowCount()!=1){
					JOptionPane.showMessageDialog(frame,"Select a row first!");
				}else{
					frame.machineOperationSelectjCombobox.setVisible(true);
					frame.machineOperationjButton.setVisible(false);
					comboboxWithMachinesOperations();
				}
			}
		}
		
		if(e.getSource()==frame.machineOperationSelectjCombobox){
			frame.machineOperationjButton.setVisible(true);
		}
		if(e.getSource()==frame.machineOperationjButton){
			if (frame.machineOperationaddJcombobox.getSelectedItem().toString().equals("Add")){
				if (frame.jTable3Machines.getSelectedRowCount()!=1){
					JOptionPane.showMessageDialog(frame,"Select a row first!");
				}else{
					if (frame.machineOperationSelectjCombobox.equals("Select")){
						JOptionPane.showMessageDialog(frame,"Select an operation first!");
					}
					else{
						frame.machineOperationSelectjCombobox.setVisible(false);
						frame.machineOperationjButton.setVisible(false);
						insertIntoMachineTypeOperations();
					}
				}
			}else{
				if (frame.machineOperationSelectjCombobox.equals("Select")){
					JOptionPane.showMessageDialog(frame,"Select an operation first!");
				}
				else{
					frame.machineOperationSelectjCombobox.setVisible(false);
					frame.machineOperationjButton.setVisible(false);
					deletefromMachineTypeOperations();
				}
			}
			
		}
		
		//base button events
		if (e.getSource()==frame.baseNewjButton){
			frame.baseNewjPanel.setVisible(true);
		}
		if (e.getSource()==frame.baseOkjButton){
			baseInsert();
		}
		//plant button events
		if (e.getSource()==frame.plantsNewButton){
			setPlantsLableTextVisibility(true);
		}
		if (e.getSource()==frame.plantsConfirmNewButton){
			plantsInsert();
		}
		
		//operation button events
		if (e.getSource()==frame.operationNewjButton){
			setOperationsLableTextVisibility(true);
		}
		if (e.getSource()==frame.operationsConfirm){
			operationsInsert();
		}
		
		if (e.getSource()==frame.machinesNewJbutton){
			setMachinesLableTextVisibility(true);
		}
		if (e.getSource()==frame.machinesConfirm){
			machinesInsert();
		}
		//simulation events
		if (e.getSource()==frame.simulationStartjButton){
			simulate();
		}
		
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource()==frame.simulationOperationsjList){
			Object[] ob=new Object[100];
			ob=frame.simulationOperationsjList.getSelectedValues();
			String selected[]=new String[ob.length];
			for (int i=0;i<ob.length;i++){
				selected[i]=ob[i].toString();
			}
			ListModel simulationMachinesjListModel = 
				new DefaultComboBoxModel(fillSimulationMachinesList(selected));
				frame.simulationMachinesjList.setModel(simulationMachinesjListModel);
		}
		
	}
	private String[][] getalldataBase() {
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		ArrayList<Base> bases=new ArrayList<Base>();
		try{
			bases=(ArrayList<Base>) baseDAO.getAllBases();
		}catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		int row=bases.size();
		String[][] data=new String[row][]; 
		for (int i=0;i<row;i++){
			data[i]=bases.get(i).toStringArray();
		}
		return data;
	}

	
	private String[][] getalldataPlants(){
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		ArrayList<Plants> plants=new ArrayList<Plants>();
		try{
		plants=(ArrayList<Plants>) plantsDAO.getAllPlants();
		}catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		int row=plants.size();
		String[][] data=new String[row][]; 
		for (int i=0;i<row;i++){
			data[i]=plants.get(i).toStringArrayWithoutID();
		}
		return data;
	}
	private String[][] getalldataOperations() {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDao=df.getOperationsDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try{
		operationsAL=(ArrayList<Operations>) operationsDao.getAllOperations();
		}catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		int row=operationsAL.size();
		String[][] data=new String[row][]; 
		for (int i=0;i<row;i++){
			data[i]=operationsAL.get(i).toStringArrayWithoutID();
		}
		return data;
	}
	private String[][] getalldataMachines() {
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		ArrayList<Machines> machinesAL=new ArrayList<Machines>();
		try{
		machinesAL=(ArrayList<Machines>) machinesDAO.getAllMachines(frame.currentBase);
		}catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		int row=machinesAL.size();
		String[][] data=new String[row][]; 
		for (int i=0;i<row;i++){
			data[i]=machinesAL.get(i).toStringArray();
		}
		return data;
	}
	private void comboboxWithOperationsPlants(){
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDao=df.getOperationsDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try{
		operationsAL=(ArrayList<Operations>) operationsDao.getAllOperations();
		}catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		int row=operationsAL.size();
		frame.plantoperationselectjComboBox.removeAllItems();
		frame.plantoperationselectjComboBox.addItem("Select");
		for (int i=0;i<row;i++){
			frame.plantoperationselectjComboBox.addItem(operationsAL.get(i).getName());
		}
	}
	
	private void comboboxWithOperationsMachines(){
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDao=df.getOperationsDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try{
		operationsAL=(ArrayList<Operations>) operationsDao.getAllOperations();
		}catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		int row=operationsAL.size();
		frame.machineOperationSelectjCombobox.removeAllItems();
		frame.machineOperationSelectjCombobox.addItem("Select");
		for (int i=0;i<row;i++){
			frame.machineOperationSelectjCombobox.addItem(operationsAL.get(i).getName());
		}
	}

	private void comboboxWithPlantsOperations() {
		String type;
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		type=frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(),0).toString();
		try {
			int plantID=plantsDAO.getPlantsIDbyType(type);
			OperationsDAO operationsDao=df.getOperationsDAO();
			operationsAL=(ArrayList<Operations>) operationsDao.getAllOperationsForPlant(plantID);
			int row=operationsAL.size();
			frame.plantoperationselectjComboBox.removeAllItems();
			frame.plantoperationselectjComboBox.addItem("Select");
			for (int i=0;i<row;i++){
				frame.plantoperationselectjComboBox.addItem(operationsAL.get(i).getName());
			}
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}	
	}
	
	private void comboboxWithMachinesOperations() {
		String machineType;
		DAOFactory df=DAOFactory.getInstance();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		machineType=frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(),1).toString();
		try {
			OperationsDAO operationsDao=df.getOperationsDAO();
			operationsAL=(ArrayList<Operations>) operationsDao.getAllOperationsForMachine(machineType);
			int row=operationsAL.size();
			frame.machineOperationSelectjCombobox.removeAllItems();
			frame.machineOperationSelectjCombobox.addItem("Select");
			for (int i=0;i<row;i++){
				frame.machineOperationSelectjCombobox.addItem(operationsAL.get(i).getName());
			}
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}	
	}
	
	private void baseInsert() {
		frame.baseNewjPanel.setVisible(false);
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		try {
			String name=frame.baseNameTextField.getText();
			double latitude=Double.parseDouble(frame.baseLatitudeTextField.getText());
			double longitude=Double.parseDouble(frame.baseLongitudeTextField.getText());
			baseDAO.insertBase(new Base(name,latitude,longitude));
			refreshBaseTable();
			}
			catch (NumberFormatException e){
				JOptionPane.showMessageDialog(frame,"Invalid input data!");
			}
			catch (DbException e) {
				JOptionPane.showMessageDialog(frame,e.getMessage());
			}
		
	}
	
	public void plantsInsert(){
		setPlantsLableTextVisibility(false);
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		try {
		String type=frame.plantsTypeText.getText();
		int seed=Integer.parseInt(frame.plantsSeedExpense.getText());
		int income=Integer.parseInt(frame.plantsIncomeText.getText());
			plantsDAO.insertPlant(new Plants(0,type,seed,income));
			refreshPlantTable();
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(frame,"Invalid input data!");
		}
		catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
	}
	
	private void operationsInsert() {
		setOperationsLableTextVisibility(false);
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		try {
		String name=frame.operationsNameText.getText();
		int price=Integer.parseInt(frame.operationsPriceText.getText());
		double time=Double.parseDouble(frame.operationsTimeText.getText());
		if (time<1){
			time=1;
		}
		Operations operation=new Operations(name,price,time);
		operationsDAO.insertOperation(operation);
		refreshOperationsTable();
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(frame,"Invalid input data!");
		}
		catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
	}
	
	private void machinesInsert() {
		setMachinesLableTextVisibility(false);
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		try {
		String id=frame.machinesNewText.getText();
		String type=frame.machinesTypeText.getText();
		int speed=Integer.parseInt(frame.machinesSpeedText.getText());
		double workspeed=Double.parseDouble(frame.machinesWorkSpeedText.getText());
		Machines machine=new Machines(id,type,speed,workspeed);
		machinesDAO.insertMachine(machine,frame.currentBase);
		refreshMachinesTable();
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(frame,"Invalid input data!");
		}
		catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
	}
	
	private void refreshPlantTable(){
		String s[][]=getalldataPlants();
		TableModel jTable1Model =new DefaultTableModel(s,new String[] { "Plant", "Seed","Income" });
		frame.jTable1.setModel(jTable1Model);
	}
	
	private void refreshOperationsTable(){
		String s[][]=getalldataOperations();
		TableModel jTable1Model =new DefaultTableModel(s,new String[] { "Name", "Price/ha","Machines/ha" });
		frame.jTable2.setModel(jTable1Model);
	}
	private void refreshMachinesTable(){
		String s[][]=getalldataMachines();
		TableModel jTable1Model =new DefaultTableModel(s,new String[] { "ID", "Type","Speed(km/h)","WorkSpeed(ha/h)" });
		frame.jTable3Machines.setModel(jTable1Model);
	}
	public void refreshBaseTable(){
		String s[][]=getalldataBase();
		TableModel jTable1Model =new DefaultTableModel(s,new String[] { "Name", "Latitude","Longitude"});
		frame.jTable4Base.setModel(jTable1Model);
	}
	
	private void setPlantsLableTextVisibility(boolean b){
		frame.plantsIncomLable.setVisible(b);
		frame.plantsIncomeText.setVisible(b);
		frame.plantsSeedExpense.setVisible(b);
		frame.plantsSeedLable.setVisible(b);
		frame.plantsTypeText.setVisible(b);
		frame.plantsTypeLabel.setVisible(b);
		frame.plantsConfirmNewButton.setVisible(b);
	}
	private void setOperationsLableTextVisibility(boolean b) {
		frame.operationsNamejLable.setVisible(b);
		frame.operationsNameText.setVisible(b);
		frame.operationsPricejLable.setVisible(b);
		frame.operationsPriceText.setVisible(b);
		frame.operationsTimejLable.setVisible(b);
		frame.operationsTimeText.setVisible(b);
		frame.operationsConfirm.setVisible(b);
		
	}
	private void setMachinesLableTextVisibility(boolean b){
		frame.machinenewJpanel.setVisible(b);
	}
	
	/**
	 * Delete selected base
	 * @return boolean  
	 */
	private boolean baseDelete() {
		boolean deleted=true;
		String name=frame.jTable4Base.getValueAt(frame.jTable4Base.getSelectedRow(), 0).toString();
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		try{
		int baseID= baseDAO.getBaseIdByName(name);
		if ((baseID==ApplicationMap.baseID)){
			JOptionPane.showMessageDialog(frame,"Close map first!");
			deleted=false;
		}else{
			if (baseID==frame.currentBase){
				baseDAO.deleteBaseByName(name);
				frame.jMenu1.setEnabled(false);
				frame.mapjMenu.setEnabled(false);
				refreshBaseTable();
			}else{
				baseDAO.deleteBaseByName(name);
				refreshBaseTable();
			}
		}
		}catch(DbException e){
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		return deleted;
	}
	
	private void plantsDelete(){
		String type=frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(), 0).toString();
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantdao=df.getPlantsDAO();
		try{
		plantdao.deletePlantByType(type);
		refreshPlantTable();
		}catch(DbException e){
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}	
	}
	private void operationsDelete() {
		String name=frame.jTable2.getValueAt(frame.jTable2.getSelectedRow(), 0).toString();
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		try{
			operationsDAO.deleteOperationByName(name);
			refreshOperationsTable();
		}catch(DbException e){
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}		
	}
	private void machinesDelete() {
		String name=frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(), 0).toString();
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		try{
			machinesDAO.deleteMachineByIDAndBaseID(name, frame.currentBase);
			refreshMachinesTable();
		}catch(DbException e){
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}		
	}
	/**
	 * Updates machines database table based on user input in plants jtable
	 */
	private void machinesUpdate(){
		try{
			String id=frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(), 0).toString();
			String type =frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(), 1).toString();
			double speed =Double.parseDouble(frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(), 2).toString());
			double workspeed =Double.parseDouble(frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(), 3).toString());
			DAOFactory df=DAOFactory.getInstance();
			MachinesDAO machinesDao=df.getMachinesDAO();
			machinesDao.updateMachine(new Machines(id, type, (int)speed, workspeed));		
			refreshMachinesTable();
			}catch(NumberFormatException exc){
				JOptionPane.showMessageDialog(frame,"Error on update!Incorrect input data!");
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(frame,e.getMessage());
			}
	}
	/**
	 * Updates plants database table based on user input in plants jtable
	 */
	private void plantsUpdate(){
		try{
			String type=frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(), 0).toString();
			int seed =Integer.parseInt(frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(), 1).toString());
			int income =Integer.parseInt(frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(), 2).toString());
			String s[][]=getalldataPlants();
			String oldType=s[frame.jTable1.getSelectedRow()][0];
			DAOFactory df=DAOFactory.getInstance();
			PlantsDAO plantdao=df.getPlantsDAO();
			plantdao.updatePlant(oldType,new Plants(0,type,seed,income));
			refreshPlantTable();
			}catch(NumberFormatException exc){
				JOptionPane.showMessageDialog(frame,"Error on update!Incorrect input data!");
			}
			catch(DbException e){
				JOptionPane.showMessageDialog(frame,e.getMessage());
			}
	}
	/**
	 * Updates operations database table based on user input in plants jtable
	 */
	private void operationsUpdate(){
		try{
			Operations operation=new Operations();
			operation.setName(frame.jTable2.getValueAt(frame.jTable2.getSelectedRow(), 0).toString());
			operation.setPrice(Integer.parseInt(frame.jTable2.getValueAt(frame.jTable2.getSelectedRow(), 1).toString()));
			int time=(int)Double.parseDouble(frame.jTable2.getValueAt(frame.jTable2.getSelectedRow(), 2).toString());
			if (time<1)
				time++;
			double t=time;
			operation.setTime(t);
			
			String s[][]=getalldataOperations();
			String oldName=s[frame.jTable2.getSelectedRow()][0];
			DAOFactory df=DAOFactory.getInstance();
			OperationsDAO operationsDAO=df.getOperationsDAO();	
			operationsDAO.updateOperationWithOldname(oldName,operation);
			refreshOperationsTable();
			}catch(NumberFormatException exc){
				JOptionPane.showMessageDialog(frame,"Error on update!Incorrect input data!");
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(frame,e.getMessage());
			}
	}
	/**
	 * Inserts a row into the databases operations table
	 */
	private void insertIntoPlantOperations(){
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		PlantsDAO plantsDAO=df.getPlantsDAO();
	try{
		String name=frame.plantoperationselectjComboBox.getSelectedItem().toString();
		if (name.equals("Select")){
			JOptionPane.showMessageDialog(frame,"Select an operation!");
		}else{
			int operationID=operationsDAO.getOperationIDbyName(name);
			name=frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(),0).toString();
			int plantID=plantsDAO.getPlantsIDbyType(name);
			operationsDAO.insertPlantOperation(operationID, plantID);
		}
	}
	catch (DbException e) {
		JOptionPane.showMessageDialog(frame,e.getMessage());
	}
	}
	
	private void insertIntoMachineTypeOperations(){
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
	try{
		String name=frame.machineOperationSelectjCombobox.getSelectedItem().toString();
		if (name.equals("Select")){
			JOptionPane.showMessageDialog(frame,"Select an operation!");
		}else{
			int operationID=operationsDAO.getOperationIDbyName(name);
			String machinetype=frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(),1).toString();
			operationsDAO.insertMachineOperation(operationID, machinetype);
		}
	}
	catch (DbException e) {
		JOptionPane.showMessageDialog(frame,e.getMessage());
	}
	}
	
	
	private void deletefromPlantOperations() {

		DAOFactory df = DAOFactory.getInstance();
		OperationsDAO operationsDAO = df.getOperationsDAO();
		PlantsDAO plantsDAO = df.getPlantsDAO();
		try {
			String name = frame.plantoperationselectjComboBox.getSelectedItem()
					.toString();
			if (name.equals("Select")) {
				JOptionPane.showMessageDialog(frame, "Select an operation!");
			} else {
				int operationID = operationsDAO.getOperationIDbyName(name);
				name = frame.jTable1.getValueAt(frame.jTable1.getSelectedRow(),
						0).toString();
				int plantID = plantsDAO.getPlantsIDbyType(name);
				operationsDAO.deleteOperationForPlant(operationID, plantID);
			}
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
	}
	
	private void deletefromMachineTypeOperations(){
		DAOFactory df = DAOFactory.getInstance();
		OperationsDAO operationsDAO = df.getOperationsDAO();
		try {
			String name = frame.machineOperationSelectjCombobox.getSelectedItem().toString();
			if (name.equals("Select")) {
				JOptionPane.showMessageDialog(frame, "Select an operation!");
			} else {
				int operationID = operationsDAO.getOperationIDbyName(name);
				name = frame.jTable3Machines.getValueAt(frame.jTable3Machines.getSelectedRow(),1).toString();
				operationsDAO.deleteOperationForMachineType(operationID, name);
			}
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
	}
	
	private void simulate(){
		
	    // Get all the selected items using the indices from operation listbox
		int[] selectedIx = frame.simulationOperationsjList.getSelectedIndices();
		ArrayList<Operations> operationAL=new ArrayList<Operations>();
	    for (int i = 0; i < selectedIx.length; i++) {
	      Object sel = frame.simulationOperationsjList.getModel().getElementAt(selectedIx[i]);
	      DAOFactory df=DAOFactory.getInstance();
	      OperationsDAO operationsDAO=df.getOperationsDAO();   
	      try{
	    	  operationAL.add(operationsDAO.getOperationByID(operationsDAO.getOperationIDbyName(sel.toString())));
	         } catch (DbException e) {
	        	 JOptionPane.showMessageDialog(frame,e.getMessage());
			 }
	    }
	    //all selected machines
	    selectedIx=null;
	    selectedIx = frame.simulationMachinesjList.getSelectedIndices();
	    ArrayList<Machines> machinesAL=new ArrayList<Machines>();
	    for (int i = 0; i < selectedIx.length; i++) {
	      Object sel = frame.simulationMachinesjList.getModel().getElementAt(selectedIx[i]);
	      DAOFactory df=DAOFactory.getInstance();
	      MachinesDAO machinesDAO=df.getMachinesDAO();   
	      try{
	    	  machinesAL.add(machinesDAO.getMachineByID(sel.toString()));
	         } catch (DbException e) {
	      		JOptionPane.showMessageDialog(frame, e.getMessage());
			 }
	    }
	    //selected fields
	    selectedIx=null;
	    selectedIx = frame.simulationFieldsjList.getSelectedIndices();
	    ArrayList<Parcel> parcelAL=new ArrayList<Parcel>();
	    	for (int i=0; i < selectedIx.length; i++){
	    		 Object sel = frame.simulationFieldsjList.getModel().getElementAt(selectedIx[i]);
	    		 DAOFactory df=DAOFactory.getInstance();
	   	      	 ParcelDAO parcelDAO=df.getParcelDao();
	   	      	 try {
					parcelAL.add(parcelDAO.getParcelByID(parcelDAO.getParcelIdByName(sel.toString())));
				} catch (DbException e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}
	    	}
	    	if (!Simulate.isSimulationRunning()){
	    		
	    		Simulate simulate=Simulate.getSimulationInstance(operationAL,machinesAL,parcelAL,frame.currentBase);
	    		Thread t=new Thread(simulate);
	    		t.start();
	    	}
	    	
	    }
	    
	    
	private String[] fillSimulationOperationjList() {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		ArrayList<Operations> opAL=new ArrayList<Operations>();
		try{
		opAL=(ArrayList<Operations>) operationsDAO.getAllOperations();
		}
		catch(DbException e){
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
		int row=opAL.size();
		String[] s=new String[row];
		for (int i=0;i<row;i++){
			s[i]=opAL.get(i).getName();
		}
		return s;
	}
	private String[] fillSimulationMachinesList(String[] selected){
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		ArrayList<String> machines=new ArrayList<String>();
		for (int i=0;i<selected.length;i++){
			Operations op=new Operations();
			ArrayList<Machines> machinesArrayList=new ArrayList<Machines>();
			try{
				op=operationsDAO.getOperationByID(operationsDAO.getOperationIDbyName(selected[i]));
				machinesArrayList=(ArrayList<Machines>) machinesDAO.getAllMachinesForOperationFromBase(frame.currentBase, op.getId());
			}
			catch(DbException e){
				JOptionPane.showMessageDialog(frame,e.getMessage());
			}
			int row=machinesArrayList.size();
			for (int j=0;j<row;j++){
				machines.add(machinesArrayList.get(j).getID());
			}
		}
		String s[]=new String[machines.size()];
		Iterator<String> iterator=machines.iterator();
		int k=0;
		while(iterator.hasNext()){
			s[k]=iterator.next().toString();
			k++;
		}
		
		return s;
		
	}

	
	private void selectBase(){
		String name=frame.jTable4Base.getValueAt(frame.jTable4Base.getSelectedRow(), 0).toString();
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		if (!ApplicationMap.isMapRunning()){
			try {
				int baseID=baseDAO.getBaseIdByName(name);
				frame.currentBase=baseID;
				frame.jMenu1.setEnabled(true);
				frame.mapjMenu.setEnabled(true);
			} catch (DbException e) {
				JOptionPane.showMessageDialog(frame,e.getMessage());
			}
		}/*else{
			JOptionPane.showMessageDialog(frame,"Close map first");
		}*/
	}
	private void initSimulationLists(){
		DAOFactory df=DAOFactory.getInstance();
		ParcelDAO parcelDAO=df.getParcelDao();
		ArrayList<Parcel> parcelAL=new ArrayList<Parcel>();
		try {
			parcelAL=(ArrayList<Parcel>) parcelDAO.getAllParcelFromBase(frame.currentBase);
		} catch (DbException e) {
			JOptionPane.showMessageDialog(frame,e.getMessage());
		}
		Iterator<Parcel> iterator=parcelAL.iterator();
		String[] s=new String[parcelAL.size()];
		int i=0;
		while (iterator.hasNext()){
			s[i]=iterator.next().getName();
			i++;
		}
		ListModel simulationFieldsjListModel = 
			new DefaultComboBoxModel(s);
		frame.simulationFieldsjList.setModel(simulationFieldsjListModel);
		
		ListModel simulationOperationsjListModel = 
			new DefaultComboBoxModel(fillSimulationOperationjList());
		frame.simulationOperationsjList.setModel(simulationOperationsjListModel);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	    if (e.getSource()==frame.jTable4Base){
		selectBase();
	    }
	    
	}

	/* (non-Javadoc)
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
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	 // no logic required
	    
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
	 // no logic required
	    
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
	 // no logic required
	    
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == Event.DELETE) {
			if (event.getSource() == frame.jTable4Base) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected base?") == 0) {
					baseDelete();
				}
			}
			if (event.getSource() == frame.jTable3Machines) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected machine?") == 0) {
					machinesDelete();
				}
			}
			if (event.getSource() == frame.jTable1) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected plant?") == 0) {
					plantsDelete();
				}
			}
			if (event.getSource() == frame.jTable2) {
				if (JOptionPane.showConfirmDialog(frame,
						"Delete selected operation?") == 0) {
					operationsDelete();
				}
			}
		}
		if (event.getKeyCode() == Event.ENTER) {
			if (event.getSource() == frame.jTable3Machines) {
				if (JOptionPane.showConfirmDialog(frame,
						"Update selected row?") == 0) {
					machinesUpdate();
				}
			}
			if (event.getSource() == frame.jTable1) {
				if (JOptionPane.showConfirmDialog(frame,
						"Update selected row?") == 0) {
					plantsUpdate();
				}
			}
			if (event.getSource() == frame.jTable2) {
				if (JOptionPane.showConfirmDialog(frame,
						"Update selected row?") == 0) {
					operationsUpdate();
				}
			}
		}
    }
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
	    // no logic
	    
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
	    // no logic
	    
	}
	

	

}
