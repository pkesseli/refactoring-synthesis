package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.ObjenesisStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

/**
 * Helper to reflectively invoke a Java method based on a counterexample.
 */
public class Invoker {

  /**
   * Name of constructor methods.
   */
  public static final String INIT = "<init>";

  /**
   * Fully qualified name of the class whose method to invoke.
   */
  private final String fullyQualifiedClassName;

  /**
   * Simple name of the method to invoke.
   */
  private final String methodName;

  /**
   * Fully qualified type names of the invoked method's parameters.
   */
  private final List<String> fullyQualifiedParameterTypeNames;

  /**
   * Helper to prepare a Java program state based on a given counterexample.
   */
  private final IStateFactory stateFactory = new ObjenesisStateFactory();

  /**
   * @param fullyQualifiedClassName          {@link #fullyQualifiedClassName}
   * @param methodName                       {@link #methodName}
   * @param fullyQualifiedParameterTypeNames {@link #fullyQualifiedParameterTypeNames}
   */
  public Invoker(final String fullyQualifiedClassName, final String methodName,
      final List<String> fullyQualifiedParameterTypeNames) {
    this.fullyQualifiedClassName = fullyQualifiedClassName;
    this.methodName = methodName;
    this.fullyQualifiedParameterTypeNames = fullyQualifiedParameterTypeNames;
  }

  /**
   * Invokes the configured method using the given counterexample in the provided
   * {@link ClassLoader}.
   * 
   * @param counterexample {@link Counterexample} modelling the state in which to
   *                       invoke the configured method.
   */
  public ExecutionResult invoke(final Counterexample counterexample)
      throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException,
      NoSuchFieldException, NoSuchMethodException {
    final IsolatedClassLoader classLoader = ClassLoaders.createIsolated();
    final State state = stateFactory.create(classLoader, counterexample);
    final Class<?> cls = classLoader.loadClass(fullyQualifiedClassName);
    final Class<?>[] parameterTypes = new Class<?>[fullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i) {
      final String parameterType = fullyQualifiedParameterTypeNames.get(i);
      parameterTypes[i] = ClassLoaders.loadClass(classLoader, parameterType);
    }

    final Executable executable;
    final Invokable execute;
    if (INIT.equals(methodName)) {
      final Constructor<?> constructor = cls.getDeclaredConstructor(parameterTypes);
      executable = constructor;
      execute = constructor::newInstance;
    } else {
      final Method method = cls.getDeclaredMethod(methodName, parameterTypes);
      executable = method;
      execute = args -> method.invoke(state.Instance, args);
    }

    executable.setAccessible(true);
    final Object result;
    try {
      result = execute.invoke(state.Arguments);
    } catch (final Throwable e) {
      return new ExecutionResult(classLoader, e);
    }
    return new ExecutionResult(classLoader, result);
  }

}
