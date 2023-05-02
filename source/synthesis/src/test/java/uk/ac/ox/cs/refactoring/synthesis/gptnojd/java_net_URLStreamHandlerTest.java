
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLStreamHandlerTest {
  @Test
  void setURL() throws Exception {
    assertThat(synthesiseGPT("this.setURL(a, b, c, d, e, f);\n\n", "URL url = new URL(null, a, new Handler(b, c, d, e, f));\nthis.setURL(url);\n", "java.net.URLStreamHandler", "setURL", "java.net.URL", "java.lang.String", "java.lang.String", "int", "java.lang.String", "java.lang.String"), Matchers.anything());
  }
}