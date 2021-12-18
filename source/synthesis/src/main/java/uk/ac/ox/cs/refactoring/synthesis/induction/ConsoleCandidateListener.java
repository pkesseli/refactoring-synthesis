package uk.ac.ox.cs.refactoring.synthesis.induction;

/** Implements {@link CandidateListener} by writing to the console. */
public class ConsoleCandidateListener<Candidate> implements CandidateListener<Candidate> {

  @Override
  public void initial(final Candidate candidate) {
    System.out.println("CA initial: " + candidate);
  }

  @Override
  public void spurious(final Candidate candidate) {
    System.out.println("CA spurious: " + candidate);
  }

  @Override
  public void genuine(final Candidate candidate) {
    System.out.println("CA genuine: " + candidate);
  }

}
