package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class JDBCHelper {
	/**
	 * Connect to DB
	 * 
	 * @return
	 * @throws DbException
	 */
	public static Connection connect() throws DbException {
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
		} catch (SQLException e) {
			/* logger.error(e.getMessage(), e); */ Logger.getLogger(JDBCHelper.class).error(e.getMessage());
			throw new DbException("Could not connect to DB turn server on!!");
		}
		return connection;
	}
}
