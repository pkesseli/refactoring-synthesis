package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.util.Set;

import uk.ac.ox.cs.refactoring.synthesis.candidate.Candidate;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.induction.Synthesis;
import uk.ac.ox.cs.refactoring.synthesis.verification.Verification;

/**
 * 
 */
public class CegisLoop {
  /**
   * 
   */
  private Set<Counterexample> counterexamples;

  /**
   * 
   */
  private final Verification verification = new Verification();

  /**
   * @return
   */
  public Candidate synthesise() {
    final Synthesis synthesis = new Synthesis();
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
    counterexamples = verification.verify(candidate);
    return !counterexamples.isEmpty();
  }
}
