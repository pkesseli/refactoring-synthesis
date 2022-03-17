package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_rmi_server_RMIClassLoaderTest {

  @Test
  void loadClass() throws Exception {
    assertThat(synthesiseAlias("java.rmi.server.RMIClassLoader", "loadClass", "java.lang.String"),
        allOf(contains(".loadClass("), contains("null")));
  }
}
