package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Hepler for reflective fields in Java.
 */
public final class Fields {

  /**
   * Assigns a field. If necessary, searches the parents of {@code cls} for
   * matching fields.
   * 
   * @param cls       Fields is set in this type or its parent types.
   * @param object    Instance on which to set the field, {@code null} for static
   *                  fields.
   * @param fieldName Name of the field to set.
   * @param value     Value of the field to set.
   * @throws NoSuchFieldException   if the field could not be found in {@code cls}
   *                                or its parents.
   * @throws IllegalAccessException {@link Field#set(Object, Object)}
   */
  public static void set(final Class<?> cls, final Object object, final String fieldName, final Object value)
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> target = cls;
    while (target != null && !hasField(target, fieldName))
      target = target.getSuperclass();

    if (target == null) {
      throw new NoSuchFieldException(fieldName);
    }
    final Field field = target.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  /**
   * Indicats whether {@code cls} has a certain field.
   * 
   * @param cls  Class to check.
   * @param name Name of the field.
   * @return {@code true} if {@code cls} has a field with {@code name},
   *         {@code false} otherwise.
   */
  private static boolean hasField(final Class<?> cls, final String name) {
    return getFields(cls, field -> name.equals(field.getName())).findAny().isPresent();
  }

  /**
   * Provides all declared instance fields of the given class and its supertypes.
   * 
   * @param cls {@link Class} whose instance fields to retrieve.
   * @return All instance {@link Field}s of {@code cls}.
   */
  public static Field[] getInstance(Class<?> cls) {
    Stream<Field> allFields = Stream.empty();
    while (cls != null) {
      allFields = Stream.concat(allFields, getFields(cls, Fields::isInstance));
      cls = cls.getSuperclass();
    }
    return allFields.toArray(Field[]::new);
  }

  /**
   * Provides all declared static fields of the given class.
   * 
   * @param cls {@link Class} whose static fields to retrieve.
   * @return All static {@link Field}s of {@code cls}.
   */
  public static Field[] getStatic(final Class<?> cls) {
    return getFields(cls, Fields::isStatic).toArray(Field[]::new);
  }

  /**
   * Provides all fields which satisfy a given {@link Predicate}.
   * 
   * @param cls       {@link Class} whose fields to retrieve.
   * @param predicate {@link Predicate} of fields which we want to retrieve.
   * @return All {@link Field} which satisfy {@code predicate}.
   */
  private static Stream<Field> getFields(final Class<?> cls, final Predicate<Field> predicate) {
    return Arrays.stream(cls.getDeclaredFields()).filter(predicate);
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

  private Fields() {
  }
}
