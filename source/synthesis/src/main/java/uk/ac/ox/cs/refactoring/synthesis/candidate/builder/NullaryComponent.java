package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.Collections;
import java.util.List;

/**
 * Component without arguments constructing a pre-determined value.
 */
public class NullaryComponent<K, V> implements Component<K, V> {

  /**
   * Component to be provided.
   */
  private final V component;

  /**
   * @param component {@link #component}
   */
  public NullaryComponent(final V component) {
    this.component = component;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public List<K> getParameterKeys() {
    return Collections.emptyList();
  }

  @Override
  public V construct(final Object[] arguments) {
    return component;
  }
}
