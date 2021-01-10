package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import org.apache.commons.lang3.ClassUtils;

/**
 * Helper providing operations to manage literal types in counterexamples.
 * Literal types are basic types which can be compard using
 * {@link Object#equals(Object)} for equivalence, such as strings or numbers.
 */
public final class Literals {
  private Literals() {
  }

  /**
   * Decides whether the given type is a literal type.
   * 
   * @param cls {@link Class} to check.
   * @return {@code true} if the given type is a literal, {@code false} otherwise.
   */
  public static boolean isLiteralType(final Class<?> cls) {
    if (ClassUtils.isPrimitiveOrWrapper(cls)) {
      return true;
    }
    return String.class == cls;
  }
}
