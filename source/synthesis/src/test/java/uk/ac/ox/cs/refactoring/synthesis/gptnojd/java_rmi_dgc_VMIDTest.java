
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_rmi_dgc_VMIDTest {
  @Test
  void isUnique() throws Exception {
    assertThat(synthesiseGPT("public class MyClass {\n    private java.rmi.dgc.VMID id;\n\n    public MyClass() {\n        id = new java.rmi.dgc.VMID();\n    }\n\n    public boolean isIdUnique() {\n        return id.isUnique();\n    }\n}\n", "public class MyClass {\n    private java.rmi.dgc.VMID id;\n\n    public MyClass() {\n        id = new java.rmi.dgc.VMID();\n    }\n\n    public boolean isIdUnique() {\n        return true;\n    }\n}\n", "java.rmi.dgc.VMID", "isUnique"), Matchers.anything());
  }
}