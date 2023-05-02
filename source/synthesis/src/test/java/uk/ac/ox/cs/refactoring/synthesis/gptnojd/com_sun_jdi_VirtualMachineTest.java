
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class com_sun_jdi_VirtualMachineTest {
  @Test
  void canAddMethod() throws Exception {
    assertThat(synthesiseGPT("if (this.canAddMethod()) {\n    // do something\n}\n\n", "if (this.canBeModified()) {\n    // do something\n}\n", "com.sun.jdi.VirtualMachine", "canAddMethod"), Matchers.anything());
  }

  @Test
  void canUnrestrictedlyRedefineClasses() throws Exception {
    assertThat(synthesiseGPT("this.canUnrestrictedlyRedefineClasses();\n\n", "this.canRedefineClasses();\n", "com.sun.jdi.VirtualMachine", "canUnrestrictedlyRedefineClasses"), Matchers.anything());
  }
}