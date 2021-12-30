package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.GuidanceException;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/**
 * Wraps any guidance, enforcing a maximum number of errors, i.e.
 * counterexamples.
 */
class VerificationGuidance implements CloseableGuidance {

  /** Wrapped {@link Guidance}. */
  private final CloseableGuidance guidance;

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
  VerificationGuidance(final CloseableGuidance guidance, final long maximumNumberOfCounterexamples,
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

  @Override
  public void close() throws IOException {
    guidance.close();
  }
}
