package uk.ac.ox.cs.refactoring.synthesis.statistics;

import java.time.Instant;

import uk.ac.ox.cs.refactoring.synthesis.induction.CandidateListener;

class CountingCandiateListener<Candidate> implements CandidateListener<Candidate> {

  Instant start;

  int Spurious;

  int Genuine;

  @Override
  public void initial(final Candidate candidate) {
    start = Instant.now();
  }

  @Override
  public void spurious(final Candidate candidate) {
    ++Spurious;
  }

  @Override
  public void genuine(final Candidate candidate) {
    ++Genuine;
  }

}
