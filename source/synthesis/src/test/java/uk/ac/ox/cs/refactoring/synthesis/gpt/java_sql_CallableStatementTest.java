
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_CallableStatementTest {
  @Test
  void getBigDecimal() throws Exception {
    assertThat(synthesiseGPT("this.getBigDecimal(a, b);\n\n", "this.getBigDecimal(a);\n", "java.sql.CallableStatement", "getBigDecimal", "int", "int"), anyOf(contains("getBigDecimal")));
  }
}