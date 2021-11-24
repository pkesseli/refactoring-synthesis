package uk.ac.ox.cs.refactoring.synthesis.state;

import java.util.function.Function;

import org.mockito.Mockito;
import org.objenesis.ObjenesisStd;

/**
 * Wrapper allowing the use of {@link ObjenesisStd#newInstance(Class)} without
 * reflection after loading it in an isolated class loader by mapping the shared
 * interface {@link Function} on the method. If multiplace classes need to be
 * loaded in this manner, it may be easier to switch to a controlled parent
 * class loader for the isolated class loaders and load
 * {@link org.objenesis.Objenesis} in that shared parent.
 */
public class ObjenesisWrapper implements Function<Class<?>, Object> {
  /**
   * Not using the interface {@link org.objenesis.Objenesis} to avoid double
   * virtual indirection.
   */
  private final ObjenesisStd objenesis = new ObjenesisStd();

  @Override
  public Object apply(final Class<?> clazz) {
    if (Polymorphism.canBeInstantiated(clazz)) {
      return objenesis.newInstance(clazz);
    }
    return Mockito.mock(clazz);
  }
}
