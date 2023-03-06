package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Random;
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
   * @param phaseName {@link ZestGuidance#ZestGuidance(String, Duration, java.io.File)}
   * @param duratino  {@link ZestGuidance#ZestGuidance(String, Duration, java.io.File)}
   * @throws IOException {@link Path#toFile()}
   */
  CloseableZestGuidance(final String phaseName, final Duration timeout) throws IOException {
    final File seedInputFile;
    try {
      seedInputFile = Paths.get(CloseableZestGuidance.class.getResource("/seed/zest.bin").toURI()).toFile();
    } catch (final URISyntaxException e) {
      throw new IllegalStateException("Missing seed.", e);
    }
    guidance = new ZestGuidance(phaseName, timeout, outputDirectory.toFile(), new File[] { seedInputFile });
    Seeds.setSeed(guidance);
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
