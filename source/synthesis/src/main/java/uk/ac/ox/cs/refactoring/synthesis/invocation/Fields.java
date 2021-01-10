package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Hepler for reflective fields in Java.
 */
public final class Fields {
  private Fields() {
  }

  /**
   * Provides all declared instance fields of the given class.
   * 
   * @param cls {@link Class} whose instance fields to retrieve.
   * @return All instance {@link Field}s of {@code cls}.
   */
  public static Field[] getInstance(final Class<?> cls) {
    return getFields(cls, Fields::isInstance);
  }

  /**
   * Provides all declared static fields of the given class.
   * 
   * @param cls {@link Class} whose static fields to retrieve.
   * @return All static {@link Field}s of {@code cls}.
   */
  public static Field[] getStatic(final Class<?> cls) {
    return getFields(cls, Fields::isStatic);
  }

  /**
   * Provides all fields which satisfy a given {@link Predicate}.
   * 
   * @param cls       {@link Class} whose fields to retrieve.
   * @param predicate {@link Predicate} of fields which we want to retrieve.
   * @return All {@link Field} which satisfy {@code predicate}.
   */
  private static Field[] getFields(final Class<?> cls, final Predicate<Field> predicate) {
    return Arrays.stream(cls.getDeclaredFields()).filter(predicate).toArray(Field[]::new);
  }

  /**
   * Checks if the given {@link Field} is an instance field.
   * 
   * @param field {@link Field} to check.
   * @return {@code true} if the field is an instance field, {@code false}
   *         otherwise.
   */
  private static boolean isInstance(final Field field) {
    return !isStatic(field);
  }

  /**
   * Checks if the given {@link Field} is static.
   * 
   * @param field {@link Field} to check.
   * @return {@code true} if the field is static, {@code false} otherwise.
   */
  private static boolean isStatic(final Field field) {
    return Modifier.isStatic(field.getModifiers());
  }
}
