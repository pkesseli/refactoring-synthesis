
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_net_URLConnectionTest {
  @Test
  void getDefaultRequestProperty() throws Exception {
    assertThat(synthesiseGPT("String property = this.getDefaultRequestProperty(a);\n\n", "String property = this.getRequestProperty(a);\n", "java.net.URLConnection", "getDefaultRequestProperty", "java.lang.String"), Matchers.anything());
  }

  @Test
  void setDefaultRequestProperty() throws Exception {
    assertThat(synthesiseGPT("this.setDefaultRequestProperty(a, b);\n", "URLConnection connection = this.openConnection();\nconnection.setRequestProperty(a, b);\n", "java.net.URLConnection", "setDefaultRequestProperty", "java.lang.String", "java.lang.String"), Matchers.anything());
  }
}