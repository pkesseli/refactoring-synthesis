package uk.ac.ox.cs.refactoring.synthesis.state;

/**
 * 
 */
public class State {
  /**
   * 
   */
  public final Object Instance;

  /**
   * 
   */
  public final Object[] Arguments;

  /**
   * 
   * @param instance
   * @param arguments
   */
  public State(Object instance, Object[] arguments) {
    Instance = instance;
    Arguments = arguments;
  }
}
