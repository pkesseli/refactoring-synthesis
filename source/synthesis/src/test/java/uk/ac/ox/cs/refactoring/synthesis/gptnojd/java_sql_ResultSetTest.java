
package uk.ac.ox.cs.refactoring.synthesis.gptnojd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static uk.ac.ox.cs.refactoring.synthesis.matchers.CegisMatchers.contains;
import static uk.ac.ox.cs.refactoring.synthesis.presets.Deprecation.synthesiseGPT;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class java_sql_ResultSetTest {
  @Test
  void getBigDecimal1() throws Exception {
    assertThat(synthesiseGPT("public BigDecimal getSomeValue(int a, int b) throws SQLException {\n    ResultSet rs = statement.executeQuery(\"SELECT some_value FROM some_table\");\n    if (rs.next()) {\n        return rs.getBigDecimal(a, b);\n    }\n    return null;\n}\n", "public BigDecimal getSomeValue(int a, int b) throws SQLException {\n    ResultSet rs = statement.executeQuery(\"SELECT some_value FROM some_table\");\n    if (rs.next()) {\n        return rs.getBigDecimal(a).setScale(b);\n    }\n    return null;\n}\n", "java.sql.ResultSet", "getBigDecimal", "int", "int"), anyOf(contains("getBigDecimal")));
  }

  @Test
  void getBigDecimal2() throws Exception {
    assertThat(synthesiseGPT("public BigDecimal getSomeValue(String a, int b) throws SQLException {\n    ResultSet rs = statement.executeQuery(\"SELECT some_value FROM some_table\");\n    return rs.getBigDecimal(a, b);\n}\n", "public BigDecimal getSomeValue(String a, int b) throws SQLException {\n    ResultSet rs = statement.executeQuery(\"SELECT some_value FROM some_table\");\n    rs.next();\n    return rs.getBigDecimal(1).setScale(b);\n}\n", "java.sql.ResultSet", "getBigDecimal", "java.lang.String", "int"), anyOf(contains("getBigDecimal")));
  }

  @Test
  void getUnicodeStream1() throws Exception {
    assertThat(synthesiseGPT("public void someMethod(int a) throws SQLException {\n    InputStream inputStream = this.getUnicodeStream(a);\n    // Do something with inputStream\n}\n\n", "public void someMethod(int a) throws SQLException {\n    Reader reader = this.getCharacterStream(a);\n    InputStream inputStream = new ReaderInputStream(reader);\n    // Do something with inputStream\n}\n", "java.sql.ResultSet", "getUnicodeStream", "int"), anyOf(contains("getCharacterStream"), contains("getUnicodeStream")));
  }

  @Test
  void getUnicodeStream2() throws Exception {
    assertThat(synthesiseGPT("public void someMethod(String a) throws SQLException {\n    InputStream inputStream = this.getUnicodeStream(a);\n    // do something with inputStream\n}\n\n", "public void someMethod(String a) throws SQLException {\n    Reader reader = this.getCharacterStream(a);\n    InputStream inputStream = new ReaderInputStream(reader);\n    // do something with inputStream\n}\n", "java.sql.ResultSet", "getUnicodeStream", "java.lang.String"), anyOf(contains("getCharacterStream")));
  }
}