
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_DriverManagerTest {
  @Test
  void getLogStream() throws Exception {
assertThat (synthesiseNeural ("getLogStream" , "this.getLogStream();" , "\nthis.getLogWriter();\n" , "java.sql.DriverManager" , "getLogStream") , anyOf (contains ("getLogWriter"))) ;
  }

  @Test
  void setLogStream() throws Exception {
assertThat (synthesiseNeural ("setLogStream" , "this.setLogStream(param0);" , "\nthis.setLogWriter(new PrintWriter(param0));\n" , "java.sql.DriverManager" , "setLogStream" , "java.io.PrintStream") , anyOf (contains ("setLogWriter"))) ;
  }
}
