
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_MonitorMBeanTest {
  @Test
  void getObservedObject() throws Exception {
assertThat (synthesiseNeural ("getObservedObject" , "this.getObservedObject();" , "\nthis.getObservedObjects()[0];\n" , "javax.management.monitor.MonitorMBean" , "getObservedObject") , anyOf (contains ("getObservedObjects"))) ;
  }

  @Test
  void setObservedObject() throws Exception {
assertThat (synthesiseNeural ("setObservedObject" , "this.setObservedObject(param0);" , "\nthis.addObservedObject(param0);\n" , "javax.management.monitor.MonitorMBean" , "setObservedObject" , "javax.management.ObjectName") , anyOf (contains ("addObservedObject"))) ;
  }
}
