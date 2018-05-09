package dao;

import java.util.List;

import core.Machines;
import dao.jdbc.DbException;

public interface MachinesDAO {
	/**Gets all machines which belongs to a base
	 * @param baseID
	 * @return
	 * @throws Exception
	 */
	List<Machines> getAllMachines(int baseID) throws DbException;
	/**
	 * @param id
	 * @throws DbException
	 */
	void deleteMachineByID(String id) throws DbException;
	/**insert machine into base
	 * @param machine
	 * @param baseID
	 * @throws DbException
	 */
	void insertMachine(Machines machine,int baseID)throws DbException;
	/**Deletes a machine belonging to a base
	 * @param name
	 * @param currentBase
	 * @throws DbException
	 */
	void deleteMachineByIDAndBaseID(String name, int currentBase)throws DbException;
	/**Gets all machines suitable for a operaton from a base
	 * @param baseID
	 * @param operationID
	 * @return
	 * @throws DbException
	 */
	List<Machines> getAllMachinesForOperationFromBase(int baseID,int operationID) throws DbException;
	/**
	 * @param id
	 * @return
	 * @throws DbException
	 */
	Machines getMachineByID(String id) throws DbException;
	/** Determines if the machine can be used in a operation
	 * @param operationID
	 * @param machineType
	 * @return
	 * @throws DbException
	 */
	boolean isMachineSuitableForOperation(int operationID,String machineType) throws DbException;
	/**
	 * @param id
	 * @param type
	 * @param speed
	 * @param workspeed
	 * @throws DbException
	 */
	void updateMachine(Machines machine) throws DbException;
}
