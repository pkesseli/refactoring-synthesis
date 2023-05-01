
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_server_OperationTest {
  @Test
  void getOperation() throws Exception {
    assertThat(synthesiseGPT("this.getOperation();\n\n", "", "java.rmi.server.Operation", "getOperation"), Matchers.anything());
  }

  @Test
  void toString1() throws Exception {
    assertThat(synthesiseGPT("public String myMethod() {\n    return this.toString();\n}\n", "public String myMethod() {\n    return super.toString();\n}\n", "java.rmi.server.Operation", "toString"), Matchers.anything());
  }
}