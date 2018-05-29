package calculations.simulation;

/**
 * Inspects the time on each machine thread
 * 
 * @author Gergely Meszaros
 *
 */
public class Timer {
	private int[] timeOnMachineThreads;

	private int machinecount;

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

}
