package dao;

import gov.nasa.worldwind.geom.Position;


import java.util.List;
import core.Parcel;
import dao.jdbc.DbException;

public interface ParcelDAO {
     /**
     * @return
     * @throws DbException
     */
    List<Parcel> getAllParcel() throws DbException;
	 /**
	 * @param p
	 * @param posAL
	 * @param i
	 * @throws DbException
	 */
	void insertParcel(Parcel p, List<Position> posAL, int i) throws DbException;
	/**
	 * @param id
	 * @return
	 * @throws DbException
	 */
	Parcel getParcelByID(int id) throws DbException;
	/**
	 * @return
	 * @throws DbException
	 */
	int getMaxIdFromParcel() throws DbException;
	/**
	 * @param name
	 * @return
	 * @throws DbException
	 */
	int getParcelIdByName(String name) throws DbException;
	/**
	 * @param baseID
	 * @return
	 * @throws DbException
	 */
	List<Parcel> getAllParcelFromBase(int baseID)throws DbException;
	/**
	 * @param baseID
	 * @return
	 * @throws DbException
	 */
	int getCountFromparcelsWithBase(int baseID)throws DbException;
	/**
	 * @param name
	 * @throws DbException
	 */
	void deleteParcelByName(String name)throws DbException;
	/**
	 * @param plantID
	 * @param parcelID
	 * @throws DbException
	 */
	void updateParcelPlant(int plantID,int parcelID)throws DbException;

}
