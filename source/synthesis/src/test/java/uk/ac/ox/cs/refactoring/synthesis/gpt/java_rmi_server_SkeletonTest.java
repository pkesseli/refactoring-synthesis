
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_SkeletonTest {
  @Test
  void dispatch() throws Exception {
    assertThat(synthesiseGPT("this.dispatch(a, b, c, d);\n", "throw new UnsupportedOperationException(\"This method is deprecated and has no replacement.\");\n", "java.rmi.server.Skeleton", "dispatch", "java.rmi.Remote", "java.rmi.server.RemoteCall", "int", "long"), Matchers.anything());
  }

  @Test
  void getOperations() throws Exception {
    assertThat(synthesiseGPT("this.getOperations();\n\n", "", "java.rmi.server.Skeleton", "getOperations"), Matchers.anything());
  }
}