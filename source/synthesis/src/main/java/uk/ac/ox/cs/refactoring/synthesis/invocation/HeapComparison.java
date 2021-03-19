package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Literals;

/**
 * Compares {@link ExecutionResult}s from two different heaps for equivalence.
 */
public final class HeapComparison {
  private HeapComparison() {
  }

  /**
   * Compares two {@link ExecutionResult} executed in two different
   * {@link IsolatedClassLoader}s for equivalence.
   * 
   * @param lhs First {@link ExecutionResult} to compare.
   * @param rhs Second {@link ExecutionResult} to compare.
   * @return {@code true} if the two executions mark an equivalent result,
   *         {@code false}.
   * @throws ClassNotFoundException if a class loaded by one
   *                                {@link IsolatedClassLoader} cannot be loaded
   *                                in the other.
   * @throws IllegalAccessException if an object field cannot be read reflectively
   *                                for comparison
   */
  public static boolean equals(final ExecutionResult lhs, final ExecutionResult rhs)
      throws ClassNotFoundException, IllegalAccessException {
    if (lhs.Error == null && rhs.Error != null) {
      return false;
    }

    final IsolatedClassLoader lhsClassLoader = lhs.ClassLoader;
    final IsolatedClassLoader rhsClassLoader = rhs.ClassLoader;
    loadMissingClasses(lhsClassLoader, rhsClassLoader);
    loadMissingClasses(rhsClassLoader, lhsClassLoader);

    final ObjectIdComparator comparator = new ObjectIdComparator();
    if (!equals(comparator, lhs.Error, rhs.Error)) {
      return false;
    }
    if (!equals(comparator, lhs.Value, rhs.Value)) {
      return false;
    }

    for (final String className : lhsClassLoader.LoadedClasses) {
      final Class<?> lhsClass = lhsClassLoader.loadClass(className);
      final Class<?> rhsClass = rhsClassLoader.loadClass(className);
      if (!equals(comparator, null, Fields.getStatic(lhsClass), null, Fields.getStatic(rhsClass))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Compares two objects within two different execution results on two different
   * heaps for equivalence.
   * 
   * @param comparator {@link ObjectIdComparator} to evaluate aliasing
   *                   equivalence.
   * @param lhs        First object to compare.
   * @param rhs        Second object to compare.
   * @return {@code true} if the two objects are equivalent on their respeceive
   *         heaps, {@code false} otherwise.
   * @throws IllegalAccessException if a field cannot be accessed reflectively for
   *                                value comparison.
   */
  private static boolean equals(final ObjectIdComparator comparator, final Object lhs, final Object rhs)
      throws IllegalAccessException {
    if (lhs == null) {
      return rhs == null;
    }
    if (rhs == null) {
      return false;
    }

    Class<?> lhsClass = lhs.getClass();
    if (!isAliasingEquivalent(comparator, lhs, rhs)) {
      return false;
    }

    if (Literals.isLiteralType(lhsClass)) {
      return Objects.equals(lhs, rhs);
    }
    Class<?> rhsClass = rhs.getClass();
    while (lhsClass != null) {
      if (!lhsClass.getName().equals(rhsClass.getName())) {
        return false;
      }
      if (!equals(comparator, lhs, Fields.getInstance(lhsClass), rhs, Fields.getInstance(rhsClass))) {
        return false;
      }
      lhsClass = lhsClass.getSuperclass();
      rhsClass = rhsClass.getSuperclass();
    }

    return true;
  }

  /**
   * Compares two sets of fields for equivalence.
   * 
   * @param comparator {@link ObjectIdComparator} to evaluate aliasing equivalence
   *                   of field values.
   * @param lhs        First object whose fields to compare. {@code null} for
   *                   static fields.
   * @param lhsFields  All fields of the first object.
   * @param rhs        Second object whose fields to compare. {@code null} for
   *                   static fields.
   * @param rhsFields  All fields of the second object.
   * @return {@code true} if the objects' fields are equivalent on their
   *         respective heaps.
   * @throws IllegalAccessException if a field cannot be accessed.
   */
  private static boolean equals(final ObjectIdComparator comparator, final Object lhs, final Field[] lhsFields,
      final Object rhs, final Field[] rhsFields) throws IllegalAccessException {
    for (int i = 0; i < lhsFields.length; ++i) {
      final Field lhsField = lhsFields[i];
      final Field rhsField = rhsFields[i];
      lhsField.setAccessible(true);
      rhsField.setAccessible(true);
      if (!equals(comparator, lhsField.get(lhs), rhsField.get(rhs))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the two objects' references are equivalent with respect to aliasing
   * on their respective heaps.
   * 
   * @param comparator {@link ObjectIdComparator} to evaluate aliasing equivalence
   *                   of field values.
   * @param lhs        First object to compare.
   * @param rhs        Second object to compare.
   * @return {@code true} if the two objects alias the same references on their
   *         respective heaps or are primitive types, {@code false} otherwise.
   */
  private static boolean isAliasingEquivalent(final ObjectIdComparator comparator, final Object lhs, final Object rhs) {
    Class<?> lhsClass = lhs.getClass();
    return ClassUtils.isPrimitiveOrWrapper(lhsClass) || comparator.hasSameId(lhs, rhs);
  }

  /**
   * Ensures that all classes which were loaded in {@code rhs} are also loaded in
   * {@code rhs}.
   * 
   * @param lhs {@link IsolatedClassLoader} in which to load missing classes.
   * @param rhs {@link IsolatedClassLoader} whose classes all need to be present
   *            in {@code lhs}.
   * @throws ClassNotFoundException if loading a missing class fails.
   */
  private static void loadMissingClasses(final IsolatedClassLoader lhs, final IsolatedClassLoader rhs)
      throws ClassNotFoundException {
    final Set<String> missing = new HashSet<>(rhs.LoadedClasses);
    missing.removeAll(lhs.LoadedClasses);
    for (final String missingClass : missing) {
      lhs.loadClass(missingClass);
    }
  }
}
