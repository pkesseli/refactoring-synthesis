package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.berkeley.cs.jqf.fuzz.guidance.Guidance;
import edu.berkeley.cs.jqf.fuzz.junit.GuidedFuzzing;

/**
 * 
 */
public class FuzzingConfiguration implements AutoCloseable {

  /**
   * 
   * @param guidance
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public FuzzingConfiguration(final Guidance guidance)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    final Method method = GuidedFuzzing.class.getDeclaredMethod("setGuidance", Guidance.class);
    method.setAccessible(true);
    method.invoke(null, guidance);
  }

  @Override
  public void close() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    final Method method = GuidedFuzzing.class.getDeclaredMethod("unsetGuidance");
    method.setAccessible(true);
    method.invoke(null);
  }

}
