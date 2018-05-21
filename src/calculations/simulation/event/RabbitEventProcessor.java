
package calculations.simulation.event;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import calculations.ModelAndGraphBuilder;
import calculations.simulation.MachineSimulate;
import calculations.simulation.Timer;
import control.observer.Listener;
import control.observer.Observable;
import control.observer.StateMachineEvents;
import event.Event;
import event.EventType;

/**
 * Rabbit mq event processor
 * 
 * @author Geraj
 */
public class RabbitEventProcessor implements Observable, EventProcessor {

  /**
   * 
   */
  private LinkedList<Listener> listeners = new LinkedList<Listener>();

  /** Simulation model for infrastructure */
  private ModelAndGraphBuilder infrastructureModel;

  /** Object following the time on different simulation threads */
  private Timer timer;

  /** Last common time */
  private int lastCommonTime;

  /**
   * Connector to rabbitmq exchange
   */
  private RabbitExchangeConnecter rabbitExchangeConnector = new RabbitExchangeConnecter();

  /**
   * Constructs a new instance.
   * @param infrastructureModel 
   * 
   * @param t
   */
  public RabbitEventProcessor(ModelAndGraphBuilder infrastructureModel, Timer t) {
    this.infrastructureModel = infrastructureModel;
    this.timer = t;
    rabbitExchangeConnector.connect(this);
  }

  /**
   * TODO DESCRIPTION
   */
  public void closeConnection() {
    rabbitExchangeConnector.disconnect();
  }

  /**
   * @see control.observer.Observable#notifyObservers()
   */
  @Override
  public void notifyObservers() {
    for (Listener listener : this.listeners) {
      listener.handleEvent(StateMachineEvents.TICK, new Integer(this.lastCommonTime), this);
    }

  }

  /**
   * @see control.observer.Observable#registerObserver(control.observer.Listener)
   */
  @Override
  public void registerObserver(Listener listener) {
    this.listeners.add(listener);

  }

  /**
   * @see control.observer.Observable#unRegisterObserver(control.observer.Listener)
   */
  @Override
  public void unRegisterObserver(Listener listener) {
    this.listeners.remove(listener);
  }

  /**
   * @see calculations.simulation.event.EventProcessor#handleSimulationEvent(event.Event)
   */
  public void handleSimulationEvent(Event event) {

		if (event.getType().equals(EventType.TIME_CHANGE)) {
			synchronized (timer) {
				boolean handled = false;
				int retryCount = 0;
				while (!handled) {
					int comonTimeonMachines = -1;
					while (comonTimeonMachines < 0 && comonTimeonMachines != MachineSimulate.time) {
						comonTimeonMachines = timer.getCommonTimeOnMachineThreads();
					}

					if ((comonTimeonMachines == MachineSimulate.time)) {
						this.lastCommonTime = comonTimeonMachines;
						this.notifyObservers();
						MachineSimulate.time = comonTimeonMachines + 1;
						handled = true;
						// System.out.println(MachineSimulate.time);
					} else {
						if (retryCount > 10) {
							Logger.getLogger(RabbitEventProcessor.class)
									.info("Time change event not handled after 10 tries");
							break;
						}
						retryCount += 1;
						//
					}
				}
			}
		}
  }

}
