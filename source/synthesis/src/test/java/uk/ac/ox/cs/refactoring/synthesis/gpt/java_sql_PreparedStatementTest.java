
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_PreparedStatementTest {
  @Test
  void setUnicodeStream() throws Exception {
    assertThat(synthesiseGPT("this.setUnicodeStream(a, b, c);\n\n", "this.setCharacterStream(a, new InputStreamReader(b, StandardCharsets.UTF_8), c);\n", "java.sql.PreparedStatement", "setUnicodeStream", "int", "java.io.InputStream", "int"), anyOf(contains("setCharacterStream")));
  }
}