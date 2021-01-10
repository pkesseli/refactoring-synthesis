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
}
