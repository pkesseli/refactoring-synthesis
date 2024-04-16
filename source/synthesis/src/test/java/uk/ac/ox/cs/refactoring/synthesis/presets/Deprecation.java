package uk.ac.ox.cs.refactoring.synthesis.presets;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfigurations;
import uk.ac.ox.cs.refactoring.synthesis.cegis.GPTHints;

public final class Deprecation {

  public static SnippetCandidate synthesiseAliasBenchmark(final String benchmarkName,
      final String fullyQualifiedClassName, final String methodName, final String... fullyQualifiedParameterClassNames)
      throws ClassNotFoundException, IllegalAccessException, NoSuchElementException, NoSuchFieldException,
      NoSuchMethodException, IOException {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, methodName,
        Arrays.asList(fullyQualifiedParameterClassNames));
    return Synthesis.synthesise(GeneratorConfigurations.experimentConfiguration(methodToRefactor), methodToRefactor,
        benchmarkName, null);
  }

  public static SnippetCandidate synthesiseAlias(final String fullyQualifiedClassName, final String methodName,
      final String... fullyQualifiedParameterClassNames) throws ClassNotFoundException, IllegalAccessException,
      NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {
    return synthesiseAliasBenchmark(null, fullyQualifiedClassName, methodName, fullyQualifiedParameterClassNames);
  }

  public static SnippetCandidate synthesiseGPT(final String benchmarkMethodName, final String before,
      final String after, final String fullyQualifiedClassName, final String methodName,
      final String... fullyQualifiedParameterClassNames) throws ClassNotFoundException, IllegalAccessException,
      NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {

    final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, methodName,
        Arrays.asList(fullyQualifiedParameterClassNames));

    final GPTHints hints = new GPTHints(before, after);
    return Synthesis.synthesise(GeneratorConfigurations.experimentConfiguration(methodToRefactor), methodToRefactor,
        benchmarkMethodName, hints);
  }

  private Deprecation() {
  }
}
