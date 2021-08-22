package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.ClassOrInterfaceType;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.FunctionComponent;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaComponents;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.expression.Invoke;

public class ConsumerSeed implements InstructionSetSeed {

  private final ClassLoader classLoader;

  public ConsumerSeed(final ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  public void seed(final ComponentDirectory components) throws ClassNotFoundException {
    final Set<JavaLanguageKey> allComponents = components.keySet(JavaLanguageKey.class);
    final Set<JavaLanguageKey> unusedComponents = new HashSet<>(allComponents);
    final Set<JavaLanguageKey> parameters = components.parameterKeySet(JavaLanguageKey.class);
    unusedComponents.removeAll(parameters);

    final Set<String> consumerTypes = getConsumerTypes(components, allComponents);
    for (final String consumerType : consumerTypes)
      addConsumerType(components, consumerType, allComponents, unusedComponents);
  }

  private void addConsumerType(final ComponentDirectory components, final String consumerType,
      final Set<JavaLanguageKey> allComponents, final Set<JavaLanguageKey> unusedComponents)
      throws ClassNotFoundException {
    final JavaComponents javaComponents = new JavaComponents(components);
    final Class<?> cls = classLoader.loadClass(consumerType);
    for (final Method method : cls.getDeclaredMethods()) {
      if (!Modifier.isPublic(method.getModifiers()) || method.getParameterTypes().length == 0
          || !Dependencies.isUsable(allComponents, method) || !Dependencies.usesAny(unusedComponents, method))
        continue;
      Invoke.register(javaComponents, method);
    }
  }

  private static Set<String> getConsumerTypes(final ComponentDirectory components,
      final Set<JavaLanguageKey> allComponents) {
    return allComponents.stream().flatMap(key -> components.get(key, Integer.MAX_VALUE))
        .filter(FunctionComponent.class::isInstance).map(Component::getParameterKeys).filter(keys -> !keys.isEmpty())
        .map(keys -> keys.get(0).Type.asClassOrInterfaceType()).filter(Objects::nonNull)
        .map(ClassOrInterfaceType::getNameWithScope).collect(Collectors.toSet());
  }

}
