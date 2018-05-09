package dao.jdbc;


import dao.BaseDAO;
import dao.MachinesDAO;
import dao.OperationsDAO;
import dao.ParcelDAO;
import dao.DAOFactory;
import dao.PathDAO;
import dao.PathPointsDAO;
import dao.PlantsDAO;
import dao.PointsDAO;
/**
 * JDBC DAO factory class
 * @author Geraj
 *
 */
public class JdbcDAOFactory extends DAOFactory {

	@Override
	public ParcelDAO getParcelDao(){
		return new ParcelJdbcDao();
	}

	@Override
	public PointsDAO getPointsDao() {
		return new PointsJdbcDao();
	}
	@Override
	public PathDAO getPathDAO() {
		return new PathJdbcDAO();
	}

	@Override
	public PathPointsDAO getPathPointsDAO() {
		return  new PathPointsJdbcDAO();
	}

	@Override
	public BaseDAO getBaseDAO() {
		return new BaseJdbcDAO();
	}

	@Override
	public PlantsDAO getPlantsDAO() {
		return new PlantsJdbcDAO();
	}

	@Override
	public OperationsDAO getOperationsDAO() {
		return new OperationsJdbcDAO();
	}

	@Override
	public MachinesDAO getMachinesDAO() {
		return new MachinesJdbcDAO();
	}

}
