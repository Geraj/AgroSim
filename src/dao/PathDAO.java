package dao;

import gov.nasa.worldwind.geom.Position;

import java.util.List;

import core.Path;
import dao.jdbc.DbException;

public interface PathDAO {
	/**Gets all paths of a base from DB
	 * @param baseID
	 * @return
	 * @throws DbException
	 */
	List<Path> getAllPathofBase(int baseID) throws DbException;
	/**Inserts a path into DB
	 * @param p
	 * @param posAL
	 * @param baseID
	 * @throws DbException
	 */
	void insertPath(Path p, List<Position> posAL,int baseID) throws DbException;
	/**Gets a path by id
	 * @param id
	 * @return
	 * @throws DbException
	 */
	Path getPathByID(int id) throws DbException;

	/**Get the shortest edge between two parcels
	 * @param parcelId1
	 * @param parcelId2
	 * @return
	 * @throws DbException
	 */
	int getPathIDbyConnectedParcelsMinimDistance(int parcelId1,int parcelId2) throws DbException;
	/**
	 * @return
	 * @throws DbException
	 */
	int getMaxIdFromPath() throws DbException;
	/**
	 * @param parcelID
	 * @throws DbException
	 */
	void deletePathWithParcelID(int parcelID)throws DbException;
	/**
	 * @param baseID
	 * @return
	 * @throws DbException
	 */
	int getCountFromPathsWithBase(int baseID)throws DbException;
	/**
	 * @param pathid
	 * @throws DbException
	 */
	void deletePathWithPathID(int pathid) throws DbException;
}
