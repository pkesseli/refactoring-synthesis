
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_GaugeMonitorMBeanTest {
  @Test
  void getDerivedGauge() throws Exception {
assertThat (synthesiseNeural ("getDerivedGauge" , "this.getDerivedGauge();" , "\nthis.getDerivedGauge(null);\n" , "javax.management.monitor.GaugeMonitorMBean" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseNeural ("getDerivedGaugeTimeStamp" , "this.getDerivedGaugeTimeStamp();" , "\nthis.getDerivedGaugeTimeStamp(null);\n" , "javax.management.monitor.GaugeMonitorMBean" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }
}
