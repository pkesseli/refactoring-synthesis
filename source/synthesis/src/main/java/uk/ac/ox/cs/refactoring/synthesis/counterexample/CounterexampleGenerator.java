package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.cs.refactoring.synthesis.invocation.Fields;

/**
 * JQF {@link Generator} for {@link Counterexample}. Used during verification
 * using fuzzing.
 */
public class CounterexampleGenerator extends Generator<Counterexample> {

  /** Sink for errors about accessing the source files. */
  private static final Logger logger = LoggerFactory.getLogger(CounterexampleGenerator.class);

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
      if (Literals.isLiteralType(cls)) {
        counterexample.LiteralArguments.put(i, generateObject(cls, random, status));
      } else {
        counterexample.ObjectArguments.put(i, generate(cls, random, status));
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
    return generate(instanceType, random, status);
  }

  private Object generateObject(final Class<?> type, final SourceOfRandomness sourceOfRandomness,
      final GenerationStatus status) {
    return repository.type(type).generate(sourceOfRandomness, status);
  }

  /**
   * Recursive root of
   * {@link #generate(Class, Set, SourceOfRandomness, GenerationStatus)}, starting
   * with an empty set of explored types.
   * 
   * @param type   @link #generate(Class, Set, SourceOfRandomness,
   *               GenerationStatus)}
   * @param random {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @param status {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @return @link #generate(Class, Set, SourceOfRandomness, GenerationStatus)}
   */
  private ObjectDescription generate(final Class<?> type, final SourceOfRandomness random,
      final GenerationStatus status) {
    // TODO: Handle interfaces
    if (type.isInterface())
      return null;
    return generate(type, new HashSet<>(), random, status);
  }

  /**
   * Generates an {@link ObjectDescription} of the given type. First tries to
   * generate a real object using {@link #repository} and convert it to a
   * {@link ObjectDescription}. If no generator is available, it explicitly
   * creates an object description for the given type and recursively initialises
   * its fields using this method.
   * 
   * @param type         Type of object to create.
   * @param visitedTypes
   * @param random       {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @param status       {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @return Counterexample object description.
   */
  private ObjectDescription generate(final Class<?> type, final Set<Class<?>> visitedTypes,
      final SourceOfRandomness random, final GenerationStatus status) {
    if (visitedTypes.contains(type))
      return null;

    final Object object;
    try {
      object = generateObject(type, random, status);
    } catch (final Throwable e) {
      logger.trace("Could not use native generator.", e);
      return generateObjectDescription(type, visitedTypes, random, status);
    }

    return toObjectDescription(object);
  }

  /**
   * Generates an {@link ObjectDescription} for a type which no generator is
   * available in {@link #repository}.
   * 
   * @param type         Type of the object to generate.
   * @param visitedTypes
   * @param random       Source of randomness to generate field values.
   * @param status       {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @return Counterexample {@link ObjectDescription} of the given type.
   */
  private ObjectDescription generateObjectDescription(final Class<?> type, final Set<Class<?>> visitedTypes,
      final SourceOfRandomness random, final GenerationStatus status) {
    if (random.nextBoolean()) {
      return null;
    }

    final ObjectDescription objectDescription = new ObjectDescription(type.getName());
    Class<?> cls = type;
    while (cls != null) {
      visitedTypes.add(type);
      setFields(objectDescription, cls, visitedTypes, random, status);
      cls = cls.getSuperclass();
    }
    return objectDescription;
  }

  /**
   * Initialises all fields of the given {@link ObjectDescription} counterexample.
   * 
   * @param objectDescription Counterexample object to be initialised.
   * @param cls               Type of {@code objectDescription}.
   * @param visitedTypes
   * @param random            {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @param status            {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   */
  private void setFields(final ObjectDescription objectDescription, final Class<?> cls,
      final Set<Class<?>> visitedTypes, final SourceOfRandomness random, final GenerationStatus status) {
    for (final Field field : Fields.getInstance(cls)) {
      field.setAccessible(true);
      final String name = field.getName();
      final Class<?> fieldType = field.getType();
      if (Literals.isLiteralType(fieldType)) {
        objectDescription.LiteralFields.put(name, generateObject(fieldType, random, status));
      } else {
        objectDescription.ObjectFields.put(name, generate(fieldType, visitedTypes, random, status));
      }
    }
  }

  /**
   * Converts a concrete object to a classloader-agnostic counterexample.
   * 
   * @param object Concrete object to convert.
   * @return Equivalent classloader-agnostic representation.
   */
  private static ObjectDescription toObjectDescription(final Object object) {
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
        description.ObjectFields.put(name, toObjectDescription(value));
      }
    }
  }

  @Override
  public Generator<Counterexample> copy() {
    return new CounterexampleGenerator(repository, instanceType, parameterTypes);
  }
}
