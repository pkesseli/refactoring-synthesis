
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_CounterMonitorTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseGPT("double derivedGauge = this.getDerivedGauge();\n\n", "double derivedGauge = ((CounterMonitor)this.getMonitor()).getDerivedGauge();\n", "javax.management.monitor.CounterMonitor", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "long timeStamp = this.getDerivedGauge().getTimestamp();\n", "javax.management.monitor.CounterMonitor", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }

  @Test
  void getThreshold() throws Exception {
    assertThat(synthesiseGPT("public void someMethod() {\n    double threshold = ((CounterMonitor) monitor).getThreshold();\n    // rest of the code\n}\n", "public void someMethod() {\n    double threshold = ((CounterMonitor) monitor).getDerivedGauge().getValue().doubleValue();\n    // rest of the code\n}\n", "javax.management.monitor.CounterMonitor", "getThreshold"), anyOf(contains("getThreshold")));
  }

  @Test
  void setThreshold() throws Exception {
    assertThat(synthesiseGPT("this.setThreshold(a);\n", "this.getThreshold().setValue(a);\n", "javax.management.monitor.CounterMonitor", "setThreshold", "java.lang.Number"), anyOf(contains("setInitThreshold")));
  }
}