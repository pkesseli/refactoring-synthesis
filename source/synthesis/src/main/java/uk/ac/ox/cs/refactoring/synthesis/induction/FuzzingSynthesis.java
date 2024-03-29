package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.TestClass;
import org.opentest4j.AssertionFailedError;

import edu.berkeley.cs.jqf.fuzz.guidance.GuidanceException;
import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.FuzzStatement;
import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.NonTrackingGenerationStatus;
import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.cegis.CegisLoopListener;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.guidance.CloseableGuidance;
import uk.ac.ox.cs.refactoring.synthesis.guidance.GuidanceFactory;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

/** Synthesis phase implemented using JQF fuzzing. */
public class FuzzingSynthesis<Candidate> {

  /** Sink for logging candidates. */
  protected final CegisLoopListener<Candidate> listener;

  /** Used to look up JQF generators necessary to construct candidates. */
  private final GeneratorRepository generatorRepository;

  /** Fuzzer's random input stram, used to construct a default candidate. */
  private final SourceOfRandomness sourceOfRandomness;

  /** Explicit candidate type, used to construct a default candidate using JQF. */
  private final Class<Candidate> candidateType;

  /** Confiugration for guidance. */
  protected final GeneratorConfiguration generatorConfiguration;

  /**
   * JUnit method instance which describes the API to invoke using fuzzed inputs.
   * The actual execution is intercepted and performed in
   * {@link CandidateSynthesis}, so this method only exists to communicate the
   * correct signature to JQF.
   */
  private final Method frameworkMethodPlaceholder;

  /** Used to try candidates against counterexamples. */
  private final CandidateExecutor<Candidate> executor;

  /**
   * @param generatorConfiguration     {@link #generatorConfiguration}
   * @param generatorRepository        {@link #generatorRepository}
   * @param sourceOfRandomness         {@link #sourceOfRandomness}
   * @param candidateType              {@link #candidateType}
   * @param frameworkMethodPlaceholder {@link #frameworkMethodPlaceholder}
   * @param executor                   {@link #executor}
   * @param listener                   {@link #listener}
   */
  public FuzzingSynthesis(final GeneratorConfiguration generatorConfiguration,
      final GeneratorRepository generatorRepository, final SourceOfRandomness sourceOfRandomness,
      final Class<Candidate> candidateType, final Method frameworkMethodPlaceholder,
      final CandidateExecutor<Candidate> executor, final CegisLoopListener<Candidate> listener) {
    this.generatorConfiguration = generatorConfiguration;
    this.generatorRepository = generatorRepository;
    this.sourceOfRandomness = sourceOfRandomness;
    this.candidateType = candidateType;
    this.frameworkMethodPlaceholder = frameworkMethodPlaceholder;
    this.executor = executor;
    this.listener = listener;
  }

  /** Provides a default candidate to use in the absence of counterexamples. */
  public Candidate getDefault() {
    final Candidate initial = generatorRepository.type(candidateType).generate(sourceOfRandomness,
        new NonTrackingGenerationStatus(sourceOfRandomness));
    listener.initial(initial);
    return initial;
  }

  /**
   * Performs CEGIS' inductive synthesis phase using JQF fuzzing.
   * 
   * @param counterexamples Specification a new candidate must satisfy.
   * @return Candidate satisfying all counterexamples, if available.
   * @throws IOException            if fuzzing guidance file I/O fails.
   * @throws IllegalAccessException if a necessary method could not be invoked
   *                                reflectively.
   * @throws ClassNotFoundException if a class in the execution context could not
   *                                be found.
   * @throws NoSuchElementException if no candidate could be found using the
   *                                allotted time and fuzzing trials.
   * @throws NoSuchFieldException   if a field could not be set reflectively
   *                                during state initialisation.
   */
  public Candidate synthesise(final Map<Counterexample, ExecutionResult> counterexamples)
      throws ClassNotFoundException, IOException, IllegalAccessException, NoSuchElementException, NoSuchFieldException {
    final CandidateSynthesis<Candidate> frameworkMethod = new CandidateSynthesis<>(listener, counterexamples, executor,
        frameworkMethodPlaceholder);
    final TestClass testClass = new TestClass(frameworkMethodPlaceholder.getDeclaringClass());

    try (final CloseableGuidance guidance = GuidanceFactory.synthesis(generatorConfiguration)) {
      final FuzzStatement fuzzStatement = new FuzzStatement(frameworkMethod, testClass, generatorRepository,
          guidance);
      fuzzStatement.evaluate();
    } catch (final MultipleFailureException e) {
      return e.getFailures().stream().map(AssertionFailedError.class::cast).map(this::getCandidate).findAny().get();
    } catch (final AssertionFailedError e) {
      return getCandidate(e);
    } catch (final GuidanceException e) {
      throw new NoSuchElementException(e);
    } catch (final Throwable e) {
      throw new IllegalStateException(e);
    }
    throw new NoSuchElementException();
  }

  /**
   * Extracts a candidate out of an assertion violation.
   * 
   * @param e Assertion violation containing candidate.
   * @return Extracted candidate.
   */
  private Candidate getCandidate(final AssertionFailedError e) {
    final Object result = e.getActual().getEphemeralValue();
    @SuppressWarnings("unchecked")
    final Candidate candidate = (Candidate) result;
    return candidate;
  }
}
