package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * {@link HierarchicalComponent} for objects which are constructed reflectively
 * using a {@link Constructor}.
 */
public class ConstructorComponent<K, V> implements HierarchicalComponent<K, V> {

  /**
   * {@link Constructor} to create the object.
   */
  private final Constructor<V> constructor;

  /**
   * {@link #getParameterKeys()}
   */
  private final List<K> parameterKeys;

  /**
   * @param parameterKeys {@link #parameterKeys}
   * @param type          Class which to construct. First declared constructor is
   *                      used.
   */
  @SuppressWarnings("unchecked")
  public ConstructorComponent(final List<K> parameterKeys, final Class<V> type) {
    this.constructor = (Constructor<V>) type.getDeclaredConstructors()[0];
    this.parameterKeys = parameterKeys;
    constructor.setAccessible(true);
  }

  @Override
  public V construct(final Object[] arguments) {
    try {
      return constructor.newInstance(arguments);
    } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<K> getParameterKeys() {
    return parameterKeys;
  }
}
