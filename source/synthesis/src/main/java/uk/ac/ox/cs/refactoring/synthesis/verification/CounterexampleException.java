package uk.ac.ox.cs.refactoring.synthesis.verification;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;

/**
 * 
 */
public class CounterexampleException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 8022715955740182261L;

  /**
   * 
   */
  public final Counterexample Counterexample;

  /**
   * @param counterexample {@link #Counterexample}
   */
  public CounterexampleException(final Counterexample counterexample) {
    this.Counterexample = counterexample;
  }
}
