package calculations.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import calculations.ModelAndGraphBuilder;
import core.Path;
import core.PathPoints;
import core.Points;
import dao.DAOFactory;
import dao.PathDAO;
import dao.PathPointsDAO;
import dao.PointsDAO;
import dao.jdbc.DbException;
import gov.nasa.worldwind.geom.Position;

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
	public static LinkedList<PathPoints> createCordinates(ArrayList<Integer> pathArrayList, ModelAndGraphBuilder graph) {
		// minim 2 parcels in path
		int count = 2;
		int pathlength = pathArrayList.size();
		DAOFactory daoFactory = DAOFactory.getInstance();
		PathDAO pathDAO = daoFactory.getPathDAO();
		PathPointsDAO pathPointsDAO = daoFactory.getPathPointsDAO();
		LinkedList<PathPoints> pathPointsToUse = new LinkedList<PathPoints>();
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
					pathPoints = AnnimationHelper
							.reverse((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID));
				} else {
					Path p = pathDAO.getPathByID(pathID);
					// not the path from dijkstra based on length
					if (((int) graph.D[frominD][toinD]) != (int) p.getLength()) {
						// System.out.println("reverse 2");
						pathID = pathDAO.getPathIDbyConnectedParcelsMinimDistance(toParcelID, fromParcelID);
						pathPoints = AnnimationHelper.reverse(AnnimationHelper
								.removeDuplicates((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID)));
					} else {
						// right path
						pathPoints = AnnimationHelper
								.removeDuplicates((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID));
					}
				}
				// add points between two parcel to the points to use
				for (int i = 0; i < pathPoints.size(); i++)
					pathPointsToUse.add(pathPoints.get(i));
				count++;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return pathPointsToUse;
	}

	/**
	 * 
	 */
	private static HashMap<Integer, Position> parcelMiddelPostion = new HashMap<>();

	/**
	 * 
	 * Middle position of parcel
	 * 
	 * @param parcelToWork
	 * @return
	 * @throws DbException
	 */
	public static Position parcelMiddlePosition(int parcelToWork)  {
		if (parcelMiddelPostion.get(parcelToWork) != null) {
			return parcelMiddelPostion.get(parcelToWork);
		}
		Integer fromParcelID = Integer.valueOf(parcelToWork);
		DAOFactory df = DAOFactory.getInstance();
		PointsDAO parcelPointsDAO = df.getPointsDao();
		ArrayList<Points> parcelpoints = new ArrayList<>();
		try {
			parcelpoints = (ArrayList<Points>) parcelPointsDAO.getPointsByParcelID(fromParcelID);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// -1 becouse the first and last parcel point in the db ar the same
		double midlelatitude = 0;
		double midlellongitude = 0;
		// TODO need a beter algorithm to calculat middle position of a polygon
		for (int i = 0; i < parcelpoints.size() - 1; i++) {
			// calculate midle point
			midlelatitude = midlelatitude + parcelpoints.get(i).getLatitude();
			midlellongitude = midlellongitude + parcelpoints.get(i).getLongitude();
		}
		midlelatitude = midlelatitude / (parcelpoints.size() - 1);
		midlellongitude = midlellongitude / (parcelpoints.size() - 1);
		parcelMiddelPostion.put(parcelToWork, Position.fromDegrees(midlelatitude, midlellongitude));
		return Position.fromDegrees(midlelatitude, midlellongitude);
	}
	
	  /**
	   * Creates howmany points on a path from a given list of pathpoins
	   * 
	   * @param originalPositionsAL
	   * @param howmany
	   * @return
	   */
	  public static LinkedList<PathPoints> expandPositions(LinkedList<PathPoints> originalPositionsAL,
	      int howmany) {

	    int sizeoforiginal = originalPositionsAL.size();
	    LinkedList<PathPoints> result = new LinkedList<PathPoints>();
	    // reduce number of points
	    if (howmany < sizeoforiginal) {
	      int needtoremove = originalPositionsAL.size() - howmany;
	      for (int i = 1; i <= needtoremove; i++) {
	        originalPositionsAL.remove(i);
	      }
	      result = originalPositionsAL;
	    } // increase number of points
	    else {
	      int multi = howmany / (sizeoforiginal - 1);// multi-how many points
	      // to insert between
	      // each originalpoint
	      for (int i = 0; i < sizeoforiginal - 1; i++) {
	        for (int j = 0; j < multi; j++) {

	          double jj = j;
	          double multiDouble = multi;
	          PathPoints p = new PathPoints((originalPositionsAL.get(i).getLatitude() + (jj
	              / multiDouble) * (originalPositionsAL.get(i + 1).getLatitude()
	                  - originalPositionsAL.get(i).getLatitude())), (originalPositionsAL.get(
	                      i).getLongitude() + (jj / multiDouble) * (originalPositionsAL.get(i
	                          + 1).getLongitude() - originalPositionsAL.get(i).getLongitude())));
	          result.add(p);

	        }
	      }
	      Random r = new Random();
	      while (result.size() < howmany) {// leftover points
	        int pos = r.nextInt(result.size() - 1);
	        PathPoints p1 = result.get(pos);
	        PathPoints p2 = result.get(pos + 1);
	        PathPoints newPos = new PathPoints((p1.getLatitude() + p2.getLatitude()) / 2,
	            (p1.getLongitude() + p2.getLongitude()) / 2);
	        result.add(pos + 1, newPos);
	      }
	    }
	    return result;
	  }
}
