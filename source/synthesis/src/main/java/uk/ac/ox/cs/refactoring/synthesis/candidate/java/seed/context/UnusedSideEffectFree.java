package uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.context;

import java.util.Map;
import java.util.Set;

import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.Component;
import uk.ac.ox.cs.refactoring.synthesis.candidate.builder.ComponentDirectory;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.builder.JavaLanguageKey;

/**
 * Removes instructions from the seed which have no side effect (e.g.
 * polymorphic implicit casts) and are not required to invoke another component.
 */
public class UnusedSideEffectFree implements InstructionSetSeed {

  /** Previously inserted side effect free components. */
  private final Map<JavaLanguageKey, Component<JavaLanguageKey, ?>> sideEffectFreeComponents;

  /** @param {@link #sideEffectFreeComponents} */
  public UnusedSideEffectFree(final Map<JavaLanguageKey, Component<JavaLanguageKey, ?>> sideEffectFreeComponents) {
    this.sideEffectFreeComponents = sideEffectFreeComponents;
  }

  @Override
  public void seed(final ComponentDirectory components) {
    final Set<JavaLanguageKey> parameterKeys = components.parameterKeySet(JavaLanguageKey.class);
    for (final Map.Entry<JavaLanguageKey, Component<JavaLanguageKey, ?>> sideEffectFreeComponent : sideEffectFreeComponents
        .entrySet()) {
      final JavaLanguageKey key = sideEffectFreeComponent.getKey();
      if (!parameterKeys.contains(key))
        components.remove(key, sideEffectFreeComponent.getValue());
    }
  }
}
