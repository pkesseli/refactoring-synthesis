
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class java_sql_CallableStatementTest {
  @Test
  void getBigDecimal() throws Exception {
    assertThat(synthesiseAlias("java.sql.CallableStatement", "getBigDecimal", "int", "int"), anyOf(contains("getBigDecimal")));
  }
}