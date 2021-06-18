package uk.ac.ox.cs.refactoring.classloader;

/**
 * Helper to construct isolated class loaders.
 */
public final class ClassLoaders {
  private ClassLoaders() {
  }

  /**
   * Equivalent to calling {@link #createIsolated(ClassLoader)} using the class
   * loader which loaded {@link ClassLoaders}.
   */
  public static IsolatedClassLoader createIsolated() {
    return createIsolated(ClassLoaders.class.getClassLoader());
  }

  /**
   * Creates an isolated version of the given {@link ClassLoader}. No classes are
   * loaded on the given class loder itself, and all classes which would normally
   * be provided by the given class loader are instead provided by the create one.
   * Instances provided by this method are thus isolated from each other with
   * respect to the class files provided by the given class loader.
   * 
   * @param classLoader {@link ClassLoader} whose classes to isolate.
   * @return Isolated version of the given class loader.
   */
  public static IsolatedClassLoader createIsolated(final ClassLoader classLoader) {
    return new InstrumentingClassLoader(NullClassFileTransformer.NULL, classLoader);
  }

  /**
   * Extension method for {@link ClassLoader#loadClass(String)} handling primitive
   * type names correctly.
   * 
   * @param classLoader {@link} {@link ClassLoader} to which to delegate for
   *                    non-primitive types.
   * @param name        {@link String Name} of the type to load.
   * @return {@link Class} representing the requested type.
   * @throws ClassNotFoundException {@link ClassLoader#loadClass(String)}
   */
  public static Class<?> loadClass(final ClassLoader classLoader, final String name) throws ClassNotFoundException {
    switch (name) {
    case "double":
      return double.class;
    case "int":
      return int.class;
    }
    return classLoader.loadClass(name);
  }
}
