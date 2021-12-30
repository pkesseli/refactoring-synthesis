package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.InputStream;
import java.util.function.Consumer;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/** Adapter from {@link Guidance} to {@link CloseableGuidance}. */
class CloseableGuidanceAdapter implements CloseableGuidance {

  /** Adapted {@link Guidance}. */
  private final Guidance guidance;

  /** @param guidance {@link #guidance} */
  CloseableGuidanceAdapter(final Guidance guidance) {
    this.guidance = guidance;
  }

  @Override
  public Consumer<TraceEvent> generateCallBack(final Thread thread) {
    return guidance.generateCallBack(thread);
  }

  @Override
  public InputStream getInput() {
    return guidance.getInput();
  }

  @Override
  public void handleResult(final Result result, final Throwable error) {
    guidance.handleResult(result, error);
  }

  @Override
  public boolean hasInput() {
    return guidance.hasInput();
  }

  @Override
  public void close() {
  }
}
