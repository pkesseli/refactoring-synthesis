
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
    assertThat(synthesiseGPT("double derivedGauge = this.getDerivedGauge();\n\n", "ObjectName objectName = new ObjectName(\"yourObjectNameHere\");\ndouble derivedGauge = ((CounterMonitorMBean) this).getDerivedGauge(objectName);\n", "javax.management.monitor.CounterMonitorMBean", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "ObjectName objectName = this.getObservedObject();\nlong timeStamp = ((CounterMonitorMBean) this.getMBeanServer().newProxyInstance(objectName,\n                CounterMonitorMBean.class, false)).getDerivedGaugeTimeStamp(objectName);\n", "javax.management.monitor.CounterMonitorMBean", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }

  @Test
  void getThreshold() throws Exception {
    assertThat(synthesiseGPT("long threshold = this.getThreshold();\n\n", "ObjectName objectName = this.getObservedObject();\nlong threshold = ((CounterMonitorMBean) this.getMBeanServer().newProxyInstance(objectName,\n                CounterMonitorMBean.class, false)).getThreshold();\n", "javax.management.monitor.CounterMonitorMBean", "getThreshold"), anyOf(contains("getThreshold")));
  }

  @Test
  void setThreshold() throws Exception {
    assertThat(synthesiseGPT("this.setThreshold(a);\n", "this.setInitThreshold(a);\n", "javax.management.monitor.CounterMonitorMBean", "setThreshold", "java.lang.Number"), anyOf(contains("setInitThreshold")));
  }
}