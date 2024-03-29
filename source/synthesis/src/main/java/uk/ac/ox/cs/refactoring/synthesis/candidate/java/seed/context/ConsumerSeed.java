package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;

/**
 * Introduces methods to the instruction set which consume previously unused
 * components, e.g. unused program parameters. The goal is to add at least some
 * options to use all parameters of the program, even though the synthesiser
 * still has the option of not using them and producing programs where some
 * parameters are ignored.
 */
public class ConsumerSeed implements InstructionSetSeed {

  /** Used to load classes whose methods we might add to the instruction set. */
  private final ClassLoader classLoader;

  /** {@link #classLoader} */
  public ConsumerSeed(final ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException {
    final Set<JavaLanguageKey> allComponents = components.keySet(JavaLanguageKey.class);
    final Set<JavaLanguageKey> unusedComponents = new HashSet<>(allComponents);
    final Set<JavaLanguageKey> parameters = components.parameterKeySet(JavaLanguageKey.class);
    addNonNullTypes(parameters);
    unusedComponents.removeAll(parameters);

    for (final String consumerType : components.InvolvedClasses)
      addConsumerType(components, consumerType, allComponents, unusedComponents);
  }

  /**
   * If a method accepts a nullable parameter, it implicitly also accepts a
   * non-null parameter. This needs to be considered when deciding whether a
   * certain type of parameter has been consumed yet and is thus added to the set.
   * 
   * @param parameters Used parameter set to expand.
   */
  private void addNonNullTypes(Set<JavaLanguageKey> parameters) {
    final Set<JavaLanguageKey> toDuplicate = parameters.stream().filter(k -> !k.Nonnull).collect(Collectors.toSet());
    for (final JavaLanguageKey javaLanguageKey : toDuplicate) {
      parameters.add(JavaLanguageKeys.nonnull(javaLanguageKey.Type));
    }
  }

  /**
   * Adds all methods of the given {@link consumerType} if they consume currently
   * unused program parameter types.
   * 
   * @param components       Instruction set to extend.
   * @param consumerType     Class whose methods to inspect.
   * @param allComponents    All methods considered from {@code components}.
   * @param unusedComponents Unused components for which to find a consumer.
   * @throws ClassNotFoundException if {@code consumerType} could not be loaded.
   */
  private void addConsumerType(final ComponentDirectory components, final String consumerType,
      final Set<JavaLanguageKey> allComponents, final Set<JavaLanguageKey> unusedComponents)
      throws ClassNotFoundException {
    final JavaComponents javaComponents = new JavaComponents(components);
    final Class<?> cls = classLoader.loadClass(consumerType);
    for (final Method method : cls.getDeclaredMethods()) {
      if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length != 0
          && method.getAnnotation(Deprecated.class) == null && Dependencies.isUsable(allComponents, method)
          && Dependencies.usesAny(unusedComponents, method))
        Invoke.register(javaComponents, method);
    }
  }
}
