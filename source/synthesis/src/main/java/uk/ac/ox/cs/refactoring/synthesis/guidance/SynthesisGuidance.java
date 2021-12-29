package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.GuidanceException;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/** Wrapper for {@link Guidance} terminating if a solution is found. */
class SynthesisGuidance implements CloseableGuidance {

  /** Wrapped {@link Guidance}. */
  private final CloseableGuidance guidance;

  /** Indicates whether any valid solution was already found. */
  private boolean hasSolution = false;

  /** @param guidance {@link #guidance} */
  SynthesisGuidance(final CloseableGuidance guidance) {
    this.guidance = guidance;
  }

  @Override
  public InputStream getInput() throws IllegalStateException, GuidanceException {
    return guidance.getInput();
  }

  @Override
  public boolean hasInput() {
    return !hasSolution && guidance.hasInput();
  }

  @Override
  public void handleResult(final Result result, final Throwable error) throws GuidanceException {
    guidance.handleResult(result, error);
    hasSolution |= Result.FAILURE == result;
  }

  @Override
  public Consumer<TraceEvent> generateCallBack(final Thread thread) {
    return guidance.generateCallBack(thread);
  }

  @Override
  public void close() throws IOException {
    guidance.close();
  }
}
