package dao.jdbc;

import gov.nasa.worldwind.geom.Position;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import core.Path;
import dao.DAOFactory;
import dao.PathDAO;
import dao.PathPointsDAO;

public class PathJdbcDAO implements PathDAO{
	static Logger logger = Logger.getLogger(PathJdbcDAO.class);
	public Connection Connect() throws DbException{
		Connection connection=null;
		try {
			// Create a connection to the database
			String serverName = "localhost";
			String mydatabase = "allamvizsga";
			String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a
			// JDBC
			String username = "root";
			String password = "";
			connection= DriverManager.getConnection(url,username, password);
			
		} catch (Exception e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Could not connect to DB!");
		}
		return connection;
	}
	@Override
	public ArrayList<Path> getAllPathofBase(int baseID) throws DbException {
		Connection connection=Connect();
		ArrayList<Path> pathAll=new ArrayList<Path>();
		try{
		 	String sql = "SELECT ID,parcelFrom,parcelTO,Length FROM path,basepath where path.ID=basepath.PathID and basepath.BaseID="+baseID;
	        PreparedStatement prest = connection.prepareStatement(sql);
	        ResultSet rs = prest.executeQuery();
	        while (rs.next()){
	        	Path pathfromDB=new Path(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getDouble(4));
	        	pathAll.add(pathfromDB);
	        }
	        prest.close();
	      }
	      catch (SQLException e){
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
	      }
	      finally  {
	    	  try{
	    	  connection.close();
	    	  }
	    	  catch (SQLException e){
	  			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
	    	  }
	      }
		return pathAll;
	}

	@Override
	public int getMaxIdFromPath() throws DbException {
		Connection connection=Connect();
		int id=0;
		try{
			String sql="Select max(ID) from path";
			PreparedStatement prest = connection.prepareStatement(sql);
	        ResultSet rs = prest.executeQuery();
	        while (rs.next()){
	        	id=Integer.parseInt(rs.getString(1));
	        }
		}
		catch (SQLException e){
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		}
		catch (NumberFormatException e){
			id=0;
			return id;
		}
		finally{
				 try {
					connection.close();
				} catch (SQLException e) {
					/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
					throw new DbException("Data acces error!");
				}
		}	
		return id;
	}

	@Override
	public Path getPathByID(int id) throws DbException {
		Path path=new Path();
		Connection connection=Connect();
		try{
		 	String sql = "SELECT * FROM path where ID="+id;
	        PreparedStatement prest = connection.prepareStatement(sql);
	        ResultSet rs = prest.executeQuery();
	        while (rs.next()){
	        	path=new Path(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getDouble(4));
	        }
	        prest.close();
	      }
	      catch (SQLException e){
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
	      }
	      finally  {
	    	  try{
	    	  connection.close();
	    	  }
	    	  catch (SQLException e){
	  			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
	    	  }
	      }
		return path;
	}

	@Override
	public void insertPath(Path p, List<Position> posAL,int baseID) throws DbException {
		Connection connection=Connect();
		try{
			int pathID=getMaxIdFromPath()+1;
	        Statement st = connection.createStatement();
	        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO path (ID,parcelFrom,parcelTO,Length) VALUES (?,?,?,?)");
	        pstmt.setInt(1,pathID);
	        pstmt.setInt(2,p.getFrom());
	        pstmt.setInt(3,p.getTo());
	        pstmt.setDouble(4,p.getLength());
	        pstmt.executeUpdate();
	        pstmt = connection.prepareStatement("INSERT INTO basepath (BaseID,PathID) VALUES (?,?)");
	        pstmt.setInt(1,baseID);
	        pstmt.setInt(2,pathID);
	        pstmt.executeUpdate();
	        DAOFactory daoF=DAOFactory.getInstance();
   		 	PathPointsDAO pathpointsDao=daoF.getPathPointsDAO();
   		 pathpointsDao.insertPathPointsFromPositionArray(pathID,posAL);
	        
	        //System.out.println("1 row affected");
	      }
	      catch (SQLException e){
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
	      }
	      finally  {
	    	  try{
	    	  connection.close();
	    	  }
	    	  catch (SQLException e){
	  			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
	    	  }
	      }
		
	}
	
	
	
	
	@Override
	public void deletePathWithParcelID(int parcelID) throws DbException {
		Connection connection = Connect();
		try {
			String sql = "Select ID FROM path where parcelFrom="+parcelID+" or parcelTO="+parcelID;
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				int pathID=rs.getInt(1);
				sql="delete from pathpoints where PathID="+pathID;
				prest=connection.prepareStatement(sql);
				prest.executeUpdate();
				sql="delete from basepath where PathID="+pathID;
				prest=connection.prepareStatement(sql);
				prest.executeUpdate();
				sql="delete from path where ID="+pathID;
				prest=connection.prepareStatement(sql);
				prest.executeUpdate();
				
			}
			prest.close();
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
			}
		}
	}
	@Override
	public int getCountFromPathsWithBase(int baseID)throws DbException {
		Connection connection=Connect();
		int id=0;
		try{
			String sql="Select count(*) from path,basepath where path.ID=basepath.PathID and basepath.BaseID="+baseID;
			PreparedStatement prest = connection.prepareStatement(sql);
	        ResultSet rs = prest.executeQuery();
	        while (rs.next()){
	        	id=Integer.parseInt(rs.getString(1));
	        }
		}
		catch (SQLException e){
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		}
		catch (NumberFormatException e){
			id=0;
			return id;
		}
		finally{
				 try {
					connection.close();
				} catch (SQLException e) {
					/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
					throw new DbException("Data acces error!");
				}
		}	
		return id;
	}
	@Override
	public void deletePathWithPathID(int pathid) throws DbException {
		Connection connection = Connect();
		try {
				String sql="";
				
				sql="delete from pathpoints where PathID="+pathid;
				PreparedStatement prest=connection.prepareStatement(sql);
				prest.executeUpdate();
				sql="delete from basepath where PathID="+pathid;
				prest=connection.prepareStatement(sql);
				prest.executeUpdate();
				sql="delete from path where ID="+pathid;
				prest=connection.prepareStatement(sql);
				prest.executeUpdate();
				prest.close();
			}
		catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
			}
		}
		
	}
	@Override
	public int getPathIDbyConnectedParcelsMinimDistance(int parcelId1,
			int parcelId2) throws DbException {
		Connection connection = Connect();
		int id =-1;
		try {
			String sql = "SELECT ID FROM path WHERE parcelFrom="+parcelId1+" and parcelTO="+parcelId2+" order by Length limit 1";
			PreparedStatement prest = connection.prepareStatement(sql);
			ResultSet rs = prest.executeQuery();
			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
			throw new DbException("Data acces error!");
		} catch (NumberFormatException e) {
			id = 0;
			return id;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				/*logger.error(e.getMessage(), e);*/ logger.error(e.getMessage());
				throw new DbException("Data acces error!");
			}
		}
		return id;
	}
		
	

}
