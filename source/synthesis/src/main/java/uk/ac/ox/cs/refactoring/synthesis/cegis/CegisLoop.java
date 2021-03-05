package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.api.CandidateExecutor;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
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
  private final Map<Counterexample, ExecutionResult> counterexamples = new HashMap<>();

  /**
   * 
   */
  private final FuzzingVerification<Candidate> verification;

  /**
   * @param executor
   * @param invoker
   */
  public CegisLoop(final CandidateExecutor<Candidate> executor, final Invoker invoker) {
    final GeneratorRepository generatorRepository = new GeneratorRepository(new SourceOfRandomness(new Random()))
        .register(new ServiceLoaderGeneratorSource());
    verification = new FuzzingVerification<Candidate>(generatorRepository, executor, invoker);
  }

  /**
   * @return
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   */
  public Candidate synthesise() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
    final FuzzingSynthesis<Candidate> synthesis = new FuzzingSynthesis<Candidate>(null, null);
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
