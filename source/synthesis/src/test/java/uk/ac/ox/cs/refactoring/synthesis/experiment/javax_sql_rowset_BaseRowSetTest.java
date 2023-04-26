
package uk.ac.ox.cs.refactoring.synthesis.experiment;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseAlias;

import org.junit.jupiter.api.Test;

class javax_sql_rowset_BaseRowSetTest {
  @Test
  void setUnicodeStream() throws Exception {
    assertThat(synthesiseAlias("javax.sql.rowset.BaseRowSet", "setUnicodeStream", "int", "java.io.InputStream", "int"), contains("getCharacterStream"));
  }
}