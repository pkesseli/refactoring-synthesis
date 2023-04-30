
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_GaugeMonitorMBeanTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseGPT("double derivedGauge = gaugeMonitorMBean.getDerivedGauge();\n\n", "ObjectName objectName = gaugeMonitorMBean.getObservedObject();\ndouble derivedGauge = gaugeMonitorMBean.getDerivedGauge(objectName);\n", "javax.management.monitor.GaugeMonitorMBean", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "ObjectName objectName = this.getObservedObject();\nlong timeStamp = ((GaugeMonitorMBean) this.getMBeanServerConnection().newProxyInstance(objectName,\n                GaugeMonitorMBean.class, false)).getDerivedGaugeTimeStamp(objectName);\n", "javax.management.monitor.GaugeMonitorMBean", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }
}