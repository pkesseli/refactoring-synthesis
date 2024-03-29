package uk.ac.ox.cs.refactoring.synthesis.counterexample;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.mockito.exceptions.base.MockitoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedConstructor;
import com.fasterxml.classmate.members.ResolvedField;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

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

  /** Used for {@link #memberResolver}. */
  private final TypeResolver typeResolver = new TypeResolver();

  /** Used to look up constructors. */
  private final MemberResolver memberResolver = new MemberResolver(typeResolver);

  /** Repository of all available generators. Used to generate literal values. */
  private final GeneratorRepository repository;

  /**
   * Type of the instance for the instance method to synthesise. {@code null} for
   * {@code static} methods.
   */
  private final ResolvedType instanceType;

  /**
   * Parameter types in the function to synthesise, and thus to be added to every
   * counterexample.
   */
  private final List<ResolvedType> parameterTypes;

  /**
   * @param repository     {@link #repository}
   * @param instanceType   {@link #instanceType}
   * @param parameterTypes {@link #parameterTypes}
   */
  public CounterexampleGenerator(final GeneratorRepository repository, final ResolvedType instanceType,
      final List<ResolvedType> parameterTypes) {
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
    for (final ResolvedType parameterType : parameterTypes)
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
      final Function<Class<?>, Object> objenesis, final ResolvedType type) {
    if (type == null)
      return null;

    final int depth = RandomnessAccessor.nextByte(random, (byte) 2, MAX_OBJECT_DEPTH);
    return generate(random, status, classLoader, objenesis, type, new HashSet<>(), depth);
  }

  private static Object getDefaultValue(final Class<?> cls) {
    if (cls == byte.class || cls == Byte.class) {
      return (byte) 0;
    }
    if (cls == short.class || cls == Short.class) {
      return (short) 0;
    }
    if (cls == int.class || cls == Integer.class) {
      return 0;
    }
    if (cls == long.class || cls == Long.class) {
      return 0L;
    }
    if (cls == char.class || cls == Character.class) {
      return '\0';
    }
    if (cls == float.class || cls == Float.class) {
      return 0.0f;
    }
    if (cls == double.class || cls == Double.class) {
      return 0.0;
    }
    if (cls == boolean.class || cls == Boolean.class) {
      return false;
    }
    return null;
  }

  /**
   * Recursive handler of
   * {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)},
   * with a limit on recursing on aggregations on the same type.
   * 
   * @param random         {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param status         {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param classLoader    {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param objenesis      {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param type           {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   * @param visitedClasses Already visited classes in this branch, avoiding
   *                       infinite recursion.
   * @return {@link #generate(SourceOfRandomness, GenerationStatus, ClassLoader, Function, Class)}
   */
  private Object generate(final SourceOfRandomness random, final GenerationStatus status, final ClassLoader classLoader,
      final Function<Class<?>, Object> objenesis, final ResolvedType type, final Set<Class<?>> visitedClasses,
      final int depth) {

    final Class<?> cls = type.getErasedType();
    if (!Literals.isLiteralType(cls)) {
      if (depth <= 0)
        return getDefaultValue(cls);
    }

    final Set<Class<?>> visitedTypesInBranch = new HashSet<Class<?>>(visitedClasses);
    if (!isSupported(type) || !visitedTypesInBranch.add(cls))
      return getDefaultValue(cls);

    try {
      return repository.type(cls).generate(random, status);
    } catch (final Throwable e) {
      logger.trace("Could not use JQF native generator.", e);
    }

    final Function<ResolvedType, Object> generateElement = elementType -> generate(random, status, classLoader,
        objenesis, elementType, visitedTypesInBranch, depth - 1);
    if (type.isArray()) {
      final int length = CollectionsGenerator.getRandomArrayLength(random);
      final Class<?> componentClass = type.getArrayElementType().getErasedType();
      final Object array = Array.newInstance(componentClass, length);
      final ResolvedType componentType = typeResolver.resolve(componentClass);
      for (int i = 0; i < length; ++i) {
        final Object value = generateElement.apply(componentType);
        Array.set(array, i, value);
      }
      return array;
    }

    final Object createdCollection;
    try {
      createdCollection = CollectionsGenerator.createCollection(random, typeResolver, generateElement, type);
      if (createdCollection != null)
        return createdCollection;
    } catch (final Throwable e) {
      logger.trace("Error when creating well-known collection type.", e);
    }

    if (!type.isInterface() && !type.isAbstract()) {
      // TODO: Handle collection types explicitly and re-enable this.
      final boolean tryPublicConstructor = false /* random.nextBoolean() */;
      if (tryPublicConstructor) {
        final ResolvedTypeWithMembers typeWithMembers = memberResolver.resolve(type, null, null);
        final Optional<ResolvedConstructor> constructorWithMostArguments = Arrays
            .stream(typeWithMembers.getConstructors()).filter(c -> c.isPublic())
            .max(CounterexampleGenerator::orderConstructorsByNumberOfParameters);
        if (constructorWithMostArguments.isPresent()) {
          final ResolvedConstructor constructor = constructorWithMostArguments.get();
          final Object[] arguments = IntStream.range(0, constructor.getArgumentCount())
              .mapToObj(constructor::getArgumentType)
              .map(t -> generate(random, status, classLoader, objenesis, t, visitedTypesInBranch, depth - 1))
              .toArray(Object[]::new);
          try {
            final Object constructed = constructor.getRawMember().newInstance(arguments);
            postProcess(memberResolver, type, constructed);
            System.out.println("Constructed: " + type);
            return constructed;
          } catch (final Throwable e) {
            logger.warn("Could not use native constructor", e);
          }
        }
      }
    }

    final Object object;
    try {
      object = objenesis.apply(cls);
    } catch (final MockitoException e) {
      logger.warn("Could not instantiate part of counterexample.", e);
      return getDefaultValue(cls);
    }
    for (final ResolvedField field : Fields.getInstance(memberResolver, type)) {
      final ResolvedType fieldType = field.getType();
      if (!isSupported(fieldType))
        continue;

      final Object value = generate(random, status, classLoader, objenesis, fieldType,
          visitedTypesInBranch, depth - 1);
      try {
        Fields.set(cls, object, field.getName(), value);
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
   * Recursive entry point for
   * {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}.
   * 
   * @param memberResolver  {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}
   * @param resolvedType    {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}
   * @param constructed     {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}
   * @param alreadyVisisted {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}
   * @throws NoSuchFieldException   {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}
   * @throws IllegalAccessException {@link #postProcess(MemberResolver, ResolvedType, Object, Set)}
   */
  public static void postProcess(final MemberResolver memberResolver, final ResolvedType resolvedType,
      final Object constructed) throws NoSuchFieldException, IllegalAccessException {
    postProcess(memberResolver, resolvedType, constructed, Collections.newSetFromMap(new IdentityHashMap<>()));
  }

  /**
   * Using public constructors may lead to heaps with objects of unsupported
   * types (e.g. {@link Reference}). We null out those fields here.
   * 
   * @param memberResolver  Used to identify fields.
   * @param resolvedType    Type of {@code constructed}.
   * @param constructed     Object created using constructor.
   * @param alreadyVisisted Already checked objects to avoid cycles.
   * @throws NoSuchFieldException   if a type mismatch occurs.
   * @throws IllegalAccessException if a necessary field could not be accessed.
   */
  private static void postProcess(final MemberResolver memberResolver, final ResolvedType resolvedType,
      final Object constructed, final Set<Object> alreadyVisisted) throws NoSuchFieldException, IllegalAccessException {
    if (constructed == null || !alreadyVisisted.add(constructed))
      return;

    for (final ResolvedField field : Fields.getInstance(memberResolver, resolvedType)) {
      final ResolvedType fieldType = field.getType();
      if (fieldType.isPrimitive())
        continue;

      if (isSupported(fieldType)) {
        final Field rawField = field.getRawMember();
        rawField.setAccessible(true);
        postProcess(memberResolver, fieldType, rawField.get(constructed), alreadyVisisted);
      } else {
        Fields.set(resolvedType.getErasedType(), constructed, field.getName(), null);
      }
    }
  }

  /**
   * Some types are not yet supported in subsequent steps (e.g. cloning) and must
   * be excluded from generated counterexamples. We just null such fields out.
   * 
   * @param type Type to check.
   * @return {@code true} if the type is allowed in counterexamples, {@code false}
   *         otherwise.
   */
  public static boolean isSupported(final ResolvedType type) {
    final Class<?> cls = type.getErasedType();
    if (Reference.class.isAssignableFrom(cls))
      return false;

    final String typeName = type.getTypeName();
    return !typeName.equals("sun.awt.AppContext") && !typeName.equals("jdk.internal.access.foreign.MemorySegmentProxy");
  }

  /**
   * Orders constructors by their number of parameters.
   * 
   * @param lhs {@link java.util.Comparator#compare(Object, Object)}
   * @param rhs {@link java.util.Comparator#compare(Object, Object)}
   * @return {@link java.util.Comparator#compare(Object, Object)}
   */
  private static int orderConstructorsByNumberOfParameters(final ResolvedConstructor lhs,
      final ResolvedConstructor rhs) {
    return lhs.getArgumentCount() - rhs.getArgumentCount();
  }
}
