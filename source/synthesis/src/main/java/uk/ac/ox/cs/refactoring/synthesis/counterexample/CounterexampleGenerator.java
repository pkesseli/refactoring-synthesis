package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.Field;
import java.util.List;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import uk.ac.ox.cs.refactoring.synthesis.invocation.Fields;

/**
 * JQF {@link Generator} for {@link Counterexample}. Used during verification
 * using fuzzing.
 */
public class CounterexampleGenerator extends Generator<Counterexample> {

  /**
   * Repository of all available generators. Used to generate literal values.
   */
  private final GeneratorRepository repository;

  /**
   * Type of the instance for the instance method to synthesise. {@code null} for
   * {@code static} methods.
   */
  private final Class<?> instanceType;

  /**
   * Parameter types in the function to synthesise, and thus to be added to every
   * counterexample.
   */
  private final List<Class<?>> parameterTypes;

  /**
   * @param repository     {@link #repository}
   * @param instanceType   {@link #instanceType}
   * @param parameterTypes {@link #parameterTypes}
   */
  public CounterexampleGenerator(final GeneratorRepository repository, final Class<?> instanceType,
      final List<Class<?>> parameterTypes) {
    super(Counterexample.class);
    this.repository = repository;
    this.instanceType = instanceType;
    this.parameterTypes = parameterTypes;
  }

  @Override
  public Counterexample generate(final SourceOfRandomness random, final GenerationStatus status) {
    final ObjectDescription instance = createInstance(random, status);
    final Counterexample counterexample = new Counterexample(instance);
    for (int i = 0; i < parameterTypes.size(); ++i) {
      final Class<?> cls = parameterTypes.get(i);
      final Object value = repository.type(cls).generate(random, status);
      if (Literals.isLiteralType(cls)) {
        counterexample.LiteralArguments.put(i, value);
      } else {
        counterexample.ObjectArguments.put(i, createObjectDescription(value));
      }
    }
    return counterexample;
  }

  /**
   * Generates {@code this} instance of the counterexample.
   * 
   * @param random {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @param status {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @return {@code this} instance of counterexample.
   */
  private ObjectDescription createInstance(final SourceOfRandomness random, final GenerationStatus status) {
    if (instanceType == null) {
      return null;
    }

    return createObjectDescription(repository.type(instanceType).generate(random, status));
  }

  /**
   * Converts a concrete object to a classloader-agnostic counterexample.
   * 
   * @param object Concrete object to convert.
   * @return Equivalent classloader-agnostic representation.
   */
  private static ObjectDescription createObjectDescription(final Object object) {
    if (object == null) {
      return null;
    }

    Class<?> cls = object.getClass();
    final ObjectDescription description = new ObjectDescription(cls.getName());
    while (cls != null) {
      setFields(description, cls, object);
      cls = cls.getSuperclass();
    }

    return description;
  }

  /**
   * Populates the fields of the given {@code description} with the values of
   * {@code object}.
   * 
   * @param description Counterexample to populate.
   * @param cls         Type of {@code description}.
   * @param object      Observed values to turn into counterexample fields.
   */
  private static void setFields(final ObjectDescription description, final Class<?> cls, final Object object) {
    for (final Field field : Fields.getInstance(cls)) {
      field.setAccessible(true);
      final String name = field.getName();
      Object value;
      try {
        value = field.get(object);
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException(e);
      }
      if (Literals.isLiteralType(field.getType())) {
        description.LiteralFields.put(name, value);
      } else {
        description.ObjectFields.put(name, createObjectDescription(value));
      }
    }
  }

  @Override
  public Generator<Counterexample> copy() {
    return new CounterexampleGenerator(repository, instanceType, parameterTypes);
  }

}
