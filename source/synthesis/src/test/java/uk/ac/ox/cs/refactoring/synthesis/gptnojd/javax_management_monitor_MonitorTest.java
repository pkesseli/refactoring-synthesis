
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_MonitorTest {
  @Test
  void getObservedObject() throws Exception {
    assertThat(synthesiseGPT("Object observedObject = this.getObservedObject();\n\n", "Object observedObject = this.getObservedAttribute(\"ObservedObject\");\n", "javax.management.monitor.Monitor", "getObservedObject"), anyOf(contains("getObservedObjects")));
  }

  @Test
  void setObservedObject() throws Exception {
    assertThat(synthesiseGPT("this.setObservedObject(a);\n", "this.addObservedObject(a);\n", "javax.management.monitor.Monitor", "setObservedObject", "javax.management.ObjectName"), anyOf(contains("addObservedObject")));
  }
}