package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import core.Base;
import core.Machines;
import core.Parcel;
import core.Path;
import dao.BaseDAO;
import dao.DAOFactory;
import dao.MachinesDAO;
import dao.ParcelDAO;
import dao.PathDAO;

public class BaseJdbcDAO implements BaseDAO {
	static Logger logger = Logger.getLogger(BaseJdbcDAO.class);

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.BaseDAO#getBaseByID(int)
	 */
	@Override
	public Base getBaseByID(int id) throws DbException {
		Base base = new Base();
		Connection connection = JDBCHelper.connect();
		try {
			String sql = "SELECT * FROM base where ID=" + id;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				base = new Base(rs.getInt(1), rs.getString(2), rs.getDouble(3),
						rs.getDouble(4));
			}
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data access error!");
			}
		}
		return base;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.BaseDAO#getAllBases()
	 */
	@Override
	public ArrayList<Base> getAllBases() throws DbException {
		Connection connection = JDBCHelper.connect();
		ArrayList<Base> baseAL = new ArrayList<Base>();
		try {
			String sql = "SELECT * from base";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				Base plantfromdb = new Base(rs.getInt(1), rs.getString(2), rs
						.getDouble(3), rs.getDouble(4));
				baseAL.add(plantfromdb);
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
		return baseAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.BaseDAO#insertBase(core.Base)
	 */
	@Override
	public void insertBase(Base base) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO Base (Name,Latitude,Longitude) VALUES (?,?,?)");
			pstmt.setString(1, base.getName());
			pstmt.setDouble(2, base.getLatitude());
			pstmt.setDouble(3, base.getLongitude());
			pstmt.executeUpdate();
			pstmt.close();
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
	 * @see dao.BaseDAO#deleteBaseByName(java.lang.String)
	 */
	@Override
	public void deleteBaseByName(String name) throws DbException {
		Connection connection = JDBCHelper.connect();
		try {
			int basid = getBaseIdByName(name);
			DAOFactory df = DAOFactory.getInstance();
			ParcelDAO parcelDAO = df.getParcelDao();
			PathDAO pathDAO = df.getPathDAO();
			MachinesDAO machinesDAO = df.getMachinesDAO();
			ArrayList<Machines> machinAL = (ArrayList<Machines>) machinesDAO
					.getAllMachines(basid);
			ArrayList<Parcel> parcelAL = (ArrayList<Parcel>) parcelDAO
					.getAllParcelFromBase(basid);
			ArrayList<Path> pathAL = (ArrayList<Path>) pathDAO
					.getAllPathofBase(basid);
			Iterator<Path> itpath = pathAL.iterator();
			while (itpath.hasNext()) {
				Path p = itpath.next();
				pathDAO.deletePathWithPathID(p.getID());
			}
			Iterator<Parcel> itparcel = parcelAL.iterator();
			while (itparcel.hasNext()) {
				Parcel p = itparcel.next();
				parcelDAO.deleteParcelByName(p.getName());
			}
			Iterator<Machines> itmachines = machinAL.iterator();
			while (itmachines.hasNext()) {
				Machines m = itmachines.next();
				machinesDAO.deleteMachineByIDAndBaseID(m.getID(), basid);
			}

			String sql = "Delete FROM base where ID=" + basid;
			PreparedStatement prest = connection.prepareStatement(sql);
			prest.executeUpdate();
			prest.executeUpdate();
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data access error!");
		} catch (Exception e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException(e.getMessage());
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
	 * @see dao.BaseDAO#getBaseIdByName(java.lang.String)
	 */
	public int getBaseIdByName(String name) throws DbException {
		int id = 0;
		Connection connection = JDBCHelper.connect();
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT ID FROM base where Name=?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
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
		return id;
	}

}
