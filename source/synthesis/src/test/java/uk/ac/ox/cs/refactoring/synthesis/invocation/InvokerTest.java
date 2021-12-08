package uk.ac.ox.cs.refactoring.synthesis.invocation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.IsolatedObject;

public class InvokerTest {
  @Test
  void max() throws Exception {
    final ClassLoader counterexampleClassLoader = ClassLoaders.createIsolated();
    final IsolatedObject max = new IsolatedObject(counterexampleClassLoader, Benchmarks.SUM);
    final IsolatedObject min = new IsolatedObject(counterexampleClassLoader, Benchmarks.INTEGER_WRAPPER);
    min.setField("value", 3);
    max.setField("base", min.Object);
    final Counterexample counterexample = new Counterexample(max.Object);
    final IsolatedObject lhs = new IsolatedObject(counterexampleClassLoader, Benchmarks.INTEGER_WRAPPER);
    lhs.setField("value", 10);
    final IsolatedObject rhs = new IsolatedObject(counterexampleClassLoader, Benchmarks.INTEGER_WRAPPER);
    rhs.setField("value", 20);
    counterexample.Arguments.add(lhs.Object);
    counterexample.Arguments.add(rhs.Object);

    final Invoker invoker = new Invoker(new MethodIdentifier(Benchmarks.SUM, "max",
        Arrays.asList(Benchmarks.INTEGER_WRAPPER, Benchmarks.INTEGER_WRAPPER)));
    final int result = (int) invoker.invoke(counterexample).Value;
    assertEquals(33, result);
  }
}
