package uk.ac.ox.cs.refactoring.synthesis.state;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ObjectDescription;

/**
 * Implements {@link IStateFactory} by creating all necessary objects using
 * Objenesis.
 */
public class ObjenesisStateFactory implements IStateFactory {
  @Override
  public State create(final ClassLoader classLoader, final Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final Function<Class<?>, Object> objenesis = createObjenesis(classLoader);
    final Map<ObjectDescription, Object> instances = new HashMap<>();
    final Map<String, Object> objectFields = getOrCreate(objenesis, classLoader, instances, counterexample.ObjectFields,
        new HashMap<>());
    final SortedMap<Integer, Object> objectParameters = getOrCreate(objenesis, classLoader, instances,
        counterexample.ObjectArguments, new TreeMap<>());

    setStaticFields(classLoader, objectFields);
    setStaticFields(classLoader, counterexample.LiteralFields);
    final Object instance = getOrCreate(objenesis, classLoader, instances, counterexample.Instance);
    final Object[] arguments = merge(counterexample.LiteralArguments, objectParameters);
    return new State(instance, arguments);
  }

  /**
   * Loads the class {@link org.objenesis.Objenesis Objenesis} in the given
   * {@link ClassLoader} and provides an instance of it, without loading the class
   * in this class' {@link ClassLoader}. The intent of this method is to
   * instantiate the objenesis object factory in the class loader in which we
   * intend to use it to create instances.
   * 
   * We load {@link org.objenesis.Objenesis Objenesis} in the target class loader
   * since we anticipate that instantiating certain objects will trigger loading
   * previously unloaded classes, and those should be loaded in the isolated class
   * loader as well.
   * 
   * @param classLoader {@link ClassLoader} in which to load the
   *                    {@link org.objenesis.Objenesis Objenesis} class.
   * @return {@link org.objenesis.Objenesis Objenesis} with which to instantiate
   *         objects in the given {@link ClassLoader}.
   */
  private static Function<Class<?>, Object> createObjenesis(final ClassLoader classLoader) {
    final String packageName = ObjenesisStateFactory.class.getPackageName();
    final String name = packageName + JavaLanguage.PACKAGE_SEPARATOR + "ObjenesisWrapper";
    final Class<?> cls;
    try {
      cls = classLoader.loadClass(name);
    } catch (final ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
    final Constructor<?> constructor;
    try {
      constructor = cls.getDeclaredConstructor();
    } catch (final NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(e);
    }
    constructor.setAccessible(true);

    final Object instance;
    try {
      instance = constructor.newInstance();
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
    @SuppressWarnings("unchecked")
    final Function<Class<?>, Object> objenesis = (Function<Class<?>, Object>) instance;
    return objenesis;
  }

  /**
   * Merges two sorted parameter maps into an array which can be used for
   * reflective method invocations.
   * 
   * @param literalArguments {@link Counterexample#LiteralArguments}
   * @param objectArguments  Converted {@link Counterexample#ObjectArguments}
   */
  private Object[] merge(final SortedMap<Integer, Object> literalArguments,
      final SortedMap<Integer, Object> objectArguments) {
    final SortedMap<Integer, Object> union = new TreeMap<>();
    union.putAll(objectArguments);
    union.putAll(literalArguments);
    return union.entrySet().stream().map(Map.Entry::getValue).toArray(Object[]::new);
  }

  /**
   * Converts a single {@link ObjectDescription} to an object on the isolated
   * heap. Aliasing properties of {@link ObjectDescription} are respected.
   * 
   * @param objenesis         Object factory.
   * @param classLoader       Isolated {@link ClassLoader} in which to load all
   *                          classes.
   * @param instances         All created instances on the isolated heap.
   * @param objectDescription {@link ObjectDescription} based on which to create
   *                          the object.
   * @return A Java {@link Object} instance built according to the given object
   *         description. Aliasing of {@link ObjectDescription}s is respected.
   * @throws ClassNotFoundException if
   *                                {@link ObjectDescription#FullyQualifiedClassName}
   *                                could not be loaded.
   * @throws IllegalAccessException if a reflective field mutation fails.
   * @throws NoSuchFieldException   if a field in {@link ObjectDescription} could
   *                                not be found.
   */
  private Object getOrCreate(final Function<Class<?>, Object> objenesis, final ClassLoader classLoader,
      final Map<ObjectDescription, Object> instances, final ObjectDescription objectDescription)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final Object existing = instances.get(objectDescription);
    if (existing != null) {
      return existing;
    }

    final Object created = instantiate(objenesis, classLoader, objectDescription);
    final Class<?> cls = created.getClass();
    for (final Map.Entry<String, Object> literalField : objectDescription.LiteralFields.entrySet()) {
      setField(cls, created, literalField.getKey(), literalField.getValue());
    }
    for (final Map.Entry<String, ObjectDescription> objectField : objectDescription.ObjectFields.entrySet()) {
      final Object nested = getOrCreate(objenesis, classLoader, instances, objectField.getValue());
      setField(cls, created, objectField.getKey(), nested);
    }
    instances.put(objectDescription, created);
    return created;
  }

  /**
   * Helper to instantiate multiple objects based on a a {@link Map} of
   * {@link ObjectDescription} and associating the created objects with the keys
   * of their original {@link ObjectDescription}s.
   * 
   * @param <MapType>          Result map type of instantiated objects.
   * @param <K>                Key type used both in the original and the result
   *                           map.
   * @param objenesis          {@link #getOrCreate(Function, ClassLoader, Map, ObjectDescription)}
   * @param classLoader        {@link #getOrCreate(Function, ClassLoader, Map, ObjectDescription)}
   * @param instances          {@link #getOrCreate(Function, ClassLoader, Map, ObjectDescription)}
   * @param objectDescriptions Map of object descriptions to instantiate.
   * @param objects            Result object to use. Since the result type is
   *                           generic, we ask the invoker to instantiate this.
   * @return <code>objects</code>
   * @throws ClassNotFoundException {@link #getOrCreate(Function, ClassLoader, Map, ObjectDescription)}
   * @throws NoSuchFieldException   {@link #getOrCreate(Function, ClassLoader, Map, ObjectDescription)}
   * @throws IllegalAccessException {@link #getOrCreate(Function, ClassLoader, Map, ObjectDescription)}
   */
  private <MapType extends Map<K, Object>, K> MapType getOrCreate(final Function<Class<?>, Object> objenesis,
      final ClassLoader classLoader, final Map<ObjectDescription, Object> instances,
      final Map<K, ObjectDescription> objectDescriptions, final MapType objects)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    for (final Map.Entry<K, ObjectDescription> namedObjectId : objectDescriptions.entrySet()) {
      final K key = namedObjectId.getKey();
      final Object value = getOrCreate(objenesis, classLoader, instances, namedObjectId.getValue());
      objects.put(key, value);
    }
    return objects;
  }

