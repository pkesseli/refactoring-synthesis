package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Fields;

/** Helper to clone an object into the context of a class loader. */
public class ClassLoaderCloner {

  /** Sink for warnings. */
  private static final Logger logger = LoggerFactory.getLogger(ClassLoaderCloner.class);

  /**
   * Isolated class loader in which classes for creating cloned objects should be
   * loaded.
   */
  private final ClassLoader classLoader;

  /**
   * Stores all created objects so that aliasing references can be looked up
   * instead of being recreated.
   */
  private final Map<Object, Object> objectsToClones = new IdentityHashMap<>();

  /** @param classLoader {@link #classLoader} */
  public ClassLoaderCloner(final ClassLoader classLoader) {
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
  public Object clone(final Object object)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    if (object == null)
      return null;

    final Class<?> cls = object.getClass();
    if (Polymorphism.isDynamic(cls)) {
      if (ClassLoaders.isUserClass(classLoader, cls)) {
        // logger.warn("Lambdas in user classes are not yet supported.");
      }
      return object;
    }

    if (Literals.isLiteralType(cls))
      return object;

    if (Class.class == cls) {
      final Class<?> classObject = (Class<?>) object;
      if (Polymorphism.isDynamic(classObject))
        return classObject;

      return ClassLoaders.loadClass(classLoader, classObject);
    }

    final Object existingClone = objectsToClones.get(object);
    if (existingClone != null) {
      return existingClone;
    }

    if (cls.isArray()) {
      final Class<?> cloneClass = ClassLoaders.loadClass(classLoader, cls);
      final int length = Array.getLength(object);
      final Object clone = Array.newInstance(cloneClass.getComponentType(), length);
      objectsToClones.put(object, clone);

      for (int i = 0; i < length; ++i) {
        Array.set(clone, i, clone(Array.get(object, i)));
      }
      return clone;
    }

    final Class<?> cloneableClass = Polymorphism.getModelledClass(cls);
    final Object clone = createObject(cloneableClass);
    objectsToClones.put(object, clone);

    final Class<?> cloneClass = Polymorphism.getModelledClass(clone.getClass());
    final Field[] cloneFields = Fields.getInstance(cloneClass);
    for (final Field field : Fields.getInstance(cloneableClass)) {
      final Optional<Field> cloneField = Arrays.stream(cloneFields).filter(new FieldEquals(field)).findAny();
      if (!cloneField.isPresent())
        throw new NoSuchFieldException(field.getName());

      field.setAccessible(true);
      final Field targetField = cloneField.get();
      targetField.setAccessible(true);

      final Object value = field.get(object);
      targetField.set(clone, clone(value));
    }
    return clone;
  }

  /**
   * Helper to apply {@link ObjenesisFactory}.
   * 
   * @param originalClass {@link Class} we would like to instantiate.
   * @return Minimally initialised object of type {@code originalClass}.
   * @throws ClassNotFoundException {@link ClassLoaders#loadClass(ClassLoader, Class)}
   */
  private Object createObject(final Class<?> originalClass) throws ClassNotFoundException {
    final Class<?> cloneClass = ClassLoaders.loadClass(classLoader, originalClass);
    final Function<Class<?>, Object> objenesis = ObjenesisFactory.createObjenesis(classLoader);
    final Thread currentThread = Thread.currentThread();
    final ClassLoader originalContextClassLoader = currentThread.getContextClassLoader();
    try {
      return objenesis.apply(cloneClass);
    } finally {
      currentThread.setContextClassLoader(originalContextClassLoader);
    }
  }
}
