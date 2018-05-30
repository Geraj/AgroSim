package calculations.simulation;

import control.observer.StateListener;
import control.observer.StateMachineEvents;
import event.Event;
import event.EventType;

/**
 * Inspects the time on each machine thread
 * 
 * @author Gergely Meszaros
 *
 */
public class Timer implements StateListener {
	private int[] timeOnMachineThreads;

	private int machinecount;

	/**
	 * machine simulation global time in minutes
	 */
	private int time;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param machinecount
	 */
	public Timer(int machinecount) {
		this.machinecount = machinecount;
		timeOnMachineThreads = new int[machinecount];
		for (int i = 0; i < machinecount; i++) {
			timeOnMachineThreads[i] = -1;
		}
	}

	/**
	 * 
	 * @return -1 if the time is not the same on the machines otherwise returns the
	 *         common time
	 */
	public int getCommonTimeOnMachineThreads() {
		int commontime = timeOnMachineThreads[0];
		for (int i = 0; i < machinecount; i++) {
			if (commontime != timeOnMachineThreads[i]) {
				commontime = -1;
				break;
			}
		}
		return commontime;
	}

	/**
	 * @return the timeOnMachineThreads
	 */
	public int[] getTimeOnMachineThreads() {
		return timeOnMachineThreads;
	}

	/**
	 * control.observer.StateListener
	 */
	public void handleEvent(StateMachineEvents eventType, Object message, Object source) {
		if (StateMachineEvents.SIMULATION_EVENT.equals(eventType)){
			if (EventType.TIME_CHANGE.equals(((Event) message).getType())) {
				if (getCommonTimeOnMachineThreads()>0) {
					time ++;
				}
			}
		}		
	}
	
	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

}
