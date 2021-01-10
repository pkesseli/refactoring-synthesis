package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.ObjenesisStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

/**
 * Helper to reflectively invoke a Java method based on a counterexample.
 */
public class Invoker {
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
   * @param classLoader    {@link ClassLoader} in which to invoke the configured
   *                       method.
   * @param counterexample {@link Counterexample} modelling the state in which to
   *                       invoke the configured method.
   */
  public ExecutionResult invoke(final ClassLoader classLoader, final Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException {
    final Class<?> cls = classLoader.loadClass(fullyQualifiedClassName);
    final Class<?>[] parameterTypes = new Class<?>[fullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i) {
      final String parameterType = fullyQualifiedParameterTypeNames.get(i);
      parameterTypes[i] = classLoader.loadClass(parameterType);
    }
    final Method method = cls.getDeclaredMethod(methodName, parameterTypes);
    final State state = stateFactory.create(classLoader, counterexample);
    method.setAccessible(true);
    final Object result;
    try {
      result = method.invoke(state.Instance, state.Arguments);
    } catch (final Throwable e) {
      return new ExecutionResult(e);
    }
    return new ExecutionResult(result);
  }
}
