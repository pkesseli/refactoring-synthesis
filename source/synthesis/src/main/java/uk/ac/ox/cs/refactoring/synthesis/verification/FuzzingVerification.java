package uk.ac.ox.cs.refactoring.synthesis.verification;

import java.util.HashMap;
import java.util.Map;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.TestClass;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.FuzzStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/**
 * Verification phase implemented using JQF fuzzing.
 */
public class FuzzingVerification<Candidate> {

  /**
   * Used to look up generators to construct counterexamples.
   */
  private final GeneratorRepository generatorRepository;

  /**
   * Used to try the a candidate against newly fuzzed inputs.
   */
  private final CandidateExecutor<Candidate> executor;

  /**
   * Used to invoke the spec against newly fuzzed inputs.
   */
  private final Invoker invoker;

  /**
   * @param generatorRepository {@link #generatorRepository}
   * @param executor            {@link #executor}
   * @param invoker             {@link #invoker}
   */
  public FuzzingVerification(final GeneratorRepository generatorRepository, final CandidateExecutor<Candidate> executor,
      final Invoker invoker) {
    this.generatorRepository = generatorRepository;
    this.executor = executor;
    this.invoker = invoker;
  }

  /**
   * Tries to find a new counterexample using fuzzing for which the given
   * candidate does not match the spec.
   * 
   * @param candidate Candidate to verify.
   * @return Newly found counterexamples. Synthesis will succeed if empty.
   */
  public Map<Counterexample, ExecutionResult> verify(final Candidate candidate) {
    final CandidateTest<Candidate> frameworkMethod = new CandidateTest<>(candidate, executor, invoker);
    final TestClass testClass = new TestClass(frameworkMethod.getDeclaringClass());
    final FuzzStatement fuzzStatement = new FuzzStatement(frameworkMethod, testClass, generatorRepository);
    final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();
    try {
      fuzzStatement.evaluate();
    } catch (final AssertionFailedError e) {
      storeCounterexample(counterexamples, e);
    } catch (final MultipleFailureException e) {
      for (final Throwable failure : e.getFailures()) {
        if (failure instanceof AssertionFailedError) {
          storeCounterexample(counterexamples, (AssertionFailedError) failure);
        } else {
          throw new IllegalStateException(e);
        }
      }
    } catch (final Throwable e) {
      throw new IllegalStateException(e);
    }
    return counterexamples;
  }

  /**
   * Extracts a counterexample from a fuzzing unit test exception and stores it.
   * 
   * @param counterexamples Collection of counterexamples in which to store the
   *                        new one.
   * @param e               {@link AssertionFailedError} wrapping the new
   *                        counterexample.
   */
  private static void storeCounterexample(final Map<Counterexample, ExecutionResult> counterexamples,
      final AssertionFailedError e) {
    final CounterexampleException cause = (CounterexampleException) e.getCause();
    counterexamples.put(cause.Counterexample, (ExecutionResult) e.getExpected().getEphemeralValue());
  }

}
