
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class com_sun_management_OperatingSystemMXBeanTest {
  @Test
  void getFreePhysicalMemorySize() throws Exception {
    assertThat(synthesiseGPT("long freeMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();\n\n", "long freeMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemory();\n", "com.sun.management.OperatingSystemMXBean", "getFreePhysicalMemorySize"), anyOf(contains("getFreeMemorySize")));
  }

  @Test
  void getSystemCpuLoad() throws Exception {
    assertThat(synthesiseGPT("double cpuLoad = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();\n\n", "double cpuLoad = 0.0;\nOperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);\nif (osBean != null) {\n    cpuLoad = osBean.getSystemCpuLoad();\n}\n", "com.sun.management.OperatingSystemMXBean", "getSystemCpuLoad"), anyOf(contains("getCpuLoad")));
  }

  @Test
  void getTotalPhysicalMemorySize() throws Exception {
    assertThat(synthesiseGPT("long totalMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();\n\n", "long totalMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize();\n", "com.sun.management.OperatingSystemMXBean", "getTotalPhysicalMemorySize"), anyOf(contains("getTotalMemorySize")));
  }
}