  /**
   * Creates an instance of the class indicated in
   * {@link ObjectDescription#FullyQualifiedClassName}. Does not assign any
   * fields.
   * 
   * @param objenesis         Objenesis object factory to instantiate the objects.
   * @param classLoader       {@link ClassLoader} in which to load the
   *                          instantiated object's class.
   * @param ObjectDescription Description of object to instantiate.
   * @return Instantiated object.
   */
  private Object instantiate(final Function<Class<?>, Object> objenesis, final ClassLoader classLoader,
      final ObjectDescription objectDescription) throws ClassNotFoundException {
    final Class<?> cls = classLoader.loadClass(objectDescription.FullyQualifiedClassName);
    final Thread currentThread = Thread.currentThread();
    final ClassLoader originalContextClassLoader = currentThread.getContextClassLoader();
    try {
      return objenesis.apply(cls);
    } finally {
      currentThread.setContextClassLoader(originalContextClassLoader);
    }
  }

  /**
   * Reflectively assigns a field.
   * 
   * @param cls   Class of object whose field to assign.
   * @param obj   Object whose field to assign.
   * @param name  {@link String Name} of the field to assign.
   * @param value Value to assign.
   * @throws NoSuchFieldException   {@link Class#getDeclaredField(String)}
   * @throws IllegalAccessException {@link Field#set(Object, Object)}
   */
  private static void setField(final Class<?> cls, final Object obj, final String name, final Object value)
      throws NoSuchFieldException, IllegalAccessException {
    final Field field = cls.getDeclaredField(name);
    field.setAccessible(true);
    field.set(obj, value);
  }

  /**
   * Assigns a static field by name.
   * 
   * @param classLoader             Class loader in which to assign the static
   *                                field.
   * @param fullyQualifiedFieldName Fully qualified field name to assign.
   * @param value                   {@link #setField(Class, Object, String, Object)}
   */
  private static void setStaticField(final ClassLoader classLoader, final String fullyQualifiedFieldName,
      final Object value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final int index = fullyQualifiedFieldName.lastIndexOf(JavaLanguage.PACKAGE_SEPARATOR);
    if (index == -1) {
      throw new IllegalArgumentException();
    }
    final String className = fullyQualifiedFieldName.substring(0, index);
    final Class<?> cls = classLoader.loadClass(className);
    final String name = fullyQualifiedFieldName.substring(index + 1);
    setStaticField(cls, name, value);
  }

  /**
   * Reflectively assigns a static field.
   * 
   * @param cls {@link #setField(Class, Object, String, Object)}
   * @throws IllegalAccessException {@link #setField(Class, Object, String, Object)}
   * @throws NoSuchFieldException   {@link #setField(Class, Object, String, Object)}
   */
  private static void setStaticField(final Class<?> cls, final String name, final Object value)
      throws NoSuchFieldException, IllegalAccessException {
    setField(cls, null, name, value);
  }

  /**
   * Assigns a map of static fields to their associated values.
   * 
   * @param classLoader {@link #setStaticField(ClassLoader, String, Object)}
   * @throws ClassNotFoundException {@link #setStaticField(ClassLoader, String, Object)}
   * @throws IllegalAccessException {@link #setStaticField(ClassLoader, String, Object)}
   * @throws NoSuchFieldException   {@link #setStaticField(ClassLoader, String, Object)}
   */
  private static void setStaticFields(final ClassLoader classLoader, final Map<String, Object> fields)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    for (final Map.Entry<String, Object> field : fields.entrySet()) {
      setStaticField(classLoader, field.getKey(), field.getValue());
    }
  }
}
