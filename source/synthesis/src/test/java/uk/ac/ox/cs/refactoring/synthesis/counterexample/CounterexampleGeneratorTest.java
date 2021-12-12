package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.CheckboxGroup;
import java.util.Collections;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.berkeley.cs.jqf.fuzz.junit.quickcheck.NonTrackingGenerationStatus;
import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.state.ClassLoaderClonerStateFactory;
import uk.ac.ox.cs.refactoring.synthesis.state.State;
import uk.ac.ox.cs.refactoring.synthesis.state.StateFactory;

class CounterexampleGeneratorTest {

  private final SourceOfRandomness sourceOfRandomness = Mockito.mock(SourceOfRandomness.class);

  private final GeneratorRepository repository = new GeneratorRepository(sourceOfRandomness)
      .register(new ServiceLoaderGeneratorSource());

  @Test
  void checkboxGroup() throws Exception {
    final CounterexampleGenerator generator = new CounterexampleGenerator(repository, CheckboxGroup.class,
        Collections.emptyList());
    final Counterexample counterexample = generator.generate(sourceOfRandomness,
        new NonTrackingGenerationStatus(sourceOfRandomness));

    assertNotNull(counterexample);
    assertNotNull(counterexample.Instance);

    final StateFactory stateFatory = new ClassLoaderClonerStateFactory();
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final State state = stateFatory.create(classLoader, counterexample);
    assertNotNull(state);
    assertNotNull(state.Instance);
  }
}
