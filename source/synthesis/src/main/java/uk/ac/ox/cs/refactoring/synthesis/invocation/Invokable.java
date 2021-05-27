package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.InvocationTargetException;

/**
 * Functional interface for methods and constructors.
 */
@FunctionalInterface
public interface Invokable {

  /**
   * Invokes the given method or constructor.
   * 
   * @param args Method or construtor arguments.
   * @return Invocation result.
   * @throws IllegalAccessException    if the invoked method or constructor
   *                                   throws.
   * @throws InstantiationException    if the invoked method or constructor
   *                                   throws.
   * @throws InvocationTargetException if the invoked method or constructor
   *                                   throws.
   */
  Object invoke(Object[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException;

}
