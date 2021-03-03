package uk.ac.ox.cs.refactoring.synthesis.candidate.api;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/**
 * 
 */
public interface CandidateExecutor<Candidate> {
  /**
   * @param candidate
   * @param classLoader
   * @param counterexample
   * @return
   */
  ExecutionResult execute(Candidate candidate, ClassLoader classLoader, Counterexample counterexample);
}
