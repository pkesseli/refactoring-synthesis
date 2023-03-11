package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simplifies looking up {@link Component}s by identifying keys (e.g. types) and
 * sizes. Used by builders to construct objects of limited size.
 */
public class ComponentDirectory {

  /**
   * Provides information about which classes are involved in the component
   * library, e.g. components which invoke methods of a class.
   * 
   * TODO: The fact that this meta-information is necessary suggests that the idea
   * of a Java-agnostic component system is not precise enough. It would be best
   * to drop this approach and assume that supporting other languages in the
   * future will require language-specific instruction sets.
   */
  public final Set<String> InvolvedClasses = new HashSet<>();

  /**
   * Contains all registered {@link Component}s.
   */
  private final Map<Object, SortedMap<Integer, List<? extends Component<?, ?>>>> components = new HashMap<>();

  /**
   * Maps component keys to the minimux size necessary to construct it.
   */
  private final Map<Object, Integer> minSizes = new HashMap<>();

  /**
   * Provides a component matching a given identifier (e.g. types) and size.
   * 
   * @param <K>  Key type.
   * @param <V>  Component type.
   * @param key  Key identifying the categor of components.
   * @param size Maximum size of the component.
   * @return All components matching the given constraints.
   */
  @SuppressWarnings("unchecked")
  public <K, V> Stream<Component<K, V>> get(final K key, final int size) {
    final int maxSize = Math.max(size, minSizes.get(key));
    final int limit = maxSize == Integer.MAX_VALUE ? Integer.MAX_VALUE : maxSize + 1;
    final SortedMap<Integer, List<? extends Component<?, ?>>> result = components.get(key);
    return result.headMap(limit).values().stream().flatMap(Collection::stream)
        .map(component -> (Component<K, V>) component);
  }

  @SuppressWarnings("unchecked")
  public <K, V> Stream<Component<K, V>> get(final K key) {
    return components.get(key).values().stream().flatMap(List::stream).map(component -> (Component<K, V>) component);
  }

  /**
   * Registers a given component in the directory.
   * 
   * @param <K>       Key type.
   * @param <V>       Component type.
   * @param key       Category under which to store the component.
   * @param component Component to register.
   */
  public <K> void put(final K key, final Component<K, ?> component) {
    final List<? extends Component<?, ?>> bucket = getOrCreate(key, component.size());
    @SuppressWarnings("unchecked")
    final List<Component<K, ?>> converted = (List<Component<K, ?>>) bucket;
    converted.add(component);
  }

  /**
   * Adds all components to {@code this}.
   * 
   * TODO: This is inefficient and unnecessary. We should introduce a session
   * object to which we just keep adding temporary variable components.
   * 
   * @param componentDirectory Components to add.
   */
  public void putAll(final ComponentDirectory componentDirectory) {
    for (final Map.Entry<Object, SortedMap<Integer, List<? extends Component<?, ?>>>> entry : componentDirectory.components
        .entrySet()) {
      final Object key = entry.getKey();
      for (final Map.Entry<Integer, List<? extends Component<?, ?>>> sizedEntry : entry.getValue().entrySet()) {
        final int size = sizedEntry.getKey();
        @SuppressWarnings("unchecked")
        final List<Component<?, ?>> converted = (List<Component<?, ?>>) getOrCreate(key, size);
        converted.addAll(sizedEntry.getValue());
      }
    }
  }

  /**
   * Provides the existing container in {@code this} directory for components of
   * the given type and size, or creates one for this category of components.
   * 
   * @param <K>  Key type.
   * @param <V>  Component type.
   * @param key  Category of the desired component.
   * @param size Maximum length of the desired component.
   * @return Container for components with the specified properties.
   */
  private <K, V> List<? extends Component<?, ?>> getOrCreate(final K key, final int size) {
    minSizes.compute(key, (_1, previousSize) -> previousSize == null ? size : Math.min(size, previousSize));
    final SortedMap<Integer, List<? extends Component<?, ?>>> sizeBucket = components.computeIfAbsent(key,
        k -> new TreeMap<Integer, List<? extends Component<?, ?>>>());
    return sizeBucket.computeIfAbsent(size, k -> new ArrayList<>());
  }

  /**
   * Provides a filtered list of the provided keys, for which we can construct at
   * least one component of {@code maxSize} or less.
   * 
   * @param <K>     Key type.
   * @param allKeys All keys the synthesis phase is interested in.
   * @param maxSize Size for which a component must be constructed.
   * @return Filtered list with eligible keys.
   */
  public <K> List<K> getEligibleKeys(final List<K> allKeys, final int maxSize) {
    return allKeys.stream().filter(key -> minSizes.get(key) <= maxSize).collect(Collectors.toList());
  }

  /**
   * Provides all available component keys.
   * 
   * TODO: Consider making K a class type parameter.
   * 
   * @param <K>      Key type.
   * @param keyClass Key type class.
   * @return All available component keys.
   */
  public <K> Set<K> keySet(final Class<K> keyClass) {
    return components.entrySet().stream().map(Map.Entry::getKey).filter(keyClass::isInstance).map(keyClass::cast)
        .collect(Collectors.toSet());
  }

  /**
   * Provides all keys which are required to build the components in the library.
   * 
   * TODO: Consider making K a class type parameter.
   * 
   * @param <K>      Key type.
   * @param keyClass Key type class.
   * @return All parameter keys.
   */
  public <K> Set<K> parameterKeySet(final Class<K> keyClass) {
    return components.entrySet().stream().filter(entry -> keyClass.isInstance(entry.getKey())).map(Map.Entry::getValue)
        .map(Map::values).flatMap(Collection::stream).flatMap(List::stream).map(Component::getParameterKeys)
        .flatMap(List::stream).filter(keyClass::isInstance).map(keyClass::cast).collect(Collectors.toSet());
  }

  /**
   * Provides number of all components, across types and sizes.
   * 
   * @return Number of components.
   */
  public int size() {
    return components.entrySet().stream().map(Map.Entry::getValue).map(Map::entrySet).flatMap(Collection::stream)
        .map(Map.Entry::getValue).mapToInt(Collection::size).sum();
  }
}
