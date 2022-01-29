package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.mockito.exceptions.base.MockitoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.random.RandomnessAccessor;
import uk.ac.ox.cs.refactoring.synthesis.invocation.Fields;

/**
 * JQF {@link Generator} for {@link Counterexample}. Used during verification
 * using fuzzing.
 */
public class CounterexampleGenerator extends Generator<Counterexample> {

  /** Sink for errors about accessing the source files. */
  private static final Logger logger = LoggerFactory.getLogger(CounterexampleGenerator.class);

  /** Limits the number of objects we generate per counterexample component. */
  private static final byte MAX_OBJECT_DEPTH = 5;

  /** Repository of all available generators. Used to generate literal values. */
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
    final ClassLoader classLoader = ClassLoaders.createIsolated();
    final Function<Class<?>, Object> objenesis = ObjenesisFactory.createObjenesis(classLoader);
    final Counterexample counterexample = new Counterexample(
        generate(random, status, classLoader, objenesis, instanceType));
    for (final Class<?> parameterType : parameterTypes)
      counterexample.Arguments.add(generate(random, status, classLoader, objenesis, parameterType));
    return counterexample;
  }

  /**
   * Generates a Java object of type {@code type}, using classes loaded in
   * {@code classLoader}. Also creates primitive values and arrays.
   * 
   * @param random      Used to make choices about values in the object.
   * @param status      {@link Generator#generate(SourceOfRandomness, GenerationStatus)}
   * @param classLoader Isolated class loader from which to load all classes.
   * @param objenesis   Used to instantiate objects.
   * @param type        Type of the object to create.
   * @return Created object.
   */
  private Object generate(final SourceOfRandomness random, final GenerationStatus status, final ClassLoader classLoader,
      final Function<Class<?>, Object> objenesis, final Class<?> type) {
    if (type == null)
      return null;

    final int depth = RandomnessAccessor.nextByte(random, (byte) 2, MAX_OBJECT_DEPTH);
    return generate(random, status, classLoader, objenesis, type, new HashSet<>(), depth);
  }

  /**
   * Recursive handler of
   * {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)},
   * with a limit on recursing on aggregations on the same type.
   * 
   * @param random       {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param status       {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param classLoader  {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param objenesis    {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param type         {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param visitedTypes Already visited types in this branch, avoiding infinite
   *                     recursion.
   * @return {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   */
  private Object generate(final SourceOfRandomness random, final GenerationStatus status, final ClassLoader classLoader,
      final Function<Class<?>, Object> objenesis, final Class<?> type,
      final Set<Class<?>> visitedTypes, final int depth) {

    if (!Literals.isLiteralType(type)) {
      if (depth <= 0)
        return null;
    }

    final Set<Class<?>> visitedTypesInBranch = new HashSet<Class<?>>(visitedTypes);
    if (!isSupported(type) || !visitedTypesInBranch.add(type))
      return null;

    try {
      return repository.type(type).generate(random, status);
    } catch (final Throwable e) {
      logger.trace("Could not use JQF native generator.", e);
    }

    if (type.isArray()) {
      final int length = (int) random.nextByte(Byte.MIN_VALUE, Byte.MAX_VALUE) - (int) Byte.MIN_VALUE;
      final Object array = Array.newInstance(type.getComponentType(), length);
      for (int i = 0; i < length; ++i) {
        final Object value = generate(random, status, classLoader, objenesis, type, visitedTypesInBranch,
            depth - 1);
        Array.set(array, i, value);
      }
      return array;
    }

    if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
      final boolean tryPublicConstructor = random.nextBoolean();
      if (tryPublicConstructor) {
        final Optional<Constructor<?>> constructorWithMostArguments = Arrays.stream(type.getDeclaredConstructors())
            .filter(c -> Modifier.isPublic(c.getModifiers()))
            .max(CounterexampleGenerator::orderConstructorsByNumberOfParameters);
        if (constructorWithMostArguments.isPresent()) {
          final Constructor<?> constructor = constructorWithMostArguments.get();
          final Object[] arguments = Arrays.stream(constructor.getParameterTypes())
              .map(t -> generate(random, status, classLoader, objenesis, t,
                  visitedTypesInBranch, depth - 1))
              .toArray(Object[]::new);
          try {
            final Object constructed = constructor.newInstance(arguments);
            postProcess(type, constructed, Collections.newSetFromMap(new IdentityHashMap<>()));
            return constructed;
          } catch (final Throwable e) {
            logger.warn("Could not use native constructor", e);
          }
        }
      }
    }

    final Object object;
    try {
      object = objenesis.apply(type);
    } catch (final MockitoException e) {
      logger.warn("Could not instantiate part of counterexample.", e);
      return null;
    }
    for (final Field field : Fields.getInstance(type)) {
      final Class<?> fieldType = field.getType();
      if (!isSupported(fieldType))
        continue;

      final Object value = generate(random, status, classLoader, objenesis, fieldType,
          visitedTypesInBranch, depth - 1);
      try {
        Fields.set(type, object, field.getName(), value);
      } catch (final NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
        logger.warn("Could not set counterexample member.", e);
      }
    }
    return object;
  }

  @Override
  public Generator<Counterexample> copy() {
    return new CounterexampleGenerator(repository, instanceType, parameterTypes);
  }

  /**
   * Using public constructors may lead to heaps with objects of unsupported
   * types (e.g. {@link Reference}). We null out those fields here.
   * 
   * @param type            Type of {@code constructed}.
   * @param constructed     Object created using constructor.
   * @param alreadyVisisted Already checked objects to avoid cycles.
   * @throws NoSuchFieldException   if a type mismatch occurs.
   * @throws IllegalAccessException if a necessary field could not be accessed.
   */
  private static void postProcess(final Class<?> type, final Object constructed, final Set<Object> alreadyVisisted)
      throws NoSuchFieldException, IllegalAccessException {
    if (constructed == null || !alreadyVisisted.add(constructed))
      return;

    for (final Field field : Fields.getInstance(constructed.getClass())) {
      final Class<?> fieldType = field.getType();
      if (isSupported(fieldType))
        postProcess(fieldType, field.get(constructed), alreadyVisisted);
      else
        Fields.set(type, constructed, field.getName(), null);
    }
  }

  /**
   * Some types are not yet supported in subsequent steps (e.g. cloning) and must
   * be excluded from generated counterexamples. We just null such fields out.
   * 
   * @param cls Type to check.
   * @return {@code true} if the type is allowed in counterexamples, {@code false}
   *         otherwise.
   */
  private static boolean isSupported(final Class<?> cls) {
    return !Reference.class.isAssignableFrom(cls);
  }

  /**
   * Orders constructors by their number of parameters.
   * 
   * @param lhs {@link java.util.Comparator#compare(Object, Object)}
   * @param rhs {@link java.util.Comparator#compare(Object, Object)}
   * @return {@link java.util.Comparator#compare(Object, Object)}
   */
  private static int orderConstructorsByNumberOfParameters(final Constructor<?> lhs, final Constructor<?> rhs) {
    return lhs.getParameterTypes().length - rhs.getParameterTypes().length;
  }
}
