
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_management_monitor_StringMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseAlias("javax.management.monitor.StringMonitor", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseAlias("javax.management.monitor.StringMonitor", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }
}