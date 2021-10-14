package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.junit.GuidedFuzzing;

/**
 * Helper to configure fuzzing guidance. Necessary since JQF guidance
 * configuration is not available using our JUnit entry point.
 */
public class FuzzingConfiguration implements AutoCloseable {

  /**
   * Temporarily enables {@code guidance} globally.
   * 
   * @param guidance {@link Guidance} to enable.
   * @throws NoSuchMethodException     if the private method
   *                                   {@link GuidedFuzzing#setGuidance} is not
   *                                   found.
   * @throws InvocationTargetException if {@link GuidedFuzzing#setGuidance} throws
   *                                   an exception.
   * @throws IllegalAccessException    if invoking
   *                                   {@link GuidedFuzzing#setGuidance} is not
   *                                   allowed.
   */
  public FuzzingConfiguration(final Guidance guidance)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    final Method method = GuidedFuzzing.class.getDeclaredMethod("setGuidance", Guidance.class);
    method.setAccessible(true);
    method.invoke(null, guidance);
  }

  @Override
  public void close() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
    final Method method = GuidedFuzzing.class.getDeclaredMethod("unsetGuidance");
    method.setAccessible(true);
    method.invoke(null);
  }

}
