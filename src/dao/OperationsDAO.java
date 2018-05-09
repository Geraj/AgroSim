package dao;

import java.util.List;


import core.Operations;
import dao.jdbc.DbException;

public interface OperationsDAO {
	/**
	 * @return
	 * @throws DbException
	 */
	List<Operations> getAllOperations() throws DbException;
	/**
	 * @param operation
	 * @throws DbException
	 */
	void insertOperation(Operations operation) throws DbException;
	/**
	 * @param name
	 * @throws DbException
	 */
	void deleteOperationByName(String name) throws DbException;
	/**Updates an operation
	 * @param oldName
	 * @param operation
	 * @throws DbException
	 */
	void updateOperationWithOldname(String oldName, Operations operation) throws DbException;
	/**Assigns an operation to a plant
	 * @param operationID
	 * @param plantID
	 * @throws DbException
	 */
	void insertPlantOperation(int operationID,int plantID) throws DbException;
	/**
	 * @param name
	 * @return
	 * @throws DbException
	 */
	int getOperationIDbyName(String name) throws DbException;
	/**Gets the operations required for a plant
	 * @param plantID
	 * @return
	 * @throws DbException
	 */
	List<Operations> getAllOperationsForPlant(int plantID) throws DbException;
    /**
     * @param operationID
     * @param plantID
     * @throws DbException
     */
    void deleteOperationForPlant(int operationID,int plantID)throws DbException;
	/**
	 * @param machineID
	 * @return
	 * @throws DbException
	 */
	List<Operations> getAllOperationsForMachine(String machineID)throws DbException;
	/**
	 * @param operationID
	 * @param machinetype
	 * @throws DbException
	 */
	void insertMachineOperation(int operationID, String machinetype)throws DbException;
	/**
	 * @param operationID
	 * @param MachineType
	 * @throws DbException
	 */
	void deleteOperationForMachineType(int operationID, String MachineType)throws DbException;
	/**
	 * @param plantid
	 * @param operationID
	 * @return
	 * @throws DbException
	 */
	boolean doasePlantNeedThisOperation(int plantid,int operationID) throws DbException;
	/**
	 * @param operationID
	 * @return
	 * @throws DbException
	 */
	Operations getOperationByID(int operationID) throws DbException;
}
