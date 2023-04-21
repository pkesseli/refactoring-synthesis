package uk.ac.ox.cs.refactoring.synthesis.invocation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.CounterexampleGenerator;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Literals;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Polymorphism;

/**
 * Compares {@link ExecutionResult}s from two different heaps for equivalence.
 */
public final class HeapComparison {

  /** Sink for warnings about native {@link Object#equals(Object)} usage. */
  private static final Logger logger = LoggerFactory.getLogger(HeapComparison.class);

  /** Byte buddy package, whose classes are assumed not relevant to state. */
  private static final String[] PRUNED_PREFIXES = {
      "net.bytebuddy",
      "java.util.Random",
      "org.mockito.internal",
      "edu.berkeley.cs.jqf.fuzz.junit.quickcheck.InputStreamGenerator",
      "edu.berkeley.cs.jqf.fuzz.junit.quickcheck.FastSourceOfRandomness",
      "edu.berkeley.cs.jqf.fuzz.guidance.StreamBackedRandom",
      "edu.berkeley.cs.jqf.fuzz.ei.ZestGuidance"
  };

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
    if (lhs.Error != null) {
      if (rhs.Error == null || !lhs.Error.getClass().getName().equals(rhs.Error.getClass().getName()))
        return false;
    }

    final IsolatedClassLoader lhsClassLoader = lhs.ClassLoader;
    final IsolatedClassLoader rhsClassLoader = rhs.ClassLoader;
    loadMissingClasses(lhsClassLoader, rhsClassLoader);
    loadMissingClasses(rhsClassLoader, lhsClassLoader);

    final ObjectIdComparator comparator = new ObjectIdComparator();
    if (!equals(comparator, lhsClassLoader, lhs.Value, rhsClassLoader, rhs.Value)
        || !equals(comparator, lhsClassLoader, lhs.Instance, rhsClassLoader, rhs.Instance)) {
      return false;
    }

    for (final String className : lhsClassLoader.LoadedClasses) {
      if (!isRelevant(className))
        continue;

      final Class<?> lhsClass = ClassLoaders.loadClass(lhsClassLoader, className);
      final Class<?> rhsClass = ClassLoaders.loadClass(rhsClassLoader, className);
      if (!equals(comparator, lhsClassLoader, null, Fields.getStatic(lhsClass), rhsClassLoader, null,
          Fields.getStatic(rhsClass))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Prunes classes with well-known randomness which are irrelevant to the
   * program's state.
   * 
   * @param className {@link String} class name to check.
   * @return {@code true} if the class can be ignored, {@code false} otherwise.
   */
  private static boolean isRelevant(final String className) {
    return Arrays.stream(PRUNED_PREFIXES).noneMatch(className::startsWith);
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
  private static boolean equals(final ObjectIdComparator comparator, final ClassLoader lhsClassLoader, final Object lhs,
      final ClassLoader rhsClassLoader, final Object rhs)
      throws IllegalAccessException {
    if (lhs == null) {
      return rhs == null;
    }
    if (rhs == null) {
      return false;
    }

    if (comparator.wereAlreadyCompared(lhs, rhs)) {
      return true;
    }
    if (!isAliasingEquivalent(comparator, lhs, rhs)) {
      return false;
    }

    Class<?> lhsClass = lhs.getClass();
    if (Literals.isLiteralType(lhsClass)) {
      return lhs.equals(rhs);
    }
    Class<?> rhsClass = rhs.getClass();
    if (!isSameClass(lhsClass, rhsClass)) {
      return false;
    }

    // TODO maybe put it here
    // if (!isRelevant(lhsClass.getName())
    //       || Polymorphism.isMockitoCodegen(lhsClass)
    //       // || !CounterexampleGenerator.isSupported(typeResolver.resolve(declaringClass))
    //       || Polymorphism.isDynamic(lhsClass)) {
    //         // System.out.println("Skipping " + lhsClass.getName());
    //         return true;
    //       }

    if (shouldUseNativeEquals(lhsClassLoader, lhsClass))
      try {
        return lhs.equals(rhs);
      } catch (final Throwable e) {
        logger.warn("Could not use native equals operation.", e);
      }

    return equals(comparator, lhsClassLoader, lhs, Fields.getInstance(lhsClass), rhsClassLoader, rhs,
        Fields.getInstance(rhsClass));
  }

  /**
   * For JCL classes we trust {@link Object#equals(Object)} implementations, if
   * they do not throw and if the compared type explicitly declares it.
   * 
   * @param classLoader Used to decide if the class is from the JCL.
   * @param cls         {@link Class} to examine.
   * @return {@code true} if the class' {@link Object#equals(Object)} operation
   *         should be used.
   */
  static boolean shouldUseNativeEquals(final ClassLoader classLoader, final Class<?> cls) {
    if (ClassLoaders.isUserClass(classLoader, cls))
      return false;

    final Class<?> superclass = cls.getSuperclass();
    if (superclass == null || !"java.lang.Object".equals(superclass.getName()))
      return false;

    if (Arrays.stream(cls.getInterfaces()).map(Class::getName).noneMatch("java.lang.Comparable"::equals))
      return false;

    return Arrays.stream(cls.getDeclaredMethods()).map(Method::getName).anyMatch("equals"::equals);
  }

  /**
   * Indicates whether these two class objects model the same class, even if they
   * were loaded on different class loaders.
   * 
   * @param lhs {@link Class} to compared.
   * @param rhs {@link Class} to compared.
   * @return {@code true} if the same class, {@code false} otherwise.
   */
  private static boolean isSameClass(final Class<?> lhs, final Class<?> rhs) {
    return Polymorphism.getModelledClass(lhs).getName().equals(Polymorphism.getModelledClass(rhs).getName());
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
  private static boolean equals(final ObjectIdComparator comparator, final ClassLoader lhsClassLoader, final Object lhs,
      final Field[] lhsFields,
      final ClassLoader rhsClassLoader, final Object rhs, final Field[] rhsFields) throws IllegalAccessException {
    for (int i = 0; i < lhsFields.length; ++i) {
      final Field lhsField = lhsFields[i];
      final Class<?> declaringClass = lhsField.getDeclaringClass();
      final com.fasterxml.classmate.TypeResolver typeResolver = new com.fasterxml.classmate.TypeResolver();
      if (!isRelevant(declaringClass.getName())
          || Polymorphism.isMockitoCodegen(declaringClass)
          || !CounterexampleGenerator.isSupported(typeResolver.resolve(declaringClass))
          || Polymorphism.isDynamic(declaringClass)) 
          continue;
      final Field rhsField = rhsFields[i];
      lhsField.setAccessible(true);
      rhsField.setAccessible(true);
      if (!equals(comparator, lhsClassLoader, lhsField.get(lhs), rhsClassLoader, rhsField.get(rhs))) {
        // System.out.println(lhs.getClass().toString() + " -> " + lhsField.toString());
        // System.out.println("here");
        // System.out.println("LHS: " + lhsField.get(lhs).toString() + ", RHS: " + rhsField.get(rhs));
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

  private HeapComparison() {
  }
}
