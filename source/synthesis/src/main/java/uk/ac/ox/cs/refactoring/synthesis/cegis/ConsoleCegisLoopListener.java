package uk.ac.ox.cs.refactoring.synthesis.cegis;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ConsoleCounterexampleListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.induction.ConsoleCandidateListener;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/**
 * Implements {@link CegisLoopListener} by printing information to the console.
 */
public class ConsoleCegisLoopListener implements CegisLoopListener<SnippetCandidate> {

  /** Prints candidate information. */
  private final ConsoleCandidateListener<SnippetCandidate> candidateListener = new ConsoleCandidateListener<>();

  /** Prints counterexample information. */
  private final ConsoleCounterexampleListener counterexampleListener = new ConsoleCounterexampleListener();

  @Override
  public void initial(final SnippetCandidate candidate) {
    candidateListener.initial(candidate);
  }

  @Override
  public void spurious(final SnippetCandidate candidate) {
    candidateListener.spurious(candidate);
  }

  @Override
  public void genuine(final SnippetCandidate candidate) {
    candidateListener.genuine(candidate);
  }

  @Override
  public void spurious(final Counterexample counterexample) {
    counterexampleListener.spurious(counterexample);
  }

  @Override
  public void genuine(final Counterexample counterexample, final ExecutionResult expected,
      final ExecutionResult actual) {
    counterexampleListener.genuine(counterexample, expected, actual);
  }

  @Override
  public void verified(final SnippetCandidate candidate) {
    candidateListener.verified(candidate);
  }

  @Override
  public void close() {
  }
}
