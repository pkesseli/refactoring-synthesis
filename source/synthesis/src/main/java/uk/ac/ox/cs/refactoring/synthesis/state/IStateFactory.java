package uk.ac.ox.cs.refactoring.synthesis.state;

import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;

/**
 * Implementations create a Java heap {@link State} based on a given
 * {@link Counterexample}. All operations are performed with respect to a given,
 * isolated {@link ClassLoader}.
 */
public interface IStateFactory {
  /**
   * @param classLoader    Isolated class loader in which to load all classes. Any
   *                       static state is applied to the classes in this class
   *                       loader.
   * @param counterexample Counterexample to transform into a heap state and
   *                       method parameters.
   * @return Heap state on which a method invocation can be performed. Full heap
   *         state is modelled in conjunction with static data in the provided
   *         {@link ClassLoader}.
   */
  State create(ClassLoader classLoader, Counterexample counterexample)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException;
}
