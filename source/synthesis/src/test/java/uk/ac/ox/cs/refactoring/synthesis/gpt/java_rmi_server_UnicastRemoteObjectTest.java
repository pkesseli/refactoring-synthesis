
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_UnicastRemoteObjectTest {
  @Test
  void exportObject() throws Exception {
    assertThat(synthesiseGPT("this.exportObject(a);\n\n", "this.exportObject(a, 0);\n", "java.rmi.server.UnicastRemoteObject", "exportObject", "java.rmi.Remote"), anyOf(contains("exportObject")));
  }
}