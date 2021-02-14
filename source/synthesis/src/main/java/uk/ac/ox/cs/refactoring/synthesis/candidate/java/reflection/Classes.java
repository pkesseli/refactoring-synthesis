package uk.ac.ox.cs.refactoring.synthesis.candidate.java.reflection;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Helper to reflectively access {@link Class} data.
 */
public final class Classes {
  private Classes() {
  }

  /**
   * Provides all concrete member classes.
   * 
   * @param parent Parent {@link Class} whose members to retrieve.
   * @return All non-abstract member classes.
   */
  public static Stream<Class<?>> getNonAbstractMemberClasses(final Class<?> parent) {
    return Arrays.stream(parent.getDeclaredClasses()).filter(cls -> !Modifier.isAbstract(cls.getModifiers()));
  }
}
