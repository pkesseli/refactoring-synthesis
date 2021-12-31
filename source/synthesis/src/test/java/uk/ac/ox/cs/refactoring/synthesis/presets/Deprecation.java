package uk.ac.ox.cs.refactoring.synthesis.presets;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfigurations;

public final class Deprecation {

  public static SnippetCandidate synthesise(final MethodIdentifier methodToRefactor) throws ClassNotFoundException,
      IllegalAccessException, NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {
    return Synthesis.synthesise(GeneratorConfigurations.deprecatedMethodWithJavaDoc(methodToRefactor),
        methodToRefactor);
  }

  public static SnippetCandidate synthesiseAlias(final String fullyQualifiedClassName, final String methodName,
      final String... fullyQualifiedParameterClassNames) throws ClassNotFoundException, IllegalAccessException,
      NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, methodName,
        Arrays.asList(fullyQualifiedParameterClassNames));
    return synthesise(methodToRefactor);
  }

  private Deprecation() {
  }
}
