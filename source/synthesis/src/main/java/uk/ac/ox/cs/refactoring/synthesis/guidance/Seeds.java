package uk.ac.ox.cs.refactoring.synthesis.guidance;

import java.lang.reflect.Field;
import java.util.Random;

import edu.berkeley.cs.jqf.fuzz.ei.ZestGuidance;
import edu.berkeley.cs.jqf.fuzz.random.NoGuidance;

final class Seeds {
  private Seeds() {
  }

  /**
   * Retrieves the {@code random} field from {@link ZestGuidance} or
   * {@link NoGuidance} instances and reflectively sets their seed to a value
   * configured in a system property.
   * 
   * @param randomHolder Guidance for which to set a seed.
   */
  static void setSeed(final Object randomHolder) {
    Field randomField;
    try {
      randomField = randomHolder.getClass().getDeclaredField("random");
    } catch (final NoSuchFieldException e) {
      throw new IllegalStateException(e);
    }
    randomField.setAccessible(true);
    final Random random;
    try {
      random = (Random) randomField.get(randomHolder);
    } catch (final IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    final Long seed = Long.getLong("resynth.seed");
    if (seed == null) {
      throw new IllegalStateException("Specify a seed using -Dresynth.seed=<seed>");
    }
    random.setSeed(seed);
  }
}
