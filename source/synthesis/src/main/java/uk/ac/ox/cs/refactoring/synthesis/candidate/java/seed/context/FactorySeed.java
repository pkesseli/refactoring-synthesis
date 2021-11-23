package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.github.javaparser.ast.type.ClassOrInterfaceType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;

/**
 * Tries to add factory methods for unavailable parameter components. Uses
 * {@link ConstantSeed} as a fallback.
 */
public class FactorySeed implements InstructionSetSeed {

  /** Used as a fallback for primitive types. */
  private final ConstantSeed constantSeed = new ConstantSeed();

  /** Used to analyse class types reflectively. */
  private final ClassLoader classLoader;

  /** @param classLoader {@link #classLoader} */
  public FactorySeed(final ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException {
    final Set<JavaLanguageKey> availableComponents = components.keySet(JavaLanguageKey.class);
    final Set<JavaLanguageKey> requiredComponents = components.parameterKeySet(JavaLanguageKey.class);
    requiredComponents.removeAll(availableComponents);

    final JavaComponents javaComponents = new JavaComponents(components);
    for (final JavaLanguageKey javaLanguageKey : requiredComponents) {
      if (javaLanguageKey.Type.isPrimitiveType()) {
        constantSeed.seed(javaComponents, javaLanguageKey);
        continue;
      }
      final ClassOrInterfaceType type = javaLanguageKey.Type.asClassOrInterfaceType();
      if (type == null)
        continue;

      final String fullyQualifiedName = type.getNameWithScope();
      final Class<?> cls = classLoader.loadClass(fullyQualifiedName);
      for (final Method method : cls.getDeclaredMethods()) {
        if (!Modifier.isStatic(method.getModifiers()) || !method.canAccess(null))
          continue;
        final Class<?> returnType = method.getReturnType();
        if (returnType != cls)
          continue;

        if (Dependencies.isUsable(components, method))
          Invoke.register(javaComponents, method);
      }
    }
  }

}
