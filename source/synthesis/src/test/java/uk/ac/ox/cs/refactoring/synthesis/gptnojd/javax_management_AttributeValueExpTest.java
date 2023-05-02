
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_AttributeValueExpTest {
  @Test
  void setMBeanServer() throws Exception {
    assertThat(synthesiseGPT("this.setMBeanServer(a);\n", "MBeanServerFactory.setMBeanServer(a);\n", "javax.management.AttributeValueExp", "setMBeanServer", "javax.management.MBeanServer"), anyOf(contains("getMBeanServer")));
  }
}