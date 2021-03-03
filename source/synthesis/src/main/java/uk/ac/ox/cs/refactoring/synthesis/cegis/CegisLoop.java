package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.induction.FuzzingSynthesis;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.verification.Verification;

/**
 * 
 */
public class CegisLoop<Candidate> {
  /**
   * 
   */
  private final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();

  /**
   * 
   */
  private final Verification<Candidate> verification = new Verification<Candidate>();

  /**
   * @return
   */
  public Candidate synthesise() {
    final FuzzingSynthesis<Candidate> synthesis = new FuzzingSynthesis<Candidate>(null, null);
    Candidate candidate = synthesis.getDefault();
    while (needsRefinement(candidate)) {
      candidate = synthesis.synthesise(counterexamples);
    }
    return candidate;
  }

  /**
   * @param candidate
   * @return
   */
  private boolean needsRefinement(final Candidate candidate) {
    final Map<Counterexample, ExecutionResult> newCounterexamples = verification.verify(candidate);
    counterexamples.putAll(newCounterexamples);
    return !newCounterexamples.isEmpty();
  }
}
