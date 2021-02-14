package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * Builder constructing {@link Component}s of limited size.
 */
public class SizedBuilder<K, V> implements Supplier<V> {

  /**
   * Minimum size of a constructed component.
   */
  private static final int MIN_SIZE = 1;

  /**
   * Registered component buliding blocks.
   */
  private final ComponentDirectory components;

  /**
   * Maximum size of a constructoed component.
   */
  private final int maxSize;

  /**
   * Category of the desired result component.
   */
  private final K resultKey;

  /**
   * {@link SourceOfRandomness} guiding all choices between component building
   * blocks.
   */
  private final SourceOfRandomness sourceOfRandomness;

  /**
   * @param components         {@link #components}
   * @param maxSize            {@link #maxSize}
   * @param resultKey          {@link #resultKey}
   * @param sourceOfRandomness {@link #sourceOfRandomness}
   */
  public SizedBuilder(final ComponentDirectory components, int maxSize, final K resultKey,
      final SourceOfRandomness sourceOfRandomness) {
    this.components = components;
    this.maxSize = maxSize;
    this.resultKey = resultKey;
    this.sourceOfRandomness = sourceOfRandomness;
  }

  /**
   * Generates a component of the desired type and size.
   * 
   * @param key  Component type to construct.
   * @param size Maximum size of component to construct.
   * @return {@link Component#construct(Object[]) Constructed} component.
   */
  private Object generate(final K key, final int size) {
    final List<Component<K, ?>> candidates = components.get(key, size).collect(Collectors.toList());
    final int selection = sourceOfRandomness.nextInt(0, candidates.size() - 1);
    final Component<K, ?> component = candidates.get(selection);
    final List<K> argumentTypes = component.getParameterKeys();
    final Object[] arguments = new Object[argumentTypes.size()];
    for (int i = 0; i < arguments.length; ++i) {
      arguments[i] = generate(argumentTypes.get(i), size - 1);
    }
    return component.construct(arguments);
  }

  @Override
  public V get() {
    final int size = sourceOfRandomness.nextInt(MIN_SIZE, maxSize);
    @SuppressWarnings("unchecked")
    final V result = (V) generate(resultKey, size);
    return result;
  }
}
