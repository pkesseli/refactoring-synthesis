
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

// A sequence of `newCall`, `invoke`, `done` should be replaced by a single method
// `invoke`. Can this be handled?
class java_rmi_server_RemoteRefTest {
  @Test
  void done() throws Exception {
    assertThat(synthesiseAlias("java.rmi.server.RemoteRef", "done", "java.rmi.server.RemoteCall"), anyOf(contains("invoke")));
  }

  @Test
  void invoke() throws Exception {
    assertThat(synthesiseAlias("java.rmi.server.RemoteRef", "invoke", "java.rmi.server.RemoteCall"), anyOf(contains("invoke")));
  }

  @Test
  void newCall() throws Exception {
    assertThat(synthesiseAlias("java.rmi.server.RemoteRef", "newCall", "java.rmi.server.RemoteObject", "java.rmi.server.Operation[]", "int", "long"), anyOf(contains("invoke")));
  }
}