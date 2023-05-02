
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_PreparedStatementTest {
  @Test
  void setUnicodeStream() throws Exception {
    assertThat(synthesiseGPT("this.setUnicodeStream(a, b, c);\nthis.setBinaryStream(a, b, c);\n", "", "java.sql.PreparedStatement", "setUnicodeStream", "int", "java.io.InputStream", "int"), anyOf(contains("setCharacterStream")));
  }
}