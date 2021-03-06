package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helper to generate and compare abstract reference IDs in order to compare
 * object aliasing from two different heaps.
 * 
 * TODO: We need to load more classes in the isolated class loaders for this to
 * work reliably. For classes loaded by the parent (usually system) class
 * loader, we resort to reference equality, which is far too strict if entity
 * classes such as dates or strings are loaded by the system class loader.
 */
public class ObjectIdComparator {
  /**
   * Maps {@link System#identityHashCode(Object) system hash codes} of
   * <code>lhs</code> heap to abstract reference ids.
   */
  private final Map<Integer, Integer> lhsIds = new HashMap<>();

  /**
   * {@link #lhsIds}
   */
  private final Map<Integer, Integer> rhsIds = new HashMap<>();

  /**
   * Checks whether the two given objects have the same aliasing ID, and thus
   * alias with the same objects on their respective heaps.
   * 
   * @param lhs Left hand side object to compare.
   * @param rhs Right hand side object to compare.
   * @throws NullPointerException if either {@code lhs} or {@code rhs} are
   *                              {@code null}.
   */
  public boolean hasSameId(final Object lhs, final Object rhs) {
    Objects.requireNonNull(lhs);
    Objects.requireNonNull(rhs);
    return getId(lhsIds, lhs) == getId(rhsIds, rhs);
  }

  /**
   * Provides an abstract, heap-neutral id for the given object.
   * 
   * @param ids    {@link #lhsIds}
   * @param object Object for which to provide an id.
   * @return Heap-neutral id associated with the given object.
   */
  private static int getId(final Map<Integer, Integer> ids, final Object object) {
    final int identityHash = System.identityHashCode(object);
    return ids.computeIfAbsent(identityHash, k -> ids.size());
  }
}
