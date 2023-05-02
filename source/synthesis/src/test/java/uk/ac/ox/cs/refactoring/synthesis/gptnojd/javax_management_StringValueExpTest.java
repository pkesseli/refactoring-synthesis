
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_StringValueExpTest {
  @Test
  void setMBeanServer() throws Exception {
    assertThat(synthesiseGPT("this.setMBeanServer(a);\n", "public class MyClass {\n  private final MBeanServer mBeanServer;\n\n  public MyClass(MBeanServer mBeanServer) {\n    this.mBeanServer = mBeanServer;\n  }\n}\n", "javax.management.StringValueExp", "setMBeanServer", "javax.management.MBeanServer"), Matchers.anything());
  }
}