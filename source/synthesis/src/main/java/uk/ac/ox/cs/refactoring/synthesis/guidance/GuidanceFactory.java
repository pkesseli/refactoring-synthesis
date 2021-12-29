package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.io.IOException;

import edu.berkeley.cs.jqf.fuzz.ei.ZestGuidance;
import edu.berkeley.cs.jqf.fuzz.random.NoGuidance;

/** Helper to create configured synthesis and verification guidances. */
public final class GuidanceFactory {

  /**
   * Provides synthesis guidance configuration.
   * 
   * @return Configured synthesis JQF guidance.
   * @throws IOException if Zest guidance could not be initialised.
   */
  public static CloseableGuidance synthesis() throws IOException {
    return new SynthesisGuidance(getBaseGuidance("inductive synthesis"));
  }

  /**
   * Provides verification guidance configuration.
   * 
   * @param maximumNumberOfCounterexamples {@link VerificationGuidance#VerificationGuidance(CloseableGuidance, long, long)}
   * @param maximumNumberOfInputs          {@link VerificationGuidance#VerificationGuidance(CloseableGuidance, long, long)}
   * @return Configured {@link VerificationGuidance}.
   * @throws IOException if Zest guidance could not be initialised.
   */
  public static CloseableGuidance verification(final long maximumNumberOfCounterexamples,
      final long maximumNumberOfInputs) throws IOException {
    return new VerificationGuidance(getBaseGuidance("verification"), maximumNumberOfCounterexamples,
        maximumNumberOfInputs);
  }

  /**
   * Provides shared base guidance (random or Zest) for synthesis and
   * verification.
   * 
   * @param phaseName {@link ZestGuidance#ZestGuidance(String, java.time.Duration, java.io.File)}
   * @return Base fuzzing guidance.
   * @throws IOException if Zest guidance could not be initialised.
   */
  private static CloseableGuidance getBaseGuidance(final String phaseName) throws IOException {
    return useRandom() ? new CloseableGuidanceAdapter(new NoGuidance(Long.MAX_VALUE, null))
        : new CloseableZestGuidance(phaseName);
  }

  /**
   * Indicates whether Zest or random guidance should be used.
   * 
   * @return {@code true} if random guidance should be used, {@code false} if Zest
   *         guidance should be used.
   */
  private static boolean useRandom() {
    return Boolean.getBoolean("resynth.fuzzing.random");
  }

  private GuidanceFactory() {
  }
}
