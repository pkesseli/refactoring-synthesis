package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
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
    unusedComponents.removeAll(parameters);

    for (final String consumerType : components.InvolvedClasses)
      addConsumerType(components, consumerType, allComponents, unusedComponents);
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
          && Dependencies.isUsable(allComponents, method) && Dependencies.usesAny(unusedComponents, method))
        Invoke.register(javaComponents, method);
    }
  }
}
