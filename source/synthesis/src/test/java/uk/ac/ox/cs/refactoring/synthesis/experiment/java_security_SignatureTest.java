
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_security_SignatureTest {
  @Test
  @Disabled("Empty javadoc")
  void getParameter() throws Exception {
  }

  @Test
  void setParameter() throws Exception {
    assertThat(synthesiseAlias("java.security.Signature", "setParameter", "java.lang.String", "java.lang.Object"), anyOf(contains("setParameter")));
  }
}