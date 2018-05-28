package calculations.simulation;

import java.util.LinkedList;

import control.observer.StateMachineEvents;
import control.observer.StateListener;
import control.observer.Observable;

import calculations.ModelAndGraphBuilder;

/**
 * Thread responsable for timing
 * 
 * @author Gergely Meszaros
 *
 * Not used
 */
@Deprecated
public class TimerTick implements Runnable, Observable {
  private LinkedList<StateListener> listeners = new LinkedList<StateListener>();

  private ModelAndGraphBuilder graphBuilder;

  private Timer timer;

  private int lastCommonTime;

  private static TimerTick instance = null;

  private TimerTick(ModelAndGraphBuilder g, Timer t) {
    this.graphBuilder = g;
    this.timer = t;
    MachineSimulate.time = 0;
  }

  public static TimerTick getInstance(ModelAndGraphBuilder g, Timer t) {
    if (instance == null) {
      instance = new TimerTick(g, t);
    }
    return instance;
  }

  public static TimerTick getInstance() {
    return instance;
  }

  /**
   * 
   * @return are there any uncompleted parcels
   */
  public boolean isReady() {
    boolean isready = true;
    for (int i = 0; i < graphBuilder.n; i++) {
      if (graphBuilder.parcelstatus[i] < 100) {
        isready = false;
      }
      if (isready == false) {
        i = graphBuilder.n;
      }

    }

    return isready;
  }

  @Override
  public void run() {
    while (!isReady()) {
      int comonTimeonMachines = timer.getCommonTimeOnMachineThreads();
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if ((comonTimeonMachines != -1) && (comonTimeonMachines == MachineSimulate.time)) {
        this.lastCommonTime = comonTimeonMachines;
        this.notifyObservers();
        MachineSimulate.time = comonTimeonMachines + 1;
        // System.out.println(MachineSimulate.time);
      }
    }
  }

  @Override
  public void notifyObservers() {
    for (StateListener listener : this.listeners) {
      listener.handleEvent(StateMachineEvents.TICK, new Integer(this.lastCommonTime), this);
    }

  }

  @Override
  public void registerObserver(StateListener listener) {
    this.listeners.add(listener);

  }

  @Override
  public void unRegisterObserver(StateListener listener) {
    this.listeners.remove(listener);
  }

}
