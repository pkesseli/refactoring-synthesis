package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateGenerator;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.CounterexampleGenerator;
import uk.ac.ox.cs.refactoring.synthesis.induction.FuzzingSynthesis;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.verification.FuzzingVerification;

/**
 * Implements a CEGIS loop based on JQF fuzzing for a configurable candidate
 * type.
 */
public class CegisLoop<Candidate> {

  /**
   * Inductive synthesis using fuzzing.
   */
  private final FuzzingSynthesis<Candidate> synthesis;

  /**
   * Verification using fuzzing.
   */
  private final FuzzingVerification<Candidate> verification;

  /**
   * All counterexamples to satisfy, including expeced output values.
   */
  private final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();

  static {
    System.setProperty("jqf.ei.MAX_INPUT_SIZE", "102400");
    System.setProperty("jqf.ei.QUIET_MODE", Boolean.toString(true));
  }

  /**
   * @param executor               {@link CandidateExecutor} used to try
   *                               candidates against inputs.
   * @param invoker                {@link Invoker} used to run the original method
   *                               to be replaced. Effectively models the
   *                               specification of the synthesis.
   * @param generatorConfiguration Search configuration.
   * @param candidateType          Used to configure JQF candidate generators.
   * @param listener               Listener for synthesis and verification events.
   */
  public CegisLoop(final CandidateExecutor<Candidate> executor, final Invoker invoker,
      final GeneratorConfiguration generatorConfiguration, final Class<Candidate> candidateType,
      final CegisLoopListener<Candidate> listener) {
    final SourceOfRandomness sourceOfRandomness = new SourceOfRandomness(new Random());
    final GeneratorRepository baseRepository = new GeneratorRepository(sourceOfRandomness)
        .register(new ObjectGenerator())
        .register(new ServiceLoaderGeneratorSource())
        .register(new ClassGenerator())
        .register(new MethodHandlesLookupGenerator())
        .register(new OperatingSystemMXBeanGenerator());
    baseRepository.register(new RuntimeVersionGenerator(baseRepository));
    final GeneratorRepository verificationRepository = new GeneratorRepository(sourceOfRandomness)
        .register(new ServiceLoaderGeneratorSource())
        .register(new CounterexampleGenerator(baseRepository, generatorConfiguration.InstanceType,
            generatorConfiguration.ParameterTypes));
    final GeneratorRepository synthesisRepository = new GeneratorRepository(sourceOfRandomness)
        .register(new ServiceLoaderGeneratorSource())
        .register(new SnippetCandidateGenerator(generatorConfiguration));

    final Method fuzzingSynthesisFrameworkMethod = SnippetCandidateGenerator.TestClass
        .getFrameworkMethodPlaceholder(null);

    synthesis = new FuzzingSynthesis<>(generatorConfiguration, synthesisRepository, sourceOfRandomness, candidateType,
        fuzzingSynthesisFrameworkMethod, executor, listener);
    verification = new FuzzingVerification<>(generatorConfiguration, verificationRepository, executor, invoker,
        listener);
  }

  /**
   * Performs the actual CEGIS loop.
   * 
   * @return Candidate satisfying the given spec.
   * @throws IOException            if fuzzing guidance file I/O fails.
   * @throws ClassNotFoundException if a necessary class could not be looked up.
   * @throws IllegalAccessException if a reflective execution fails.
   * @throws NoSuchFieldException   if a reflective field assignment fails.
   * @throws NoSuchElementException if no candidate could be found in the alloted
   *                                fuzzing time and trials.
   */
  public Candidate synthesise()
      throws ClassNotFoundException, IOException, IllegalAccessException, NoSuchFieldException, NoSuchElementException {
    Candidate candidate = synthesis.getDefault();
    while (needsRefinement(candidate)) {
      candidate = synthesis.synthesise(counterexamples);
    }
    return candidate;
  }

  /**
   * Indiciates whether the candidate needs to be refined.
   * 
   * @param candidate {@code Candidate} to check.
   * @return {@code true} if verification found additional counterexamples,
   *         {@code false} otherwise.
   */
  private boolean needsRefinement(final Candidate candidate) {
    final Map<Counterexample, ExecutionResult> newCounterexamples = verification.verify(candidate);
    counterexamples.putAll(newCounterexamples);
    return !newCounterexamples.isEmpty();
  }
}
