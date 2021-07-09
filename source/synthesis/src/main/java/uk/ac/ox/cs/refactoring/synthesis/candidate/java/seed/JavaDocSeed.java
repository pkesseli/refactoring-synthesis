package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.lang.reflect.Method;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;

/**
 * Adds instructions based on hints in JavaDoc.
 * 
 * TODO: Implement!
 */
public class JavaDocSeed implements InstructionSetSeed {

  /**
   * Used to load pre-configured classes.
   */
  private final ClassLoader classLoader;

  /**
   * @param classLoader {@link #classLoader}
   */
  public JavaDocSeed(final ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException, NoSuchMethodException {
    final JavaComponents javaComponents = new JavaComponents(components);
    final Class<?> date = classLoader.loadClass("java.util.Date");
    final Class<?> calendar = classLoader.loadClass("java.util.Calendar");
    final Method[] methods = { calendar.getDeclaredMethod("get", int.class), calendar.getDeclaredMethod("getInstance"),
        calendar.getDeclaredMethod("setTime", date) };
    for (final Method method : methods)
      Invoke.register(javaComponents, method);
  }

}
