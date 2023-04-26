
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_net_URLStreamHandlerTest {
  @Test
  void setURL() throws Exception {
    assertThat(synthesiseAlias("java.net.URLStreamHandler", "setURL", "java.net.URL", "java.lang.String", "java.lang.String", "int", "java.lang.String", "java.lang.String"), contains("setURL"));
  }
}