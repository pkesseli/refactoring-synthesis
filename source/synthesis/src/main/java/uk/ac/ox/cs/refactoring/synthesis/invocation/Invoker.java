package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.state.IStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.ObjenesisStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;

/**
 * 
 */
public class Invoker {
  private final String fullyQualifiedClassName;
  private final String methodName;
  private final List<String> fullyQualifiedParameterTypeNames;
  private final IStateFactory stateFactory = new ObjenesisStateFactory();

  public Invoker(final String fullyQualifiedClassName, final String methodName,
      final List<String> fullyQualifiedParameterTypeNames) {
    this.fullyQualifiedClassName = fullyQualifiedClassName;
    this.methodName = methodName;
    this.fullyQualifiedParameterTypeNames = fullyQualifiedParameterTypeNames;
  }

  public void invoke(final ClassLoader classLoader, final Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    final Class<?> cls = classLoader.loadClass(fullyQualifiedClassName);
    final Class<?>[] parameterTypes = new Class<?>[fullyQualifiedParameterTypeNames.size()];
    for (int i = 0; i < parameterTypes.length; ++i) {
      final String parameterType = fullyQualifiedParameterTypeNames.get(i);
      parameterTypes[i] = classLoader.loadClass(parameterType);
    }
    final Method method = cls.getDeclaredMethod(methodName, parameterTypes);
    final State state = stateFactory.create(classLoader, counterexample);
    method.setAccessible(true);
    method.invoke(state.Instance, state.Arguments);
  }
}
