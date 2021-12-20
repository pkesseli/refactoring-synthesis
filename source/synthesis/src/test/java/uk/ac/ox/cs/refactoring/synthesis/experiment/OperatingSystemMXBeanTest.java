package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.testAlias;

import org.junit.jupiter.api.Test;

class OperatingSystemMXBeanTest {
  @Test
  void getFreePhysicalMemorySize() throws Exception {
    testAlias(".getFreeMemorySize(", "com.sun.management.OperatingSystemMXBean", "getFreePhysicalMemorySize");
  }

  @Test
  void getSystemCpuLoad() throws Exception {
    testAlias(".getCpuLoad(", "com.sun.management.OperatingSystemMXBean", "getSystemCpuLoad");
  }

  @Test
  void getTotalPhysicalMemorySize() throws Exception {
    testAlias(".getTotalMemorySize(", "com.sun.management.OperatingSystemMXBean", "getTotalPhysicalMemorySize");
  }
}
