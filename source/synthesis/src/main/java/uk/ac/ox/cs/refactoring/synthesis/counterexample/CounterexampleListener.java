package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/** Listener for counterexample events, usually used for logging. */
public interface CounterexampleListener {

  /**
   * Generated a spurious counterexample (e.g. created using fuzzing) which passed
   * for the current candidate.
   * 
   * @param counterexample Spurious counterexample.
   */
  void spurious(Counterexample counterexample);

  /**
   * Generated a genuine counterexample for which expected and actual results
   * differ.
   * 
   * @param counterexample Genuine counterexample.
   * @param expected       Output of specification.
   * @param actual         Output of synthesised function.
   */
  void genuine(Counterexample counterexample, ExecutionResult expected, ExecutionResult actual);
}
