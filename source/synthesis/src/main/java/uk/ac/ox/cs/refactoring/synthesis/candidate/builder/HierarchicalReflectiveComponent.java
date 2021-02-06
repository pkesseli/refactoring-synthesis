package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class HierarchicalReflectiveComponent<T> implements HierarchicalComponent<T> {

  private final Constructor<T> constructor;

  @SuppressWarnings("unchecked")
  public HierarchicalReflectiveComponent(final Class<T> type) {
    this.constructor = (Constructor<T>) type.getDeclaredConstructors()[0];
    constructor.setAccessible(true);
  }

  @Override
  public List<Class<?>> getArgumentTypes() {
    return Arrays.asList(constructor.getParameterTypes());
  }

  @Override
  public T construct(final Object[] arguments) {
    try {
      return constructor.newInstance(arguments);
    } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }
}
