package dao;

import java.util.List;

import core.Plants;
import dao.jdbc.DbException;

/**
 * @author Gergely Meszaros
 *
 */
public interface PlantsDAO {
	
	/**Get a plant by its id
	 * @param id
	 * @return
	 * @throws DbException
	 */
	Plants getPlantByID(int id)throws DbException;
	/**Get all plants
	 * @return list of plants
	 * @throws DbException
	 */
	List<Plants> getAllPlants() throws DbException;
	/**Get plants id by type
	 * @param name
	 * @return
	 * @throws DbException
	 */
	int getPlantsIDbyType(String name) throws DbException;
	/**Insert a plant
	 * @param plant
	 * @throws DbException
	 */
	void insertPlant(Plants plant)throws DbException;
	/**Delete a plant
	 * @param type
	 * @throws DbException
	 */
	void deletePlantByType(String type)throws DbException;
	/**Update plant data
	 * @param oldType
	 * @param type
	 * @param seed
	 * @param income
	 * @throws DbException
	 */
	void updatePlant(String oldType,Plants plant) throws DbException;
}
