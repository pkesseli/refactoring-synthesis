package uk.ac.ox.cs.refactoring.synthesis.induction;

/** Listener for candidate events, usually for logging. */
public interface CandidateListener<Candidate> {

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
}
