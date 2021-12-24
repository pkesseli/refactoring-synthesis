package uk.ac.ox.cs.refactoring.synthesis.verification;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.GuidanceException;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/**
 * Wraps any guidance, enforcing a maximum number of errors, i.e.
 * counterexamples.
 */
class VerificationGuidance implements Guidance {

  /**
   * Produces a guidance decorator factory, modifying a given {@link Guidance}'s
   * termination condition with a maximum number of counterexamples.
   * 
   * @param maximumNumberOfCounterexamples {@link #maximumNumberOfCounterexamples}
   * @param maximumNumberOfInputs          {@link #maximumNumberOfInputs}
   * @return {@link Function} modifying a guidance, enforcing a maximum number of
   *         errors.
   */
  public static Function<Guidance, ? extends Guidance> createFactory(final long maximumNumberOfCounterexamples,
      final long maximumNumberOfInputs) {
    return guidance -> new VerificationGuidance(guidance, maximumNumberOfCounterexamples, maximumNumberOfInputs);
  }

  /** Wrapped {@link Guidance}. */
  private final Guidance guidance;

  /** Desired number of counterexamples. */
  private final long maximumNumberOfCounterexamples;

  /** Maximum number of inputs to try. */
  private final long maximumNumberOfInputs;

  /** Number of found counterexamples. */
  private long numberOfCounterexamples;

  /** Number of inputs tried to produce counterexamples. */
  private long numberOfInputs;

  /**
   * @param guidance                       {@link #guidance}
   * @param maximumNumberOfCounterexamples {@link #numberOfCounterexamples}
   * @param maximumNumberOfInputs          {@link #maximumNumberOfInputs}
   */
  private VerificationGuidance(final Guidance guidance, final long maximumNumberOfCounterexamples,
      final long maximumNumberOfInputs) {
    this.guidance = guidance;
    this.maximumNumberOfCounterexamples = maximumNumberOfCounterexamples;
    this.maximumNumberOfInputs = maximumNumberOfInputs;
  }

  @Override
  public InputStream getInput() throws IllegalStateException, GuidanceException {
    return guidance.getInput();
  }

  @Override
  public boolean hasInput() {
    return numberOfInputs < maximumNumberOfInputs && numberOfCounterexamples < maximumNumberOfCounterexamples
        && guidance.hasInput();
  }

  @Override
  public void handleResult(Result result, Throwable error) throws GuidanceException {
    guidance.handleResult(result, error);
    ++numberOfInputs;
    if (Result.FAILURE == result)
      ++numberOfCounterexamples;
  }

  @Override
  public Consumer<TraceEvent> generateCallBack(Thread thread) {
    return guidance.generateCallBack(thread);
  }
}
