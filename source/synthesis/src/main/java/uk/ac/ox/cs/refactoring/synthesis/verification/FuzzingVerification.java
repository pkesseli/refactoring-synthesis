package uk.ac.ox.cs.refactoring.synthesis.verification;

import java.util.HashMap;
import java.util.Map;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.TestClass;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.FuzzStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.guidance.CloseableGuidance;
import uk.ac.ox.cs.refactoring.synthesis.guidance.GuidanceFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/** Verification phase implemented using JQF fuzzing. */
public class FuzzingVerification<Candidate> {

  /** Used to look up generators to construct counterexamples. */
  private final GeneratorRepository generatorRepository;

  /** Used to try the a candidate against newly fuzzed inputs. */
  private final CandidateExecutor<Candidate> executor;

  /** Used to invoke the spec against newly fuzzed inputs. */
  private final Invoker invoker;

  /** Sink for counterexample events. */
  private final CegisLoopListener<Candidate> listener;

  /** Confiugration for guidance. */
  private final GeneratorConfiguration generatorConfiguration;

  /**
   * @param generatorRepository    {@link #generatorRepository}
   * @param executor               {@link #executor}
   * @param invoker                {@link #invoker}
   * @param listener               {@link #listener}
   * @param generatorConfiguration {@link #generatorConfiguration}
   */
  public FuzzingVerification(final GeneratorConfiguration generatorConfiguration,
      final GeneratorRepository generatorRepository, final CandidateExecutor<Candidate> executor,
      final Invoker invoker, final CegisLoopListener<Candidate> listener) {
    this.generatorConfiguration = generatorConfiguration;
    this.generatorRepository = generatorRepository;
    this.executor = executor;
    this.invoker = invoker;
    this.listener = listener;
  }

  /**
   * Tries to find a new counterexample using fuzzing for which the given
   * candidate does not match the spec.
   * 
   * @param candidate Candidate to verify.
   * @return Newly found counterexamples. Synthesis will succeed if empty.
   */
  public Map<Counterexample, ExecutionResult> verify(final Candidate candidate) {
    final CandidateTest<Candidate> frameworkMethod = new CandidateTest<>(candidate, executor, invoker, listener);
    final TestClass testClass = new TestClass(frameworkMethod.getDeclaringClass());
    final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();
    fuzz(frameworkMethod, testClass, counterexamples, generatorConfiguration.Stage1MaxCounterexamples,
        generatorConfiguration.Stage1MaxInputs);
    if (counterexamples.isEmpty())
      fuzz(frameworkMethod, testClass, counterexamples, generatorConfiguration.Stage2MaxCounterexamples,
          generatorConfiguration.Stage2MaxInputs);

    if (counterexamples.isEmpty()) {
      listener.verified(candidate);
    }
    return counterexamples;
  }

  /**
   * 
   * @param frameworkMethod
   * @param testClass
   * @param counterexamples
   * @param maximumNumberOfCounterexamples
   * @param maximumNumberOfInputs
   */
  private void fuzz(final CandidateTest<Candidate> frameworkMethod, final TestClass testClass,
      final Map<Counterexample, ExecutionResult> counterexamples, final long maximumNumberOfCounterexamples,
      final long maximumNumberOfInputs) {
    try (final CloseableGuidance guidance = GuidanceFactory.verification(generatorConfiguration,
        maximumNumberOfCounterexamples,
        maximumNumberOfInputs)) {
      final FuzzStatement fuzzStatement = new FuzzStatement(frameworkMethod, testClass, generatorRepository,
          guidance);
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
