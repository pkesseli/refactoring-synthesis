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
    final Class<?> cls = getClass(classLoader, methodIdentifier);
    final Class<?>[] parameterTypes = getParameterTypes(classLoader, methodIdentifier);

    if (INIT.equals(methodIdentifier.MethodName)) {
      final Constructor<?> constructor = cls.getDeclaredConstructor(parameterTypes);
      constructor.setAccessible(true);
      return new ConstructorInvokable(constructor);
    } else {
      return new MethodInvokable(getMethod(cls, methodIdentifier, parameterTypes));
    }
  }

  /**
   * Looks up a {@link MethodIdentifier method} in a {@link ClassLoader}.
   * 
   * @param classLoader      {@link ClassLoader} from which to load the method.
   * @param methodIdentifier Identifier for the method.
   * @return {@link #getMethod(Class, MethodIdentifier, Class[])}
   * @throws NoSuchMethodException  {@link #getMethod(Class, MethodIdentifier, Class[])}
   * @throws ClassNotFoundException {@link #getClass(ClassLoader, MethodIdentifier)}
   */
  public static Method getMethod(final ClassLoader classLoader, final MethodIdentifier methodIdentifier)
      throws NoSuchMethodException, ClassNotFoundException {
    return getMethod(getClass(classLoader, methodIdentifier), methodIdentifier,
        getParameterTypes(classLoader, methodIdentifier));
  }

  /**
   * Retrieves a method from a given class.
   * 
   * @param cls              {@link Class} in which to look up a method.
   * @param methodIdentifier {@link MethodIdentifier Method} to look up.
   * @param parameterTypes   {@link Class Parameters} of the method.
   * @return Reflective methd described by {@code MethodIdentifier}.
   * @throws NoSuchMethodException if the method was not found in {@code cls}.
   */
  private static Method getMethod(final Class<?> cls, final MethodIdentifier methodIdentifier,
      final Class<?>[] parameterTypes) throws NoSuchMethodException {
    final Method method = cls.getDeclaredMethod(methodIdentifier.MethodName, parameterTypes);
    method.setAccessible(true);
    return method;
  }

  /**
   * Retrieve the class of the given {@link MethodIdentifier method}.
   * 
   * @param classLoader      {@link ClassLoader} from which to look up the class.
   * @param methodIdentifier {@link MethodIdentifier Method} whose class to
   *                         retrieve.
   * @return {@link Class} identified by the given {@link MethodIdentifier}.
   * @throws ClassNotFoundException if the class could not be looked up.
   */
  private static Class<?> getClass(final ClassLoader classLoader, final MethodIdentifier methodIdentifier)
      throws ClassNotFoundException {
    return classLoader.loadClass(methodIdentifier.FullyQualifiedClassName);
  }

  /**
   * Looks up the parameter {@link Class types} for the given method.
   * 
   * @param classLoader      {@link ClassLoader} from which to load the classes.
   * @param methodIdentifier {@link MethodIdentifier Method} whose parameters to
   *                         look up.
   * @return Array of parameter classes which can be used in
   *         {@link Class#getDeclaredMethod(String, Class...)} to look up the
   *         method.
   * @throws ClassNotFoundException if a parameter type could not be looked up.
   */
  private static Class<?>[] getParameterTypes(final ClassLoader classLoader, final MethodIdentifier methodIdentifier)
      throws ClassNotFoundException {
    final Class<?>[] parameterTypes = new Class<?>[methodIdentifier.FullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i) {
      final String parameterType = methodIdentifier.FullyQualifiedParameterTypeNames.get(i);
      parameterTypes[i] = ClassLoaders.loadClass(classLoader, parameterType);
    }
    return parameterTypes;
  }

  private Methods() {
  }
}
