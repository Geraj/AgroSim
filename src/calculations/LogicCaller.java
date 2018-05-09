package calculations;

import java.util.ArrayList;
import java.util.List;

import appmap.ApplicationMap;
import calculations.simulation.Simulate;
import core.Base;
import core.Machines;
import core.Operations;
import core.Parcel;
import core.Plants;
import dao.BaseDAO;
import dao.DAOFactory;
import dao.MachinesDAO;
import dao.OperationsDAO;
import dao.ParcelDAO;
import dao.PlantsDAO;
import dao.jdbc.DbException;

public class LogicCaller implements LogicCallerInterface {
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#getalldataBase()
	 */
	public ArrayList<Base> getalldataBase() throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		ArrayList<Base> bases=new ArrayList<Base>();
		try{
			bases=(ArrayList<Base>) baseDAO.getAllBases();
		}catch (DbException e) {
			throw new DbException(e);
		}
		return bases;
	}

	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#getalldataPlants()
	 */
	public ArrayList<Plants> getalldataPlants() throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		ArrayList<Plants> plants=new ArrayList<Plants>();
		try{
		plants=(ArrayList<Plants>) plantsDAO.getAllPlants();
		}catch (DbException e) {
			throw new DbException(e);
		}
		return plants;
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#getalldataOperations()
	 */
	public ArrayList<Operations> getalldataOperations()throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDao=df.getOperationsDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try{
		operationsAL=(ArrayList<Operations>) operationsDao.getAllOperations();
		}catch (DbException e) {
			throw new DbException(e);
		}
		return operationsAL;
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#getalldataMachines(int)
	 */
	public ArrayList<Machines> getalldataMachines(int currentBase)throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		ArrayList<Machines> machinesAL=new ArrayList<Machines>();
		try{
		machinesAL=(ArrayList<Machines>) machinesDAO.getAllMachines(currentBase);
		}catch (DbException e) {
			throw new DbException(e);
		}
		int row=machinesAL.size();
		String[][] data=new String[row][]; 
		for (int i=0;i<row;i++){
			data[i]=machinesAL.get(i).toStringArray();
		}
		return machinesAL;
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#comboboxWithMachinesOperations(java.lang.String)
	 */
	public List<Operations> getMachinesOperations(String machineType) throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try {
			OperationsDAO operationsDao=df.getOperationsDAO();
			operationsAL=(ArrayList<Operations>) operationsDao.getAllOperationsForMachine(machineType);
		} catch (DbException e) {
			throw e;
		}
		return operationsAL;
	}
	
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#baseInsert(core.Base)
	 */
	public void baseInsert(Base base)throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		try {
			baseDAO.insertBase(base);
			}
			catch (DbException e) {
				throw new DbException(e);
			}
		
	}
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#plantsInsert(java.lang.String, int, int)
	 */
	public void plantsInsert(Plants plant)throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		try {
			plantsDAO.insertPlant(plant);
		}
		catch (DbException e) {
			throw new DbException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#operationsInsert(core.Operations)
	 */
	public void operationsInsert(Operations operation) throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		try {
		operationsDAO.insertOperation(operation);
		}
		catch (DbException e) {
		throw new DbException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#machinesInsert(core.Machines, int)
	 */
	public void machinesInsert(Machines machine, int currentbase) throws DbException{

		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		try {;
		machinesDAO.insertMachine(machine,currentbase);

		}
		catch (DbException e) {
			throw new DbException(e);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#baseDelete(java.lang.String, int)
	 */
	public boolean baseDelete(String name,int currentBase)throws DbException {
		boolean deleted=true;
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		try{
		int baseID= baseDAO.getBaseIdByName(name);
		if ((baseID==ApplicationMap.baseID)){deleted=false;
			throw new DbException("close Map First");
			
		}else{
			baseDAO.deleteBaseByName(name);
		}
		}catch(DbException exc){
			throw new DbException(exc);
		}
		return deleted;
	}
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#plantsDelete(java.lang.String)
	 */
	public void plantsDelete(String type)throws DbException{
		
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantdao=df.getPlantsDAO();
		try{
		plantdao.deletePlantByType(type);
		}catch(DbException exc){
			throw new DbException(exc);
		}	
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#operationsDelete(java.lang.String)
	 */
	public void operationsDelete(String name)throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		try{
			operationsDAO.deleteOperationByName(name);
		}catch(DbException exc){
			throw new DbException(exc);
		}		
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#machinesDelete(java.lang.String, int)
	 */
	public void machinesDelete(String name,int base)throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		try{
			machinesDAO.deleteMachineByIDAndBaseID(name, base);
		}catch(DbException exc){
			throw new DbException(exc);
		}		
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#machinesUpdate(core.Machines)
	 */
	public void machinesUpdate(Machines machine) throws DbException {
		try {
			DAOFactory df = DAOFactory.getInstance();
			MachinesDAO machinesDao = df.getMachinesDAO();
			machinesDao.updateMachine(machine);
		} catch (DbException exc) {
			throw exc;
		}
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#plantsUpdate(java.lang.String, core.Plants)
	 */
	public void plantsUpdate(String oldType, Plants plant) throws DbException {
		try {
			DAOFactory df = DAOFactory.getInstance();
			PlantsDAO plantdao = df.getPlantsDAO();
			plantdao.updatePlant(oldType, plant);
		} catch (DbException exc) {
			throw exc;
		}
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#operationsUpdate(core.Operations, java.lang.String)
	 */
	public void operationsUpdate(Operations operation, String oldName)
			throws DbException {
		try {
			DAOFactory df = DAOFactory.getInstance();
			OperationsDAO operationsDAO = df.getOperationsDAO();
			operationsDAO.updateOperationWithOldname(oldName, operation);
		} catch (DbException exc) {
			throw exc;
		}
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#insertIntoPlantOperations(int, int)
	 */
	public void insertIntoPlantOperations(String operationName, String plantName)
			throws DbException {
		DAOFactory df = DAOFactory.getInstance();
		OperationsDAO operationsDAO = df.getOperationsDAO();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		try {
			operationsDAO.insertPlantOperation(operationsDAO.getOperationIDbyName(operationName), plantsDAO.getPlantsIDbyType(plantName));
		} catch (DbException e) {
			throw new DbException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#insertIntoMachineTypeOperations(int, java.lang.String)
	 */
	public void insertIntoMachineTypeOperations(String operationName, String machinetype) throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
	try{
			operationsDAO.insertMachineOperation(operationsDAO.getOperationIDbyName(operationName), machinetype);
	}
	catch (DbException e) {
		throw new DbException(e);
	}
	}
	
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#deletefromPlantOperations(int, int)
	 */
	public void deletefromPlantOperations(String operationName, String plantName)
			throws DbException {

		DAOFactory df = DAOFactory.getInstance();
		OperationsDAO operationsDAO = df.getOperationsDAO();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		try {
			operationsDAO.deleteOperationForPlant(operationsDAO.getOperationIDbyName(operationName), plantsDAO.getPlantsIDbyType(plantName));
		} catch (DbException e) {
			throw new DbException(e);
		}
	}

	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#deletefromMachineTypeOperations(int, java.lang.String)
	 */
	public void deletefromMachineTypeOperations(String operationName,String machinetype)
			throws DbException {
		DAOFactory df = DAOFactory.getInstance();
		OperationsDAO operationsDAO = df.getOperationsDAO();
		try {
			operationsDAO.deleteOperationForMachineType(operationsDAO.getOperationIDbyName(operationName), machinetype);
		} catch (DbException e) {
			throw new DbException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#simulate(java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, int)
	 */
	public void simulate(List<String> selectedOperationsName,List<String> selectedMachinesName, List<String> selectedParcelsName,int baseID) throws DbException{
		
	    // Get all the selected items using the indices from operation listbox
		ArrayList<Operations> operationAL=new ArrayList<Operations>();
		DAOFactory df=DAOFactory.getInstance();
	    OperationsDAO operationsDAO=df.getOperationsDAO(); 
	    for (int i = 0; i < selectedOperationsName.size(); i++) {  
	      try{
	    	  operationAL.add(operationsDAO.getOperationByID(operationsDAO.getOperationIDbyName(selectedOperationsName.get(i))));
	         } catch (DbException e) {
	      		throw e;
			 }
	    }
	    //all selected machines
	    ArrayList<Machines> machinesAL=new ArrayList<Machines>();
	    MachinesDAO machinesDAO=df.getMachinesDAO();   
	    for (int i = 0; i < selectedMachinesName.size(); i++) {
	      try{
	    	  machinesAL.add(machinesDAO.getMachineByID(selectedMachinesName.get(i)));
	         } catch (DbException e) {
	      		throw e;
			 }
	    }
	    //selected fields
	    ArrayList<Parcel> parcelAL=new ArrayList<Parcel>();
	    ParcelDAO parcelDAO=df.getParcelDao();
	    	for (int i=0; i < selectedParcelsName.size(); i++){
	   	      	try {
					parcelAL.add(parcelDAO.getParcelByID(parcelDAO.getParcelIdByName(selectedParcelsName.get(i))));
				} catch (DbException e) {
					throw e;
				}
	    	}
	    	if (!Simulate.isSimulationRunning()){
	    		Simulate simulate=Simulate.getSimulationInstance(operationAL,machinesAL,parcelAL,baseID);
	    		Thread t=new Thread(simulate);
	    		t.start();
	    	}
	    	
	    }
	    
	    
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#fillSimulationOperationjList()
	 */
	/*public String[] fillSimulationOperationjList() throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		ArrayList<Operations> opAL=new ArrayList<Operations>();
		try{
		opAL=(ArrayList<Operations>) operationsDAO.getAllOperations();
		}
		catch(DbException e){
			throw e;
		}
		int row=opAL.size();
		String[] s=new String[row];
		for (int i=0;i<row;i++){
			s[i]=opAL.get(i).getName();
		}
		return s;
	}
	*/
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#fillSimulationMachinesList(java.lang.String[], int)
	 */
	public ArrayList<Machines> simulationMachines(String[] selected, int baseID) throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		MachinesDAO machinesDAO=df.getMachinesDAO();
		OperationsDAO operationsDAO=df.getOperationsDAO();
		ArrayList<Machines> machines=new ArrayList<Machines>();
		for (int i=0;i<selected.length;i++){
			Operations op=new Operations();
			ArrayList<Machines> machinesArrayList=new ArrayList<Machines>();
			try{
				op=operationsDAO.getOperationByID(operationsDAO.getOperationIDbyName(selected[i]));
				machinesArrayList=(ArrayList<Machines>) machinesDAO.getAllMachinesForOperationFromBase(baseID, op.getId());
			}
			catch(DbException e){
				throw e;
			}
			int row=machinesArrayList.size();
			for (int j=0;j<row;j++){
					String machineid=machinesArrayList.get(j).getID();
					boolean contains=false;
					for (int z=0;z<machines.size();z++){
						if (machines.get(z).getID().equals(machineid)){
							contains=true;
						}
					}
					if (!contains){
						machines.add(machinesArrayList.get(j));
					}
			}
		}
		return machines;
		
	}
	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#selectBase(java.lang.String)
	 */
	public Base selectBase(String name) throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		BaseDAO baseDAO=df.getBaseDAO();
		Base base=new Base();
		int baseID=-1;
		if (!ApplicationMap.isMapRunning()){
			try {
				 baseID=baseDAO.getBaseIdByName(name);
				 base=baseDAO.getBaseByID(baseID);
			} catch (DbException e) {
				throw e;
			}	
		}else{
			throw new DbException("Close Map First");
		}
		return base;
	}

	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#initSimulationLists(int)
	 */
	public ArrayList<Parcel> getBaseParcells(int baseId) throws DbException{
		DAOFactory df=DAOFactory.getInstance();
		ParcelDAO parcelDAO=df.getParcelDao();
		ArrayList<Parcel> parcelAL=new ArrayList<Parcel>();
		try {
			parcelAL=(ArrayList<Parcel>) parcelDAO.getAllParcelFromBase(baseId);
		} catch (DbException e) {
			throw e;
		}
		return parcelAL;
	}


	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#getAllOperationForPlantType(java.lang.String)
	 */
	@Override
	public List<Operations> getAllOperationForPlantType(String type)
			throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDao=df.getOperationsDAO();
		PlantsDAO plantsDAO=df.getPlantsDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try{
		operationsAL=(ArrayList<Operations>) operationsDao.getAllOperationsForPlant(plantsDAO.getPlantsIDbyType(type));
		}catch (DbException e) {
			throw new DbException(e);
		}
		return operationsAL;
	}


	/* (non-Javadoc)
	 * @see calculations.LogicCallerInterface#getAllOperationForMachineType(java.lang.String)
	 */
	@Override
	public List<Operations> getAllOperationForMachineType(String machineName)
			throws DbException {
		DAOFactory df=DAOFactory.getInstance();
		OperationsDAO operationsDao=df.getOperationsDAO();
		MachinesDAO machineDao=df.getMachinesDAO();
		ArrayList<Operations> operationsAL=new ArrayList<Operations>();
		try{
		operationsAL=(ArrayList<Operations>) operationsDao.getAllOperationsForMachine(machineDao.getMachineByID(machineName).getID());
		}catch (DbException e) {
			throw new DbException(e);
		}
		return operationsAL;
	}

}