package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.runners.model.FrameworkMethod;
import org.opentest4j.AssertionFailedError;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.HeapComparison;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/**
 * 
 */
public class CandidateSynthesis<Candidate> extends FrameworkMethod {

  /**
   * 
   */
  private final Map<Counterexample, ExecutionResult> counterexamples;

  /**
   * 
   */
  private final CandidateExecutor<Candidate> executor;

  /**
   * 
   */
  private final Invoker invoker;

  /**
   * 
   * @param counterexamples
   * @param executor
   * @param invoker
   * @param method
   */
  public CandidateSynthesis(final Map<Counterexample, ExecutionResult> counterexamples,
      final CandidateExecutor<Candidate> executor, final Invoker invoker, final Method method) {
    super(method);
    this.executor = executor;
    this.invoker = invoker;
    this.counterexamples = counterexamples;
  }

  @Override
  public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
    @SuppressWarnings("unchecked")
    final Candidate candidate = (Candidate) target;
    for (final Map.Entry<Counterexample, ExecutionResult> counterexample : counterexamples.entrySet()) {
      final Counterexample input = counterexample.getKey();
      final ExecutionResult expected = invoker.invoke(input);
      final ExecutionResult actual = executor.execute(candidate, input);
      if (!HeapComparison.equals(expected, actual)) {
        return null;
      }
    }
    throw new AssertionFailedError("", null, candidate);
  }

}
