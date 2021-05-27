package uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods;

import java.lang.reflect.Method;

/**
 * 
 */
public final class Methods {

  /**
   * 
   * @param classLoader
   * @param method
   * @return
   * @throws ClassNotFoundException
   * @throws NoSuchMethodException
   */
  public static Method create(final ClassLoader classLoader, final MethodIdentifier method)
      throws ClassNotFoundException, NoSuchMethodException {
    final Class<?> cls = classLoader.loadClass(method.FullyQualifiedClassName);
    final Class<?>[] parameterTypes = new Class<?>[method.FullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i)
      parameterTypes[i] = classLoader.loadClass(method.FullyQualifiedParameterTypeNames.get(i));

    return cls.getDeclaredMethod(method.MethodName, parameterTypes);
  }

  private Methods() {
  }

}
