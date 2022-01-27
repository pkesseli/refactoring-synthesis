package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKeys;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.type.TypeFactory;

/**
 * Helper for checks of dependencies between components.
 */
final class Dependencies {

  private Dependencies() {
  }

  /**
   * Equivalent to {@link #isUsable(Collection, Method)} for all components in the
   * given directory.
   * 
   * @param components Available {@link ComponentDirectory components}.
   * @param method     {@link #isUsable(Collection, Method)}
   * @return {@link #isUsable(Collection, Method)}
   */
  public static boolean isUsable(final ComponentDirectory components, final Method method) {
    return isUsable(components.keySet(JavaLanguageKey.class), method);
  }

  /**
   * Checks whether the given method could be invoked with just the given
   * components.
   * 
   * @param available Available components.
   * @param method    {@link Method} to invoke.
   * @return {@code true} if the given method can be invoked, {@code false}
   *         otherwise.
   */
  public static boolean isUsable(final Collection<JavaLanguageKey> available, final Method method) {
    return checkSignature(method, available::containsAll);
  }

  /**
   * 
   * @param available
   * @param method
   * @return
   */
  public static boolean usesAny(final Collection<JavaLanguageKey> available, final Method method) {
    return checkSignature(method, keys -> CollectionUtils.containsAny(available, keys));
  }

  /**
   * 
   * @param method
   * @param predicate
   * @return
   */
  private static boolean checkSignature(final Method method, final Predicate<Set<JavaLanguageKey>> predicate) {
    Stream<Class<?>> types = Stream.of(method.getParameterTypes());
    if (!Modifier.isStatic(method.getModifiers())) {
      types = Stream.concat(types, Stream.of(method.getDeclaringClass()));
    }
    final Set<JavaLanguageKey> keys = types.map(TypeFactory::create).map(JavaLanguageKeys::nonnull)
        .collect(Collectors.toSet());
    return predicate.test(keys);
  }
}
