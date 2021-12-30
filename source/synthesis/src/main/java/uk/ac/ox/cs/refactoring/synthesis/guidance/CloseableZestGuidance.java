package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;

import edu.berkeley.cs.jqf.fuzz.ei.ZestGuidance;
import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.guidance.Result;
import edu.berkeley.cs.jqf.instrument.tracing.events.TraceEvent;

/**
 * Creates a {@link ZestGuidance} in a temporary directory which is
 * automatically disposed.
 */
class CloseableZestGuidance implements CloseableGuidance {

  /** Zest output path. */
  private final Path outputDirectory = Files.createTempDirectory("zest");

  /** Adapted Zest guidance. */
  private final Guidance guidance;

  /**
   * @param phaseName {@link ZestGuidance}
   * @throws IOException {@link Path#toFile()}
   */
  CloseableZestGuidance(final String phaseName) throws IOException {
    guidance = new ZestGuidance(phaseName, null, outputDirectory.toFile());
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
  public void close() throws IOException {
    FileUtils.deleteDirectory(outputDirectory.toFile());
  }
}
