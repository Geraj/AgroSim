package calculations;

import java.util.List;

import core.Base;
import core.Machines;
import core.Operations;
import core.Parcel;
import core.Plants;
import dao.jdbc.DbException;

/**
 * @author Gergely Meszaros
 *
 */
public interface LogicCallerInterface {
	/**
	 * Gets all Base data form database 
	 * @return List containing base data
	 * @throws DbException
	 */
	public abstract List<Base> getalldataBase() throws DbException;

	/**
	 * Gets all plants data form database 
	 * @return List containing plants data
	 * @throws DbException
	 */
	public abstract List<Plants> getalldataPlants() throws DbException;

	/**Gets all operations data form database 
	 * @return List containing operations data
	 * @throws DbException
	 */
	public abstract List<Operations> getalldataOperations() throws DbException;

	/**Gets all machines belonging to a base
	 * @param currentBase id of the currently selected base
	 * @return List containing bases machines
	 * @throws DbException
	 */
	public abstract List<Machines> getalldataMachines(int currentBase)
			throws DbException;

	/**Get all operations for machine type
	 * @param machineType
	 * @return list of operations which a machine type can perform
	 * @throws DbException
	 */
	public abstract List<Operations> getMachinesOperations(
			String machineType) throws DbException;

	
	/**Insert base into DB
	 * @param base -base to insert
	 * @throws DbException
	 */
	public abstract void baseInsert(Base base) throws DbException;

	
	/**Insert a plant into DB
	 * @param plant - plant to insert
	 * @throws DbException
	 */
	public abstract void plantsInsert(Plants plant)
			throws DbException;

	/**Insert a operation into DB
	 * @param operation - operation to insert
	 * @throws DbException
	 */
	public abstract void operationsInsert(Operations operation)
			throws DbException;

	/**Insert a machine into DB
	 * @param machine - machine to insert
	 * @param currentbase - base which will contain the machine
	 * @throws DbException
	 */
	public abstract void machinesInsert(Machines machine, int currentbase)
			throws DbException;

	/**
	 * Delete base if it has no map started.
	 * @param name - name of the base
	 * @param currentBase - currently selected base
	 * @return true if delete is successful
	 * @throws DbException
	 */
	public abstract boolean baseDelete(String name, int currentBase)
			throws DbException;

	/**Delete plant
	 * @param type - plant type to delete
	 * @throws DbException
	 */
	public abstract void plantsDelete(String type) throws DbException;

	/**Delete operation with certain name
	 * @param name - name of the operation
	 * @throws DbException
	 */
	public abstract void operationsDelete(String name) throws DbException;

	/**Delete a machine belonging to a base
	 * @param name - name of the machine
	 * @param base - base which contains the machine
	 * @throws DbException
	 */
	public abstract void machinesDelete(String name, int base)
			throws DbException;

	/**
	 * Updates machine
	 * @param machine
	 * @throws DbException
	 */
	public abstract void machinesUpdate(Machines machine) throws DbException;

	/**
	 * Updates plants based on its type
	 * @param oldType
	 * @param plant
	 * @throws DbException
	 */
	public abstract void plantsUpdate(String oldType, Plants plant)
			throws DbException;

	/**
	 * Updates operation
	 * @param operation
	 * @param oldName
	 * @throws DbException
	 */
	public abstract void operationsUpdate(Operations operation, String oldName)
			throws DbException;
	/**
	 * Inserts plants operations
	 * @param operationName
	 * @param plantName
	 * @throws DbException
	 */
	public abstract void insertIntoPlantOperations(String operationName, String plantName)
			throws DbException;

	/**Add an operation to a machine type
	 * @param operationName
	 * @param machinetype
	 * @throws DbException
	 */
	public abstract void insertIntoMachineTypeOperations(String operationName,
			String machinetype) throws DbException;

	/**Remove an operation from plant type
	 * @param operationName
	 * @param machinetype
	 * @throws DbException
	 */
	public abstract void deletefromPlantOperations(String operationName,
			String machinetype)	throws DbException;

	/**
	 * @param operationName
	 * @param machinetype
	 * @throws DbException
	 */
	public abstract void deletefromMachineTypeOperations(String operationName,
			String machinetype) throws DbException;

	/**Start a simulation based on input
	 * @param selectedOperationsName
	 * @param selectedMachinesName
	 * @param selectedParcelsName
	 * @param baseID
	 * @throws DbException
	 */
	public abstract void simulate(List<String> selectedOperationsName,
			List<String> selectedMachinesName,
			List<String> selectedParcelsName, int baseID)
			throws DbException;

	/**Get machines for selected operations
	 * @param selected - selected operations names
	 * @param baseID - id of currently selected base
	 * @return list of machines from base which can perform at least on of the selected operations
	 * @throws DbException
	 */
	public abstract List<Machines> simulationMachines(String[] selected,
			int baseID) throws DbException;

	/**Gets a base by it's name
	 * @param name - name of the base
	 * @return base
	 * @throws DbException
	 */
	public abstract Base selectBase(String name) throws DbException;

	/**Get all parcels belonging to a base by id
	 * @param baseID - id of the base
	 * @return list of parcels related to the base
	 * @throws DbException
	 */
	public abstract List<Parcel> getBaseParcells(int baseID) throws DbException;
	
	/**Get all operations required by a plant
	 * @param type - plant name
	 * @return list of operations
	 * @throws DbException
	 */
	public abstract List<Operations> getAllOperationForPlantType(String type) throws DbException;
	
	/**
	 * @param machineName
	 * @return
	 * @throws DbException
	 */
	public abstract List<Operations> getAllOperationForMachineType(String machineName) throws DbException;

}