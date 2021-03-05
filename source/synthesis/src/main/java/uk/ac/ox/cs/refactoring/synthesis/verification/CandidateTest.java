package uk.ac.ox.cs.refactoring.synthesis.verification;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.HeapComparison;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/**
 * 
 */
public class CandidateTest<Candidate> extends FrameworkMethod {

  /**
   * 
   */
  private final Candidate candidate;

  /**
   * 
   */
  private final CandidateExecutor<Candidate> executor;

  /**
   * 
   */
  private final Invoker invoker;

  /**
   * @param candidate {@link #candidate}
   * @param executor  {@link #executor}
   * @param invoker   {@link #invoker}
   */
  public CandidateTest(final Candidate candidate, final CandidateExecutor<Candidate> executor, final Invoker invoker) {
    super(getPlaceholder(null));
    this.candidate = candidate;
    this.executor = executor;
    this.invoker = invoker;
  }

  @Override
  public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
    final Counterexample counterexample = (Counterexample) params[0];
    final ExecutionResult expected = invoker.invoke(counterexample);
    final ExecutionResult actual = executor.execute(candidate, counterexample);
    if (!HeapComparison.equals(expected, actual)) {
      throw new AssertionFailedError("", expected, actual, new CounterexampleException(counterexample));
    }
    return null;
  }

  /**
   * 
   * @return
   */
  @Fuzz
  private static Method getPlaceholder(final Counterexample counterexample) {
    try {
      return CandidateTest.class.getDeclaredMethod("getPlaceholder", Counterexample.class);
    } catch (final NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(e);
    }
  }
}
