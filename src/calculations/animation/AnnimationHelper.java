package calculations.animation;

import java.util.ArrayList;

import calculations.ModelAndGraphBuilder;
import core.Path;
import core.PathPoints;
import dao.DAOFactory;
import dao.PathDAO;
import dao.PathPointsDAO;
import dao.jdbc.DbException;

public class AnnimationHelper {
	  /**
	   * Travel the path backwards
	   * 
	   * @param pathPointsByPathID
	   * @return
	   */
	  public static ArrayList<PathPoints> reverse(ArrayList<PathPoints> pathPointsByPathID) {
	    ArrayList<PathPoints> points = new ArrayList<PathPoints>();
	    pathPointsByPathID = removeDuplicates(pathPointsByPathID);
	    for (int i = pathPointsByPathID.size() - 1; i > -1; i--) {
	      points.add(pathPointsByPathID.get(i));
	    }
	    return points;
	  }

	  /**
	   * removes duplicate pathpoints
	   * 
	   * @param pathPoint
	   * @return
	   */
	  public static ArrayList<PathPoints> removeDuplicates(ArrayList<PathPoints> pathPoints) {
	    for (int i = 0; i < pathPoints.size() - 1; i++) {
	      if ((pathPoints.get(i).getLatitude() == pathPoints.get(i + 1).getLatitude())
	          && (pathPoints.get(i).getLongitude() == pathPoints.get(i + 1).getLongitude())) {
	        pathPoints.remove(i);
	      }
	    }
	    ArrayList<PathPoints> points = new ArrayList<PathPoints>();
	    points = pathPoints;
	    return pathPoints;
	  }
	  
		/**
	   * Creates the cordinates for annimation
	   * 
	   * @param pathArrayList
	   * @param timewhenthere
	   * @param timetotravel
	 * @return 
	   * @throws Exception
	   */
	  public static ArrayList<PathPoints> createCordinates(ArrayList<Integer> pathArrayList, ModelAndGraphBuilder graph) {
	    // minim 2 parcels in path
	    int count = 2;
	    int pathlength = pathArrayList.size();
	    DAOFactory daoFactory = DAOFactory.getInstance();
	    PathDAO pathDAO = daoFactory.getPathDAO();
	    PathPointsDAO pathPointsDAO = daoFactory.getPathPointsDAO();
	    ArrayList<PathPoints> pathPointsToUse = new ArrayList<PathPoints>();
	    ArrayList<PathPoints> pathPoints = new ArrayList<PathPoints>();
	    try {
	    while (count <= pathlength) {
	      int fromParcelID = pathArrayList.get(count - 2);
	      int toParcelID = pathArrayList.get(count - 1);
	      // base id is 0 in the DB, -1 in .ani
	      if (fromParcelID == -1)
	        fromParcelID = 0;
	      if (toParcelID == -1)
	        toParcelID = 0;
	      int frominD = 0;
	      int toinD = 0;
	      for (int i = 0; i < graph.parcels.length; i++) {
	        if (fromParcelID == graph.parcels[i].getID()) {
	          frominD = i;
	        }
	        if (toParcelID == graph.parcels[i].getID()) {
	          toinD = i;
	        }
	      }
	      // System.out.println(graph.D[frominD][toinD]);
	      int pathID = pathDAO.getPathIDbyConnectedParcelsMinimDistance(fromParcelID, toParcelID);
	      // reversed path between two parcels
	      if ((pathID == -1)) {
	        // System.out.println("reverse");
	        pathID = pathDAO.getPathIDbyConnectedParcelsMinimDistance(toParcelID, fromParcelID);
	        pathPoints = AnnimationHelper.reverse((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID));
	      } else {
	        Path p = pathDAO.getPathByID(pathID);
	        // not the path from dijkstra based on length
	        if (((int) graph.D[frominD][toinD]) != (int) p.getLength()) {
	          // System.out.println("reverse 2");
	          pathID = pathDAO.getPathIDbyConnectedParcelsMinimDistance(toParcelID, fromParcelID);
	          pathPoints = AnnimationHelper.reverse(AnnimationHelper.removeDuplicates(
	              (ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID)));
	        } else {
	          // right path
	          pathPoints = AnnimationHelper.removeDuplicates((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(
	              pathID));
	        }
	      }
	      // add points between two parcel to the points to use
	      for (int i = 0; i < pathPoints.size(); i++)
	        pathPointsToUse.add(pathPoints.get(i));
	        count++;
	    }
	    }catch (DbException e) {
	    	e.printStackTrace();
	    }
	    return pathPointsToUse;
	  }
}
