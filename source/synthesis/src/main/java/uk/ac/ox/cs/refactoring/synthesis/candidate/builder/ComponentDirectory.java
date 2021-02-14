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
    final SortedMap<Integer, List<? extends Component<?, ?>>> sizeBucket = components.computeIfAbsent(key,
        k -> new TreeMap<Integer, List<? extends Component<?, ?>>>());
    final List<? extends Component<?, ?>> bucket = sizeBucket.computeIfAbsent(component.size(), k -> new ArrayList<>());
    @SuppressWarnings("unchecked")
    final List<Component<K, ?>> converted = (List<Component<K, ?>>) bucket;
    converted.add(component);
  }
}
