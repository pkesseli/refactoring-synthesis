package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class com_sun_management_OperatingSystemMXBeanTest {

  @Test
  void getFreePhysicalMemorySize() throws Exception {
    assertThat(synthesiseAlias("com.sun.management.OperatingSystemMXBean", "getFreePhysicalMemorySize"),
        contains(".getFreeMemorySize("));
  }

  @Test
  void getSystemCpuLoad() throws Exception {
    assertThat(synthesiseAlias("com.sun.management.OperatingSystemMXBean", "getSystemCpuLoad"),
        contains(".getCpuLoad("));
  }

  @Test
  void getTotalPhysicalMemorySize() throws Exception {
    assertThat(synthesiseAlias("com.sun.management.OperatingSystemMXBean", "getTotalPhysicalMemorySize"),
        contains(".getTotalMemorySize("));
  }
}
