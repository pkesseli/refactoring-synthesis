
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLStreamHandlerTest {
  @Test
  void setURL() throws Exception {
    assertThat(synthesiseGPT("this.setURL(a, b, c, d, e, f);\n\n", "this.setURL(a, b, c, d, e, f, null, null);\n", "java.net.URLStreamHandler", "setURL", "java.net.URL", "java.lang.String", "java.lang.String", "int", "java.lang.String", "java.lang.String"), Matchers.anything());
  }
}