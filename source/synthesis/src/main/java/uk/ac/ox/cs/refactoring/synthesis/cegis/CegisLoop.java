package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateGenerator;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.This;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
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

  /**
   * @param executor       {@link CandidateExecutor} used to try candidates
   *                       against inputs.
   * @param invoker        {@link Invoker} used to run the original method to be
   *                       replaced. Effectively models the specification of the
   *                       synthesis.
   * @param resultType     Expected output type of the synthesised program.
   * @param instanceType   Type of the {@code this} argument that both
   *                       {@code executor} and {@code invoker} accept.
   * @param parameterTypes Types of parameters that both {@code executor} and
   *                       {@code invoker} accept.
   * @param candidateType  Used to configure JQF candidate generators.
   */
  public CegisLoop(final CandidateExecutor<Candidate> executor, final Invoker invoker, final Class<?> resultType,
      final Class<?> instanceType, final List<Class<?>> parameterTypes, final Iterable<Method> methods,
      final Class<Candidate> candidateType) {
    final SourceOfRandomness sourceOfRandomness = new SourceOfRandomness(new Random());
    final GeneratorRepository generatorRepository = new GeneratorRepository(sourceOfRandomness)
        .register(new ServiceLoaderGeneratorSource());
    generatorRepository.register(new CounterexampleGenerator(generatorRepository, instanceType, parameterTypes));
    final This instance = This.create(TypeFactory.create(instanceType));
    final List<Parameter> parameters = new ArrayList<>();
    for (int i = 0; i < parameterTypes.size(); ++i) {
      final Class<?> parameterType = parameterTypes.get(i);
      parameters.add(new Parameter(i, TypeFactory.create(parameterType)));
    }
    generatorRepository
        .register(new SnippetCandidateGenerator(instance, resultType, parameters, Collections.emptyList(), methods));
    final Method fuzzingSynthesisFrameworkMethod = SnippetCandidateGenerator.TestClass
        .getFrameworkMethodPlaceholder(null);

    synthesis = new FuzzingSynthesis<>(generatorRepository, sourceOfRandomness, candidateType,
        fuzzingSynthesisFrameworkMethod, executor);
    verification = new FuzzingVerification<>(generatorRepository, executor, invoker);
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
