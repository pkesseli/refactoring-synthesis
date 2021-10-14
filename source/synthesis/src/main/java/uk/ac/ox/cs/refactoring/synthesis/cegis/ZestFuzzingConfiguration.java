package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;

import edu.berkeley.cs.jqf.fuzz.ei.ZestGuidance;
import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;

/**
 * {@link FuzzingConfiguration} aimed at Zest type configurations, which require
 * a temporary directory to be deleted on {@link #close()}.
 */
public class ZestFuzzingConfiguration implements AutoCloseable {

  /** Zest output path. */
  private final Path outputDirectory = Files.createTempDirectory("zest");

  /** Wrapped {@link FuzzingConfiguration} */
  private final FuzzingConfiguration fuzzingConfiguration;

  /**
   * Equivalent to {@link #ZestFuzzingConfiguration(String, Function)} without
   * modifying the default zest guidance.
   * 
   * @param phaseName {@link #ZestFuzzingConfiguration(String, Function)}
   * @throws NoSuchMethodException     {@link #ZestFuzzingConfiguration(String, Function)}
   * @throws IllegalAccessException    {@link #ZestFuzzingConfiguration(String, Function)}
   * @throws InvocationTargetException {@link #ZestFuzzingConfiguration(String, Function)}
   * @throws IOException               {@link #ZestFuzzingConfiguration(String, Function)}
   */
  public ZestFuzzingConfiguration(final String phaseName)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
    this(phaseName, Function.identity());
  }

  /**
   * Creates a {@link FuzzingConfiguration} using a {@link ZestGuidance}, but
   * extends it by applying {@code modifyZestGuidance}.
   * 
   * @param phaseName          Name of the fuzzing phase, usually `synthesis` or
   *                           `verification`.
   * @param modifyZestGuidance Allows further configuring the
   *                           {@link ZestGuidance}.
   * @throws NoSuchMethodException     {@llink FuzzingConfiguration}
   * @throws IllegalAccessException    {@llink FuzzingConfiguration}
   * @throws InvocationTargetException {@llink FuzzingConfiguration}
   * @throws IOException               if the temporary zest directory could not
   *                                   be created.
   */
  public ZestFuzzingConfiguration(final String phaseName,
      final Function<Guidance, ? extends Guidance> modifyZestGuidance)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
    final ZestGuidance zestGuidance = new ZestGuidance(phaseName, null, outputDirectory.toFile());
    fuzzingConfiguration = new FuzzingConfiguration(modifyZestGuidance.apply(zestGuidance));
  }

  @Override
  public void close() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
    fuzzingConfiguration.close();
    FileUtils.deleteDirectory(outputDirectory.toFile());
  }

}
