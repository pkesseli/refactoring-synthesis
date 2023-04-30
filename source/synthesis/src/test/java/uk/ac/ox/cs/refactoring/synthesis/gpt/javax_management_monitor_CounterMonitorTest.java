
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
    assertThat(synthesiseGPT("double derivedGauge = counterMonitor.getDerivedGauge();\n\n", "ObjectName objectName = counterMonitor.getObservedObject();\ndouble derivedGauge = counterMonitor.getDerivedGauge(objectName);\n", "javax.management.monitor.CounterMonitor", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "ObjectName objectName = this.getObservedObject();\nlong timeStamp = ((CounterMonitor) this.getMonitor()).getDerivedGaugeTimeStamp(objectName);\n", "javax.management.monitor.CounterMonitor", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }

  @Test
  void getThreshold() throws Exception {
    assertThat(synthesiseGPT("this.getThreshold();\n\n", "counterMonitor.getThreshold(monitor.getMBean()); \n", "javax.management.monitor.CounterMonitor", "getThreshold"), anyOf(contains("getThreshold")));
  }

  @Test
  void setThreshold() throws Exception {
    assertThat(synthesiseGPT("this.setThreshold(a);\n", "this.setInitThreshold(a);\n", "javax.management.monitor.CounterMonitor", "setThreshold", "java.lang.Number"), anyOf(contains("setInitThreshold")));
  }
}