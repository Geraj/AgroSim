package dao;

import gov.nasa.worldwind.geom.Position;
import java.util.List;
import java.util.Set;

import core.PathPoints;
import dao.jdbc.DbException;

/**
 * @author Gergely Meszaros
 *
 */
public interface PathPointsDAO {
	/**Get path points by path id
	 * @param pathID
	 * @return
	 * @throws DbException
	 */
	List<PathPoints> getPathPointsByPathID(int pathID) throws DbException;
	/** insert pathPoints
	 * @param pathid
	 * @param posAL
	 * @throws DbException
	 */
	void insertPathPointsFromPositionArray(int pathid, List<Position> posAL) throws DbException;
	/** get a positions pathId
	 * @param position
	 * @return
	 * @throws DbException
	 */
	Set<Integer> getPositionsPathID(Position position) throws DbException;
}
