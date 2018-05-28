package calculations.simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import control.observer.EventDispatcher;
import control.observer.StateMachineEvents;
import control.statemachine.MovingToParcel;
import calculations.ModelAndGraphBuilder;

import core.Machines;

/**
 * Simulates a machine
 * 
 * @author Gergely Meszaros
 * 
 */
public class MachineSimulate implements Runnable {

	private ModelAndGraphBuilder graph;
	

	/**
	 * machine simulation global time
	 */
	public static int time;
	private int speed;
	/**
	 * time spent on road
	 */
	private int onroad;
	/**
	 * id of the current parcel
	 */
	private int currentparcel;
	private double timeperHA;
	private String machineName;
	private Timer myTimer;
	private int myTimerID;
	public double areaWorked;
	public int distancetraveld;
	private FileWriter fstream;
	private BufferedWriter out;
	private FileWriter fstreamanim;
	private BufferedWriter outanim;
	//public String log;
	private Semaphore sem;
	private Semaphore sem2;

	/**
	 * 
	 * @param graph
	 *            - graph to use
	 * @param speed
	 *            - machines travel speed (km/h)
	 * @param timeperHA
	 *            - machines work speed (ha/h)
	 * @param machineName
	 *            - id of the machine
	 * @param myTimer
	 * @param timerID
	 *            - position in Timer's timeOnMachineThreads array which
	 *            corresponds to this machine
	 * @param sem
	 * @param sem2
	 */
	public MachineSimulate(ModelAndGraphBuilder graph, Machines currentMachine,
			Timer myTimer, int timerID, Semaphore sem, Semaphore sem2) {
		this.graph = graph;
		this.speed = currentMachine.getSpeed();
		this.timeperHA = currentMachine.getWorkspeed();
		this.machineName = currentMachine.getID();
		this.myTimer = myTimer;
		this.myTimerID = timerID;
		this.sem = sem;
		this.sem2 = sem2;
		onroad = 0;
		currentparcel = 0;
		String log = "Start Machine" + machineName + "\n";
		String title = graph.base.getName() + "_" + machineName + "_Operation_"
				+ graph.operation.getName();
		try {
			FileWriter masterwrite = new FileWriter("log/" + "Master" + ".txt",
					true);
			BufferedWriter outmaster = new BufferedWriter(masterwrite);
			outmaster.write(graph.operation.getName() + " " + machineName + " "
					+ title + "\n");
			outmaster.close();
			fstreamanim = new FileWriter("log/" + title + ".ani");
			outanim = new BufferedWriter(fstreamanim);
			fstream = new FileWriter("log/" + title + ".txt");
			out = new BufferedWriter(fstream);
			out.write(machineName + " should do:\n");
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * @return are there any uncompleted parcels
	 */
	public boolean isReady() {
		boolean isready = true;
		for (int i = 0; i < graph.n; i++) {
			if (graph.parcelstatus[i] < 100) {
				isready = false;
			}
			if (isready == false) {
				i = graph.n;
			}
		}
		return isready;
	}

	/**
	 * Converts minutes into Day:Time format considering 10 hour workdays
	 * 
	 * @param minute
	 * @return String with the time
	 */
	public String convertMinutes(int minute) {
		String converted = "";
		int days = minute / 600;// 10 hour workdays
		minute = minute - days * 600;
		int hours = (minute / 60) + 8;// work starts at 8
		minute = minute % 60;
		if (minute < 10)
			converted = "Day:" + days + " Time:" + hours + ":0" + minute + " ";
		else
			converted = "Day:" + days + " Time:" + hours + ":" + minute + " ";
		return converted;
	}

	/**
	 * 
	 * @return finds the nearest workable parcel's id
	 */
	private int getparceltowork() {
		int work = 0;
		double min = Integer.MAX_VALUE;
		for (int i = 0; i < graph.n; i++) {
			// select nearest parcel to work
			if (((graph.D[currentparcel][i]) < min)
					&& (graph.placeonparcel[i] > graph.workersonparcell[i])
					&& (graph.parcelstatus[i] < 100) && (i != currentparcel)) {
				work = i;
				min = graph.D[currentparcel][i];
			}
		}
		return work;
	}

	/**
	 * Start working
	 */
	@Override
	public void run() {
		double haperminute = timeperHA / 60;
		//EventDispatcher.getInstance().fireEvent(Events.VEHICLE_STARTED, null, this.machineName);
		while (!isReady()) {
			int x = 3;
			synchronized (myTimer) {
				// no more work here
				if ((graph.parcelstatus[currentparcel] >= 100) && isBehind()) {
					myTimer.timeOnMachineThreads[myTimerID] = time;
					// not heading tovards a complete parcel (base)
					if (time > onroad) {
						int work = getparceltowork();// get the nearest uncompleted
														// parcel with free place
						if (work != 0) {
							if (currentparcel != 0) {// dont show if at base
								writetofile(" " + convertMinutes(time) + "Completed:"
										+ graph.parcels[currentparcel].getName() + " parcel\n");
							}
							// critical code for reserving a place on the parcel
							try {
								sem.acquire();
							} catch (InterruptedException e) {
							}
							if (graph.workersonparcell[work] < graph.placeonparcel[work]) {
								int distanceToParcel = (int) graph.D[currentparcel][work];
								distancetraveld += distanceToParcel;
								int timetotravel = (int) (distanceToParcel * 60 / ((speed * myrand() * 1000)) + 1);// m/minute
																													// +1
								onroad = time + timetotravel;
								graph.workersonparcell[currentparcel] -= 1;
								graph.workersonparcell[work] += 1;
								EventDispatcher.getInstance().fireEvent(StateMachineEvents.VEHICLE_MOVING_TO_PARCEL,
										new MovingToParcel(graph.parcels[currentparcel].getID(),
												graph.parcels[work].getID()),
										this.machineName);								
								writetofile(addpathtolog(work, currentparcel, timetotravel));
								currentparcel = work;
							}
							sem.release();
						}
					}
				} else {
					// work a minute if tick is 1min in plus
					if ((time >= onroad) && isBehind() && (currentparcel != 0)
							&& (graph.parcelstatus[currentparcel] < 100)) {
						int minutesToBase = (int) (graph.D[currentparcel][0] * 60 / ((speed * 1000)) + 1);// TODO test
																											// this
						if ((((time % 599 == 1)) || (((time + minutesToBase) % 599 == 1))) && (time != 1)) {// next day
							graph.workersonparcell[currentparcel] -= 1;
							returnToBase(true);
							currentparcel = 0;
							writetofile("work is ower for today:)\n");

						} else {
							// Critical, only one thread should modify the status of
							// a parcel
							try {
								sem2.acquire();
							} catch (InterruptedException e) {

							}
							if (graph.parcelstatus[currentparcel] < 100) {
								double hapm = haperminute * myrand();
								double percentOfParcelPerMinute = (hapm * 100 / graph.parcels[currentparcel].getArea());// %
																														// of
																														// parcel
																														// per
																														// minute
								if (percentOfParcelPerMinute < (100 - graph.parcelstatus[currentparcel])) {// is
																											// parcel
																											// almost
																											// complete?
									graph.parcelstatus[currentparcel] += percentOfParcelPerMinute;
									areaWorked += hapm;
									double d = hapm * 100 / graph.parcels[currentparcel].getArea() * 100;
									int p = (int) d;
									writetofile(convertMinutes(time) + "Completed on "
											+ graph.parcels[currentparcel].getName() + " %" + p / 100.0 + "\n");
									EventDispatcher.getInstance().fireEvent(StateMachineEvents.VEHICLE_WORKING,
											graph.parcels[currentparcel].getID(), this.machineName);
								} else {
									double remaining = 100 - graph.parcelstatus[currentparcel];
									areaWorked += (remaining * graph.parcels[currentparcel].getArea()) / 100;
									graph.parcelstatus[currentparcel] = 100;
									double d = remaining * 100;
									int p = (int) d;
									writetofile(convertMinutes(time) + "Added to "
											+ graph.parcels[currentparcel].getName() + " %" + p / 100.0 + "\n");
								}
							}
						}
						sem2.release();
						myTimer.timeOnMachineThreads[myTimerID] = time;
					}
					// travel a minute
					if ((time < onroad) && isBehind()) {
						myTimer.timeOnMachineThreads[myTimerID] = time;
						if ((time % 599 == 1) && (time != 1)) {// next day
							// System.out.println(machineName+" "+convertMinutes(time)+":work is ower for
							// today:)");
							graph.workersonparcell[currentparcel] -= 1;
							distancetraveld += graph.D[currentparcel][0];
							// //////
							EventDispatcher.getInstance().fireEvent(StateMachineEvents.VEHICLE_MOVING_TO_PARCEL,
									new MovingToParcel(graph.parcels[currentparcel].getID(), 0), this.machineName);
							currentparcel = 0;

						}
					}
				}
			}
		}
		try {

			// System.out.println(machineName+":thread ready");
			out.write("All parcels are ready, return to base \n");
			returnToBase();
			double d = this.areaWorked * graph.operation.getPrice() * 100;
			int p = (int) d;
			double area = this.areaWorked * 100;
			int a1 = (int) area;
			writetoanim(Integer.toString(onroad));
			out.write("\n Area:" + a1 / 100.0 + "(Price:" + p / 100.0 + ")"
					+ " distance:" + this.distancetraveld);
			out.close();
			outanim.close();
			EventDispatcher.getInstance().fireEvent(StateMachineEvents.VEHICLE_STOPPED, this.currentparcel, this.machineName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isBehind() {
		return myTimer.timeOnMachineThreads[myTimerID] < time;
	}

	/**
	 * Return to base and log the path.
	 */
	private void returnToBase() {
		distancetraveld += graph.D[currentparcel][0];
		int timetotravel = (int) (graph.D[currentparcel][0] * 60
				/ ((speed * myrand() * 1000)) + 1);
		onroad = timetotravel + time;
		writetofile(addpathtolog(0, currentparcel, timetotravel));
		EventDispatcher.getInstance().fireEvent(StateMachineEvents.VEHICLE_MOVING_TO_PARCEL, new MovingToParcel(graph.parcels[currentparcel].getID(), 0), this.machineName);
	}

	/**
	 * Return to base and log the path. Dont use rand if specified.
	 */
	private void returnToBase(boolean norand) {
		distancetraveld += graph.D[currentparcel][0];
		int timetotravel = 0;
		if (norand) {
			timetotravel = (int) (graph.D[currentparcel][0] * 60
					/ ((speed * 1000)) + 1);
		} else {
			timetotravel = (int) (graph.D[currentparcel][0] * 60
					/ ((speed * myrand() * 1000)) + 1);
		}
		onroad = timetotravel + time;	
		writetofile(addpathtolog(0, currentparcel, timetotravel));
	}

	private void writetofile(String s) {
		try {
			out.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writetoanim(String s) {
		try {
			outanim.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int getPreviousPoint(int from, int to) {
		return graph.P[from][to];
	}

	/**
	 * Add's the traveled path to the log file.
	 * 
	 * @param newparcel
	 * @param oldparcel
	 * @param timetotravel
	 * @return 
	 */
	private String addpathtolog(int newparcel, int oldparcel, int timetotravel) {
		StringBuilder log = new StringBuilder();
		log.append(" " + convertMinutes(time) + "seletced:"
				+ graph.parcels[newparcel].getName() + " ,time when there:"
				+ convertMinutes(onroad) + "time to travel:" + timetotravel
				+ "\n");
		writetoanim("Select " + graph.parcels[newparcel].getID() + " " + onroad
				+ " " + timetotravel + "\n");
		ArrayList<Integer> path = new ArrayList<Integer>();
		int n = 0;
		int np = newparcel;
		while (getPreviousPoint(oldparcel, np) != oldparcel) {
			np = getPreviousPoint(oldparcel, np);
			path.add(0, np);
			n++;
		}
		if (n == 0) {
			log.append(" Connection(from:" + graph.parcels[oldparcel].getName()
					+ " to:" + graph.parcels[newparcel].getName()
					+ "): Direct connection" + " distance:"
					+ graph.D[newparcel][oldparcel] + "m\n");
			writetoanim("Con " + graph.parcels[oldparcel].getID() + " "
					+ graph.parcels[newparcel].getID() + "\n");
		} else {
			log.append( " Connection(from:" + graph.parcels[oldparcel].getName()
					+ " to:" + graph.parcels[newparcel].getName() + "): ");
			writetoanim("Con " + graph.parcels[oldparcel].getID());
			log.append(graph.parcels[oldparcel].getName() + "->");
			for (int i = 0; i < n; i++) {
				log.append( graph.parcels[path.get(i)].getName() + "->");
				writetoanim(" " + graph.parcels[path.get(i)].getID());
			}
			log.append(graph.parcels[newparcel].getName() + " distance:"
					+ graph.D[newparcel][oldparcel] + "m\n");
			writetoanim(" " + graph.parcels[newparcel].getID() + "\n");
			
		}
		return log.toString();
	}

	/**
	 * Generates a uniformly distributed random number between 0.90 and 1.00 .
	 * 
	 * @return see description
	 */
	private double myrand() {
		Random r = new Random();
		double x = r.nextDouble();
		double d = 90 + Math.abs((x / 6) * 10);
		return d / 100;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getOnroad() {
		return onroad;
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrentparcel() {
		return currentparcel;
	}

	public String getCurrentParcelName(){
		return graph.parcels[this.getCurrentparcel()].getName();
	}
	/**
	 * 
	 * @return
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * 
	 * @return
	 */
	public double getAreaWorked() {
		return areaWorked;
	}
}
