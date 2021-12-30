package uk.ac.ox.cs.refactoring.synthesis.verification;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.HeapComparison;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/** Simulates a JUnit test method that JQF can fuzz to generate candidates. */
public class CandidateTest<Candidate> extends FrameworkMethod {

  /** Sink for counterexample events. */
  private final CegisLoopListener<Candidate> listener;

  /** Candidate to verify. */
  private final Candidate candidate;

  /** Executor to try the candidate against different inputs. */
  private final CandidateExecutor<Candidate> executor;

  /**
   * Invoker to run inputs against the original specification, finding new
   * counterexamples where the spec and candidate differ.
   */
  private final Invoker invoker;

  /**
   * @param candidate {@link #candidate}
   * @param executor  {@link #executor}
   * @param invoker   {@link #invoker}
   * @param listener  {@link #listener}
   */
  public CandidateTest(final Candidate candidate, final CandidateExecutor<Candidate> executor, final Invoker invoker,
      final CegisLoopListener<Candidate> listener) {
    super(TestClass.getPlaceholder(null));
    this.candidate = candidate;
    this.executor = executor;
    this.invoker = invoker;
    this.listener = listener;
  }

  @Override
  public Object invokeExplosively(final Object target, final Object... params) throws Exception {
    final Counterexample counterexample = (Counterexample) params[0];
    final ExecutionResult expected = invoker.invoke(counterexample);
    final ExecutionResult actual = executor.execute(candidate, counterexample);
    if (!HeapComparison.equals(expected, actual)) {
      listener.genuine(counterexample, expected, actual);
      throw new AssertionFailedError("", expected, actual, new CounterexampleException(counterexample));
    }
    listener.spurious(counterexample);
    return null;
  }

  /**
   * Describes a test class and method that can be passed to JQF to convey the
   * method signature used for fuzzgin.
   */
  public static class TestClass {
    /**
     * Returns a reflective reference to itself. Used solely to convey the correct
     * method signature to JQF.
     * 
     * @param counterexample Parameter which JQF will be fuzzing.
     * @return {@code getPlaceholder}
     */
    @Fuzz
    private static Method getPlaceholder(final Counterexample counterexample) {
      try {
        return TestClass.class.getDeclaredMethod("getPlaceholder", Counterexample.class);
      } catch (final NoSuchMethodException | SecurityException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
