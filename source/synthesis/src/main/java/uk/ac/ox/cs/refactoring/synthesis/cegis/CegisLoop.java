package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidateGenerator;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Parameter;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.CounterexampleGenerator;
import uk.ac.ox.cs.refactoring.synthesis.induction.FuzzingSynthesis;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invoker;
import uk.ac.ox.cs.refactoring.synthesis.verification.FuzzingVerification;

/**
 * 
 */
public class CegisLoop<Candidate> {

  /**
   * 
   */
  private final FuzzingSynthesis<Candidate> synthesis;

  /**
   * 
   */
  private final FuzzingVerification<Candidate> verification;

  /**
   * 
   */
  private final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();

  /**
   * @param executor
   * @param invoker
   */
  public CegisLoop(final CandidateExecutor<Candidate> executor, final Invoker invoker,
      final List<Class<?>> parameterTypes, final Class<Candidate> candidateType) {
    final SourceOfRandomness sourceOfRandomness = new SourceOfRandomness(new Random());
    final GeneratorRepository generatorRepository = new GeneratorRepository(sourceOfRandomness)
        .register(new ServiceLoaderGeneratorSource());
    generatorRepository.register(new CounterexampleGenerator(generatorRepository, parameterTypes));
    final List<Parameter> parameters = new ArrayList<>();
    for (int i = 0; i < parameterTypes.size(); ++i) {
      final Class<?> parameterType = parameterTypes.get(i);
      parameters.add(new Parameter(i, TypeFactory.create(parameterType)));
    }
    generatorRepository.register(new SnippetCandidateGenerator(null, parameters, Collections.emptyList()));
    final Method fuzzingSynthesisFrameworkMethod = SnippetCandidateGenerator.getFrameworkMethodPlaceholder(null);

    synthesis = new FuzzingSynthesis<>(generatorRepository, sourceOfRandomness, candidateType,
        fuzzingSynthesisFrameworkMethod, executor, invoker);
    verification = new FuzzingVerification<>(generatorRepository, executor, invoker);
  }

  /**
   * @return
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   */
  public Candidate synthesise() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
    Candidate candidate = synthesis.getDefault();
    while (needsRefinement(candidate)) {
      candidate = synthesis.synthesise(counterexamples);
    }
    return candidate;
  }

  /**
   * @param candidate
   * @return
   */
  private boolean needsRefinement(final Candidate candidate) {
    final Map<Counterexample, ExecutionResult> newCounterexamples = verification.verify(candidate);
    counterexamples.putAll(newCounterexamples);
    return !newCounterexamples.isEmpty();
  }
}
