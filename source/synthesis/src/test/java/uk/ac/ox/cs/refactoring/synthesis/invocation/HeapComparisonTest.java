package uk.ac.ox.cs.refactoring.synthesis.invocation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ObjenesisFactory;
import uk.ac.ox.cs.refactoring.synthesis.factory.ObjectFactory;

public class HeapComparisonTest {
  private final IsolatedClassLoader lhsClassLoader = ClassLoaders.createIsolated();
  private final IsolatedClassLoader rhsClassLoader = ClassLoaders.createIsolated();

  @Test
  void singleInt() throws Exception {
    final IntConsumer lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.STATIC_INT_CONSUMER);
    final IntConsumer rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.STATIC_INT_CONSUMER);
    final ExecutionResult lhsResult = new ExecutionResult(lhsClassLoader, lhs, null);
    final ExecutionResult rhsResult = new ExecutionResult(rhsClassLoader, rhs, null);

    lhs.accept(10);
    rhs.accept(10);
    assertTrue(HeapComparison.equals(lhsResult, rhsResult));
    rhs.accept(11);
    assertFalse(HeapComparison.equals(lhsResult, rhsResult));
  }

  @Test
  void singleInteger() throws Exception {
    final Consumer<Integer> lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.STATIC_INTEGER_CONSUMER);
    final Consumer<Integer> rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.STATIC_INTEGER_CONSUMER);
    final ExecutionResult lhsResult = new ExecutionResult(lhsClassLoader, lhs, null);
    final ExecutionResult rhsResult = new ExecutionResult(rhsClassLoader, rhs, null);

    Callable<Boolean> evaluate = () -> HeapComparison.equals(lhsResult, rhsResult);
    lhs.accept(10);
    rhs.accept(10);
    assertTrue(evaluate.call());
    rhs.accept(11);
    assertFalse(evaluate.call());
    rhs.accept(null);
    assertFalse(evaluate.call());
    lhs.accept(null);
    assertTrue(evaluate.call());
    rhs.accept(11);
    assertFalse(evaluate.call());
  }

  @Test
  void biIntegerAliasing() throws Exception {
    final BiConsumer<Integer, Integer> lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.INTEGER_ALIASING);
    final BiConsumer<Integer, Integer> rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.INTEGER_ALIASING);
    final ExecutionResult lhsResult = new ExecutionResult(lhsClassLoader, lhs, null);
    final ExecutionResult rhsResult = new ExecutionResult(rhsClassLoader, rhs, null);
    final Integer first = 129;
    final Integer second = 129;

    Callable<Boolean> evaluate = () -> HeapComparison.equals(lhsResult, rhsResult);
    lhs.accept(first, second);
    rhs.accept(first, first);
    assertTrue(evaluate.call());
    rhs.accept(first, second);
    assertTrue(evaluate.call());
  }

  @Test
  void biObjectAliasing() throws Exception {
    final BiConsumer<Object, Object> lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.OBJECT_ALIASING);
    final Object lhsFirst = ObjectFactory.create(lhsClassLoader, Benchmarks.OBJECT_ALIASING);
    final Object lhsSecond = ObjectFactory.create(lhsClassLoader, Benchmarks.OBJECT_ALIASING);
    final BiConsumer<Object, Object> rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.OBJECT_ALIASING);
    final Object rhsFirst = ObjectFactory.create(rhsClassLoader, Benchmarks.OBJECT_ALIASING);
    final Object rhsSecond = ObjectFactory.create(rhsClassLoader, Benchmarks.OBJECT_ALIASING);
    final ExecutionResult lhsResult = new ExecutionResult(lhsClassLoader, lhs, null);
    final ExecutionResult rhsResult = new ExecutionResult(rhsClassLoader, rhs, null);

    Callable<Boolean> evaluate = () -> HeapComparison.equals(lhsResult, rhsResult);
    lhs.accept(lhsFirst, lhsSecond);
    rhs.accept(rhsFirst, rhsFirst);
    assertFalse(evaluate.call());
    rhs.accept(rhsFirst, rhsSecond);
    assertTrue(evaluate.call());
  }

  @Test
  void sameExceptionType() throws Exception {
    final NullPointerException lhs = new NullPointerException();
    try {
      Paths.get(null);
    } catch (final NullPointerException rhs) {
      assertTrue(
          HeapComparison.equals(new ExecutionResult(lhsClassLoader, null, lhs),
              new ExecutionResult(rhsClassLoader, null, rhs)));
    }
  }

  @Test
  void recursion() throws Exception {
    final BiConsumer<Object, Object> lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.OBJECT_ALIASING);
    lhs.accept(lhs, lhs);
    final BiConsumer<Object, Object> rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.OBJECT_ALIASING);
    rhs.accept(rhs, rhs);
    assertTrue(HeapComparison.equals(new ExecutionResult(lhsClassLoader, lhs, null),
        new ExecutionResult(rhsClassLoader, rhs, null)));
  }

  @Test
  void interfaceMock() throws Exception {
    final Object lhs = ObjenesisFactory.createObjenesis(lhsClassLoader).apply(ItemListener.class);
    final Object rhs = ObjenesisFactory.createObjenesis(rhsClassLoader).apply(ItemListener.class);
    assertTrue(HeapComparison.equals(new ExecutionResult(lhsClassLoader, lhs, null),
        new ExecutionResult(rhsClassLoader, rhs, null)));

    final Object other = new Object();
    assertFalse(HeapComparison.equals(new ExecutionResult(lhsClassLoader, other, null),
        new ExecutionResult(rhsClassLoader, rhs, null)));
  }

  @Test
  void classMock() throws Exception {
    final Object lhs = ObjenesisFactory.createObjenesis(lhsClassLoader).apply(MouseAdapter.class);
    final Object rhs = ObjenesisFactory.createObjenesis(rhsClassLoader).apply(MouseAdapter.class);
    assertTrue(HeapComparison.equals(new ExecutionResult(lhsClassLoader, lhs, null),
        new ExecutionResult(rhsClassLoader, rhs, null)));

    final Object other = new Object();
    assertFalse(HeapComparison.equals(new ExecutionResult(lhsClassLoader, other, null),
        new ExecutionResult(rhsClassLoader, rhs, null)));
  }

  @Test
  void shouldUseNativeEquals() {
    final ClassLoader classLoader = HeapComparisonTest.class.getClassLoader();
    assertFalse(HeapComparison.shouldUseNativeEquals(classLoader, HeapComparisonTest.class));
    assertFalse(HeapComparison.shouldUseNativeEquals(classLoader, Object.class));
    assertFalse(HeapComparison.shouldUseNativeEquals(classLoader, Vector.class));
    assertTrue(HeapComparison.shouldUseNativeEquals(classLoader, Date.class));
  }
}
