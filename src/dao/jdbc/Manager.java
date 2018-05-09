package dao.jdbc;

public class Manager{
public Manager() throws DbException{
	try {
		// Load the JDBC driver
		String driverName = "org.gjt.mm.mysql.Driver"; // MySQL MM JDBC
		Class.forName(driverName);
	} catch (ClassNotFoundException e) {
		// Could not find the database driver
		System.err.println(e);
		throw new DbException("Could not load JDBC driver");
	}
}
}
