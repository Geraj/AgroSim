package dao.jdbc;

import gov.nasa.worldwind.geom.Position;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import core.Parcel;
import dao.DAOFactory;
import dao.ParcelDAO;
import dao.PathDAO;
import dao.PointsDAO;

public class ParcelJdbcDao implements ParcelDAO {
	static Logger logger = Logger.getLogger(ParcelJdbcDao.class);

	public Connection Connect() throws DbException {
		Connection connection = null;
		try {
			// Create a connection to the database
			String serverName = "localhost";
			String mydatabase = "allamvizsga";
			String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a
			// JDBC
			String username = "root";
			String password = "";
			connection = DriverManager.getConnection(url, username, password);

		} catch (Exception e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Could not connect to DB!");
		}
		return connection;
	}

	@Override
	public int getMaxIdFromParcel() throws DbException {
		Connection connection = Connect();
		int id = 0;
		try {
			String sql = "Select max(ID) from parcel";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} catch (NumberFormatException e) {
			id = 0;
			return id;
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
	public ArrayList<Parcel> getAllParcel() throws DbException {
		Connection connection = Connect();
		ArrayList<Parcel> parcelAL = new ArrayList<Parcel>();
		try {
			String sql = "SELECT * FROM parcel";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Parcel parcelfromdb = new Parcel(rs.getInt(1), rs.getString(2),
						rs.getDouble(3));
				parcelAL.add(parcelfromdb);
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
		return parcelAL;
	}

	@Override
	public void insertParcel(Parcel p, List<Position> posAL, int baseID)
			throws DbException {
		Connection connection = Connect();
		try {
			int Parcelid = getMaxIdFromParcel() + 1;
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO baseparcel (BaseID,ParcelID) VALUES (?,?)");
			System.out.println(baseID + " " + Parcelid);
			pstmt.setInt(1, baseID);
			pstmt.setInt(2, Parcelid);
			pstmt.executeUpdate();
			pstmt = connection
					.prepareStatement("INSERT INTO parcel (ID,Name,Area) VALUES (?,?,?)");
			pstmt.setInt(1, Parcelid);
			pstmt.setString(2, p.getName());
			pstmt.setDouble(3, p.getArea());
			pstmt.executeUpdate();
			DAOFactory daoF = DAOFactory.getInstance();
			PointsDAO pointsDao = daoF.getPointsDao();
			pointsDao.insertPointsFromPositionArray(Parcelid, posAL);
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
	public Parcel getParcelByID(int id) throws DbException {
		Parcel parcel = new Parcel();
		Connection connection = Connect();
		try {
			String sql = "SELECT * FROM parcel where ID=" + id;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				parcel = new Parcel(rs.getInt(1), rs.getString(2), rs
						.getDouble(3), rs.getInt(4));
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
		return parcel;
	}

	@Override
	public int getParcelIdByName(String name) throws DbException {
		int id = 0;
		Connection connection = Connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT ID FROM parcel where Name=?");
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
	public ArrayList<Parcel> getAllParcelFromBase(int baseID)
			throws DbException {
		Connection connection = Connect();
		ArrayList<Parcel> parcelAL = new ArrayList<Parcel>();
		try {
			String sql = "SELECT ID,Name,Area,PlantID FROM parcel,baseparcel where parcel.ID=baseparcel.ParcelID and baseparcel.BaseID="
					+ baseID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Parcel parcelfromdb = new Parcel(rs.getInt(1), rs.getString(2),
						rs.getDouble(3), rs.getInt(4));
				parcelAL.add(parcelfromdb);
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
		return parcelAL;
	}

	@Override
	public int getCountFromparcelsWithBase(int baseID) throws DbException {
		Connection connection = Connect();
		int id = 0;
		try {
			String sql = "Select count(*) from parcel,baseparcel where parcel.ID=baseparcel.ParcelID and baseparcel.BaseID="
					+ baseID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} catch (NumberFormatException e) {
			id = 0;
			return id;
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
	public void deleteParcelByName(String name) throws DbException {
		Connection connection = Connect();
		try {
			int parcelid = getParcelIdByName(name);
			DAOFactory df = DAOFactory.getInstance();
			PointsDAO pointDao = df.getPointsDao();
			pointDao.deletePiontsWithParcelID(parcelid);
			PathDAO pathDAO = df.getPathDAO();
			pathDAO.deletePathWithParcelID(parcelid);

			String sql = "Delete FROM baseparcel where ParcelID=" + parcelid;
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
			sql = "Delete FROM parcel where ID=" + parcelid;
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

	@Override
	public void updateParcelPlant(int plantID, int parcelID) throws DbException {
		Connection connection = Connect();
		try {
			String sql = "Update parcel set PlantID=" + plantID + " where ID="
					+ parcelID;
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
}