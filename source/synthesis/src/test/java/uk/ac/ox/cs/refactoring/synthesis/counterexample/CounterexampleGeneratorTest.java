package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.when;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.util.Collections;

import com.fasterxml.classmate.TypeResolver;
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
    when(sourceOfRandomness.nextByte(anyByte(), anyByte())).thenReturn((byte) 2).thenReturn((byte) 0);
    final TypeResolver typeResolver = new TypeResolver();
    final CounterexampleGenerator generator = new CounterexampleGenerator(repository,
        typeResolver.resolve(CheckboxGroup.class), Collections.emptyList());
    final Counterexample counterexample = generator.generate(sourceOfRandomness,
        new NonTrackingGenerationStatus(sourceOfRandomness));

    assertNotNull(counterexample);
    assertNotNull(counterexample.Instance);

    final StateFactory stateFatory = new ClassLoaderClonerStateFactory();
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final State state = stateFatory.create(classLoader, counterexample);
    assertNotNull(state);
    assertNotNull(state.Instance);
    final CheckboxGroup checkboxGroup = (CheckboxGroup) state.Instance;
    final Checkbox selectedCheckbox = checkboxGroup.getSelectedCheckbox();
    assertNotNull(selectedCheckbox);
    assertThat(selectedCheckbox.getItemListeners(), arrayContaining(notNullValue()));
    assertThat(selectedCheckbox.getMouseListeners(), arrayContaining(notNullValue()));
  }
}
