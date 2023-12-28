
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_GaugeMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
assertThat (synthesiseGPT ("getDerivedGauge" , "this.getDerivedGauge();" , "\nfinal ObjectName objectName = new ObjectName(\"myObjectName\");\n((GaugeMonitor) this).getDerivedGauge(objectName);\n" , "javax.management.monitor.GaugeMonitor" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseGPT ("getDerivedGaugeTimeStamp" , "this.getDerivedGaugeTimeStamp();" , "\nfinal ObjectName objectName = new ObjectName(\"myObjectName\");\nthis.getDerivedGaugeTimeStamp(objectName);\n" , "javax.management.monitor.GaugeMonitor" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }
}
