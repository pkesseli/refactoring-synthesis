
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_StringMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseGPT("StringMonitor stringMonitor = new StringMonitor(monitorMBean);\nstringMonitor.start();\ndouble derivedGauge = stringMonitor.getDerivedGauge();\n\n", "StringMonitor stringMonitor = new StringMonitor(monitorMBean);\nstringMonitor.start();\nObject attribute = stringMonitor.getAttribute(\"DerivedGauge\");\ndouble derivedGauge = Double.parseDouble(attribute.toString());\n", "javax.management.monitor.StringMonitor", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "long timeStamp = this.getDerivedGauge().getTimestamp();\n", "javax.management.monitor.StringMonitor", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }
}