
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_StringMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
assertThat (synthesiseGPT ("getDerivedGauge" , "this.getDerivedGauge();" , "\nfinal ObjectName objectName = new ObjectName(\"com.example:type=StringMonitor\");\nthis.getDerivedGauge(objectName);\n" , "javax.management.monitor.StringMonitor" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseGPT ("getDerivedGaugeTimeStamp" , "this.getDerivedGaugeTimeStamp();" , "\nfinal ObjectName objectName = new ObjectName(\"com.example:type=StringMonitor\");\nthis.getDerivedGaugeTimeStamp(objectName);\n" , "javax.management.monitor.StringMonitor" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }
}
