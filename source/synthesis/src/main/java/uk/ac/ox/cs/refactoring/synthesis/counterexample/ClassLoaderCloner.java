package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Fields;

/** Helper to clone an object into the context of a class loader. */
class ClassLoaderCloner {

  /** Substring contained in lambda class names. */
  private static final String LAMBDA_MARKER = "$$Lambda";

  /** Substring contained in generated proxy classes. */
  private static final String PROXY_MARKER = "$Proxy";

  /**
   * Isolated class loader in which classes for creating cloned objects should be
   * loaded.
   */
  private final ClassLoader classLoader;

  /** @param classLoader {@link #classLoader} */
  ClassLoaderCloner(final ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  /**
   * Creates a copy of {@code object} using classes from {@link #classLoader}.
   * 
   * @param object Object to copy.
   * @return Copy of {@code object} with classes only from {@link #classLoader}.
   * @throws ClassNotFoundException if a class associated with {@code object} is
   *                                missing from {@link #classLoader}.
   * @throws NoSuchFieldException   if a mismatch in the fields of a class between
   *                                the class loader of {@code object} and
   *                                {@link #classLoader} occurs.
   * @throws IllegalAccessException if a field could not be accessed reflectively.
   */
  Object clone(final Object object) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    return clone(object, new HashMap<>());
  }

  /**
   * Recusive handler for {@link #clone(Object)} with shared heap.
   * 
   * @param object          {@link #clone(Object)}
   * @param objectsToClones Already cloned objects, to be reused on aliasing.
   * @return {@link #clone(Object)}
   * @throws ClassNotFoundException {@link #clone(Object)}
   * @throws NoSuchFieldException   {@link #clone(Object)}
   * @throws IllegalAccessException {@link #clone(Object)}
   */
  private Object clone(final Object object, final Map<Integer, Object> objectsToClones)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    if (object == null)
      return null;

    final Class<?> cls = object.getClass();
    final String name = cls.getName();
    if (name.contains(LAMBDA_MARKER) || name.contains(PROXY_MARKER)) {
      if (ClassLoaders.isUserClass(classLoader, cls))
        throw new IllegalArgumentException("Lambdas in counterexamples are not allowed");
      else
        return object;
    }

    if (Literals.isLiteralType(cls))
      return object;

    if (Class.class == cls) {
      return ClassLoaders.loadClass(classLoader, (Class<?>) object);
    }

    final int identityHashCode = System.identityHashCode(object);
    if (cls.isArray()) {
      final int length = Array.getLength(object);
      final Object clone = Array.newInstance(cls.getComponentType(), length);
      objectsToClones.put(identityHashCode, clone);

      for (int i = 0; i < length; ++i) {
        Array.set(clone, i, clone(Array.get(object, i), objectsToClones));
      }
      return clone;
    }

    final Class<?> cloneClass = ClassLoaders.loadClass(classLoader, cls);
    final Function<Class<?>, Object> objenesis = ObjenesisFactory.createObjenesis(classLoader);
    final Object clone = objenesis.apply(cloneClass);
    objectsToClones.put(identityHashCode, clone);

    final Field[] cloneFields = Fields.getInstance(cloneClass);
    for (final Field field : Fields.getInstance(cls)) {
      final Optional<Field> cloneField = Arrays.stream(cloneFields).filter(new FieldEquals(field)).findAny();
      if (!cloneField.isPresent())
        throw new NoSuchFieldException(field.getName());

      field.setAccessible(true);
      final Field targetField = cloneField.get();
      targetField.setAccessible(true);

      final Object value = field.get(object);
      Object clonedValue = objectsToClones.get(System.identityHashCode(value));
      if (clonedValue == null)
        clonedValue = clone(value, objectsToClones);

      targetField.set(clone, clonedValue);
    }
    return clone;
  }
}
