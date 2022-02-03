package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Implements {@link Invokable} by invoking a constructor reflectively.
 */
public class ConstructorInvokable extends ExecutableInvokable {

  /**
   * Reflective constructor to invoke.
   */
  private final Constructor<?> constructor;

  /**
   * @param constructor {@link #constructor}
   */
  public ConstructorInvokable(final Constructor<?> constructor) {
    super(constructor);
    this.constructor = constructor;
  }

  @Override
  public Object invoke(Object instance, Object[] args)
      throws IllegalAccessException, InstantiationException, InvocationTargetException {
    return constructor.newInstance(args);
  }

  @Override
  public Class<?> getInstanceType() {
    return Void.class;
  }

  @Override
  public Class<?> getReturnType() {
    return constructor.getDeclaringClass();
  }

}
