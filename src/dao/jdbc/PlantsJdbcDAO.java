package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import core.Plants;

import dao.PlantsDAO;

public class PlantsJdbcDAO implements PlantsDAO {
	static Logger logger = Logger.getLogger(PlantsJdbcDAO.class);

	@Override
	public Plants getPlantByID(int id) throws DbException {
		Connection connection = JDBCHelper.connect();;
		try {
			String sql = "SELECT * FROM plants where ID=" + id;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			rs.next();
			Plants plant = new Plants(rs.getInt(1), rs.getString(2), rs
					.getInt(3), rs.getInt(4));
			prest.close();
			return plant;
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
	public ArrayList<Plants> getAllPlants() throws DbException {
		Connection connection = JDBCHelper.connect();;
		ArrayList<Plants> plantslAL = new ArrayList<Plants>();
		try {
			String sql = "SELECT * from plants";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Plants plantfromdb = new Plants(rs.getInt(1), rs.getString(2),
						rs.getInt(3), rs.getInt(4));
				plantslAL.add(plantfromdb);
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
		return plantslAL;
	}

	@Override
	public void insertPlant(Plants plant) throws DbException {
		Connection connection = JDBCHelper.connect();;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO plants (Type,Seed,Income) VALUES (?,?,?)");
			pstmt.setString(1, plant.getType());
			pstmt.setInt(2, plant.getSeed());
			pstmt.setInt(3, plant.getIncome());
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

	@Override
	public void deletePlantByType(String type) throws DbException {
		Connection connection = JDBCHelper.connect();;
		try {
			int id = getPlantsIDbyType(type);
			if (id != 0) {
				String sql = "Delete FROM plantoperation where PlantID=" + id;
				PreparedStatement prest = connection.prepareStatement(sql);
				prest.executeUpdate();
				sql = "Update Parcel set PlantID=0 where PlantID=" + id;
				prest = connection.prepareStatement(sql);
				prest.executeUpdate();
				sql = "Delete FROM plants where Type=" + "'" + type + "'";
				prest = connection.prepareStatement(sql);
				prest.executeUpdate();
				prest.close();
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
	}

	@Override
	public void updatePlant(String oldType, Plants plant) throws DbException {

		Connection connection = JDBCHelper.connect();;
		try {
			String sql = "Update plants set Type='" + plant.getType()
					+ "', Seed=" + plant.getSeed() + ", Income="
					+ plant.getIncome() + " where Type=" + "'" + oldType + "'";
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

	public int getPlantsIDbyType(String name) throws DbException {
		int id = 0;
		Connection connection = JDBCHelper.connect();;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT ID FROM plants where Type=?");
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
}
