package uk.ac.ox.cs.refactoring.synthesis.induction;

import java.io.InputStream;
import java.util.function.Consumer;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.GuidanceException;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/** Wrapper for {@link Guidance} terminating if a solution is found. */
public class SynthesisGuidance implements Guidance {

  /** Wrapped {@link Guidance}. */
  private final Guidance guidance;

  /** Indicates whether any valid solution was already found. */
  private boolean hasSolution = false;

  /** @param guidance {@link #guidance} */
  public SynthesisGuidance(final Guidance guidance) {
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

}
