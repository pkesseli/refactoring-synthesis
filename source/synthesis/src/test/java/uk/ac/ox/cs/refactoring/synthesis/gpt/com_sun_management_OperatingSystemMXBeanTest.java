
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class com_sun_management_OperatingSystemMXBeanTest {
  @Test
  void getFreePhysicalMemorySize() throws Exception {
assertThat (synthesiseGPT ("getFreePhysicalMemorySize" , "this.getFreePhysicalMemorySize();" , "\nthis.getFreeMemorySize()\n;" , "com.sun.management.OperatingSystemMXBean" , "getFreePhysicalMemorySize") , anyOf (contains ("getFreeMemorySize"))) ;
  }

  @Test
  void getSystemCpuLoad() throws Exception {
assertThat (synthesiseGPT ("getSystemCpuLoad" , "this.getSystemCpuLoad();" , "\nthis.getCpuLoad()\n;" , "com.sun.management.OperatingSystemMXBean" , "getSystemCpuLoad") , anyOf (contains ("getCpuLoad"))) ;
  }

  @Test
  void getTotalPhysicalMemorySize() throws Exception {
assertThat (synthesiseGPT ("getTotalPhysicalMemorySize" , "this.getTotalPhysicalMemorySize();" , "\nthis.getTotalMemorySize()\n;" , "com.sun.management.OperatingSystemMXBean" , "getTotalPhysicalMemorySize") , anyOf (contains ("getTotalMemorySize"))) ;
  }
}
