
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_CounterMonitorMBeanTest {
  @Test
  void getDerivedGauge() throws Exception {
assertThat (synthesiseGPT ("this.getDerivedGauge();" , "" , "javax.management.monitor.CounterMonitorMBean" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseGPT ("this.getDerivedGaugeTimeStamp();" , "" , "javax.management.monitor.CounterMonitorMBean" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }

  @Test
  void getThreshold() throws Exception {
assertThat (synthesiseGPT ("this.getThreshold();" , "\nthis.getText()\n;" , "javax.management.monitor.CounterMonitorMBean" , "getThreshold") , anyOf (contains ("getThreshold"))) ;
  }

  @Test
  void setThreshold() throws Exception {
assertThat (synthesiseGPT ("this.setThreshold(param0);" , "\nthis.setText(String.valueOf(param0));\n;" , "javax.management.monitor.CounterMonitorMBean" , "setThreshold" , "java.lang.Number") , anyOf (contains ("setInitThreshold"))) ;
  }
}
