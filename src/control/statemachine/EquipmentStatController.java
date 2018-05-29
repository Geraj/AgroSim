package control.statemachine;

import control.observer.EventDispatcher;
import control.observer.StateMachineEvents;
import event.Event;
import control.observer.StateListener;
import calculations.simulation.MachineSimulate;

/**
 * 
 * @author Geraj
 * 
 */
public class EquipmentStatController implements StateListener {
  /** sm */
  final StateMachine stateMachine = new StateMachine();

  /** STARTED */
  static final String STARTED = "STARTED";

  /** MOVING */
  static final String MOVING = "MOVING";

  /** WORKING */
  static final String WORKING = "WORKING";

  /** STOPPED */
  static final String STOPPED = "STOPPED";

  /** equipmentName */
  final String equipmentName;

  /** event bus */
  final EventDispatcher eventBus = EventDispatcher.getInstance();

  final MachineSimulate machine;

  /** time in current state TODO */
  private double timeInState = 0;

  /**
   * Create an instance
   * 
   * @param simulatedMachine
   */
  public EquipmentStatController(MachineSimulate simulatedMachine) {
		EventDispatcher.getInstance().registerListener(EquipmentStatController.this, StateMachineEvents.TICK);
    this.machine = simulatedMachine;
    this.equipmentName = simulatedMachine.getMachineName();
    // create the logic of execution for the states
    this.buildStartedState(this.stateMachine.addState(STARTED));
    this.buildMovingState(this.stateMachine.addState(MOVING));
    this.buildWorkingState(this.stateMachine.addState(WORKING));
    this.buildStoppedState(this.stateMachine.addState(STOPPED));
    this.start();
    
  }

  /**
   * build a state
   * 
   * @param state
   */
  protected void buildStartedState(State state) {
    state.setOnEntry(new StateEventHandler() {

      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        EventDispatcher.getInstance().registerListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_MOVING_TO_PARCEL);
        EventDispatcher.getInstance().fireEvent(StateMachineEvents.VEHICLE_STARTED, parameter, EquipmentStatController.this.equipmentName);
        System.out.println(EquipmentStatController.this.equipmentName
            + " ENTER VEHICLE STARTED STATE.");
      }
    });
    state.registerTransition(StateMachineEvents.VEHICLE_MOVING_TO_PARCEL.toString(), MOVING,
        new TransitionCondition() {
          public boolean isAllowed(Object parameter) {
            boolean ret = false;
            if (parameter.equals(equipmentName)) {
              ret = true;
            }
            return ret;
          }
        });
    state.setOnExit(new StateEventHandler() {

      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        EventDispatcher.getInstance().unRegisterListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_MOVING_TO_PARCEL);
        System.out.println(EquipmentStatController.this.equipmentName
            + " EXIT VEHICLE STARTED STATE");
      }
    });
  }

  /**
   * build a state
   * 
   * @param state
   */
  protected void buildMovingState(State state) {
    state.setOnEntry(new StateEventHandler() {

      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        EventDispatcher.getInstance().registerListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_WORKING);
        EventDispatcher.getInstance().registerListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_STOPPED);
        System.out.println(EquipmentStatController.this.equipmentName + " ENTER MOVING STATE.");
      }
    });

    state.registerTransition(StateMachineEvents.VEHICLE_WORKING.toString(), WORKING,
        new TransitionCondition() {
          public boolean isAllowed(Object parameter) {
            boolean ret = false;
            if (parameter.equals(equipmentName)) {
              ret = true;
            }
            return ret;
          }
        });
    state.registerTransition(StateMachineEvents.VEHICLE_STOPPED.toString(), STOPPED,
        new TransitionCondition() {
          public boolean isAllowed(Object parameter) {
            boolean ret = false;
            if (parameter.equals(equipmentName)) {
              ret = true;
            }
            return ret;
          }
        });
    state.setOnExit(new StateEventHandler() {
      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        EventDispatcher.getInstance().unRegisterListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_WORKING);
        EventDispatcher.getInstance().unRegisterListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_STOPPED);
        System.out.println(EquipmentStatController.this.equipmentName
            + " EXIT VEHICLE MOVING STATE");
      }
    });
  }

  /**
   * build a state
   * 
   * @param state
   */
  protected void buildWorkingState(State state) {
    state.setOnEntry(new StateEventHandler() {

      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        EventDispatcher.getInstance().registerListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_MOVING_TO_PARCEL);
        System.out.println("ENTER WORKING STATE.");
      }
    });
//    state.registerEvent(StateMachineEvents.TICK.toString(), new StateEventHandler() {
//      @Override
//      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
//        // TODO logging or something useful
//        timeInState++;
//      }
//    });
    state.registerTransition(StateMachineEvents.VEHICLE_MOVING_TO_PARCEL.toString(), MOVING,
        new TransitionCondition() {
          public boolean isAllowed(Object parameter) {
            boolean ret = false;
            if (parameter.equals(equipmentName)) {
              ret = true;
            }
            return ret;
          }
        });
    state.setOnExit(new StateEventHandler() {
      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        EventDispatcher.getInstance().unRegisterListener(EquipmentStatController.this,
            StateMachineEvents.VEHICLE_MOVING_TO_PARCEL);
//        EventDispatcher.getInstance().unRegisterListener(EquipmentStatController.this,
//            StateMachineEvents.TICK);
        System.out.println("Vehicle: " + equipmentName + " was " + timeInState
            + " seconds @ parcell with id " + machine.getCurrentParcelName() + " in working state");
        timeInState = 0;
        System.out.println(EquipmentStatController.this.equipmentName
            + " EXIT VEHICLE WORKING STATE ");
      }
    });
  }

  /**
   * build a state
   * 
   * @param state
   */
  protected void buildStoppedState(State state) {
    state.setOnEntry(new StateEventHandler() {

      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        // Logger.debug(0, "ENTER PASSIVE STATE",
        // Logger.getContext().addItem(
        // OneMoveIdleStateController.this.toString()));
        EventDispatcher.getInstance().unRegisterListener(EquipmentStatController.this);
        System.out.println(EquipmentStatController.this.equipmentName
            + " ENTER VEHICLE STOPPED STATE.");

      }
    });
    state.setOnExit(new StateEventHandler() {
      public void handleEvent(@SuppressWarnings("unused") Object parameter) {
        // Unrequired
      }
    });
  }

  /**
   * Start the state machine
   */
  public void start() {
    this.stateMachine.start(STARTED);
  }

  /**
   * @see control.observer.StateListener#handleEvent(control.observer.StateMachineEvents, java.lang.Object, java.lang.Object)
   */
  @Override
  public void handleEvent(StateMachineEvents eventType, Object param, Object source) {
    if (StateMachineEvents.TICK == eventType) {
      //this.stateMachine.handleEvent(StateMachineEvents.TICK.toString(), param);
      machine.setCurrentTimeEvent((Event) param);
    } else {
      this.stateMachine.handleEvent(eventType.toString(), source);
    }

  }
}
