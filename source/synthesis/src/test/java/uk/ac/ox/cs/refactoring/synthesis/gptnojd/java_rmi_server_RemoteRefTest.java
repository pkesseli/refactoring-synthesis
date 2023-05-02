
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RemoteRefTest {
  @Test
  void done() throws Exception {
    assertThat(synthesiseGPT("this.done(a);\n", "a.release();\n", "java.rmi.server.RemoteRef", "done", "java.rmi.server.RemoteCall"), anyOf(contains("done"), contains("invoke"), contains("newCall")));
  }

  @Test
  void invoke() throws Exception {
    assertThat(synthesiseGPT("this.invoke(a);\n", "RemoteCall call = a;\nthis.invoke(call);\n", "java.rmi.server.RemoteRef", "invoke", "java.rmi.server.RemoteCall"), anyOf(contains("done"), contains("invoke"), contains("newCall")));
  }

  @Test
  void newCall() throws Exception {
    assertThat(synthesiseGPT("this.newCall(a, b, c, d);\n\n", "RemoteCall call = new RemoteCall();\ncall.setOperation(Operation[] b);\ncall.setParameters(Object[] c);\ncall.setMethodHash(long d);\nthis.invoke(RemoteObject obj, RemoteCall call);\n", "java.rmi.server.RemoteRef", "newCall", "java.rmi.server.RemoteObject", "Operation[]", "int", "long"), anyOf(contains("done"), contains("invoke"), contains("newCall")));
  }
}