
package uk.ac.ox.cs.refactoring.synthesis.gpt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseNeural;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_ResultSetTest {
  @Test
  void getBigDecimal1() throws Exception {
assertThat (synthesiseNeural ("getBigDecimal1" , "this.getBigDecimal(param0, param1);" , "\nthis.getBigDecimal(param0);\n" , "java.sql.ResultSet" , "getBigDecimal" , "int" , "int") , anyOf (contains ("getBigDecimal"))) ;
  }

  @Test
  void getBigDecimal2() throws Exception {
assertThat (synthesiseNeural ("getBigDecimal2" , "this.getBigDecimal(param0, param1);" , "\nthis.getBigDecimal(param0);\n" , "java.sql.ResultSet" , "getBigDecimal" , "java.lang.String" , "int") , anyOf (contains ("getBigDecimal"))) ;
  }

  @Test
  void getUnicodeStream1() throws Exception {
assertThat (synthesiseNeural ("getUnicodeStream1" , "this.getUnicodeStream(param0);" , "\nthis.getCharacterStream(param0);\n" , "java.sql.ResultSet" , "getUnicodeStream" , "int") , anyOf (contains ("getCharacterStream"))) ;
  }

  @Test
  void getUnicodeStream2() throws Exception {
assertThat (synthesiseNeural ("getUnicodeStream2" , "this.getUnicodeStream(param0);" , "\nthis.getCharacterStream(param0);\n" , "java.sql.ResultSet" , "getUnicodeStream" , "java.lang.String") , anyOf (contains ("getCharacterStream"))) ;
  }
}
