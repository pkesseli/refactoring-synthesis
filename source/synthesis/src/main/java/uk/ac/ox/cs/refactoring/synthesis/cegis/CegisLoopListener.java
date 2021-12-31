package uk.ac.ox.cs.refactoring.synthesis.cegis;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/** Subscribe to synthesis-related events. */
public interface CegisLoopListener<Candidate> {

  /**
   * Initial candidate.
   * 
   * @param candidate Initial candidate.
   */
  void initial(Candidate candidate);

  /**
   * Synthesised a candidate which does not satisfy current counterexamples.
   * 
   * @param candidate Spurious candidate.
   */
  void spurious(Candidate candidate);

  /**
   * Synthesised a candidate which satisfies all current counterexamples.
   * 
   * @param candidate Genuine candidate.
   */
  void genuine(Candidate candidate);

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

  /**
   * Verified a candidate by exhausting fuzzing inputs.
   * 
   * @param candidate Confirmed candidate.
   */
  void verified(Candidate candidate);
}
