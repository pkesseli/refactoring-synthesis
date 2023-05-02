
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_GaugeMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseGPT("double derivedGauge = monitor.getDerivedGauge();\n\n", "double derivedGauge = ((GaugeMonitor) monitor).getDerivedGauge();\n", "javax.management.monitor.GaugeMonitor", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "long timeStamp = this.getDerivedGauge().getTimeStamp();\n", "javax.management.monitor.GaugeMonitor", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }
}