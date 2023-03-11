package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import uk.ac.ox.cs.refactoring.classloader.ClassLoaders;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;

/**
 * Tries to add factory methods for unavailable parameter components. Uses
 * {@link ConstantSeed} as a fallback.
 */
public class FactorySeed implements InstructionSetSeed {

  /** Used to analyse class types reflectively. */
  private final ClassLoader classLoader;

  /** Used as a fallback for primitive types. */
  private final ConstantSeed constantSeed;

  /** @param classLoader {@link #classLoader} */
  public FactorySeed(final ClassLoader classLoader, final ConstantSeed constantSeed) {
    this.classLoader = classLoader;
    this.constantSeed = constantSeed;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException {
    final Set<JavaLanguageKey> availableComponents = components.keySet(JavaLanguageKey.class);
    final Set<JavaLanguageKey> requiredComponents = components.parameterKeySet(JavaLanguageKey.class);
    requiredComponents.removeAll(availableComponents);

    final JavaComponents javaComponents = new JavaComponents(components);
    for (final JavaLanguageKey javaLanguageKey : requiredComponents) {
      final Type unwrapped = unwrap(javaLanguageKey.Type);

      if (unwrapped.isPrimitiveType()) {
        constantSeed.seed(javaComponents, javaLanguageKey);
        continue;
      }
      final ClassOrInterfaceType type = unwrapped.asClassOrInterfaceType();
      if (type == null)
        continue;

      final String fullyQualifiedName = type.getNameWithScope();
      final Class<?> cls = ClassLoaders.loadClass(classLoader, fullyQualifiedName);
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

  /**
   * Unwraps array types down to their element types.
   * 
   * @param type Potentiall array type.
   * @return Unwrapped element type.
   */
  private static Type unwrap(final Type type) {
    if (!type.isArrayType())
      return type;

    return unwrap(type.asArrayType().getElementType());
  }
}
