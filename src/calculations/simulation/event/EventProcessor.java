
package calculations.simulation.event;

import event.Event;

/**
 * 
 * Interface for processing messages from messaging queue
 * 
 * @author Geraj
 */
public interface EventProcessor {
  /**
   * 
   * Handles the event
   * 
   * @param event
   */
  void handleEvent(Event event);
}
