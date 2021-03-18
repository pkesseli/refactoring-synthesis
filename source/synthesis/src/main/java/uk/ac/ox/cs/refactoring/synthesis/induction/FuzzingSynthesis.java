package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.junit.runners.model.TestClass;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.FuzzStatement;
import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.NonTrackingGenerationStatus;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;

/**
 * Explores candidates in the order provided by a {@link Supplier}.
 */
public class FuzzingSynthesis<Candidate> {

  /**
   * 
   */
  private final GeneratorRepository generatorRepository;

  /**
   * 
   */
  private final SourceOfRandomness sourceOfRandomness;

  /**
   * 
   */
  private final Class<Candidate> candidateType;

  /**
   * 
   */
  private final Method frameworkMethodPlaceholder;

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
   * @param generatorRepository
   * @param sourceOfRandomness
   * @param candidateType
   * @param frameworkMethodPlaceholder
   * @param executor                   {@link #executor}
   * @param invoker                    {@link #invoker}
   */
  public FuzzingSynthesis(final GeneratorRepository generatorRepository, final SourceOfRandomness sourceOfRandomness,
      final Class<Candidate> candidateType, final Method frameworkMethodPlaceholder,
      final CandidateExecutor<Candidate> executor, final Invoker invoker) {
    this.generatorRepository = generatorRepository;
    this.sourceOfRandomness = sourceOfRandomness;
    this.candidateType = candidateType;
    this.frameworkMethodPlaceholder = frameworkMethodPlaceholder;
    this.executor = executor;
    this.invoker = invoker;
  }

  /**
   * 
   */
  public Candidate getDefault() {
    return generatorRepository.type(candidateType).generate(sourceOfRandomness,
        new NonTrackingGenerationStatus(sourceOfRandomness));
  }

  /**
   * 
   * @param counterexamples
   * @return
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   */
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
    final CandidateSynthesis<Candidate> frameworkMethod = new CandidateSynthesis<>(counterexamples, executor, invoker,
        frameworkMethodPlaceholder);
    final TestClass testClass = new TestClass(Object.class);
    final FuzzStatement fuzzStatement = new FuzzStatement(frameworkMethod, testClass, generatorRepository);
    try {
      fuzzStatement.evaluate();
    } catch (final AssertionFailedError e) {
      final Object result = e.getActual().getEphemeralValue();
      @SuppressWarnings("unchecked")
      final Candidate candidate = (Candidate) result;
      return candidate;
    } catch (final Throwable e) {
      throw new IllegalStateException(e);
    }
    throw new NoSuchElementException();
  }
}
