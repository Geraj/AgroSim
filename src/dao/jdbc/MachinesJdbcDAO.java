package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import core.Machines;
import dao.MachinesDAO;

public class MachinesJdbcDAO implements MachinesDAO {
	static Logger logger = Logger.getLogger(MachinesJdbcDAO.class);


	/**
	 * Gets machines type by id.
	 * 
	 * @param id
	 * @return
	 * @throws DbException
	 */
	@SuppressWarnings("unused")
	private String getMachinesTypeByID(String id) throws DbException {
		String typeid = "";
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT Type FROM machines where ID=?");
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				typeid = rs.getString(1);
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error getOperationIDbyName!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
			}
		}
		return typeid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#deleteMachineByID(java.lang.String)
	 */
	@Override
	public void deleteMachineByID(String id) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			// String type = getMachinesTypeByID(id);
			PreparedStatement prest = connection
					.prepareStatement("Delete FROM machines where ID=?");
			prest.setString(1, id);
			prest.executeUpdate();
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error deleteMachineByID!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#getAllMachines(int)
	 */
	@Override
	public ArrayList<Machines> getAllMachines(int baseID) throws DbException {
		Connection connection = JDBCHelper.connect();
		ArrayList<Machines> machinesAL = new ArrayList<Machines>();
		try {
			String sql = "SELECT machines.ID,machines.Type,machines.Speed,machines.Workspeed from machines,basemachines where machines.ID=basemachines.MachineID and basemachines.BaseID="
					+ baseID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Machines machinesfromdb = new Machines(rs.getString(1), rs
						.getString(2), rs.getInt(3), rs.getDouble(4));
				machinesAL.add(machinesfromdb);
			}
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
			}
		}
		return machinesAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#insertMachine(core.Machines, int)
	 */
	@Override
	public void insertMachine(Machines machine, int baseID) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO machines (ID,Type,Speed,WorkSpeed) VALUES (?,?,?,?)");
			pstmt.setString(1, machine.getID());
			pstmt.setString(2, machine.getType());
			pstmt.setInt(3, machine.getSpeed());
			pstmt.setDouble(4, machine.getWorkspeed());
			pstmt.executeUpdate();
			pstmt = connection
					.prepareStatement("INSERT INTO basemachines (MachineID,BaseID) VALUES (?,?)");
			pstmt.setString(1, machine.getID());
			pstmt.setInt(2, baseID);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Error inserting machines!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#deleteMachineByIDAndBaseID(java.lang.String, int)
	 */
	@Override
	public void deleteMachineByIDAndBaseID(String name, int currentBase)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			// String type = getMachinesTypeByID(name);
			PreparedStatement prest = connection
					.prepareStatement("Delete FROM basemachines where MachineID=? and BaseID=?");
			prest.setString(1, name);
			prest.setInt(2, currentBase);
			prest.executeUpdate();
			prest = connection
					.prepareStatement("Delete FROM machines where ID=?");
			prest.setString(1, name);
			prest.executeUpdate();
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#getAllMachinesForOperationFromBase(int, int)
	 */
	public ArrayList<Machines> getAllMachinesForOperationFromBase(int baseID,
			int operationID) throws DbException {
		Connection connection = JDBCHelper.connect();
		ArrayList<Machines> machinesAL = new ArrayList<Machines>();
		try {
			String sql = "SELECT machines.ID, machines.Type, machines.speed, machines.workspeed FROM machines, operations, machinetypeoperation, basemachines WHERE machines.type = machinetypeoperation.MachineType AND operations.ID = machinetypeoperation.OperationID AND basemachines.MachineID = machines.ID AND operations.ID ="
					+ operationID + " AND basemachines.BaseID =" + baseID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Machines machinesfromdb = new Machines(rs.getString(1), rs
						.getString(2), rs.getInt(3), rs.getDouble(4));
				machinesAL.add(machinesfromdb);
			}
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}
		return machinesAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#getMachineByID(java.lang.String)
	 */
	@Override
	public Machines getMachineByID(String id) throws DbException {
		Connection connection = JDBCHelper.connect();
		Machines machinesfromdb = new Machines();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT * FROM machines where ID=?");
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				machinesfromdb = new Machines(rs.getString(1), rs.getString(2),
						rs.getInt(3), rs.getDouble(4));
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}
		return machinesfromdb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#isMachineSuitableForOperation(int, java.lang.String)
	 */
	@Override
	public boolean isMachineSuitableForOperation(int operationID,
			String machineType) throws DbException {
		Connection connection = JDBCHelper.connect();
		int t = 0;
		try {
			String sql = "Select count(*) from machinetypeoperation where OperationID="
					+ operationID + " and MachineType='" + machineType + "'";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				t = rs.getInt(1);
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}
		if (t == 0)
			return false;
		else
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MachinesDAO#updateMachine(core.Machines)
	 */
	@Override
	public void updateMachine(Machines m) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			String sql = "Update machines set Type='" + m.getType()
					+ "', speed=" + m.getSpeed() + ", workspeed="
					+ m.getWorkspeed() + " where ID=" + "'" + m.getID() + "'";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}
	}

}
