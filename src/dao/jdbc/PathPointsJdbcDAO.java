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
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import core.PathPoints;
import dao.PathPointsDAO;

public class PathPointsJdbcDAO implements PathPointsDAO {
	static Logger logger = Logger.getLogger(PathPointsJdbcDAO.class);

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
			// System.out.println("connected to dbf");

		} catch (Exception e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Could not connect to DB!");
		}
		return connection;
	}

	@Override
	public void insertPathPointsFromPositionArray(int pathid,
			List<Position> posAL) throws DbException {

		Connection connection = Connect();
		int id = getMaxIdFromPathPoints() + 1;
		try {
			Statement st = connection.createStatement();
			Iterator<Position> iter = posAL.iterator();
			while (iter.hasNext()) {
				Position p = iter.next();
				PathPoints newpoint = PositionToPoint(p);

				PreparedStatement pstmt = connection
						.prepareStatement("INSERT INTO pathpoints (ID,PathID,Latitude,Longitude,Elevation) VALUES (?,?,?,?,?)");
				pstmt.setInt(1, id);
				pstmt.setInt(2, pathid);
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

	private PathPoints PositionToPoint(Position pos) {
		double lat = 0;
		double lon = 0;
		if (pos.latitude.toString().length() > 10) {
			try {
				lat = Double.parseDouble(pos.latitude.toString()
						.substring(0, 9));
			} catch (Exception e) {
				logger.error("position translation error", e);
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
				logger.error("position translation error", e);
			}
		} else {
			lon = Double.parseDouble(pos.longitude.toString().substring(0,
					pos.longitude.toString().length() - 1));
		}
		// double
		// er=Double.parseDouble(pos.elevation.toString().substring(0,8));
		PathPoints point = new PathPoints(0, 0, lat, lon, pos.elevation);
		return point;
	}

	private int getMaxIdFromPathPoints() throws DbException {
		Connection connection = Connect();
		int id = -1;
		try {
			String sql = "Select max(ID) from pathpoints";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} catch (NumberFormatException e) {
			return 0;
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
	public ArrayList<PathPoints> getPathPointsByPathID(int pathID)
			throws DbException {
		ArrayList<PathPoints> pointsAL = new ArrayList<PathPoints>();
		Connection connection = Connect();
		try {
			String sql = "SELECT * FROM pathpoints where PathID=" + pathID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				PathPoints pointsfromdb = new PathPoints(rs.getInt(1), rs
						.getInt(2), rs.getDouble(3), rs.getDouble(4), rs
						.getDouble(5));
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

	@Override
	public Set<Integer> getPositionsPathID(Position position)
			throws DbException {
		Connection connection = Connect();
		Set<Integer> AL = new TreeSet<Integer>();
		int id = -1;
		try {

			PathPoints point = PositionToPoint(position);
			String sql = "Select PathID from pathpoints where Latitude="
					+ point.getLatitude() + " and Longitude="
					+ point.getLongitude();// +" Limit 1";//+" and Elevation="+point.getElevation();
			// System.out.println(sql);
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
				AL.add(id);
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
		return AL;
	}

}
