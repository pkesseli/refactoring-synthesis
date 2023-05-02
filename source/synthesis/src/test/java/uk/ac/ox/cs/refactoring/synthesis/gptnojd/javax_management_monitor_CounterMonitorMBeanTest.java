
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_CounterMonitorMBeanTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseGPT("double derivedGauge = this.getDerivedGauge();\n\n", "double derivedGauge = ((CounterMonitor) this).getDerivedGauge();\n", "javax.management.monitor.CounterMonitorMBean", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "long timeStamp = this.getDerivedGauge().getTimestamp();\n", "javax.management.monitor.CounterMonitorMBean", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }

  @Test
  void getThreshold() throws Exception {
    assertThat(synthesiseGPT("long threshold = ((CounterMonitorMBean) monitor).getThreshold();\n\n", "long threshold = monitor.getThreshold();\n", "javax.management.monitor.CounterMonitorMBean", "getThreshold"), anyOf(contains("getThreshold")));
  }

  @Test
  void setThreshold() throws Exception {
    assertThat(synthesiseGPT("this.setThreshold(a);\n", "this.setThreshold(new javax.management.monitor.MonitorNotificationThreshold(\n    a, javax.management.monitor.MonitorNotificationThreshold.BREACH_NOTIFICATION));\n", "javax.management.monitor.CounterMonitorMBean", "setThreshold", "java.lang.Number"), anyOf(contains("setInitThreshold")));
  }
}