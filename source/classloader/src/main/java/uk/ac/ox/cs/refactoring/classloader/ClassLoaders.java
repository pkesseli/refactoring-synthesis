package uk.ac.ox.cs.refactoring.classloader;

/** Helper to construct isolated class loaders. */
public final class ClassLoaders {

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
      case "void":
        return void.class;
      case "boolean":
        return boolean.class;
      case "byte":
        return byte.class;
      case "short":
        return short.class;
      case "int":
        return int.class;
      case "long":
        return long.class;
      case "float":
        return float.class;
      case "double":
        return double.class;
      case "char":
        return char.class;
    }

    if (name.endsWith("[]")) {
      final String element = name.substring(0, name.length() - 2);
      return loadClass(classLoader, element).arrayType();
    }

    return classLoader.loadClass(name);
  }

  /**
   * Convenience operation for {@link #loadClass(ClassLoader, String)}, looking
   * up {@code cls} using its {@link Class#getName() name}. Short-circuits
   * classes loaded by the bootstrap class loader.
   * 
   * @param classLoader {@link #loadClass(ClassLoader, String)}
   * @param cls         Class to look up in {@code classLoader}.
   * @return {@link #loadClass(ClassLoader, String)}
   * @throws ClassNotFoundException {@link #loadClass(ClassLoader, String)}
   */
  public static Class<?> loadClass(final ClassLoader classLoader, final Class<?> cls) throws ClassNotFoundException {
    if (!isUserClass(classLoader, cls))
      return cls;

    final String canonicalName = cls.getCanonicalName();
    final String name = cls.getName();
    final boolean isInnerClass = cls.getName().contains("$");
    return loadClass(classLoader, canonicalName == null || isInnerClass ? name : canonicalName);
  }

  /**
   * Determines whether the given class should be loaded in isolation. As an
   * example, classes loaded by a parent of {@code classLoader} do not need to be
   * reloaded.
   * 
   * @param classLoader Isolated class loader.
   * @param cls         Class to load.
   * @return {@code true} if the clas should be reloaded in {@code classLoader},
   *         {@code false} otherwise.
   */
  public static boolean isUserClass(final ClassLoader classLoader, final Class<?> cls) {
    final ClassLoader clsClassLoader = cls.getClassLoader();
    if (clsClassLoader == null)
      return false;

    ClassLoader parent = classLoader.getParent();
    while (parent != null) {
      if (parent == clsClassLoader)
        return false;
      parent = parent.getParent();
    }
    return true;
  }

  private ClassLoaders() {
  }
}
