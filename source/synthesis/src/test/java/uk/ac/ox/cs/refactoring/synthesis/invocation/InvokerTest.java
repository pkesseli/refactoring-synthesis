package uk.ac.ox.cs.refactoring.synthesis.invocation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ObjectDescription;

public class InvokerTest {
  @Test
  void max() throws Exception {
    final ObjectDescription max = new ObjectDescription(Benchmarks.SUM);
    final ObjectDescription min = new ObjectDescription(Benchmarks.INTEGER_WRAPPER);
    min.LiteralFields.put("value", 3);
    max.ObjectFields.put("base", min);
    final Counterexample counterexample = new Counterexample(max);
    final ObjectDescription lhs = new ObjectDescription(Benchmarks.INTEGER_WRAPPER);
    lhs.LiteralFields.put("value", 10);
    final ObjectDescription rhs = new ObjectDescription(Benchmarks.INTEGER_WRAPPER);
    rhs.LiteralFields.put("value", 20);
    counterexample.ObjectArguments.put(0, lhs);
    counterexample.ObjectArguments.put(1, rhs);

    final Invoker invoker = new Invoker(Benchmarks.SUM, "max",
        Arrays.asList(Benchmarks.INTEGER_WRAPPER, Benchmarks.INTEGER_WRAPPER));
    final int result = (int) invoker.invoke(counterexample).Value;
    assertEquals(33, result);
  }
}
