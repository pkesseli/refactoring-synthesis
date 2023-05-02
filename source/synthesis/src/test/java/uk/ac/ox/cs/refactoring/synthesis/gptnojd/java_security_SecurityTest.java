
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_security_SecurityTest {
  @Test
  void getAlgorithmProperty() throws Exception {
    assertThat(synthesiseGPT("String property = Security.getAlgorithmProperty(a, b);\n\n", "String property = Security.getProperty(\"Alg.Alias.\" + a + \".\" + b);\n", "java.security.Security", "getAlgorithmProperty", "java.lang.String", "java.lang.String"), anyOf(contains("AlgorithmParameters"), contains("KeyFactory")));
  }
}