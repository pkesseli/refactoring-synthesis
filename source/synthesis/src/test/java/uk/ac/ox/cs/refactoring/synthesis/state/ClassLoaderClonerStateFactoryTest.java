package uk.ac.ox.cs.refactoring.synthesis.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.IsolatedObject;

public class ClassLoaderClonerStateFactoryTest {

  private final StateFactory stateFactory = new ClassLoaderClonerStateFactory();

  private final ClassLoader counterexampleClassLoader = ClassLoaders.createIsolated();

  private final ClassLoader classLoader = ClassLoaders.createIsolated();

  @Test
  void isolation() throws Exception {
    final IsolatedObject empty = new IsolatedObject(counterexampleClassLoader, Benchmarks.EMPTY);
    final Counterexample counterexample = new Counterexample(empty.Object);
    final State lhs = stateFactory.create(ClassLoaders.createIsolated(), counterexample);
    final State rhs = stateFactory.create(ClassLoaders.createIsolated(), counterexample);
    assertNotSame(lhs.Instance.getClass(), rhs.Instance.getClass());
  }

  @Test
  void integerWrapper() throws Exception {
    final IsolatedObject object = new IsolatedObject(counterexampleClassLoader, Benchmarks.INTEGER_WRAPPER);
    object.setField("value", 37);
    final Counterexample counterexample = new Counterexample(object.Object);
    final State state = stateFactory.create(classLoader, counterexample);
    final Supplier<?> supplier = (Supplier<?>) state.Instance;
    assertEquals(37, supplier.get());
  }

  @Test
  void objenesis() throws Exception {
    final IsolatedObject objenesis = new IsolatedObject(classLoader, "org.objenesis.ObjenesisStd");
    assertSame(classLoader, objenesis.Object.getClass().getClassLoader());
  }
}
