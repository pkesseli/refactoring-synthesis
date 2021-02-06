package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;

/**
 * 
 */
public interface Component<T> {
  /**
   * 
   * @return
   */
  int size();

  /**
   * 
   * @return
   */
  List<Class<?>> getArgumentTypes();

  /**
   * 
   * @param arguments
   * @return
   */
  T construct(Object[] arguments);
}
