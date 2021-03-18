package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.List;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * TODO: Implement {@code this} and fields
 */
public class CounterexampleGenerator extends Generator<Counterexample> {

  /**
   * 
   */
  private final GeneratorRepository repository;

  /**
   * 
   */
  private final List<Class<?>> parameterTypes;

  public CounterexampleGenerator(final GeneratorRepository repository, final List<Class<?>> parameterTypes) {
    super(Counterexample.class);
    this.repository = repository;
    this.parameterTypes = parameterTypes;
  }

  @Override
  public Counterexample generate(final SourceOfRandomness random, final GenerationStatus status) {
    final Counterexample counterexample = new Counterexample(null);
    for (int i = 0; i < parameterTypes.size(); ++i) {
      final Class<?> cls = parameterTypes.get(i);
      final Object value = repository.type(cls).generate(random, status);
      if (Literals.isLiteralType(cls)) {
        counterexample.LiteralArguments.put(i, value);
      } else {
        throw new IllegalArgumentException("TODO: Support object parameters");
      }
    }
    return counterexample;
  }

}
