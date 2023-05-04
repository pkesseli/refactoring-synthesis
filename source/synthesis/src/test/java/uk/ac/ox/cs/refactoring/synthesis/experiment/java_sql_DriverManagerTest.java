package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class java_sql_DriverManagerTest {
  @Test
  @Disabled("Return types differ for the two methods. Presumably simple refactoring, but we just don't support this (yet?).")
  void getLogStream() throws Exception {
    assertThat(synthesiseAlias("java.sql.DriverManager", "getLogStream"),
        contains("getLogWriter"));
  }

  @Test
  @Disabled("Change of signature")
  void setLogStream() throws Exception {
    assertThat(synthesiseAlias("java.sql.DriverManager", "setLogStream", "java.io.PrintStream"),
        contains("setLogWriter"));
  }
}
