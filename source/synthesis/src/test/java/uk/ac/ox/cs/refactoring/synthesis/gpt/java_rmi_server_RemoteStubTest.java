
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RemoteStubTest {
  @Test
  void setRef() throws Exception {
    assertThat(synthesiseGPT("this.setRef(a, b);\n", "this.ref = new RemoteStub(b);\n", "java.rmi.server.RemoteStub", "setRef", "java.rmi.server.RemoteStub", "java.rmi.server.RemoteRef"), anyOf(contains("RemoteStub"), contains("setRef")));
  }
}