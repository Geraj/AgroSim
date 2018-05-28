package calculations.animation.log;

import java.util.ArrayList;
/**
 * Class for storing animation data
 * @author Gergely Meszaros
 *
 */
public class AnimationDataStorage {
  /**
   * List of annimationdata for a operation
   */
  public ArrayList<AnimationData> annimationData = new ArrayList<AnimationData>();

  /** Operation */
  public String operation;

  /** List of machines */
  public ArrayList<String> machine;

  /**
   * Construct instance
   */
  public AnimationDataStorage() {
    annimationData = new ArrayList<AnimationData>();
    machine = new ArrayList<String>();
    operation = "-1";
  }
}
