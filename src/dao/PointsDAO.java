package dao;

import gov.nasa.worldwind.geom.Position;

import java.util.List;

import core.Points;
import dao.jdbc.DbException;

/**
 * @author Gergely Meszaros
 *
 */
public interface PointsDAO {
    /**Get a parcels points
     * @param parcelID
     * @return
     * @throws DbException
     */
    List<Points> getPointsByParcelID(int parcelID) throws DbException;
	/**Insert points into DB
	 * @param parcelid
	 * @param posAL
	 * @throws DbException
	 */
	void insertPointsFromPositionArray(int parcelid, List<Position> posAL) throws DbException;
	/**Get a positions parcelID
	 * @param position
	 * @return
	 * @throws DbException
	 */
	int getPositionsParcelID(Position position) throws DbException;
	/**Delete points belonging to a parcell
	 * @param id
	 * @throws DbException
	 */
	public void deletePiontsWithParcelID(int id) throws DbException;
}
