
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_logging_LoggerTest {
  @Test
  void logrb1() throws Exception {
assertThat (synthesiseGPT ("this.logrb(param0, param1, param2, param3, param4);" , "" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String") , anyOf (contains ("logrb"))) ;
  }

  @Test
  void logrb2() throws Exception {
assertThat (synthesiseGPT ("this.logrb(param0, param1, param2, param3, param4, param5);" , "" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.Object") , anyOf (contains ("logrb"))) ;
  }

  @Test
  void logrb3() throws Exception {
assertThat (synthesiseGPT ("this.logrb(param0, param1, param2, param3, param4, param5);" , "\nCounterMonitor monitor = new CounterMonitor();\nmonitor.setInitThreshold(0);\nmonitor.setGranularityPeriod(1);\nmonitor.start();\n;" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "Object[]") , anyOf (contains ("logrb"))) ;
  }

  @Test
  void logrb4() throws Exception {
assertThat (synthesiseGPT ("this.logrb(param0, param1, param2, param3, param4, param5);" , "\nCounterMonitorMBean.getDerivedGauge(new ObjectName(param1), param3, param4).log(param0, param5);\n;" , "java.util.logging.Logger" , "logrb" , "java.util.logging.Level" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.String" , "java.lang.Throwable") , anyOf (contains ("logrb"))) ;
  }
}
