package uk.ac.ox.cs.refactoring.synthesis.candidate.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 */
public class ComponentDirectory {
  /**
   * 
   */
  private final Map<Class<?>, List<? extends Component<?>>> components = new HashMap<>();

  /**
   * 
   * @param <T>
   * @param key
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> List<Component<T>> get(final Class<T> key, final int size) {
    final List<? extends Component<?>> result = components.get(key);
    return result.stream().filter(component -> component.size() <= size).map(component -> (Component<T>) component)
        .collect(Collectors.toList());
  }

  /**
   * 
   * @param <T>
   * @param key
   * @param component
   */
  public <T> void put(final Class<T> key, final Component<? extends T> component) {
    final List<? extends Component<?>> bucket = components.computeIfAbsent(key,
        k -> new ArrayList<Component<? extends T>>());
    @SuppressWarnings("unchecked")
    final List<Component<? extends T>> converted = (List<Component<? extends T>>) bucket;
    converted.add(component);
  }
}
