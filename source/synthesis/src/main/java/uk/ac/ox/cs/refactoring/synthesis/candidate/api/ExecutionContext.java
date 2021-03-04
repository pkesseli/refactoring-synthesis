package uk.ac.ox.cs.refactoring.synthesis.candidate.api;

import uk.ac.ox.cs.refactoring.synthesis.state.State;

/**
 * 
 */
public class ExecutionContext {

  /**
   * 
   */
  public final ClassLoader ClassLoader;

  /**
   * 
   */
  public final State State;

  /**
   * @param classLoader
   * @param state
   */
  public ExecutionContext(final ClassLoader classLoader, final State state) {
    ClassLoader = classLoader;
    State = state;
  }
}
