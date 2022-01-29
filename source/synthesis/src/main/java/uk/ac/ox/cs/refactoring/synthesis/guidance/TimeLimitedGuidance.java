package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.GuidanceException;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/** Adds a time limit to a {@link Guidance}. */
class TimeLimitedGuidance implements Guidance {

  /** Time limit. */
  private final Duration timeout;

  /** Extended guidance. */
  private final Guidance wrapped;

  /** Point in time the first input was received. */
  private Instant start;

  /** @param wrapped {@link #wrapped} */
  TimeLimitedGuidance(final Duration timeout, final Guidance wrapped) {
    this.timeout = timeout;
    this.wrapped = wrapped;
  }

  @Override
  public InputStream getInput() throws IllegalStateException, GuidanceException {
    return wrapped.getInput();
  }

  @Override
  public boolean hasInput() {
    final Instant now = Instant.now();
    if (start == null)
      start = now;
    return Duration.between(start, now).compareTo(timeout) < 0 && wrapped.hasInput();
  }

  @Override
  public void handleResult(final Result result, final Throwable error) throws GuidanceException {
    wrapped.handleResult(result, error);
  }

  @Override
  public Consumer<TraceEvent> generateCallBack(final Thread thread) {
    return wrapped.generateCallBack(thread);
  }
}
