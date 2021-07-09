package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Functional interface for methods and constructors.
 */
public interface Invokable {

  /**
   * Invokes the given method or constructor.
   * 
   * @param instance Method instance, if applicable.
   * @param args     Method or construtor arguments.
   * @return Invocation result.
   * @throws IllegalAccessException    if the invoked method or constructor
   *                                   throws.
   * @throws InstantiationException    if the invoked method or constructor
   *                                   throws.
   * @throws InvocationTargetException if the invoked method or constructor
   *                                   throws.
   */
  Object invoke(Object instance, Object[] args)
      throws IllegalAccessException, InstantiationException, InvocationTargetException;

  /**
   * Provides the instance type of the invokable. {@link Void} if static.
   * 
   * @return Invokable instance type.
   */
  Class<?> getInstanceType();

  /**
   * Indicates whether the given invokable is an instance method.
   * 
   * @param invokable {@link Invokable} to verify.
   * @return {@code true} if {@code invokable} is an instance method,
   *         {@code false} otherwise.
   */
  static boolean hasInstance(Invokable invokable) {
    return Void.class != invokable.getInstanceType();
  }

  /**
   * Provides the parameters necessary to invoke this {@link Invokable}, excluding
   * the instance type.
   * 
   * @return Parameter types.
   */
  List<Class<?>> getParameterTypes();

  /**
   * Provides the result type.
   * 
   * @return Return type.
   */
  Class<?> getReturnType();

}
