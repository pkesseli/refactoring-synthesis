
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_logging_LogManagerTest {
  @Test
  void getLoggingMXBean() throws Exception {
assertThat (synthesiseGPT ("getLoggingMXBean" , "this.getLoggingMXBean();" , "\nimport java.lang.management.ManagementFactory;\nimport java.util.logging.LoggingMXBean;\n\n// ...\n\nLoggingMXBean loggingMXBean = ManagementFactory.getPlatformMXBean(LoggingMXBean.class);\n" , "java.util.logging.LogManager" , "getLoggingMXBean") , anyOf (contains ("LoggingMXBean") , contains ("PlatformLoggingMXBean") , contains ("getPlatformMXBean"))) ;
  }
}
