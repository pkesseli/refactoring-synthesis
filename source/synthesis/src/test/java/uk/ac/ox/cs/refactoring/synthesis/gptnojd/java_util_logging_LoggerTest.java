
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_util_logging_LoggerTest {
  @Test
  void logrb1() throws Exception {
    assertThat(synthesiseGPT("this.logrb(a, b, c, d, e);\n\n", "Logger logger = Logger.getLogger(c);\nlogger.log(Level.parse(a.toString()), d, new Object[]{e});\n", "java.util.logging.Logger", "logrb", "java.util.logging.Level", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String"), anyOf(contains("logrb")));
  }

  @Test
  void logrb2() throws Exception {
    assertThat(synthesiseGPT("this.logrb(a, b, c, d, e, f);\n\n", "if (this.isLoggable(a)) {\n    this.log(a, String.format(\"%s.%s\", c, d), e, f);\n}\n", "java.util.logging.Logger", "logrb", "java.util.logging.Level", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.Object"), anyOf(contains("logrb")));
  }

  @Test
  void logrb3() throws Exception {
    assertThat(synthesiseGPT("this.logrb(a, b, c, d, e, f);\n\n", "if (this.isLoggable(a)) {\n    this.log(a, String.format(this.getResourceBundle().getString(c), f));\n}\n", "java.util.logging.Logger", "logrb", "java.util.logging.Level", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "Object[]"), anyOf(contains("logrb")));
  }

  @Test
  void logrb4() throws Exception {
    assertThat(synthesiseGPT("this.logrb(a, b, c, d, e, f);\n\n", "Logger logger = Logger.getLogger(c);\nlogger.log(Level.parse(a.toString()), e, () -> d + \" \" + f);\n", "java.util.logging.Logger", "logrb", "java.util.logging.Level", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.Throwable"), anyOf(contains("logrb")));
  }
}