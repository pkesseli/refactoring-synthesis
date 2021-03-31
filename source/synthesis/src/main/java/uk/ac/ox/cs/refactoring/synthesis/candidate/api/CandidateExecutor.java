package uk.ac.ox.cs.refactoring.synthesis.candidate.api;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/**
 * Interpreter for candidates of a given type. Used to test candidates against
 * inputs.
 */
public interface CandidateExecutor<Candidate> {
  /**
   * Executes a candidate against an input.
   * 
   * @param candidate      Candidate to execute.
   * @param counterexample Input against which to execute.
   * @return {@link ExecutionResult}
   */
  ExecutionResult execute(Candidate candidate, Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException;
}
