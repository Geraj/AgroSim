
package dao;

import dao.jdbc.JdbcDAOFactory;


/**
 * Abstract DAO Factory pattern
 * @author Gergely Meszaros
 *
 */
public abstract class DAOFactory {
	
	public static DAOFactory getInstance() {
		return new JdbcDAOFactory();
	}

	public abstract ParcelDAO getParcelDao();

	public abstract PointsDAO getPointsDao();

	public abstract PathPointsDAO getPathPointsDAO();

	public abstract PathDAO getPathDAO();

	public abstract BaseDAO getBaseDAO();

	public abstract PlantsDAO getPlantsDAO();

	public abstract OperationsDAO getOperationsDAO();

	public abstract MachinesDAO getMachinesDAO();
}
