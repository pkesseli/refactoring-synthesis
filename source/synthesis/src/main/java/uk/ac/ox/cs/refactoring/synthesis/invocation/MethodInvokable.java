package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Implements {@link Invokable} by reflectively executing a method.
 */
public class MethodInvokable extends ExecutableInvokable {

  /**
   * Method to invoke.
   */
  private final Method method;

  /**
   * @param method {@link #method}
   */
  public MethodInvokable(final Method method) {
    super(method);
    this.method = method;
  }

  @Override
  public Object invoke(Object instance, Object[] args)
      throws IllegalAccessException, InstantiationException, InvocationTargetException {
    return method.invoke(instance, args);
  }

  @Override
  public Class<?> getInstanceType() {
    if (Modifier.isStatic(method.getModifiers()))
      return Void.class;
    return method.getDeclaringClass();
  }

  @Override
  public Class<?> getReturnType() {
    return method.getReturnType();
  }

}
