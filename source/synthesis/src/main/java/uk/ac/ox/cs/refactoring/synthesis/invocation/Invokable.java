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
   * 
   * @return
   */
  Class<?> getInstanceType();

  /**
   * 
   * @return
   */
  List<Class<?>> getParameterTypes();

  /**
   * 
   * @return
   */
  Class<?> getReturnType();

}
