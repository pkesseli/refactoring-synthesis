
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RMIClassLoaderTest {
  @Test
  void loadClass() throws Exception {
    assertThat(synthesiseGPT("this.loadClass(a);\n\n", "Class.forName(a, true, this.getClass().getClassLoader());\n", "java.rmi.server.RMIClassLoader", "loadClass", "java.lang.String"), anyOf(contains("loadClass")));
  }
}