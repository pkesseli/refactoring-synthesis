package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.runners.model.FrameworkMethod;
import org.opentest4j.AssertionFailedError;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.HeapComparison;

/** Simulates a JUnit test method that JQF can fuzz to generate candidates. */
class CandidateSynthesis<Candidate> extends FrameworkMethod {

  /** Sink for candidate logging. */
  private final CandidateListener<Candidate> listener;

  /** Counterexamples candidates must satisfy. */
  private final Map<Counterexample, ExecutionResult> counterexamples;

  /** Executor to run a candidate against a counterexample. */
  private final CandidateExecutor<Candidate> executor;

  /**
   * @param listener        {@link #listener}
   * @param counterexamples {@link #counterexamples}
   * @param executor        {@link #executor}
   * @param method          {@link FrameworkMethod#FrameworkMethod(Method)}
   */
  CandidateSynthesis(final CandidateListener<Candidate> listener,
      final Map<Counterexample, ExecutionResult> counterexamples,
      final CandidateExecutor<Candidate> executor, final Method method) {
    super(method);
    this.listener = listener;
    this.executor = executor;
    this.counterexamples = counterexamples;
  }

  @Override
  public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
    @SuppressWarnings("unchecked")
    final Candidate candidate = (Candidate) params[0];
    for (final Map.Entry<Counterexample, ExecutionResult> counterexample : counterexamples.entrySet()) {
      final Counterexample input = counterexample.getKey();
      final ExecutionResult expected = counterexample.getValue();
      final ExecutionResult actual = executor.execute(candidate, input);
      if (!HeapComparison.equals(expected, actual)) {
        listener.spurious(candidate);
        return null;
      }
    }
    listener.genuine(candidate);
    throw new AssertionFailedError("", null, candidate);
  }
}
