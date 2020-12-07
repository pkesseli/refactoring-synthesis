package uk.ac.ox.cs.refactoring.synthesis.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.benchmark.Benchmarks;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.Counterexample;
import uk.ac.ox.cs.refactoring.synthesis.counterexample.ObjectDescription;

public class ObjenesisStateFactoryTest {
  private final IStateFactory stateFactory = new ObjenesisStateFactory();

  private final ClassLoader classLoader = ClassLoaders.createIsolated();

  @Test
  void isolation() throws Exception {
    final Counterexample counterexample = new Counterexample(new ObjectDescription(Benchmarks.EMPTY));
    final State lhs = stateFactory.create(ClassLoaders.createIsolated(), counterexample);
    final State rhs = stateFactory.create(ClassLoaders.createIsolated(), counterexample);
    assertNotSame(lhs.Instance.getClass(), rhs.Instance.getClass());
  }

  @Test
  void integerWrapper() throws Exception {
    final ObjectDescription objectDescription = new ObjectDescription(Benchmarks.INTEGER_WRAPPER);
    objectDescription.LiteralFields.put("value", 37);
    final Counterexample counterexample = new Counterexample(objectDescription);
    final State state = stateFactory.create(classLoader, counterexample);
    final Supplier<?> supplier = (Supplier<?>) state.Instance;
    assertEquals(37, supplier.get());
  }

  @Test
  void integerWrapperInvalidMember() throws Exception {
    final ObjectDescription objectDescription = new ObjectDescription(Benchmarks.INTEGER_WRAPPER);
    objectDescription.LiteralFields.put("non-existing", 37);
    final Counterexample counterexample = new Counterexample(objectDescription);
    assertThrows(NoSuchFieldException.class, () -> stateFactory.create(classLoader, counterexample));
  }

  @Test
  void objenesis() throws Exception {
    final ObjectDescription objenesis = new ObjectDescription("org.objenesis.ObjenesisStd");
    final State state = stateFactory.create(classLoader, new Counterexample(objenesis));
    assertSame(classLoader, state.Instance.getClass().getClassLoader());
  }
}
