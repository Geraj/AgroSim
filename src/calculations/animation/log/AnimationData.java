package calculations.animation.log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

import calculations.ModelAndGraphBuilder;
import calculations.animation.AnnimationHelper;
import core.Base;
import core.Path;
import core.PathPoints;
import core.Points;
import dao.BaseDAO;
import dao.DAOFactory;
import dao.OperationsDAO;
import dao.PathDAO;
import dao.PathPointsDAO;
import dao.PointsDAO;
import dao.jdbc.DbException;
import gov.nasa.worldwind.geom.Position;

/**
 * Creates data for animation
 * 
 * @author Gergely Meszaros
 * 
 */
public class AnimationData {

  /***/
  private LinkedList<Position> animationPositions = new LinkedList<>(); 
  /**
 * @return the animationPositions
 */

// public int currentCordinate=1;
  /**
   * Id of the current base
   */
  public int baseID;

  /**
   * Id of the current opperation
   */
  public int operationID;

  /**
   * The last time when the current machine was on a parcel
   */
  private int lasttimeonparcel;

  /**
   * How many frames / simulation-minute
   */
  private int precision;

  /**
   * Annimate time on parcel
   */
  private boolean drawonParcel;

  public ModelAndGraphBuilder graph;

  public AnimationData(String filename, int precision, boolean drawonParcel) throws Exception {
    this.precision = precision;
    this.drawonParcel = drawonParcel;
    StringTokenizer st = new StringTokenizer(filename, "_");
    StringTokenizer st2 = new StringTokenizer(st.nextToken(), "/");
    st2.nextToken();
    DAOFactory df = DAOFactory.getInstance();
    BaseDAO baseDAO = df.getBaseDAO();
    lasttimeonparcel = 0;
    baseID = baseDAO.getBaseIdByName(st2.nextToken());
    // System.out.println(baseID);
    st.nextToken();
    st.nextToken();
    st2 = new StringTokenizer(st.nextToken(), ".");
    OperationsDAO operationsDAO = df.getOperationsDAO();
    operationID = operationsDAO.getOperationIDbyName(st2.nextToken());
    // System.out.println(operationID);
    // annimationmatrix=new double[3][time*precision*precision];
    Base base = baseDAO.getBaseByID(baseID);
    graph = new ModelAndGraphBuilder(baseID, operationID);
    animationPositions.add(Position.fromDegrees(base.getLatitude(), base.getLongitude(), 0));    
    fillAnimationmatrix(filename);
  }

  /**
   * Read the log file and create the annimation positions
   * 
   * @param filename
   * @throws Exception
   */
  public void fillAnimationmatrix(String filename) throws Exception {
    // read data from .ani file;
    FileInputStream fstream = new FileInputStream(filename);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    Integer timewhenthere = null;
    Integer previoustimetotravel = null;
    Integer timetotravel = null;
    ArrayList<Integer> pathArrayList = new ArrayList<Integer>();
    String parceltowork = new String();
    boolean travel = true;
    while ((strLine = br.readLine()) != null) {
      StringTokenizer st = new StringTokenizer(strLine);
      String firstToken = st.nextToken();
      if (firstToken.equals("Select")) {
        st.nextToken();
        previoustimetotravel = timetotravel == null ? 1 : timetotravel;
        timewhenthere = Integer.parseInt(st.nextToken());
        timetotravel = Integer.parseInt(st.nextToken());
      }
      if (firstToken.equals("Con")) {
        // get path to travel
        String x = new String();
        while (st.hasMoreTokens()) {
          x = st.nextToken();
          pathArrayList.add(Integer.parseInt(x));
        }
        // x- now contains the parcel to work
        // visit a parcel
        if (((timewhenthere - timetotravel) > lasttimeonparcel) && (!travel)) {
          lasttimeonparcel += previoustimetotravel;
          onParcel(Integer.parseInt(parceltowork), timewhenthere - timetotravel);
          lasttimeonparcel = timewhenthere - timetotravel;
          travel = true;
        }
        // store parceltowork
        travel = false;
        parceltowork = x;
        // after a select and Con create cordinates from data
        createCordinates(pathArrayList, timewhenthere, timetotravel);
        pathArrayList = new ArrayList<Integer>();

      }
    }
  }

  /**
   * Creates positions on parcel
   * 
   * @param pathArrayList
   * @param timewhenleavingparcel
   * @throws Exception
   */
  private void onParcel(int parcelToWork, int timewhenleavingparcel) throws Exception {
    // add parcel stationing to matrix
    Position middle = AnnimationHelper.parcelMiddlePosition(parcelToWork);
    //
    if (drawonParcel) {
      for (int i = lasttimeonparcel; i < timewhenleavingparcel; i++) {
        for (int j = 0; j < precision; j++) {
          animationPositions.add(middle);
          // currentCordinate++;
        }
      }
    }
  }



  /**
   * Creates the cordinates for annimation
   * 
   * @param pathArrayList
   * @param timewhenthere
   * @param timetotravel
   * @throws Exception
   */
  private void createCordinates(ArrayList<Integer> pathArrayList, Integer timewhenthere,
      Integer timetotravel) throws Exception {
    // minim 2 parcels in path
    int count = 2;
    int pathlength = pathArrayList.size();
    DAOFactory daoFactory = DAOFactory.getInstance();
    PathDAO pathDAO = daoFactory.getPathDAO();
    PathPointsDAO pathPointsDAO = daoFactory.getPathPointsDAO();
    LinkedList<PathPoints> pathPointsToUse = new LinkedList<PathPoints>();
    ArrayList<PathPoints> pathPoints = new ArrayList<PathPoints>();
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
    // build the matrix with the pathpoints
    buildMatrix(pathPointsToUse, timewhenthere, timetotravel);
  }

  /**
   * Adds positions to the annimation lists
   * 
   * @param pathPointsToUse
   * @param timewhenthere
   * @param timetotravel
   */
  private void buildMatrix(LinkedList<PathPoints> pathPointsToUse, Integer timewhenthere,
      Integer timetotravel) {
    int size = pathPointsToUse.size();
    if (size < (timetotravel * precision)) {
      PathPoints lastPosition = pathPointsToUse.get(pathPointsToUse.size() - 1);
      pathPointsToUse = AnnimationHelper.expandPositions(pathPointsToUse, (timetotravel * precision) - 1);
      pathPointsToUse.add(lastPosition);
    }
    for (PathPoints point : pathPointsToUse) {
      animationPositions.add(Position.fromDegrees(point.getLatitude(), point.getLongitude(), 0));
      // currentCordinate++;
    }
  }


   
	public LinkedList<Position> getAnimationPositions() {
		return animationPositions;
	}

	/**
	 * @param animationPositions
	 *            the animationPositions to set
	 */
	public void setAnimationPositions(LinkedList<Position> animationPositions) {
		this.animationPositions = animationPositions;
	}	

}
