
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_management_monitor_CounterMonitorTest {

  @Test
  void setThreshold() throws Exception {
    assertThat(synthesiseAlias("javax.management.monitor.CounterMonitor", "setThreshold", "java.lang.Number"),
        contains("setInitThreshold"));
  }
}
