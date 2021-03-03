package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.util.Map;
import java.util.function.Supplier;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/**
 * Explores candidates in the order provided by a {@link Supplier}.
 */
public class FuzzingSynthesis<Candidate> {

  /**
   * 
   */
  private final Supplier<Candidate> candidates;

  private final CandidateExecutor<Candidate> executor;

  /**
   * @param candidates {@link #candidates}
   * @param executor   {@link #executor}
   */
  public FuzzingSynthesis(final Supplier<Candidate> candidates, final CandidateExecutor<Candidate> executor) {
    this.candidates = candidates;
    this.executor = executor;
  }

  /**
   * 
   */
  public Candidate getDefault() {
    return candidates.get();
  }

  /**
   * 
   * @param candidate
   * @param counterexamples
   * @return
   */
  private boolean satisfiesAll(final Candidate candidate, final Map<Counterexample, ExecutionResult> counterexamples) {
    for (final Map.Entry<Counterexample, ExecutionResult> counterexample : counterexamples.entrySet()) {
      final ClassLoader classLoader = ClassLoaders.createIsolated();
      final Counterexample input = counterexample.getKey();
      final ExecutionResult expected = counterexample.getValue();
      final ExecutionResult actual = executor.execute(candidate, classLoader, input);
      if (!expected.equals(actual)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 
   * @param counterexamples
   * @return
   */
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples) {
    Candidate current;
    do {
      current = candidates.get();
    } while (!satisfiesAll(current, counterexamples));
    return current;
  }
}
