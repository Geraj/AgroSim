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

/**
 * Creates data for animation
 * 
 * @author Gergely Meszaros
 * 
 */
public class AnimationData {
  // public double[][] annimationmatrix;
  /**
   * Linked list containing latitudes
   */
  public LinkedList<Double> annimationLatitudeList;

  /**
   * Linked list containing longitudes
   */
  public LinkedList<Double> annimationLongitudeList;

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
    annimationLatitudeList = new LinkedList<Double>();
    annimationLongitudeList = new LinkedList<Double>();
    annimationLatitudeList.add(base.getLatitude());
    annimationLongitudeList.add(base.getLongitude());
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
    Integer fromParcelID = parcelToWork;
    DAOFactory df = DAOFactory.getInstance();
    PointsDAO parcelPointsDAO = df.getPointsDao();
    ArrayList<Points> parcelpoints = (ArrayList<Points>) parcelPointsDAO.getPointsByParcelID(
        fromParcelID);
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
    //
    if (drawonParcel) {
      for (int i = lasttimeonparcel; i < timewhenleavingparcel; i++) {
        for (int j = 0; j < precision; j++) {
          annimationLatitudeList.add(midlelatitude);
          annimationLongitudeList.add(midlellongitude);
          // currentCordinate++;
        }
      }
    } else {
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
    ArrayList<PathPoints> pathPointsToUse = new ArrayList<PathPoints>();
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
        pathPoints = reverse((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID));
      } else {
        Path p = pathDAO.getPathByID(pathID);
        // not the path from dijkstra based on length
        if (((int) graph.D[frominD][toinD]) != (int) p.getLength()) {
          // System.out.println("reverse 2");
          pathID = pathDAO.getPathIDbyConnectedParcelsMinimDistance(toParcelID, fromParcelID);
          pathPoints = reverse(removeDuplicates(
              (ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(pathID)));
        } else {
          // right path
          pathPoints = removeDuplicates((ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(
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
  private void buildMatrix(ArrayList<PathPoints> pathPointsToUse, Integer timewhenthere,
      Integer timetotravel) {
    int size = pathPointsToUse.size();
    if (size < (timetotravel * precision)) {
      PathPoints lastPosition = pathPointsToUse.get(pathPointsToUse.size() - 1);
      pathPointsToUse = expandPositions(pathPointsToUse, (timetotravel * precision) - 1);
      pathPointsToUse.add(lastPosition);
    }
    for (int i = 0; i < pathPointsToUse.size(); i++) {
      annimationLatitudeList.add(pathPointsToUse.get(i).getLatitude());
      annimationLongitudeList.add(pathPointsToUse.get(i).getLongitude());
      // currentCordinate++;
    }
  }

  /**
   * Creates howmany points on a path from a given list of pathpoins
   * 
   * @param originalPositionsAL
   * @param howmany
   * @return
   */
  public ArrayList<PathPoints> expandPositions(ArrayList<PathPoints> originalPositionsAL,
      int howmany) {

    int sizeoforiginal = originalPositionsAL.size();
    ArrayList<PathPoints> result = new ArrayList<PathPoints>();
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

  /**
   * Travel the path backwards
   * 
   * @param pathPointsByPathID
   * @return
   */
  private ArrayList<PathPoints> reverse(ArrayList<PathPoints> pathPointsByPathID) {
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
  private ArrayList<PathPoints> removeDuplicates(ArrayList<PathPoints> pathPoints) {
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

}
