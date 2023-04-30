
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_monitor_StringMonitorMBeanTest {
  @Test
  void getDerivedGauge() throws Exception {
    assertThat(synthesiseGPT("StringMonitorMBean stringMonitor = ...;\nString derivedGauge = stringMonitor.getDerivedGauge();\n\n", "StringMonitorMBean stringMonitor = ...;\nObjectName objectName = ...;\nString derivedGauge = stringMonitor.getDerivedGauge(objectName);\n", "javax.management.monitor.StringMonitorMBean", "getDerivedGauge"), anyOf(contains("getDerivedGauge")));
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
    assertThat(synthesiseGPT("long timeStamp = this.getDerivedGaugeTimeStamp();\n\n", "ObjectName objectName = this.getObservedObject();\nlong timeStamp = ((StringMonitorMBean) this.getMBeanServerConnection().newProxyInstance(objectName,\n                this.getClass().getName(), false)).getDerivedGaugeTimeStamp(objectName);\n", "javax.management.monitor.StringMonitorMBean", "getDerivedGaugeTimeStamp"), anyOf(contains("getDerivedGaugeTimeStamp")));
  }
}