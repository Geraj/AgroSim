package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import core.Operations;

import dao.OperationsDAO;

public class OperationsJdbcDAO implements OperationsDAO {
	static Logger logger = Logger.getLogger(OperationsJdbcDAO.class);


	@Override
	public void deleteOperationByName(String name) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			int id = getOperationIDbyName(name);
			String sql = "Delete FROM plantoperation where OperationID=" + id;
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
			sql = "Delete FROM machinetypeoperation where OperationID=" + id;
			prest = connection.prepareStatement(sql);
			prest.executeUpdate();
			sql = "Delete FROM operations where Name=" + "'" + name + "'";
			prest = connection.prepareStatement(sql);
			prest.executeUpdate();
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
	}

	public int getOperationIDbyName(String name) throws DbException {
		int id = 0;
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT ID FROM operations where Name=?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
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
		return id;
	}

	@Override
	public ArrayList<Operations> getAllOperations() throws DbException {
		Connection connection = JDBCHelper.connect();
		ArrayList<Operations> operationAL = new ArrayList<Operations>();
		try {
			String sql = "SELECT * from operations";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Operations operationfromdb = new Operations(rs.getInt(1), rs
						.getString(2), rs.getInt(3), rs.getDouble(4));
				operationAL.add(operationfromdb);
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
		return operationAL;
	}

	@Override
	public void insertOperation(Operations operation) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO operations (Name,Price,Time) VALUES (?,?,?)");
			pstmt.setString(1, operation.getName());
			pstmt.setInt(2, operation.getPrice());
			pstmt.setDouble(3, operation.getTime());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("SQL error on close!");
			}
		}

	}

	@Override
	public void updateOperationWithOldname(String oldName, Operations operation)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			String sql = "Update operations set Name='" + operation.getName()
					+ "', Price=" + operation.getPrice() + ", Time="
					+ operation.getTime() + " where Name=" + "'" + oldName
					+ "'";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
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
	}

	@Override
	public void insertPlantOperation(int operationID, int plantID)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO plantoperation (PlantID,OperationID) VALUES (?,?)");
			pstmt.setInt(1, plantID);
			pstmt.setInt(2, operationID);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("SQL error on close!");
			}
		}

	}

	@Override
	public ArrayList<Operations> getAllOperationsForPlant(int plantID)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		ArrayList<Operations> operationAL = new ArrayList<Operations>();
		try {
			String sql = "SELECT operations.id,operations.Name,operations.Price,operations.Time FROM operations, plantoperation WHERE plantoperation.OperationID = operations.ID AND plantoperation.PlantID ="
					+ plantID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Operations operationfromdb = new Operations(rs.getInt(1), rs
						.getString(2), rs.getInt(3), rs.getDouble(4));
				operationAL.add(operationfromdb);
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
		return operationAL;
	}

	public void deleteOperationForPlant(int operationID, int plantID)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			String sql = "Delete FROM plantoperation where OperationID="
					+ operationID + " and PlantID=" + plantID;
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
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
	}

	@Override
	public ArrayList<Operations> getAllOperationsForMachine(String machineType)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		ArrayList<Operations> operationAL = new ArrayList<Operations>();
		try {
			String sql = "SELECT operations.id,operations.Name,operations.Price,operations.Time FROM operations, machinetypeoperation WHERE machinetypeoperation.OperationID = operations.ID AND machinetypeoperation.MachineType ='"
					+ machineType + "'";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Operations operationfromdb = new Operations(rs.getInt(1), rs
						.getString(2), rs.getInt(3), rs.getDouble(4));
				operationAL.add(operationfromdb);
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
		return operationAL;
	}

	@Override
	public void insertMachineOperation(int operationID, String machinetype)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO machinetypeoperation (MachineType,OperationID) VALUES (?,?)");
			pstmt.setString(1, machinetype);
			pstmt.setInt(2, operationID);
			pstmt.executeUpdate();
			pstmt.close();
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

	}

	// Select count(*) FROM `plantoperation` WHERE plantoperation.PlantID=17 and
	// plantoperation.OperationID=3
	public boolean doasePlantNeedThisOperation(int plantid, int operationID)
			throws DbException {
		Connection connection = JDBCHelper.connect();
		boolean result = false;
		try {
			String sql = "Select count(*) FROM `plantoperation` WHERE plantoperation.PlantID="
					+ plantid
					+ " and plantoperation.OperationID="
					+ operationID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				int j = rs.getInt(1);
				if (j > 0)
					result = true;
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
		return result;
	}

	@Override
	public void deleteOperationForMachineType(int operationID,
			String MachineType) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			String sql = "Delete FROM machinetypeoperation where OperationID="
					+ operationID + " and MachineType='" + MachineType + "'";
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
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
	}

	@Override
	public Operations getOperationByID(int operationID) throws DbException {
		Connection connection = JDBCHelper.connect();
		Operations o = new Operations();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT * FROM operations where ID=?");
			pstmt.setInt(1, operationID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				o.setId(rs.getInt(1));
				o.setName(rs.getString(2));
				o.setPrice(rs.getInt(3));
				o.setTime(rs.getDouble(4));
			}
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
		return o;
	}
}
