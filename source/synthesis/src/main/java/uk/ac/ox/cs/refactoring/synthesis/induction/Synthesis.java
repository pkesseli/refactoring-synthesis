package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.util.Set;

import uk.ac.ox.cs.refactoring.synthesis.entity.Candidate;
import uk.ac.ox.cs.refactoring.synthesis.entity.Counterexample;

/**
 * 
 */
public class Synthesis {
  /**
   * 
   */
  public Candidate getDefault() {
    return new Candidate();
  }

  /**
   * 
   */
  public Candidate synthesise(final Set<Counterexample> counterexamples) {
    return null;
  }
}
