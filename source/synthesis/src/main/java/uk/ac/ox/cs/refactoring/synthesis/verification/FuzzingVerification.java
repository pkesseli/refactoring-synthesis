package uk.ac.ox.cs.refactoring.synthesis.verification;

import java.util.HashMap;
import java.util.Map;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;

import org.junit.runners.model.TestClass;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.FuzzStatement;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/**
 * 
 */
public class FuzzingVerification<Candidate> {

  /**
   * 
   */
  private final GeneratorRepository generatorRepository;

  /**
   * 
   */
  private final CandidateExecutor<Candidate> executor;

  /**
   * 
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
   * 
   */
  public Map<Counterexample, ExecutionResult> verify(final Candidate candidate) {
    final CandidateTest<Candidate> frameworkMethod = new CandidateTest<>(candidate, executor, invoker);
    final TestClass testClass = new TestClass(Object.class);
    final FuzzStatement fuzzStatement = new FuzzStatement(frameworkMethod, testClass, generatorRepository);
    final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();
    try {
      fuzzStatement.evaluate();
    } catch (final AssertionFailedError e) {
      final CounterexampleException cause = (CounterexampleException) e.getCause();
      counterexamples.put(cause.Counterexample, (ExecutionResult) e.getExpected().getEphemeralValue());
    } catch (final Throwable e) {
      throw new IllegalStateException(e);
    }
    return counterexamples;
  }
}
