package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Simplifies looking up {@link Component}s by identifying keys (e.g. types) and
 * sizes. Used by builders to construct objects of limited size.
 */
public class ComponentDirectory {
  /**
   * Contains all registered {@link Component}s.
   */
  private final Map<Object, SortedMap<Integer, List<? extends Component<?, ?>>>> components = new HashMap<>();

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
    final SortedMap<Integer, List<? extends Component<?, ?>>> result = components.get(key);
    return result.headMap(size + 1).values().stream().flatMap(Collection::stream)
        .map(component -> (Component<K, V>) component);
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
   * @param <K> Key type.
   * @param <V> Component type.
   * @param key Category of the desired component.
   * @param size Maximum length of the desired component.
   * @return Container for components with the specified properties.
   */
  private <K, V> List<? extends Component<?, ?>> getOrCreate(final K key, final int size) {
    final SortedMap<Integer, List<? extends Component<?, ?>>> sizeBucket = components.computeIfAbsent(key,
        k -> new TreeMap<Integer, List<? extends Component<?, ?>>>());
    return sizeBucket.computeIfAbsent(size, k -> new ArrayList<>());
  }
}
