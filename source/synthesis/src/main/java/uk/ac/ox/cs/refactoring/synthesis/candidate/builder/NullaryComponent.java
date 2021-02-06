package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.Collections;
import java.util.List;

/**
 * 
 */
public class NullaryComponent<T> implements Component<T> {

  /**
   * 
   */
  private final T component;

  /**
   * 
   * @param component
   */
  public NullaryComponent(final T component) {
    this.component = component;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public List<Class<?>> getArgumentTypes() {
    return Collections.emptyList();
  }

  @Override
  public T construct(final Object[] arguments) {
    return component;
  }
}
