package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.classloader.IsolatedClassLoader;
import uk.ac.ox.cs.refactoring.synthesis.invocation.ExecutionResult;

class ConsoleCounterexampleListenerTest {

  private static final IsolatedClassLoader classLoader = ClassLoaders.createIsolated();

  @Test
  void staticNoArgsNoFieldsCe() {
    assertEquals("Counterexample { Instance = null }",
        ConsoleCounterexampleListener.toString(new Counterexample(null)));
  }

  @Test
  void instanceNoArgsNoFieldsCe() {
    assertEquals("Counterexample { Instance = the-instance }",
        ConsoleCounterexampleListener.toString(new Counterexample("the-instance")));
  }

  @Test
  void instanceNoFieldsCe() {
    final Counterexample counterexample = new Counterexample("the-instance");
    Collections.addAll(counterexample.Arguments, 0, 1, 2);
    assertEquals("Counterexample { \r\n" +
        "  Instance = the-instance\r\n" +
        "  Arguments = [0, 1, 2]\r\n" +
        "}", ConsoleCounterexampleListener.toString(counterexample));
  }

  @Test
  void instanceWithArgsAndFieldsCe() {
    final Counterexample counterexample = new Counterexample("the-instance");
    Collections.addAll(counterexample.Arguments, 0, 1, 2);
    counterexample.Fields.put("Class1.FIELD", "10");
    assertEquals("Counterexample { \r\n" +
        "  Instance = the-instance\r\n" +
        "  Arguments = [0, 1, 2]\r\n" +
        "  Fields = {Class1.FIELD=10}\r\n" +
        "}", ConsoleCounterexampleListener.toString(counterexample));
  }

  @Test
  void staticException() {
    assertEquals("{ Exception = java.lang.NullPointerException }",
        ConsoleCounterexampleListener.toString(new ExecutionResult(classLoader, null, new NullPointerException())));
  }

  @Test
  void instanceReturn() {
    assertEquals("{ Instance = the-instance, Return = the-return }",
        ConsoleCounterexampleListener.toString(new ExecutionResult(classLoader, "the-instance", "the-return")));
  }
}
