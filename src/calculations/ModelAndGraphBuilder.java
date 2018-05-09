package calculations;

import java.util.ArrayList;
import java.util.Iterator;

import core.Base;
import core.Operations;
import core.Parcel;
import core.Path;

import dao.BaseDAO;
import dao.DAOFactory;
import dao.OperationsDAO;
import dao.ParcelDAO;
import dao.PathDAO;

/**
 * Creates a graph based on DB parcels and paths data and uses Dijkstra's algorithm to calculate
 * shortest paths
 * 
 * @author Gergely Meszaros
 *
 */
public class ModelAndGraphBuilder {
  public double neighbourMatrix[][] = null;

  public int n = 0;

  public int P[][] = null;

  public double D[][] = null;

  public Parcel parcels[] = null;

  public int placeonparcel[] = null;

  private int baseID;

  public double[] parcelstatus;

  public int[] workersonparcell;

  public int operationID;

  public Operations operation;

  public Base base;

  /**
   * Creates the infrastructure
   * 
   * @param baseID
   * @param operationID
   * @param parceltoworkAL
   * @throws Exception
   */
  public ModelAndGraphBuilder(int baseID, int operationID, ArrayList<Parcel> parceltoworkAL)
      throws Exception {
    this.baseID = baseID;
    this.operationID = operationID;
    init();
    setparceltowork(parceltoworkAL);
    dijkstra();
  }

  /**
   * Creates the infrastructure
   * 
   * @param baseID
   * @param operationID
   * @throws Exception
   */
  @Deprecated
  public ModelAndGraphBuilder(int baseID, int operationID) throws Exception {
    this.baseID = baseID;
    this.operationID = operationID;
    init();
    dijkstra();
  }

  /**
   * @param parceltowork
   */
  private void setparceltowork(ArrayList<Parcel> parceltowork) {
    for (int i = 0; i < parcels.length; i++) {
      if (parcelstatus[i] != 110) {
        parcelstatus[i] = 111;
      }
    }
    Iterator<Parcel> iter = parceltowork.iterator();
    while (iter.hasNext()) {
      Parcel p = iter.next();
      for (int i = 0; i < parcels.length; i++) {
        if ((parcels[i].getID() == p.getID()) && (parcelstatus[i] != 110)) {
          parcelstatus[i] = 0;
        }
      }
    }
  }

  /**
   * The distance between the base and the parcel is too big or there is no road between them.
   * 
   * @throws Exception
   */
  public void checkForUnreacheable() throws Exception {
    boolean unreachable = false;
    for (int i = 1; i < n; i++) {
      if (D[0][i] >= 100000) {
        parcelstatus[i] = 110;
        unreachable = true;
      }
    }
    if (unreachable) {
      throw new Exception("Warning, there are unreachable parcels");
    }
  }

  /**
   * Load data from DB and create the graph
   * 
   * @throws Exception
   */
  private void init() throws Exception {
    DAOFactory df = DAOFactory.getInstance();
    ParcelDAO parceDAO = df.getParcelDao();
    PathDAO pathDAO = df.getPathDAO();
    ArrayList<Path> pathAL = null;
    ArrayList<Parcel> parcelAL = null;
    try {
      parcelAL = (ArrayList<Parcel>) parceDAO.getAllParcelFromBase(baseID);
      pathAL = (ArrayList<Path>) pathDAO.getAllPathofBase(baseID);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error in graph building");
    }
    n = parcelAL.size();
    n = n + 1;
    neighbourMatrix = new double[n][n];
    parcels = new Parcel[n];
    parcels[0] = new Parcel(-1, "Base", 0, 0);
    // collumn coresponding to parcel;
    for (int i = 1; i < n; i++) {
      Parcel p = parcelAL.get(i - 1);
      parcels[i] = p;
    }
    // neighbore matrix
    int m = pathAL.size();
    for (int i = 0; i < m; i++) {
      Path path = pathAL.get(i);
      int pidfrom = path.getFrom();
      int pidto = path.getTo();
      int a = 0;
      int b = 0;
      for (int j = 0; j < n; j++) {
        if (parcels[j].getID() == pidfrom) {
          a = j;
        }
        if (parcels[j].getID() == pidto) {
          b = j;
        }
      }
      if ((neighbourMatrix[a][b] == 0) || (neighbourMatrix[a][b] > path.getLength())) {
        neighbourMatrix[a][b] = (int) path.getLength();
        neighbourMatrix[b][a] = (int) path.getLength();
      }
    }
    OperationsDAO operationsDAO = df.getOperationsDAO();
    operation = operationsDAO.getOperationByID(operationID);
    placeonparcel = new int[n];
    placeonparcel[0] = Integer.MAX_VALUE;
    for (int i = 1; i < n; i++) {
      placeonparcel[i] = (int) (parcelAL.get(i - 1).getArea() * operation.getTime()) + 1;
      // System.out.println("Place on parcel "+parcels[i].getName()+":"+placeonparcel[i]);
    }

    parcelstatus = new double[n];
    workersonparcell = new int[n];
    parcelstatus[0] = 110;
    BaseDAO baseDAO = df.getBaseDAO();
    base = baseDAO.getBaseByID(baseID);
    for (int i = 1; i < n; i++) {
      if (operationsDAO.doasePlantNeedThisOperation(parcels[i].getPlantID(), operationID)) {
        // empty
      } else {
        parcelstatus[i] = 110;
      }

    }
  }

  /**
   * Implementation of Dijkstra's algorithm
   */
  private void dijkstra() {

    P = new int[n][n];
    D = new double[n][n];
    for (int e = 0; e < n; e++) {
      boolean s[] = new boolean[n];
      double l[] = new double[n];
      int p[] = new int[n];
      p[e] = 0;
      l[e] = 0;
      s[e] = true;
      for (int i = 0; i < n; i++) {
        if (i != e)
          l[i] = 1000000;
      }
      int x = e;
      boolean van = false;
      for (int i = 0; i < n; i++)
        if (s[i] == false)
          van = true;
      while (van) {
        van = false;
        for (int v = 0; v < n; v++)
          if ((neighbourMatrix[x][v] != 0) && (!s[v])) {
            if (l[v] > l[x] + neighbourMatrix[x][v]) {
              l[v] = l[x] + neighbourMatrix[x][v];
              p[v] = x;
            }
          }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < n; i++)
          if ((!s[i]) && (l[i] <= min)) {
            min = l[i];
            x = i;
          }
        s[x] = true;
        for (int i = 0; i < n; i++)
          if (s[i] == false)
            van = true;
      }
      P[e] = p;
      D[e] = l;
    }
  }
}
