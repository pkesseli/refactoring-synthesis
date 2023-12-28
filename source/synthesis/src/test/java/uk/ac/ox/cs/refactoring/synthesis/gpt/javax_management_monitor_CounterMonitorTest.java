
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
assertThat (synthesiseGPT ("getDerivedGauge" , "this.getDerivedGauge();" , "\nfinal ObjectName objectName = new ObjectName(\"com.example:type=CounterMonitor\");\n((CounterMonitor) this).getDerivedGauge(objectName);\n" , "javax.management.monitor.CounterMonitor" , "getDerivedGauge") , anyOf (contains ("getDerivedGauge"))) ;
  }

  @Test
  void getDerivedGaugeTimeStamp() throws Exception {
assertThat (synthesiseGPT ("getDerivedGaugeTimeStamp" , "this.getDerivedGaugeTimeStamp();" , "\nfinal ObjectName objectName = new ObjectName(\"com.example:type=CounterMonitor\");\nfinal CounterMonitor counterMonitor = new CounterMonitor();\ncounterMonitor.getDerivedGaugeTimeStamp(objectName);\n" , "javax.management.monitor.CounterMonitor" , "getDerivedGaugeTimeStamp") , anyOf (contains ("getDerivedGaugeTimeStamp"))) ;
  }

  @Test
  void getThreshold() throws Exception {
assertThat (synthesiseGPT ("getThreshold" , "this.getThreshold();" , "\nfinal ObjectName objectName = new ObjectName(\"com.example:type=CounterMonitor\");\nfinal MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();\nfinal CounterMonitor counterMonitor = JMX.newMBeanProxy(mBeanServer, objectName, CounterMonitor.class);\ncounterMonitor.getThreshold(objectName);\n" , "javax.management.monitor.CounterMonitor" , "getThreshold") , anyOf (contains ("getThreshold"))) ;
  }

  @Test
  void setThreshold() throws Exception {
assertThat (synthesiseGPT ("setThreshold" , "this.setThreshold(param0);" , "\nthis.setInitThreshold(param0);\n" , "javax.management.monitor.CounterMonitor" , "setThreshold" , "java.lang.Number") , anyOf (contains ("setInitThreshold"))) ;
  }
}
