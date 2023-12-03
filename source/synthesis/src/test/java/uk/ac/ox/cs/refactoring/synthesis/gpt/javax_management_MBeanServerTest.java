
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class javax_management_MBeanServerTest {
  @Test
  void deserialize1() throws Exception {
assertThat (synthesiseGPT ("this.deserialize(param0, param1);" , "MonitorMBean monitorMBean = ...; // obtain the MonitorMBean instance\nObjectName objectName = new ObjectName(param0); // create ObjectName from param0\nmonitorMBean.addObservedObject(objectName, param1); // add observed object with byte[] data to MonitorMBean;" , "javax.management.MBeanServer" , "deserialize" , "java.lang.String" , "byte[]") , anyOf (contains ("getClassLoaderRepository"))) ;
  }

  @Test
  void deserialize2() throws Exception {
assertThat (synthesiseGPT ("this.deserialize(param0, param1, param2);" , "param1.getAttribute(\"StringMonitor.getDerivedGauge\");" , "javax.management.MBeanServer" , "deserialize" , "java.lang.String" , "javax.management.ObjectName" , "byte[]") , anyOf (contains ("getClassLoader"))) ;
  }

  @Test
  void deserialize3() throws Exception {
assertThat (synthesiseGPT ("this.deserialize(param0, param1);" , "param0.getMBeanServer().deserialize(param0, param1);" , "javax.management.MBeanServer" , "deserialize" , "javax.management.ObjectName" , "byte[]") , anyOf (contains ("getClassLoaderFor"))) ;
  }
}
