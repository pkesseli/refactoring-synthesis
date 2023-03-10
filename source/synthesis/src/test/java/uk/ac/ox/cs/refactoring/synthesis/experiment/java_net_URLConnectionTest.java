
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_net_URLConnectionTest {
  @Test
  @Disabled("There seems to be no replacement")
  void getDefaultRequestProperty() throws Exception {
    synthesiseAlias("java.net.URLConnection", "getDefaultRequestProperty", "java.lang.String");
  }

  @Test
  @Disabled("There seems to be no replacement")
  void setDefaultRequestProperty() throws Exception {
    synthesiseAlias("java.net.URLConnection", "setDefaultRequestProperty", "java.lang.String", "java.lang.String");
  }
}