
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_security_SignatureSpiTest {
  @Test
  @Disabled("Empty javadoc")
  void engineGetParameter() throws Exception {
  }

  @Test
  void engineSetParameter() throws Exception {
    assertThat(synthesiseAlias("java.security.SignatureSpi", "engineSetParameter", "java.lang.String", "java.lang.Object"), anyOf(contains("engineSetParameter")));
  }
}