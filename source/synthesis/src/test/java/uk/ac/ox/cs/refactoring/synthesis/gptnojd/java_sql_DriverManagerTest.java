
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_DriverManagerTest {
  @Test
  void getLogStream() throws Exception {
    assertThat(synthesiseGPT("public void someMethod() {\n    OutputStream logStream = DriverManager.getLogStream();\n    // rest of the code\n}\n\n", "public void someMethod() {\n    DriverManager.setLogWriter(new PrintWriter(System.out));\n    // rest of the code\n}\n", "java.sql.DriverManager", "getLogStream"), anyOf(contains("getLogWriter")));
  }

  @Test
  void setLogStream() throws Exception {
    assertThat(synthesiseGPT("this.setLogStream(a);\n", "DriverManager.setLogWriter(new PrintWriter(a));\n", "java.sql.DriverManager", "setLogStream", "java.io.PrintStream"), anyOf(contains("setLogWriter")));
  }
}