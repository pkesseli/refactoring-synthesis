package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.util.HashSet;
import java.util.Set;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.Candidate;
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
  private final Set<Counterexample> counterexamples = new HashSet<>();

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
    final Set<Counterexample> newCounterexamples = verification.verify(candidate);
    counterexamples.addAll(newCounterexamples);
    return !newCounterexamples.isEmpty();
  }
}
