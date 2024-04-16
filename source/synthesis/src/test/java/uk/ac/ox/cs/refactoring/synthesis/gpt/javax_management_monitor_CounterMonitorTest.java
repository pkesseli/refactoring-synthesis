
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_CounterMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
assertThat (synthesiseNeural ("getDerivedGauge" , "this.getDerivedGauge();" , "\nthis.getDerivedGauge(null);\n" , "javax.management.monitor.CounterMonitor" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseNeural ("getDerivedGaugeTimeStamp" , "this.getDerivedGaugeTimeStamp();" , "\nthis.getDerivedGaugeTimeStamp(null);\n" , "javax.management.monitor.CounterMonitor" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }

  @Test
  void getThreshold() throws Exception {
assertThat (synthesiseNeural ("getThreshold" , "this.getThreshold();" , "\nCounterMonitor.getThreshold(monitor.getMBeanName());\n" , "javax.management.monitor.CounterMonitor" , "getThreshold") , anyOf (contains ("getThreshold"))) ;
  }

  @Test
  void setThreshold() throws Exception {
assertThat (synthesiseNeural ("setThreshold" , "this.setThreshold(param0);" , "\nthis.setInitThreshold(param0);\n" , "javax.management.monitor.CounterMonitor" , "setThreshold" , "java.lang.Number") , anyOf (contains ("setInitThreshold"))) ;
  }
}
