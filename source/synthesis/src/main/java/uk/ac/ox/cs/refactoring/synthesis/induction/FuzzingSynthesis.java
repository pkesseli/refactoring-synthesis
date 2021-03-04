package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.util.Map;
import java.util.function.Supplier;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.HeapComparison;

/**
 * Explores candidates in the order provided by a {@link Supplier}.
 */
public class FuzzingSynthesis<Candidate> {

  /**
   * 
   */
  private final Supplier<Candidate> candidates;

  /**
   * 
   */
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
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   */
  private boolean satisfiesAll(final Candidate candidate, final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IllegalAccessException {
    for (final Map.Entry<Counterexample, ExecutionResult> counterexample : counterexamples.entrySet()) {
      final IsolatedClassLoader classLoader = ClassLoaders.createIsolated();
      final Counterexample input = counterexample.getKey();
      final ExecutionResult expected = counterexample.getValue();
      final ExecutionResult actual = executor.execute(candidate, classLoader, input);
      if (!HeapComparison.equals(expected, actual)) {
        return false;
      }
    }
    return true;

  }

  /**
   * 
   * @param counterexamples
   * @return
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   */
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IllegalAccessException {
    Candidate current;
    do {
      current = candidates.get();
    } while (!satisfiesAll(current, counterexamples));
    return current;
  }
}
