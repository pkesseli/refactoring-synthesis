package uk.ac.ox.cs.refactoring.synthesis.presets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import uk.ac.ox.cs.refactoring.synthesis.candidate.java.api.SnippetCandidate;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.methods.MethodIdentifier;
import uk.ac.ox.cs.refactoring.synthesis.candidate.java.seed.api.GeneratorConfigurations;

public final class Deprecation {
  private Deprecation() {
  }

  public static SnippetCandidate synthesise(final MethodIdentifier methodToRefactor) throws ClassNotFoundException,
      IllegalAccessException, NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {
    return Synthesis.synthesise(GeneratorConfigurations.deprecatedMethodWithJavaDoc(methodToRefactor),
        methodToRefactor);
  }

  public static void testAlias(final String expected, final String fullyQualifiedClassName, final String methodName,
      final String... fullyQualifiedParameterClassNames) throws ClassNotFoundException, IllegalAccessException,
      NoSuchElementException, NoSuchFieldException, NoSuchMethodException, IOException {
    final MethodIdentifier methodToRefactor = new MethodIdentifier(fullyQualifiedClassName, methodName,
        Arrays.asList(fullyQualifiedParameterClassNames));
    final SnippetCandidate candidate = Deprecation.synthesise(methodToRefactor);
    System.out.println(candidate.toString());
    assertThat(candidate.toString(), containsString(expected));
  }
}
