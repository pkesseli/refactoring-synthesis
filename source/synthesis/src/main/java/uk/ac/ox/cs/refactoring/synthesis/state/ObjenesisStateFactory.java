package uk.ac.ox.cs.refactoring.synthesis.state;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ObjectDescription;

/**
 * Implements {@link IStateFactory} by creating all necessary objects using
 * Objenesis.
 */
public class ObjenesisStateFactory implements IStateFactory {
  /**
   * Separator character for packages and classes in Java.
   */
  private static final char PACKAGE_SEPARATOR = '.';

  /**
   * {@link Objenesis} to create any object on the heap.
   */
  private final Objenesis objenesis = new ObjenesisStd();

  @Override
  public State create(final ClassLoader classLoader, final Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final Map<ObjectDescription, Object> instances = new HashMap<>();
    final Map<String, Object> objectFields = getOrCreate(classLoader, instances, counterexample.ObjectFields,
        new HashMap<>());
    final SortedMap<Integer, Object> objectParameters = getOrCreate(classLoader, instances,
        counterexample.ObjectArguments, new TreeMap<>());

    setStaticFields(classLoader, objectFields);
    setStaticFields(classLoader, counterexample.LiteralFields);
    final Object instance = getOrCreate(classLoader, instances, counterexample.Instance);
    final Object[] arguments = merge(objectParameters, counterexample.LiteralArguments);
    return new State(instance, arguments);
  }

  /**
   * Merges two sorted parameter maps into an array which can be used for
   * reflective method invocations.
   * 
   * @param objectArguments  Converted {@link Counterexample#ObjectArguments}.
   * @param literalArguments {@link Counterexample#LiteralArguments}.
   */
  private Object[] merge(final SortedMap<Integer, Object> objectArguments,
      final SortedMap<Integer, Object> literalArguments) {
    final SortedMap<Integer, Object> union = new TreeMap<>();
    union.putAll(objectArguments);
    union.putAll(literalArguments);
    return union.entrySet().stream().map(Map.Entry::getValue).toArray(Object[]::new);
  }

  /**
   * Converts a single {@link ObjectDescription} to an object on the isolated
   * heap. Aliasing properties of {@link ObjectDescription} are respected.
   * 
   * @param classLoader       Isolated {@link ClassLoader} in which to load all
   *                          classes.
   * @param instances         All created instances on the isolated heap.
   * @param objectDescription {@link ObjectDescription} based on which to create
   *                          the object.
   * @return
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  private Object getOrCreate(final ClassLoader classLoader, final Map<ObjectDescription, Object> instances,
      final ObjectDescription objectDescription)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final Object existing = instances.get(objectDescription);
    if (existing != null) {
      return existing;
    }

    final Object created = instantiate(classLoader, objectDescription);
    final Class<?> cls = created.getClass();
    for (final Map.Entry<String, Object> literalField : objectDescription.LiteralFields.entrySet()) {
      setField(cls, created, literalField.getKey(), literalField.getValue());
    }
    for (final Map.Entry<String, ObjectDescription> objectField : objectDescription.ObjectFields.entrySet()) {
      final Object nested = getOrCreate(classLoader, instances, objectField.getValue());
      setField(cls, created, objectField.getKey(), nested);
    }
    instances.put(objectDescription, created);
    return created;
  }

  private <MapType extends Map<T, Object>, T> MapType getOrCreate(final ClassLoader classLoader,
      final Map<ObjectDescription, Object> instances, final Map<T, ObjectDescription> objectDescriptions,
      final MapType objects) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    for (final Map.Entry<T, ObjectDescription> namedObjectId : objectDescriptions.entrySet()) {
      final T key = namedObjectId.getKey();
      final Object value = getOrCreate(classLoader, instances, namedObjectId.getValue());
      objects.put(key, value);
    }
    return objects;
  }

  private Object instantiate(final ClassLoader classLoader, final ObjectDescription objectDescription)
      throws ClassNotFoundException {
    final Class<?> cls = classLoader.loadClass(objectDescription.FullyQualifiedClassName);
    return objenesis.newInstance(cls);
  }

  private static void setField(final Class<?> cls, final Object obj, final String name, final Object value)
      throws NoSuchFieldException, IllegalAccessException {
    final Field field = cls.getDeclaredField(name);
    field.setAccessible(true);
    field.set(obj, value);
  }

  private static void setStaticField(final ClassLoader classLoader, final String fullyQualifiedFieldName,
      final Object value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final int index = fullyQualifiedFieldName.lastIndexOf(PACKAGE_SEPARATOR);
    if (index == -1) {
      throw new IllegalArgumentException();
    }
    final String className = fullyQualifiedFieldName.substring(0, index);
    final Class<?> cls = classLoader.loadClass(className);
    final String name = fullyQualifiedFieldName.substring(index + 1);
    setStaticField(cls, name, value);
  }

  /**
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  private static void setStaticField(final Class<?> cls, final String name, final Object value)
      throws NoSuchFieldException, IllegalAccessException {
    setField(cls, null, name, value);
  }

  /**
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  private static void setStaticFields(final ClassLoader classLoader, final Map<String, Object> fields)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    for (final Map.Entry<String, Object> field : fields.entrySet()) {
      setStaticField(classLoader, field.getKey(), field.getValue());
    }
  }
}
