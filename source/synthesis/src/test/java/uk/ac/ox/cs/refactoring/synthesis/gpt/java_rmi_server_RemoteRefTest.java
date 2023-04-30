
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_RemoteRefTest {
  @Test
  void done() throws Exception {
    assertThat(synthesiseGPT("this.done(a);\n", "", "java.rmi.server.RemoteRef", "done", "java.rmi.server.RemoteCall"), anyOf(contains("done"), contains("invoke"), contains("newCall")));
  }

  @Test
  void invoke() throws Exception {
    assertThat(synthesiseGPT("this.invoke(a);\n\n", "RemoteCall call = new RemoteCallImpl(method.hashCode());\ncall.setMethodHash(method.hashCode());\nObject[] args = {a};\ncall.setArguments(args);\nthis.newCall(call);\n", "java.rmi.server.RemoteRef", "invoke", "java.rmi.server.RemoteCall"), anyOf(contains("done"), contains("invoke"), contains("newCall")));
  }

  @Test
  void newCall() throws Exception {
    assertThat(synthesiseGPT("RemoteRef ref = this.newCall(a, b, c, d);\n\n", "RemoteRef ref = ((RemoteObject) a).ref;\nObject result = ref.invoke(a, b[0], b[1], c);\n", "java.rmi.server.RemoteRef", "newCall", "java.rmi.server.RemoteObject", "Operation[]", "int", "long"), anyOf(contains("done"), contains("invoke"), contains("newCall")));
  }
}