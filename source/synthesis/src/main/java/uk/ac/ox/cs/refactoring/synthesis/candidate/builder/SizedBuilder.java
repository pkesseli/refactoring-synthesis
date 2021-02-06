package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;
import java.util.function.Supplier;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * 
 */
public class SizedBuilder<T> implements Supplier<T> {

  /**
   * 
   */
  private static final int MIN_SIZE = 1;

  /**
   * 
   */
  private final ComponentDirectory components;

  /**
   * 
   */
  private final int maxSize;

  /**
   * 
   */
  private final Class<T> resultType;

  /**
   * 
   */
  private final SourceOfRandomness sourceOfRandomness;

  /**
   * 
   * @param components
   * @param maxSize
   * @param resultType
   * @param sourceOfRandomness
   */
  public SizedBuilder(final ComponentDirectory components, int maxSize, final Class<T> resultType,
      final SourceOfRandomness sourceOfRandomness) {
    this.components = components;
    this.maxSize = maxSize;
    this.resultType = resultType;
    this.sourceOfRandomness = sourceOfRandomness;
  }

  /**
   * 
   * @param <R>
   * @param type
   * @param size
   * @return
   */
  private <R> R generate(final Class<R> type, final int size) {
    final List<Component<R>> candidates = components.get(type, size);
    final int selection = sourceOfRandomness.nextInt(MIN_SIZE, candidates.size() - 1);
    final Component<R> component = candidates.get(selection);
    final List<Class<?>> argumentTypes = component.getArgumentTypes();
    final Object[] arguments = new Object[argumentTypes.size()];
    for (int i = 0; i < arguments.length; ++i) {
      arguments[i] = generate(argumentTypes.get(i), size - 1);
    }
    return component.construct(arguments);
  }

  @Override
  public T get() {
    final int size = sourceOfRandomness.nextInt(MIN_SIZE, maxSize);
    return generate(resultType, size);
  }
}
