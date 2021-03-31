package uk.ac.ox.cs.refactoring.synthesis.candidate.api;

import uk.ac.ox.cs.refactoring.synthesis.state.State;

/**
 * Aggregates an {@link ClassLoader} in which to execute a candidate, as well as
 * a {@link State} from which to begin execution.
 */
public class ExecutionContext {

  /**
   * {@link ClassLoader} in which to perform execution.
   */
  public final ClassLoader ClassLoader;

  /**
   * {@link State} from which to execute.
   */
  public final State State;

  /**
   * @param classLoader {@link #ClassLoader}
   * @param state       {@link #State}
   */
  public ExecutionContext(final ClassLoader classLoader, final State state) {
    ClassLoader = classLoader;
    State = state;
  }
}
