
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_management_monitor_MonitorMBeanTest {

  @Test
  void setObservedObject​​() throws Exception {
    assertThat(synthesiseAlias("javax.management.monitor.MonitorMBean", "setObservedObject", "javax.management.ObjectName"),
        contains("addObservedObject"));
  }
}
