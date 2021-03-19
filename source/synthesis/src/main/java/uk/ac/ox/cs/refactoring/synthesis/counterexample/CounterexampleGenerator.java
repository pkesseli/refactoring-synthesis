package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.util.List;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * JQF {@link Generator} for {@link Counterexample}. Used during verification
 * using fuzzing.
 * 
 * TODO: Implement {@code this} and fields
 */
public class CounterexampleGenerator extends Generator<Counterexample> {

  /**
   * Repository of all available generators. Used to generate literal values.
   */
  private final GeneratorRepository repository;

  /**
   * Parameter types in the function to synthesise, and thus to be added to every
   * counterexample.
   */
  private final List<Class<?>> parameterTypes;

  /**
   * @param repository     {@link #repository}
   * @param parameterTypes {@link #parameterTypes}
   */
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

  @Override
  public Generator<Counterexample> copy() {
    return new CounterexampleGenerator(repository, parameterTypes);
  }

}
