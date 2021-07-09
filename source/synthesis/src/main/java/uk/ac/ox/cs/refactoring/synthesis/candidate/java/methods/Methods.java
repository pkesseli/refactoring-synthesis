package uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ConstructorInvokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Invokable;
import uk.ac.ox.cs.refactoring.synthesis.invocation.MethodInvokable;

/**
 * Helper to simplify looking up reflective methods in different class loaders.
 */
public final class Methods {

  /**
   * Name of constructor methods.
   */
  public static final String INIT = "<init>";

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
  public static Invokable create(final ClassLoader classLoader, final MethodIdentifier methodIdentifier)
      throws ClassNotFoundException, NoSuchMethodException {
    final Class<?> cls = classLoader.loadClass(methodIdentifier.FullyQualifiedClassName);
    final Class<?>[] parameterTypes = new Class<?>[methodIdentifier.FullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i) {
      final String parameterType = methodIdentifier.FullyQualifiedParameterTypeNames.get(i);
      parameterTypes[i] = ClassLoaders.loadClass(classLoader, parameterType);
    }

    if (INIT.equals(methodIdentifier.MethodName)) {
      final Constructor<?> constructor = cls.getDeclaredConstructor(parameterTypes);
      constructor.setAccessible(true);
      return new ConstructorInvokable(constructor);
    } else {
      final Method method = cls.getDeclaredMethod(methodIdentifier.MethodName, parameterTypes);
      method.setAccessible(true);
      return new MethodInvokable(method);
    }
  }

  private Methods() {
  }

}
