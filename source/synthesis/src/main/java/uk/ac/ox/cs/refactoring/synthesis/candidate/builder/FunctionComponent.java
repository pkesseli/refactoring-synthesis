package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;
import java.util.function.Function;

/**
 * @param <K>
 * @param <V>
 */
public class FunctionComponent<K, V> implements HierarchicalComponent<K, V> {

  /**
   * {@link #getParameterKeys()}
   */
  private final List<K> parameterKeys;

  /**
   * 
   */
  private final Function<Object[], V> function;

  /**
   * @param parameterKeys {@link #parameterKeys}
   * @param function      {@link #function}
   */
  public FunctionComponent(final List<K> parameterKeys, final Function<Object[], V> function) {
    this.parameterKeys = parameterKeys;
    this.function = function;
  }

  @Override
  public List<K> getParameterKeys() {
    return parameterKeys;
  }

  @Override
  public V construct(final Object[] arguments) {
    return function.apply(arguments);
  }

}
