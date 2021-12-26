package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
  /** Maps objects by reference equality to abstract reference ids. */
  private final Map<Object, Integer> lhsIds = new IdentityHashMap<>();

  /** {@link #lhsIds} */
  private final Map<Object, Integer> rhsIds = new IdentityHashMap<>();

  /** Stores already performed comparisons, avoiding recursion. */
  private final Map<Object, Set<Object>> comparisons = new IdentityHashMap<>();

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
   * Indicates that both objects have already been compared, meaning that a
   * subsequent comparison should default to {@code true}. If the two objects are
   * not identical, heap comparison will fail anyway, and there is no need to
   * repeat the comparison, especially since this might cause infinite recursion.
   * 
   * @param lhs Left hand side object to compare.
   * @param rhs Right hand side object to compare.
   * @return {@code true} if both objects were compared before, {@code false}
   *         othwerwise.
   */
  public boolean wereAlreadyCompared(final Object lhs, final Object rhs) {
    Objects.requireNonNull(lhs);
    Objects.requireNonNull(rhs);
    final boolean wasLhsComparedAgainstRhs = addComparison(lhs, rhs);
    final boolean wasRhsComparedAgainstLhs = addComparison(rhs, lhs);
    return wasLhsComparedAgainstRhs || wasRhsComparedAgainstLhs;
  }

  /**
   * Marks that {@code lhs == rhs} has already been performed and need not be
   * evaluated again. This is not aiming to improve performance, but rather
   * prevent infinite recursion when comparing self-referencing objects.
   * 
   * @param lhs First object to be compared.
   * @param rhs Second object to be compared.
   * @return {@code true} if this operation was performed before, {@link false}
   *         otherwise.
   */
  private boolean addComparison(final Object lhs, final Object rhs) {
    final Set<Object> lhsComparisons = comparisons.get(lhs);
    if (lhsComparisons == null) {
      final Set<Object> newComparisons = Collections.newSetFromMap(new IdentityHashMap<>());
      newComparisons.add(rhs);
      comparisons.put(lhs, newComparisons);
      return false;
    }
    return !lhsComparisons.add(rhs);
  }

  /**
   * Provides an abstract, heap-neutral id for the given object.
   * 
   * @param ids    {@link #lhsIds} or {@link #rhsIds}.
   * @param object Object for which to provide an id.
   * @return Heap-neutral id associated with the given object.
   */
  private static int getId(final Map<Object, Integer> ids, final Object object) {
    return ids.computeIfAbsent(object, k -> ids.size());
  }
}
