package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;

/**
 * Describes a component which can be constructed by a builder both by its own
 * properties and the types of arguments necessary to construct it.
 */
public interface Component<K, V> {
  /**
   * @return Size of this sub-component.
   */
  int size();

  /**
   * 
   * @return Keys identifying arguments necessary to construct an object using
   *         {@link #construct(Object[])}.
   */
  List<K> getParameterKeys();

  /**
   * Instantiates a component described by {@code this}.
   * 
   * @param arguments Argument with which to construct the object.
   * @return Instantiation of the described object type with the given arguments.
   */
  V construct(Object[] arguments);
}
