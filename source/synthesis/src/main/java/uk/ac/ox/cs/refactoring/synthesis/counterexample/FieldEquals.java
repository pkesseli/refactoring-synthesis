package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * Checks whether a field is equivalent to configured one, even if their
 * classes were loaded on different class loaders.
 */
class FieldEquals implements Predicate<Field> {

  /** Field to compare to. */
  private final Field field;

  /** {@link Field#getDeclaringClass()} of {@link #field}. */
  private final Class<?> declaringClass;

  /** @param field {@link #field} */
  FieldEquals(final Field field) {
    this.field = field;
    declaringClass = field.getDeclaringClass();
  }

  @Override
  public boolean test(final Field t) {
    return field.getName().equals(t.getName()) && declaringClass.getName().equals(t.getDeclaringClass().getName());
  }
}
