package uk.ac.ox.cs.refactoring.synthesis.cegis;

import java.lang.Runtime.Version;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Factory {@link Runtime.Version} counterexamples. */
public class RuntimeVersionGenerator extends Generator<Runtime.Version> {

  /** Sink for errors about accessing the source files. */
  private static final Logger logger = LoggerFactory.getLogger(RuntimeVersionGenerator.class);

  /** Used to generate ints and Strings. */
  private final GeneratorRepository repository;

  private final Constructor<Version> createVersion;

  /** @param repository {@link #repository} */
  public RuntimeVersionGenerator(final GeneratorRepository repository) {
    super(Runtime.Version.class);
    this.repository = repository;
    try {
      createVersion = Version.class.getDeclaredConstructor(List.class, Optional.class,
          Optional.class, Optional.class);
    } catch (final NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException("Incompatible java.lang.Runtime.Version class in JDK", e);
    }
    createVersion.setAccessible(true);
  }

  @Override
  public Version generate(final SourceOfRandomness random, final GenerationStatus status) {
    if (random.nextBoolean())
      return Runtime.version();

    final Generator<String> strings = repository.type(String.class);
    final Generator<Integer> ints = repository.type(Integer.class);
    @SuppressWarnings("rawtypes")
    final Generator<List> lists = repository.type(List.class, Integer.class);

    @SuppressWarnings("unchecked")
    final List<Integer> unmodifiableListOfVersions = lists.generate(random, status);
    final Optional<String> pre = Optional.ofNullable(strings.generate(random, status));
    final Optional<Integer> build = Optional.ofNullable(ints.generate(random, status));
    final Optional<String> optional = Optional.ofNullable(strings.generate(random, status));
    try {
      return createVersion.newInstance(unmodifiableListOfVersions, pre, build, optional);
    } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
      logger.warn("Failed to generate java.lang.Runtime.Version counterexample.", e);
      return null;
    }
  }

  @Override
  public Generator<Version> copy() {
    return new RuntimeVersionGenerator(repository);
  }
}
