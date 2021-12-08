package uk.ac.ox.cs.refactoring.synthesis.state;

import java.util.Map;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.JavaLanguage;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ClassLoaderCloner;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Fields;

/**
 * Implements {@link StateFactory} by using {@link ClassLoaderCloner} to
 * instantiate objects in the counterexample in the isolated class loader.
 */
public class ClassLoaderClonerStateFactory implements StateFactory {

  @Override
  public State create(final ClassLoader classLoader, final Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final ClassLoaderCloner cloner = new ClassLoaderCloner(classLoader);
    setStaticFields(classLoader, cloner, counterexample.Fields);

    final Object[] arguments = new Object[counterexample.Arguments.size()];
    for (int i = 0; i < arguments.length; ++i)
      arguments[i] = cloner.clone(counterexample.Arguments.get(i));

    return new State(cloner.clone(counterexample.Instance), arguments);
  }

  /**
   * Sets all static fields in {@code classLoader} to values recored in
   * {@code counterexampleValues}.
   * 
   * @param classLoader          Class loader in which to set the field values.
   * @param cloner               Used to create copies in {@code classLoader} of
   *                             the counterexample values.
   * @param counterexampleValues Counterexample values for each static field.
   * @throws ClassNotFoundException {@link #setStaticField(ClassLoader, String, Object)}
   * @throws NoSuchFieldException   {@link #setStaticField(ClassLoader, String, Object)}
   * @throws IllegalAccessException {@link #setStaticField(ClassLoader, String, Object)}
   */
  private static void setStaticFields(final ClassLoader classLoader, final ClassLoaderCloner cloner,
      final Map<String, Object> counterexampleValues)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    for (final Map.Entry<String, Object> staticFiedValue : counterexampleValues.entrySet()) {
      setStaticField(classLoader, staticFiedValue.getKey(), cloner.clone(staticFiedValue.getValue()));
    }
  }

  /**
   * Assigns a static field by name.
   * 
   * @param classLoader             Class loader in which to assign the static
   *                                field.
   * @param fullyQualifiedFieldName Fully qualified field name to assign.
   * @param value                   {@link Fields#set(Class, Object, String, Object)}
   * @throws ClassNotFoundException {@link ClassLoaders#loadClass(ClassLoader, String)}
   * @throws NoSuchFieldException   {@link Fields#set(Class, Object, String, Object)}
   * @throws IllegalAccessException {@link Fields#set(Class, Object, String, Object)}
   */
  private static void setStaticField(final ClassLoader classLoader, final String fullyQualifiedFieldName,
      final Object value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final int index = fullyQualifiedFieldName.lastIndexOf(JavaLanguage.PACKAGE_SEPARATOR);
    if (index == -1) {
      throw new IllegalArgumentException();
    }
    final String className = fullyQualifiedFieldName.substring(0, index);
    final Class<?> cls = ClassLoaders.loadClass(classLoader, className);
    final String name = fullyQualifiedFieldName.substring(index + 1);
    Fields.set(cls, null, name, value);
  }
}
