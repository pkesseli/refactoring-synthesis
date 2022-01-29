package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.IOException;
import java.time.Duration;

import edu.berkeley.cs.jqf.fuzz.ei.ZestGuidance;
import edu.berkeley.cs.jqf.fuzz.random.NoGuidance;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfiguration;

/** Helper to create configured synthesis and verification guidances. */
public final class GuidanceFactory {

  /**
   * Provides synthesis guidance configuration.
   * 
   * @param generatorConfiguration {@link #getBaseGuidance(GeneratorConfiguration, String)}
   * @return Configured synthesis JQF guidance.
   * @throws IOException if Zest guidance could not be initialised.
   */
  public static CloseableGuidance synthesis(final GeneratorConfiguration generatorConfiguration) throws IOException {
    return new SynthesisGuidance(getBaseGuidance(generatorConfiguration, "inductive synthesis", Duration.ofMinutes(2)));
  }

  /**
   * Provides verification guidance configuration.
   * 
   * @param maximumNumberOfCounterexamples {@link VerificationGuidance#VerificationGuidance(CloseableGuidance, long, long)}
   * @param maximumNumberOfInputs          {@link VerificationGuidance#VerificationGuidance(CloseableGuidance, long, long)}
   * @return Configured {@link VerificationGuidance}.
   * @throws IOException if Zest guidance could not be initialised.
   */
  public static CloseableGuidance verification(final GeneratorConfiguration generatorConfiguration,
      final long maximumNumberOfCounterexamples,
      final long maximumNumberOfInputs) throws IOException {
    return new VerificationGuidance(getBaseGuidance(generatorConfiguration, "verification", Duration.ofMinutes(5)),
        maximumNumberOfCounterexamples,
        maximumNumberOfInputs);
  }

  /**
   * Provides shared base guidance (random or Zest) for synthesis and
   * verification.
   * 
   * @param generatorConfiguration {@link #getBaseGuidance(GeneratorConfiguration, String)}
   * @param phaseName              {@link ZestGuidance#ZestGuidance(String, java.time.Duration, java.io.File)}
   * @param duration               {@link TimeLimitedGuidance#TimeLimitedGuidance(Duration, edu.berkeley.cs.jqf.fuzz.guidance.Guidance)}
   * @return Base fuzzing guidance.
   * @throws IOException if Zest guidance could not be initialised.
   */
  private static CloseableGuidance getBaseGuidance(final GeneratorConfiguration generatorConfiguration,
      final String phaseName, final Duration timeout) throws IOException {
    return generatorConfiguration.UseRandomGuidance
        ? new CloseableGuidanceAdapter(new TimeLimitedGuidance(timeout, new NoGuidance(Long.MAX_VALUE, null)))
        : new CloseableZestGuidance(phaseName, timeout);
  }

  private GuidanceFactory() {
  }
}
