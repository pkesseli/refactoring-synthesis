
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_sql_DriverManager_setLogStreamTest {
    
  @Test
  void setLogStream() throws Exception {
    assertThat(synthesiseAlias("java.sql.DriverManager", "setLogStream", "java.io.PrintStream"),
        contains("setLogWriter"));
  }
}
