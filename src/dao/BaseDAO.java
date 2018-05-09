package dao;

import java.util.List;

import core.Base;
import dao.jdbc.DbException;

/**
 * @author Gergely Meszaros
 *
 */
public interface BaseDAO {
	/**
	 * @param id
	 * @return
	 * @throws DbException
	 */
	Base getBaseByID(int id)throws DbException;
	/**
	 * @return
	 * @throws DbException
	 */
	List<Base> getAllBases() throws DbException;
	/**
	 * @param base
	 * @throws DbException
	 */
	void insertBase(Base base) throws DbException;
	/**
	 * @param name
	 * @throws DbException
	 */
	void deleteBaseByName(String name)throws DbException;
	/**
	 * @param name
	 * @return
	 * @throws DbException
	 */
	int getBaseIdByName(String name) throws DbException;
}
