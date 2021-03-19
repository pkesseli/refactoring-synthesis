package uk.ac.ox.cs.refactoring.synthesis.verification;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;

/**
 * Used as an outcome of a fuzzed method, conveying to the fuzzing verifcation
 * that new counterexamples are available where the candidate and the spec
 * differ.
 */
public class CounterexampleException extends Exception {

  /**
   * Generated {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 8022715955740182261L;

  /**
   * New counterexample.
   */
  public final Counterexample Counterexample;

  /**
   * @param counterexample {@link #Counterexample}
   */
  public CounterexampleException(final Counterexample counterexample) {
    this.Counterexample = counterexample;
  }
}
