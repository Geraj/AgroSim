package control.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * State machine implementation
 * 
 * @author Geraj
 * 
 */
public class State {
	/** event map */
	private Map<String, StateEventHandler> eventMap = null;

	/** on entry handler */
	private StateEventHandler onEntry = null;

	/** on exit handler */
	private StateEventHandler onExit = null;

	/** reference to the state machine instance */
	protected StateMachine stateMachine = null;

	/** map containing the transition events. Event name mapped to new state */
	private Map<String, List<State.Transition>> transitionMap = null;

	/** name */
	private String name = "";

	/**
	 * Constructs a new State
	 * 
	 * @param name
	 */
	State(String name) {
		super();
		this.name = name;
	}

	/**
	 * Called to handle an event. If the event is defined the registered
	 * StateEventHandler will be called. In case of a transition registered for
	 * the given event, the state will first execute the registered handler, and
	 * after that will execute the transition.
	 * 
	 * @param event
	 * @param parameter
	 * @return true if the message was handled, and false otherways.
	 */
	public final boolean handleEvent(String event, Object parameter) {
		boolean ret = false;
		// handle event
		if (this.eventMap != null) {
			final StateEventHandler handler = this.eventMap.get(event);
			if (handler != null) {
				try {
					handler.handleEvent(parameter);
					ret = true;
				} catch (Exception e) {
					this.stateMachine.logWarn(
							"Exception caught during the handling of event '"
									+ event + "' with param '" + parameter
									+ "'.", e);
				}
			}
		}

		// handle transition

		if (this.transitionMap != null) {
			final List<State.Transition> transitions = this.transitionMap
					.get(event);
			if (transitions != null) {
				for (Transition transition : transitions) {
					if (transition.doTransition(parameter)) {
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Register an event into the state
	 * 
	 * @param event
	 * @param handler
	 */
	public final void registerEvent(String event, StateEventHandler handler) {
		if (this.eventMap == null) {
			this.eventMap = new HashMap<String, StateEventHandler>();
		}
		// cache the event
		this.eventMap.put(event, handler);
	}

	/**
	 * Used to unregister event from the state
	 * 
	 * @param eventName
	 */
	public final void unregisterEvent(String eventName) {
		if (this.eventMap != null) {
			this.eventMap.remove(eventName);
		}
	}

	/**
	 * Used to register a transition, based on an event. In case of an event
	 * receiving the state will try to handle the event using an event handler,
	 * and after if transition defined will execute the transition.
	 * 
	 * @param event
	 * @param newState
	 */
	public final void registerTransition(String event, String newState) {
		this.registerTransition(event, newState, null, null);
	}

	/**
	 * Used to register a transition, based on an event. In case of an event
	 * receiving the state will try to handle the event using an event handler,
	 * and after if transition defined will execute the transition.
	 * 
	 * @param event
	 * @param newState
	 * @param handler
	 */
	public final void registerTransition(String event, String newState,
			StateEventHandler handler) {
		this.registerTransition(event, newState, null, handler);
	}

	/**
	 * Used to unregister a transition for the given event
	 * 
	 * @param event
	 */
	public final void unregisterTransition(String event) {
		if (this.transitionMap != null) {
			this.transitionMap.remove(event);
		}
	}

	/**
	 * Used to register a transition, based on an event. In case of an event
	 * receiving the state will try to handle the event using an event handler,
	 * and after if transition defined and condition allows it will execute the
	 * transition.
	 * 
	 * @param event
	 * @param newState
	 * @param condition
	 *            the condition to be checked before executing transition
	 */
	public final void registerTransition(String event, String newState,
			TransitionCondition condition) {
		this.registerTransition(event, newState, condition, null);
	}

	/**
	 * Used to register a transition, based on an event. In case of an event
	 * receiving the state will try to handle the event using an event handler,
	 * and after if transition defined and condition allows it will execute the
	 * transition.
	 * 
	 * @param event
	 * @param newState
	 * @param condition
	 *            the condition to be checked before executing transition
	 * @param handler
	 */
	public final void registerTransition(String event, String newState,
			TransitionCondition condition, StateEventHandler handler) {
		if (this.transitionMap == null) {
			this.transitionMap = new HashMap<String, List<State.Transition>>();
		}
		// register transition
		List<Transition> list = this.transitionMap.get(event);
		if (list == null) {
			list = new ArrayList<Transition>();
			this.transitionMap.put(event, list);
		}
		list.add(new State.Transition(newState, condition, handler));
	}

	/**
	 * Set the On Entry handler
	 * 
	 * @param onEntry
	 */
	public void setOnEntry(StateEventHandler onEntry) {
		this.onEntry = onEntry;
	}

	/**
	 * Set the on exit handler
	 * 
	 * @param onExit
	 */
	public void setOnExit(StateEventHandler onExit) {
		this.onExit = onExit;
	}

	/**
	 * Called to handle the on exit
	 * 
	 * @param parameter
	 */
	final void onExit(Object parameter) {
		if (this.onExit != null) {
			try {
				this.stateMachine.logDebug("onExit[" + this.name + "]");
				this.onExit.handleEvent(parameter);
			} catch (Exception e) {
				this.stateMachine.logWarn(
						"Exception caught during the handling of onExit!"
								+ " param:'" + parameter + "'.", e);
			}
		}
	}

	/**
	 * Called to handle the on entry
	 * 
	 * @param parameter
	 */
	final void onEntry(Object parameter) {
		if (this.onEntry != null) {
			try {
				this.stateMachine.logDebug("onEntry[" + this.name + "]");
				this.onEntry.handleEvent(parameter);
			} catch (Exception e) {
				this.stateMachine.logWarn(
						"Exception caught during the handling of onEntry!"
								+ " param '" + parameter + "'.", e);
			}
		}
	}

	/**
	 * Used to set the state machine
	 * 
	 * @param machine
	 */
	final void setStateMachine(StateMachine machine) {
		this.stateMachine = machine;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("State[onEntry=").append(this.onEntry != null).append(
				", onExit=").append(this.onExit != null).append("]{\n");
		if (this.eventMap != null) {
			for (Entry<String, StateEventHandler> event : this.eventMap
					.entrySet()) {
				sb.append("\t\tEvent:").append(event.getKey()).append("\n");
			}
		}

		if (this.transitionMap != null) {
			for (Entry<String, List<Transition>> event : this.transitionMap
					.entrySet()) {
				sb.append("\t\tTransitions for event:").append(event.getKey())
						.append("(\n");
				for (Transition transition : event.getValue()) {
					sb.append("\t\t\tto state:").append(transition.toState);
					if (transition.condition != null) {
						sb.append(" with condition.");
					}
					sb.append("\n");
				}
				sb.append("\t\t)\n");
			}
		}
		sb.append("\t}");
		return sb.toString();
	}

	/**
	 * Transition definition
	 */
	private class Transition {
		/** the state to */
		protected String toState = null;

		/** condition */
		protected TransitionCondition condition = null;

		/** handler */
		protected StateEventHandler handler = null;

		/**
		 * Constructs a new Transition
		 * 
		 * @param toState
		 * @param condition
		 * @param handler
		 */
		public Transition(String toState, TransitionCondition condition,
				StateEventHandler handler) {
			super();
			this.toState = toState;
			this.condition = condition;
			this.handler = handler;
		}

		/**
		 * Execute transition if the defined condition allows it
		 * 
		 * @param parameter
		 * @return true if transition was executed
		 */
		public boolean doTransition(Object parameter) {
			boolean ret = false;
			// if condition check
			if (this.condition != null) {
				try {
					if (this.condition.isAllowed(parameter)) {
						ret = true;
					}
				} catch (Exception e) {
					State.this.stateMachine.logWarn(
							"Failed to execute transition condition delegate!",
							e);
				}
			} else {
				ret = true;
			}

			// else do transition
			if (ret) {
				// change state
				State.this.stateMachine.executeTransition(this.toState,
						parameter, this.handler);
			}
			return ret;
		}
	}
}
