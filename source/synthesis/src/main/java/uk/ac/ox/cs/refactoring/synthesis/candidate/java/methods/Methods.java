package uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods;

import java.lang.reflect.Method;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;

/**
 * Helper to simplify looking up reflective methods in different class loaders.
 */
public final class Methods {

  /**
   * Looks up the reflective method referenced in {@code method} in
   * {@code classLoader}.
   * 
   * @param classLoader {@link ClassLoader} in which to look up the method.
   * @param method      {@link MethodIdentifier} to look up.
   * @return Reflective method requested, loaded from the provided class loader.
   * @throws ClassNotFoundException {@link ClassLoader#loadClass(String)}
   * @throws NoSuchMethodException  {@link Class#getDeclaredMethod(String, Class...)}
   */
  public static Method create(final ClassLoader classLoader, final MethodIdentifier method)
      throws ClassNotFoundException, NoSuchMethodException {
    final Class<?> cls = classLoader.loadClass(method.FullyQualifiedClassName);
    final Class<?>[] parameterTypes = new Class<?>[method.FullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i)
      parameterTypes[i] = ClassLoaders.loadClass(classLoader, method.FullyQualifiedParameterTypeNames.get(i));

    return cls.getDeclaredMethod(method.MethodName, parameterTypes);
  }

  private Methods() {
  }

}
