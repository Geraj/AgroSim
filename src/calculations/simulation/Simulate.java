package calculations.simulation;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import calculations.ModelAndGraphBuilder;
import calculations.simulation.event.RabbitEventProcessor;
import control.observer.EventDispatcher;
import control.statemachine.EquipmentStatController;
import core.Machines;
import core.Operations;
import core.Parcel;
import dao.DAOFactory;
import dao.MachinesDAO;
import dao.jdbc.DbException;
import main.MainFrame;

/**
 * Starts a simulation
 * 
 * @author Gergely Meszaros
 *
 */
public class Simulate implements Runnable {
  private int baseID;

  private MainFrame frame;

  // public static int simulations=0;
  private ArrayList<Operations> operationsAL;

  private ArrayList<Machines> machinesAL;

  private ArrayList<Parcel> parcelAL;

  private static Logger logger = Logger.getLogger(Simulate.class);

  /**
   * Singleton
   */
  private static Simulate simulateInstance;

  /**
   * Private Constructor
   * 
   * @param operationsAL - operations to simulate
   * @param machinesAL - machines to use in simulation
   * @param parcelAL - parcels to consider for operations
   * @param baseID - id of the current base
   */
  private Simulate(ArrayList<Operations> operationsAL, ArrayList<Machines> machinesAL,
      ArrayList<Parcel> parcelAL, int baseID) {
    this.operationsAL = operationsAL;
    this.machinesAL = machinesAL;
    this.parcelAL = parcelAL;
    this.baseID = baseID;
  }

  /**
   * 
   * TODO DESCRIPTION
   * 
   * @param operationsAL
   * @param machinesAL
   * @param parcelAL
   * @param baseID
   * @return
   */
  public static synchronized Simulate getSimulationInstance(ArrayList<Operations> operationsAL,
      ArrayList<Machines> machinesAL, ArrayList<Parcel> parcelAL, int baseID) {
    if (simulateInstance == null)
      simulateInstance = new Simulate(operationsAL, machinesAL, parcelAL, baseID);
    return simulateInstance;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }

  public static boolean isSimulationRunning() {
    if (simulateInstance != null) {
      return true;
    }
    return false;
  }

  /**
   * Starts the simulation.Creates the graph, the timer and the machine's.
   */
  public void run() {
    Iterator<Operations> iter = operationsAL.iterator();
    // create new master.txt
    try {
      deleteDir("log");
      File file = new File("log/" + "Master" + ".txt");
      FileWriter fstream = new FileWriter(file);
      BufferedWriter out = new BufferedWriter(fstream);
      out.close();
    } catch (Exception e) {// Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
    while (iter.hasNext()) {
      // -----
      Operations currentOperation = iter.next();
      ModelAndGraphBuilder graph = null;
      try {
        // building the graph for current operation.
        graph = new ModelAndGraphBuilder(baseID, currentOperation.getId(), parcelAL);
      } catch (Exception e1) {
        e1.printStackTrace();
      }

      int n = 0;
      DAOFactory dfFactory = DAOFactory.getInstance();
      MachinesDAO machinesDAO = dfFactory.getMachinesDAO();
      ArrayList<Machines> machinesToUseForOperation = new ArrayList<Machines>();
      try {
        Iterator<Machines> iterate = machinesAL.iterator();
        while (iterate.hasNext()) {
          // Selecting suitable machines for current operation.
          Machines m = iterate.next();
          if (machinesDAO.isMachineSuitableForOperation(currentOperation.getId(), m.getType())) {
            machinesToUseForOperation.add(m);
          }
        }

        graph.checkForUnreacheable();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, e.getMessage());
        e.printStackTrace();
      }

      n = machinesToUseForOperation.size();
      if (n > 0) {
        // initiating machine simulation
        MachineSimulate machine[] = new MachineSimulate[n];
        Timer timer = new Timer(n);
        RabbitEventProcessor eventProcessor = new RabbitEventProcessor(graph, timer);
        eventProcessor.registerObserver(EventDispatcher.getInstance());
        try {
          JOptionPane.showMessageDialog(frame, "Simulation started "
              + dfFactory.getBaseDAO().getBaseByID(baseID).getName() + "for "
              + currentOperation.getName() + ", please wait");
        } catch (HeadlessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (DbException e) {
          // TODO Auto-generated catch block
           e.printStackTrace();
        }
        // simulations++;
        for (int i = 0; i < n; i++) {
          machine[i] = new MachineSimulate(graph, machinesToUseForOperation.get(i), timer, i);
          //machines[i] = new Thread(machine[i]);
          EquipmentStatController ic = new EquipmentStatController(machine[i]);
          
        }
//        for (int i = 0; i < n; i++) {
//          machines[i].start();
//        }
        for (int i = 0; i < n; i++)
          while (!machine[i].isFinished()) {
        	  try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // waiting for threads to finish
          }
        eventProcessor.closeConnection();
        double area = 0;
        int distance = 0;
        for (int i = 0; i < n; i++) {
          area += machine[i].areaWorked;
          distance += machine[i].distancetraveld;
        }
        System.out.println(area + " " + distance);
        
        int a = (int) (area * 100);
        JOptionPane.showMessageDialog(frame, "Simulation for " + currentOperation.getName()
            + " ready.\nTotal area worked(ha):" + a / 100.0 + "\nTotal distance traveld(m):"
            + distance + "\n(logs have been generated)");
        eventProcessor.unRegisterObserver(EventDispatcher.getInstance());
      } else {
        logger.info(
            "[F1.7] Simulation wont produce any data as no suitable vehicles were selected ");
        JOptionPane.showMessageDialog(frame, "Error in simulation(No suitable machine!)");
      }
    }
    logger.info("[F1.7] Simulation completed ");
    simulateInstance = null;
  }

  private static void deleteDir(String dir) {
    File directory = new File(dir);
    if (directory.isDirectory()) {
      String[] content = directory.list();
      for (int i = 0; i < content.length; i++) {
        File f = new File(directory.getName() + "/" + content[i]);
        f.delete();
      }
    }
  }
}
