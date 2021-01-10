package uk.ac.ox.cs.refactoring.synthesis.invocation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.factory.ObjectFactory;

public class HeapComparisonTest {
  private final IsolatedClassLoader lhsClassLoader = ClassLoaders.createIsolated();
  private final IsolatedClassLoader rhsClassLoader = ClassLoaders.createIsolated();

  @Test
  void singleInt() throws Exception {
    final IntConsumer lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.STATIC_INT_CONSUMER);
    final IntConsumer rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.STATIC_INT_CONSUMER);
    final ExecutionResult lhsResult = new ExecutionResult(lhs);
    final ExecutionResult rhsResult = new ExecutionResult(rhs);

    lhs.accept(10);
    rhs.accept(10);
    assertTrue(HeapComparison.equals(lhsClassLoader, lhsResult, rhsClassLoader, rhsResult));
    rhs.accept(11);
    assertFalse(HeapComparison.equals(lhsClassLoader, lhsResult, rhsClassLoader, rhsResult));
  }

  @Test
  void singleInteger() throws Exception {
    final Consumer<Integer> lhs = ObjectFactory.create(lhsClassLoader, Benchmarks.STATIC_INTEGER_CONSUMER);
    final Consumer<Integer> rhs = ObjectFactory.create(rhsClassLoader, Benchmarks.STATIC_INTEGER_CONSUMER);
    final ExecutionResult lhsResult = new ExecutionResult(lhs);
    final ExecutionResult rhsResult = new ExecutionResult(rhs);

    Callable<Boolean> evaluate = () -> HeapComparison.equals(lhsClassLoader, lhsResult, rhsClassLoader, rhsResult);
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
    final ExecutionResult lhsResult = new ExecutionResult(lhs);
    final ExecutionResult rhsResult = new ExecutionResult(rhs);
    final Integer first = 129;
    final Integer second = 129;

    Callable<Boolean> evaluate = () -> HeapComparison.equals(lhsClassLoader, lhsResult, rhsClassLoader, rhsResult);
    lhs.accept(first, second);
    rhs.accept(first, first);
    assertFalse(evaluate.call());
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
    final ExecutionResult lhsResult = new ExecutionResult(lhs);
    final ExecutionResult rhsResult = new ExecutionResult(rhs);

    Callable<Boolean> evaluate = () -> HeapComparison.equals(lhsClassLoader, lhsResult, rhsClassLoader, rhsResult);
    lhs.accept(lhsFirst, lhsSecond);
    rhs.accept(rhsFirst, rhsFirst);
    assertFalse(evaluate.call());
    rhs.accept(rhsFirst, rhsSecond);
    assertTrue(evaluate.call());
  }
}
