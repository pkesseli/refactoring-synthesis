package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.List;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.candidate.random.RandomnessAccessor;

/**
 * Builder constructing {@link Component}s of limited size.
 */
public class SizedBuilder<K, V> implements Builder<K, V> {

  /**
   * Minimum size of a constructed component.
   */
  private static final int MIN_SIZE = 1;

  /**
   * Registered component buliding blocks.
   */
  private final ComponentDirectory components;

  /**
   * Maximum size of a constructed component.
   */
  private final int maxSize;

  /**
   * @param components {@link #components}
   * @param maxSize    {@link #maxSize}
   */
  public SizedBuilder(final ComponentDirectory components, final int maxSize) {
    this.components = components;
    this.maxSize = maxSize;
  }

  /**
   * Generates a component of the desired type and size.
   * 
   * @param sourceOfRandomness guiding all choices between component building
   *                           blocks.
   * @param key                Component type to construct.
   * @param size               Maximum size of component to construct.
   * @return {@link Component#construct(Object[]) Constructed} component.
   */
  private Object generate(final SourceOfRandomness sourceOfRandomness, final K key, final int size,
      final ComponentDirectory components) {
    final List<Component<K, ?>> candidates = components.get(key, size).collect(Collectors.toList());
    final int maxIndex = candidates.size() - 1;
    final int selection = RandomnessAccessor.nextInt(sourceOfRandomness, maxIndex);
    final Component<K, ?> component = candidates.get(selection);
    final List<K> argumentTypes = component.getParameterKeys();
    final Object[] arguments = new Object[argumentTypes.size()];
    for (int i = 0; i < arguments.length; ++i) {
      arguments[i] = generate(sourceOfRandomness, argumentTypes.get(i), size - 1, components);
    }
    return component.construct(arguments);
  }

  @Override
  public V build(final SourceOfRandomness sourceOfRandomness, final List<K> resultKeys,
      final ComponentDirectory extraComponents) {
    final ComponentDirectory allComponents = new ComponentDirectory();
    allComponents.putAll(components);
    allComponents.putAll(extraComponents);

    final List<K> eligibleKeys = allComponents.getEligibleKeys(resultKeys, maxSize);
    final int resultKeyIndex = RandomnessAccessor.nextInt(sourceOfRandomness, eligibleKeys.size() - 1);
    final K resultKey = eligibleKeys.get(resultKeyIndex);
    final int size = MIN_SIZE + RandomnessAccessor.nextInt(sourceOfRandomness, maxSize);
    @SuppressWarnings("unchecked")
    final V result = (V) generate(sourceOfRandomness, resultKey, size, allComponents);
    return result;
  }
}
