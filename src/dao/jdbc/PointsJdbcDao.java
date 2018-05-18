package dao.jdbc;

import gov.nasa.worldwind.geom.Position;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import core.Points;
import dao.PointsDAO;

public class PointsJdbcDao implements PointsDAO {
	static Logger logger = Logger.getLogger(PointsJdbcDao.class);

	@Override
	public ArrayList<Points> getPointsByParcelID(int parcelID)
			throws DbException {
		ArrayList<Points> pointsAL = new ArrayList<Points>();
		Connection connection = JDBCHelper.connect();;
		try {
			String sql = "SELECT * FROM points where ParcelID=" + parcelID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Points pointsfromdb = new Points(rs.getInt(1), rs.getInt(2), rs
						.getDouble(3), rs.getDouble(4), rs.getDouble(5));
				pointsAL.add(pointsfromdb);
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
		return pointsAL;
	}

	public Points PositionToPoint(Position pos) {
		// System.out.println(pos.latitude.toString().substring(0, 10));
		double lat = 0;
		double lon = 0;
		if (pos.latitude.toString().length() > 10) {
			try {
				lat = Double.parseDouble(pos.latitude.toString()
						.substring(0, 9));
			} catch (Exception e) {
				logger.error("error in position translation");
			}
		} else {
			lat = Double.parseDouble(pos.latitude.toString().substring(0,
					pos.latitude.toString().length() - 1));
		}
		if (pos.latitude.toString().length() > 10) {

			try {
				lon = Double.parseDouble(pos.longitude.toString().substring(0,
						9));
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
				logger.error("error in position translation");
			}
		} else {
			lon = Double.parseDouble(pos.longitude.toString().substring(0,
					pos.longitude.toString().length() - 1));
		}
		// double
		// er=Double.parseDouble(pos.elevation.toString().substring(0,8));
		Points point = new Points(0, 0, lat, lon, pos.elevation);
		return point;
	}

	public int getMaxIdFromPoints() throws DbException {
		Connection connection = JDBCHelper.connect();;
		int id = -1;
		try {
			String sql = "Select max(ID) from points";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException s) {
			System.out.println("getMaxIdFromPoints error");
			throw new DbException("Sql error");
		} catch (NumberFormatException e) {
			return 0;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new DbException("Data acces error!");
			}
		}
		return id;
	}

	@Override
	public int getPositionsParcelID(Position position) throws DbException {
		Connection connection = JDBCHelper.connect();;
		int id = -1;
		try {

			Points point = PositionToPoint(position);
			String sql = "Select ParcelID from points where Latitude="
					+ point.getLatitude() + " and Longitude="
					+ point.getLongitude() + " Limit 1";// +" and Elevation="+point.getElevation();
			// System.out.println(sql);
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
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
	public void insertPointsFromPositionArray(int parcelid, List<Position> posAL)
			throws DbException {
		Connection connection = JDBCHelper.connect();;
		int id = getMaxIdFromPoints() + 1;
		try {
			Statement st = connection.createStatement();
			Iterator<Position> iter = posAL.iterator();
			while (iter.hasNext()) {
				Position p = iter.next();
				Points newpoint = PositionToPoint(p);

				PreparedStatement pstmt = connection
						.prepareStatement("INSERT INTO points (ID,ParcelID,Latitude,Longitude,Elevation) VALUES (?,?,?,?,?)");
				pstmt.setInt(1, id);
				pstmt.setInt(2, parcelid);
				pstmt.setDouble(3, newpoint.getLatitude());
				pstmt.setDouble(4, newpoint.getLongitude());
				pstmt.setDouble(5, newpoint.getElevation());
				pstmt.executeUpdate();
				id++;

			}

			// System.out.println("1 row affected");
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

	public void deletePiontsWithParcelID(int id) throws DbException {
		Connection connection = JDBCHelper.connect();;
		try {
			String sql = "Delete from points where ParcelID=" + id;
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
		} catch (Exception s) {
			System.out.println("getMaxIdFromPoints error");
			throw new DbException("Sql error");
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
