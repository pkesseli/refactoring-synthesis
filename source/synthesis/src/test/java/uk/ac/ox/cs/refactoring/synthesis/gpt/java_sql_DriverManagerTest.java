
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_DriverManagerTest {
  @Test
  void getLogStream() throws Exception {
assertThat (synthesiseGPT ("getLogStream" , "this.getLogStream();" , "" , "java.sql.DriverManager" , "getLogStream") , anyOf (contains ("getLogWriter"))) ;
  }

  @Test
  void setLogStream() throws Exception {
assertThat (synthesiseGPT ("setLogStream" , "this.setLogStream(param0);" , "\nPrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(\"logFile.txt\"), StandardCharsets.UTF_8));\njava.sql.DriverManager.setLogWriter(printWriter);\n" , "java.sql.DriverManager" , "setLogStream" , "java.io.PrintStream") , anyOf (contains ("setLogWriter"))) ;
  }
}
