
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_CounterMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
assertThat (synthesiseGPT ("this.getDerivedGauge();" , "```\nStringMonitorMBean stringMonitor = (StringMonitorMBean) monitor;\nstringMonitor.getDerivedGauge(objectName);\n```;" , "javax.management.monitor.CounterMonitor" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseGPT ("this.getDerivedGaugeTimeStamp();" , "\n((StringMonitorMBean)this).getDerivedGaugeTimeStamp(null)\n;" , "javax.management.monitor.CounterMonitor" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }

  @Test
  void getThreshold() throws Exception {
assertThat (synthesiseGPT ("this.getThreshold();" , "\n// Replace this line:\nthis.getThreshold()\n\n// With this line:\n((NumericValueExp)this.getThreshold()).getValue()\n;" , "javax.management.monitor.CounterMonitor" , "getThreshold") , anyOf (contains ("getThreshold"))) ;
  }

  @Test
  void setThreshold() throws Exception {
assertThat (synthesiseGPT ("this.setThreshold(param0);" , "\nthis.setThreshold(param0.intValue());\n;" , "javax.management.monitor.CounterMonitor" , "setThreshold" , "java.lang.Number") , anyOf (contains ("setInitThreshold"))) ;
  }
}
