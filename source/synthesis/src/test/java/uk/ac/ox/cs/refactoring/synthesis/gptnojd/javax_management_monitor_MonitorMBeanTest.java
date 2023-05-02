
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_MonitorMBeanTest {
  @Test
  void getObservedObject() throws Exception {
    assertThat(synthesiseGPT("Object observedObject = this.getObservedObject();\n\n", "Object observedObject = this.getObservedAttribute(\"ObservedObject\");\n", "javax.management.monitor.MonitorMBean", "getObservedObject"), anyOf(contains("getObservedObjects")));
  }

  @Test
  void setObservedObject() throws Exception {
    assertThat(synthesiseGPT("this.setObservedObject(a);\n", "this.setObservedObject(ObjectName.getInstance(a.toString()));\n", "javax.management.monitor.MonitorMBean", "setObservedObject", "javax.management.ObjectName"), anyOf(contains("addObservedObject")));
  }
}